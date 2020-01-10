package com.example.matchmaking;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitInterface {
    public static final String API_URL = "http://192.249.19.251:8780/";

    @FormUrlEncoded
    @POST("login")
    Call<String> sendLogin(@Field("id")String id, @Field("pw")String pw);

    @FormUrlEncoded
    @POST("sign")
    Call<String> sendSign(@Field("user")User user);
}
