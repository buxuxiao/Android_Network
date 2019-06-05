package com.example.user.myapplication.retrofit;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.user.myapplication.R;

import net.tsz.afinal.utils.Utils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
//import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
//import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity {

    private Activity mActivity = this;

    String BASE_URL = "http://101.1.3.167:7008/Liems/webservice/";

    private Button buttonGetData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        initView();
    }

    private void initView() {
        buttonGetData = (Button) findViewById(R.id.button1);
        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData2();
            }
        });
    }

    private void getData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)

//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())

                .addConverterFactory(new Converter.Factory() {
                    @Override
                    public Converter<ResponseBody, ?> responseBodyConverter(final Type type, Annotation[] annotations, Retrofit retrofit) {

                        return new Converter<ResponseBody, Object>() {
                            @Override
                            public Object convert(ResponseBody responseBody) throws IOException {
                                return JSON.parseObject(responseBody.string(), type);
                            }
                        };
                    }

                    @Override
                    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
                        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
                    }

                    @Override
                    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
                        return super.stringConverter(type, annotations, retrofit);
                    }
                })
                .addCallAdapterFactory(new CallAdapter.Factory() {
                    @Override
                    public CallAdapter<Call, WrapCall> get(final Type type, Annotation[] annotations, Retrofit retrofit) {
                        return new CallAdapter<Call, WrapCall>() {
                            @Override
                            public Type responseType() {
                                if (type instanceof ParameterizedType) {
                                    Type callReturnType = getParameterUpperBound(0, (ParameterizedType) type);
                                    return callReturnType;
                                }
                                return null;
                            }

                            @Override
                            public WrapCall adapt(Call<Call> call) {
                                return new WrapCall(call);
                            }
                        };
                    }
                })
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        service.getOutLine().call.enqueue(new Callback<List<Repo>>() {
            @Override
            public void onResponse(Call<List<Repo>> call, Response<List<Repo>> response) {
                handler.sendMessage(Message.obtain(null, 0, response.body().get(0).PROJECT));
            }

            @Override
            public void onFailure(Call<List<Repo>> call, Throwable throwable) {
                handler.sendMessage(Message.obtain(null, 0, "onFailure"));
            }
        });
    }

    private void getData2() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();
        GitHubService service = retrofit.create(GitHubService.class);
        service.getOutLine2().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    handler.sendMessage(Message.obtain(null, 0, response.body().string()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                handler.sendMessage(Message.obtain(null, 0, "onFailure"));
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    class Handler {
        public void handleMessage(Message msg) {
            Toast.makeText(mActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }

        ;

        public void sendMessage(Message msg) {
            handleMessage(msg);
        }
    }

}
