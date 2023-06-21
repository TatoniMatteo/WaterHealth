package com.tatonimatteo.waterhealth.api.security;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final String token;

    public AuthInterceptor(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (token != null) builder.addHeader("Authorization", "Bearer " + token);
        return chain.proceed(builder.build());
    }
}
