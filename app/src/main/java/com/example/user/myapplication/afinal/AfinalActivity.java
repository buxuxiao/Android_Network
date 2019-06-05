package com.example.user.myapplication.afinal;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.myapplication.R;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import net.tsz.afinal.http.HttpHandler;

import java.io.File;
import java.io.FileInputStream;

public class AfinalActivity extends AppCompatActivity {

    private Activity mActivity = this;

    private Button buttonGet;
    private Button buttonDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_afinal);

        initView();
    }

    private void initView() {
        buttonGet = (Button) findViewById(R.id.button1);
        buttonDownload = (Button) findViewById(R.id.button2);

        buttonGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get();
            }
        });

        buttonDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down();
            }
        });


    }

    private void get() {
        String GET_URL = "http://101.1.3.167:7008/Liems/webservice/getOutLine";
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.get(GET_URL, null, new AjaxCallBack<String>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(mActivity, "onFailure", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(String s) {
                Toast.makeText(mActivity, "onSuccess", Toast.LENGTH_SHORT).show();
                Log.d("abc", s);
            }
        });
    }


    private void post(){
        FinalHttp http=new FinalHttp();
        AjaxParams params=new AjaxParams();
        params.put("","");
//        params.put("",new FileInputStream((String) null));
    }


    private void down() {
        buttonGet.setText("0%");
        File file = new File(getExternalCacheDir(), "sxgf.apk");
        String DOWNLOAD_URL = "http://app-global.pgyer.com/9a6c4b3bb67fab767cfcb214055f1bb6.apk?attname=sxgf.apk&sign=08944554b4517d6164b7186c4460257b&t=5add52e9";

        FinalHttp http=new FinalHttp();
           http.download(DOWNLOAD_URL, file.getAbsolutePath(), new AjaxCallBack<File>() {
            @Override
            public void onLoading(long count, long current) {
                float percent = 100.0f * current / count;
                buttonGet.setText(String.format("%.2f%%", percent));
            }

            @Override
            public void onSuccess(File file) {
                Toast.makeText(mActivity, "onSuccess", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Toast.makeText(mActivity, "onFailure", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
