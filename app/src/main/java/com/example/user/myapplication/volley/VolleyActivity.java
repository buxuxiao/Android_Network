package com.example.user.myapplication.volley;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BaseHttpStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.user.myapplication.R;

public class VolleyActivity extends AppCompatActivity {

    private Activity mActivity = this;

    String GET_URL = "http://101.1.3.167:7008/Liems/webservice/getOutLine";

    String IMAGE_URL = "http://img0.imgtn.bdimg.com/it/u=284211779,458262523&fm=27&gp=0.jpg";

    private Button buttonGet;
    private ImageView imageView1;
    private Button buttonImageLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        initView();
    }

    private void initView() {
        buttonGet = (Button) findViewById(R.id.button1);
        imageView1 = (ImageView) findViewById(R.id.imageView1);
        buttonImageLoad = (Button) findViewById(R.id.button2);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get2();
            }
        });

        buttonImageLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage();
            }
        });
    }

    private void get() {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mActivity, "onResponse", Toast.LENGTH_SHORT).show();
                Log.d("abc", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mActivity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

        /* String TAG = "VolleyTAG";
        stringRequest.setTag(TAG);
        requestQueue.cancelAll(TAG);*/


        requestQueue.add(stringRequest);
    }

    private void get2() {
        RequestQueue requestQueue;
        Cache cache = new DiskBasedCache(getExternalCacheDir(), 1024 * 1024);
        Network network = new BasicNetwork(new HurlStack());
        requestQueue = new RequestQueue(cache, network);
        requestQueue.start();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(mActivity, "onResponse", Toast.LENGTH_SHORT).show();
                Log.d("abc", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(mActivity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void loadImage() {
        MySingleton mySingleton = MySingleton.getInstance(this);
        ImageLoader imageLoader = mySingleton.getImageLoader();

        ImageLoader.ImageListener imageListener = ImageLoader.getImageListener(imageView1,
                R.drawable.ic_launcher_background, R.drawable.ic_launcher_foreground);

        imageLoader.get(IMAGE_URL,imageListener,200,200);

    }
}
