package com.tatonimatteo.waterhealth.api;

import com.google.gson.GsonBuilder;
import com.tatonimatteo.waterhealth.api.security.AuthInterceptor;
import com.tatonimatteo.waterhealth.configuration.RecordDeserializer;
import com.tatonimatteo.waterhealth.entity.Record;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static final String BASE_URL = "http://192.168.178.142:8080/";
    private final Retrofit.Builder retrofitBuilder;
    private HttpService httpService;

    public HttpManager() {
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapter(Record.class, new RecordDeserializer())
                        .create()));
        setHttpService(retrofitBuilder);
    }

    public HttpService getHttpService() {
        return this.httpService;
    }

    private void setHttpService(Retrofit.Builder retrofitBuilder) {
        httpService = retrofitBuilder.build().create(HttpService.class);
    }

    private void setHttpService(Retrofit.Builder retrofitBuilder, String token) {
        AuthInterceptor authInterceptor = new AuthInterceptor(token);
        httpService = retrofitBuilder
                .client(new OkHttpClient.Builder()
                        .addInterceptor(authInterceptor)
                        .build())
                .build()
                .create(HttpService.class);
    }

    public boolean setToken(String token) {
        if (token == null || token.isEmpty()) return false;
        setHttpService(retrofitBuilder, token);
        return true;
    }

}
