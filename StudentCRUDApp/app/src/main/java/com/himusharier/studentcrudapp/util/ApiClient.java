package com.himusharier.studentcrudapp.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.himusharier.studentcrudapp.adapter.LocalDateAdapter;
import com.himusharier.studentcrudapp.service.ApiService;

import java.time.LocalDate;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
//    private static final String BASE_URL = "http://10.0.2.2:8081/";
    private static final String BASE_URL = "http://192.168.1.101:8081/"; // Actual IP Address
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }
}
