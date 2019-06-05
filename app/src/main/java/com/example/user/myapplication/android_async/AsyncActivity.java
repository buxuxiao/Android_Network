package com.example.user.myapplication.android_async;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.user.myapplication.R;
import com.example.user.myapplication.android_async.client.RestClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.DataAsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestHandle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cz.msebera.android.httpclient.Header;

public class AsyncActivity extends AppCompatActivity {

    private Activity mActivity = this;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async);

        initView();
        initData();
    }

    private void initView() {
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getData();
                downFile();
            }
        });
    }

    private void initData() {

    }

    private void getData() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.show();
       RestClient.get("getOutLine", null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes) {
                dialog.dismiss();
                String result=new String(bytes);
                Log.d("abc",result);
            }

            @Override
            public void onFailure(int i, cz.msebera.android.httpclient.Header[] headers, byte[] bytes, Throwable throwable) {
                dialog.dismiss();
                Toast.makeText(mActivity, "network fail", Toast.LENGTH_SHORT).show();
            }
        });

        RestClient.post("getOutLine", null, new BaseJsonHttpResponseHandler<JSONArray>() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, JSONArray response) {
                dialog.dismiss();
                String result = null;
                try {
                    result = response.getJSONObject(0).optString("PROJECT");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("abc", result);
                Toast.makeText(mActivity, result, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, JSONArray errorResponse) {
                dialog.dismiss();
                Toast.makeText(mActivity, "network fail", Toast.LENGTH_SHORT).show();
            }

            @Override
            protected JSONArray parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                return new JSONArray(rawJsonData);
            }
        });

        RestClient.post("",null,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
            }
        });


    }

    private void downFile() {
        button1.setText("0%");
        File file = new File(getExternalCacheDir(), "sxgf.apk");
        String DOWNLOAD_URL = "http://app-global.pgyer.com/9a6c4b3bb67fab767cfcb214055f1bb6.apk?attname=sxgf.apk&sign=790b958179a929551c9f23941d0fa4ef&t=5add3eeb";
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(DOWNLOAD_URL, null, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                Toast.makeText(mActivity, "onFailure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                Toast.makeText(mActivity, "onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                super.onProgress(bytesWritten, totalSize);
                float percent = 100.0f * bytesWritten / totalSize;
                button1.setText(String.format("%.2f%%", percent));
            }
        });

    }

}
