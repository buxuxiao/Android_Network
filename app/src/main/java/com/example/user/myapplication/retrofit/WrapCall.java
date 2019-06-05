package com.example.user.myapplication.retrofit;

import retrofit2.Call;

/**
 * Created by hulifei on 2018/4/24.
 */

public class WrapCall<T,K> {
    public Call call;

    public WrapCall(Call call){
        this.call=call;
    }
}
