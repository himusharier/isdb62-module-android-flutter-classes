package com.himusharier.ajpscrud.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Log;
import com.himusharier.ajpscrud.config.AppConfig;
import com.himusharier.ajpscrud.models.AuthRegisterLoginRequest;
import com.himusharier.ajpscrud.models.AuthResponse;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.*;

public class AuthService {
    private static final String TAG = "AuthService";
    private static final String TOKEN_KEY = "ajps_access_token";
    private static final String PREF_NAME = "UserPrefs";

    private Context context;
    private SharedPreferences sharedPreferences;
    private OkHttpClient httpClient;

    private String userRole;
    private String userEmail;
    private int userId;

    public AuthService(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        loadTokenAndUserData();
    }

    private void loadTokenAndUserData() {
        String token = sharedPreferences.getString(TOKEN_KEY, null);
        if (token != null) {
            decodeTokenAndSetUserData(token);
        }
    }

    private void decodeTokenAndSetUserData(String token) {
        try {
            // JWT tokens have 3 parts separated by dots
            String[] parts = token.split("\\.");
            if (parts.length == 3) {
                // Decode the payload (second part)
                String payload = new String(Base64.decode(parts[1], Base64.URL_SAFE | Base64.NO_PADDING));
                JSONObject jsonPayload = new JSONObject(payload);

                this.userRole = jsonPayload.optString("role", "").toLowerCase();
                this.userEmail = jsonPayload.optString("email", "").toLowerCase();
                this.userId = jsonPayload.optInt("id", 0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error decoding token: " + e.getMessage());
            this.userRole = null;
            this.userEmail = null;
            this.userId = 0;
        }
    }

    public interface AuthCallback {
        void onSuccess(AuthResponse response);
        void onError(AuthResponse response);
    }

    public void register(AuthRegisterLoginRequest request, AuthCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", request.getEmail());
            jsonBody.put("password", request.getPassword());
            jsonBody.put("userRole", request.getUserRole());

            RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonBody.toString()
            );

            Request httpRequest = new Request.Builder()
                    .url(AppConfig.BASE_URL + "/api/auth/register")
                    .post(body)
                    .build();

            httpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(new AuthResponse(500, "Network error: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        AuthResponse authResponse = new AuthResponse();
                        authResponse.setCode(jsonResponse.optInt("code", response.code()));
                        authResponse.setMessage(jsonResponse.optString("message", ""));
                        authResponse.setAccessToken(jsonResponse.optString("accessToken", null));

                        if (authResponse.getCode() == 200) {
                            callback.onSuccess(authResponse);
                        } else {
                            callback.onError(authResponse);
                        }
                    } catch (JSONException e) {
                        callback.onError(new AuthResponse(500, "Parse error: " + e.getMessage()));
                    }
                }
            });
        } catch (JSONException e) {
            callback.onError(new AuthResponse(500, "JSON error: " + e.getMessage()));
        }
    }

    public void login(AuthRegisterLoginRequest request, AuthCallback callback) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("email", request.getEmail());
            jsonBody.put("password", request.getPassword());
            jsonBody.put("userRole", request.getUserRole());

            RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonBody.toString()
            );

            Request httpRequest = new Request.Builder()
                    .url(AppConfig.BASE_URL + "/api/auth/login")
                    .post(body)
                    .build();

            httpClient.newCall(httpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    callback.onError(new AuthResponse(500, "Network error: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);

                        AuthResponse authResponse = new AuthResponse();
                        authResponse.setCode(jsonResponse.optInt("code", response.code()));
                        authResponse.setMessage(jsonResponse.optString("message", ""));
                        authResponse.setAccessToken(jsonResponse.optString("accessToken", null));

                        if (authResponse.getCode() == 200 && authResponse.getAccessToken() != null) {
                            setToken(authResponse.getAccessToken());
                            callback.onSuccess(authResponse);
                        } else {
                            callback.onError(authResponse);
                        }
                    } catch (JSONException e) {
                        callback.onError(new AuthResponse(500, "Parse error: " + e.getMessage()));
                    }
                }
            });
        } catch (JSONException e) {
            callback.onError(new AuthResponse(500, "JSON error: " + e.getMessage()));
        }
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
        decodeTokenAndSetUserData(token);
    }

    public String getToken() {
        return sharedPreferences.getString(TOKEN_KEY, null);
    }

    public void validateToken(AuthCallback callback) {
        String token = getToken();
        if (token == null) {
            callback.onError(new AuthResponse(401, "No token found"));
            return;
        }

        Request request = new Request.Builder()
                .url(AppConfig.BASE_URL + "/api/auth/validate-token")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError(new AuthResponse(500, "Network error: " + e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String responseBody = response.body().string();
                    if ("true".equals(responseBody)) {
                        callback.onSuccess(new AuthResponse(200, "Token valid"));
                    } else {
                        callback.onError(new AuthResponse(401, "Token invalid"));
                    }
                } else {
                    callback.onError(new AuthResponse(response.code(), "Token validation failed"));
                }
            }
        });
    }

    public boolean isAuthenticated() {
        return userRole != null && !userRole.isEmpty();
    }

    public String getUserRole() {
        return userRole != null ? userRole : "";
    }

    public String getUserEmail() {
        return userEmail != null ? userEmail : "";
    }

    public int getUserId() {
        return userId;
    }

    public void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(TOKEN_KEY);
        editor.apply();

        this.userRole = null;
        this.userEmail = null;
        this.userId = 0;
    }

    public void deleteToken() {
        logout();
    }
}
