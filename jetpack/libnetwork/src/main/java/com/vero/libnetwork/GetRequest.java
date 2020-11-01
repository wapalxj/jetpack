package com.vero.libnetwork;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;

public class GetRequest<T> extends Request<T, GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        builder.get().url(UrlCreator.createUrlFromParams(mUrl, params));
        return builder.build();
    }


}
