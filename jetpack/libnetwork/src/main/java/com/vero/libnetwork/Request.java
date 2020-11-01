package com.vero.libnetwork;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;

import com.vero.libnetwork.cache.CacheManager;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class Request<T, R extends Request> {
    //仅缓存
    public static final int CACHE_ONLY = 1;
    //先访问缓存，同时发起网络请求，然后缓存到本地
    public static final int CACHE_FIRST = 2;
    //仅仅访问网络
    public static final int NET_ONLY = 3;
    //先访问网络，然后缓存到本地
    public static final int NET_CACHE = 4;
    protected String mUrl;
    protected HashMap<String, String> headers = new HashMap<>();

    //只能是8种基本数据类型
    protected HashMap<String, Object> params = new HashMap<>();
    private String cacheKey;

    private Type mType;//同步请求时，具体的返回类型
    private Class mClaz;//同步请求时，具体的返回类型
    private int mCashStrategy;//缓存类型

    @IntDef({CACHE_ONLY, CACHE_FIRST, NET_ONLY, NET_CACHE})
    public @interface CashStrategy {

    }


    public Request(String mUrl) {
        this.mUrl = mUrl;
    }

    public R addHeader(String key, String value) {
        headers.put(key, value);
        return (R) this;
    }

    public R addParam(String key, String value) {
        try {
            Field field = value.getClass().getField("TYPE");
            Class claz = (Class) field.get(null);
            if (claz.isPrimitive()) {
                //基本数据类型
                params.put(key, value);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return (R) this;
    }

    public R cacheKey(String key) {
        this.cacheKey = key;
        return (R) this;
    }


    private Call getCall() {
        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
        addHeader(builder);
        okhttp3.Request request = generateRequest(builder);
        Call call = ApiService.INSTANCE.getOkHttpClient().newCall(request);
        return call;
    }

    protected abstract okhttp3.Request generateRequest(okhttp3.Request.Builder builder);


    private void addHeader(okhttp3.Request.Builder builder) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            builder.addHeader(entry.getKey(), entry.getValue());
        }
    }

    //异步
    @SuppressLint("RestrictedApi")
    public void execute(final JsonCallback<T> callback) {
        if (mCashStrategy != NET_ONLY) {
            ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    ApiResponse<T> response = readCache();
                    if (callback != null) {
                        callback.onCacheSuccess(response);
                    }
                }
            });
        }
        if (mCashStrategy != CACHE_ONLY) {
            getCall().enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    ApiResponse<T> apiResponse = new ApiResponse<>();
                    apiResponse.message = e.getMessage();
                    callback.onError(apiResponse);
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ApiResponse<T> apiResponse = parseResponse(response, callback);
                    if (apiResponse != null && apiResponse.success) {
                        callback.onSuccess(apiResponse);
                    } else {
                        callback.onError(apiResponse);
                    }
                }
            });
        }
    }


    //同步
    public ApiResponse<T> execute() {
        if (mCashStrategy == NET_ONLY) {
            return readCache();
        }
        try {
            Response response = getCall().execute();
            ApiResponse<T> result = parseResponse(response, null);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private ApiResponse<T> parseResponse(Response response, JsonCallback<T> callback) {
        String message = null;
        int status = response.code();
        boolean success = response.isSuccessful();
        ApiResponse<T> result = new ApiResponse<>();
        Convert convert = ApiService.INSTANCE.getSConvert();
        try {
            String content = response.body().string();
            if (success) {
                if (callback != null) {
                    ParameterizedType type = (ParameterizedType) callback.getClass().getGenericSuperclass();
                    //获取泛型的实际类型
                    Type argument = type.getActualTypeArguments()[0];
                    result.body = (T) convert.convert(content, argument);
                } else if (mType != null) {
                    //表示同步请求，
                    result.body = (T) convert.convert(content, mType);
                } else if (mClaz != null) {
                    //表示同步请求，
                    result.body = (T) convert.convert(content, mClaz);
                } else {
                    Log.e("Request", "parseResponse无法解析");
                }
            } else {
                message = content;
            }
        } catch (IOException e) {
            e.printStackTrace();
            message = e.getMessage();
            success = false;
        }

        result.success = success;
        result.status = status;
        result.message = message;

        //写入缓存
        if (mCashStrategy != NET_ONLY && result.success && result.body != null && result.body instanceof Serializable) {
            saveCache(result.body);
        }
        return result;
    }

    private void saveCache(T body) {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        CacheManager.save(key, body);

    }

    private ApiResponse<T> readCache() {
        String key = TextUtils.isEmpty(cacheKey) ? generateCacheKey() : cacheKey;
        Object cache = CacheManager.getCache(key);
        ApiResponse<T> result = new ApiResponse<>();
        result.status = 304;
        result.message = "读取缓存成功";
        result.body = (T) cache;
        result.success = true;
        return result;
    }


    //生成cachekey
    private String generateCacheKey() {
        cacheKey = UrlCreator.createUrlFromParams(mUrl, params);
        return cacheKey;
    }

    //同步
    public R responseType(Type type) {
        mType = type;
        return (R) this;
    }

    public R responseType(Class claz) {
        mClaz = claz;
        return (R) this;
    }


    public R cacheStrategy(@CashStrategy int cacheStrategy) {
        mCashStrategy = cacheStrategy;
        return (R) this;
    }
}
