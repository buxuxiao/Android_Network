package com.example.user.myapplication.okhttp;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.myapplication.R;

import java.io.IOException;
import java.net.URL;

import cz.msebera.android.httpclient.client.methods.RequestBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkhttpActivity extends AppCompatActivity {

    private Activity mActivity = this;

    private Button buttonGetData;

    String GET_URL = "http://101.1.3.167:7008/Liems/webservice/getOutLine";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        initView();
    }

    private void initView() {
        buttonGetData = (Button) findViewById(R.id.button1);
        buttonGetData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getData();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getData2();
                    }
                }).start();
            }
        });
    }

    private void getData() {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(GET_URL)
                .get();

        Call call = client.newCall(builder.build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                handler.sendMessage(Message.obtain(null, 0, "onFailure"));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("abc", result);
                handler.sendMessage(Message.obtain(null, 0, result));
            }
        });

    }

    private void getData2() {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder()
                .url(GET_URL)
                .get();

        Call call = client.newCall(builder.build());
        try {
            Response response = call.execute();
            handler.sendMessage(Message.obtain(null, 0, response.body().string()));
        } catch (IOException e) {
            e.printStackTrace();
            handler.sendMessage(Message.obtain(null, 0, "onFailure"));
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(mActivity, msg.obj.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
