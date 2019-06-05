package com.example.user.myapplication.retrofit;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hulifei on 2018/4/24.
 */

public interface GitHubService {

    @GET("getOutLine")
    WrapCall<List<Repo>,Object> getOutLine();

    @GET("getOutLine")
    Call<ResponseBody> getOutLine2();
}
