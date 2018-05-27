package com.example.marinab.testapplication

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

public interface RetrofitInterface {
    @Multipart
    @POST("/api/values")
    fun uploadUserAvatar(@Part file: MultipartBody.Part, @Part("name") name: RequestBody) : Call<Void>
}