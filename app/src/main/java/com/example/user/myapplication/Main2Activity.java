package com.example.user.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.user.myapplication.afinal.AfinalActivity;
import com.example.user.myapplication.android_async.AsyncActivity;
import com.example.user.myapplication.okhttp.OkhttpActivity;
import com.example.user.myapplication.retrofit.RetrofitActivity;
import com.example.user.myapplication.videoview.VideoViewActivity;
import com.example.user.myapplication.volley.VolleyActivity;

import java.util.ArrayList;
import java.util.List;

import network.Session;

public class Main2Activity extends AppCompatActivity {

    private Class[] clazzes={
            AsyncActivity.class,
            AfinalActivity.class,
            VolleyActivity.class,
            OkhttpActivity.class,
            RetrofitActivity.class,
            VideoViewActivity.class,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        initView();
    }

    private void initView(){
        ListView listView= (ListView) findViewById(R.id.listView);

        List<String> names=new ArrayList<>();
        for (Class clazz:clazzes){
            names.add(clazz.getSimpleName());
        }

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.setClass(Main2Activity.this,clazzes[position]);
                Main2Activity.this.startActivity(intent);
            }
        });

        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.login("15195896086","123456789");
            }
        });

        findViewById(R.id.request).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.request();
            }
        });
    }
}
