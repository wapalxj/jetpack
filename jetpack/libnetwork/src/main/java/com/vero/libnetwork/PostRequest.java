package com.vero.libnetwork;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.FormBody;

public class PostRequest<T> extends Request<T, PostRequest> {

    public PostRequest(String url) {
        super(url);
    }

    @Override
    protected okhttp3.Request generateRequest(okhttp3.Request.Builder builder) {
        FormBody.Builder bodyBuilder = new FormBody.Builder();

        for (Map.Entry<String, Object> entry : params.entrySet()) {
            String value = String.valueOf(entry.getValue());
            bodyBuilder.add(entry.getKey(), value);
        }

        builder.get().post(bodyBuilder.build());
        return builder.build();
    }


}
