package com.tatonimatteo.waterhealth.api.security;

import androidx.annotation.NonNull;

import com.tatonimatteo.waterhealth.api.HttpManager;
import com.tatonimatteo.waterhealth.configuration.LoginCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthController {
    private final HttpManager httpManager;

    public AuthController(HttpManager httpManager) {
        this.httpManager = httpManager;
    }

    public void login(String username, String password, final LoginCallback callback) {
        try {
            JSONObject loginData = new JSONObject();
            loginData.put("email", username);
            loginData.put("password", password);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), loginData.toString());
            Call<TokenResponse> call = httpManager.getHttpService().login(requestBody);
            call.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(@NonNull Call<TokenResponse> call, @NonNull Response<TokenResponse> response) {
                    if (response.isSuccessful()) {
                        TokenResponse tokenResponse = response.body();
                        if (tokenResponse != null && httpManager.setToken(tokenResponse.getToken())) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure("Token non valido");
                        }
                    } else {
                        callback.onFailure(response.message());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<TokenResponse> call, @NonNull Throwable t) {
                    callback.onFailure(t.getMessage());
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}