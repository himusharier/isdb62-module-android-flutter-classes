package com.himusharier.studentcrudapp.service;

import com.himusharier.studentcrudapp.model.Student;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("student")
    Call<Student> saveStudent(@Body Student student);

    @GET("student")
    Call<List<Student>> getAllStudent();

    @PUT("student/{id}")
    Call<Student> studentStudent(@Path("id") int id, @Body Student student);

    @DELETE("student/{id}")
    Call<Void> deleteStudent(@Path("id") int id);
}