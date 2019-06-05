package network;

import android.os.Handler;
import android.os.Message;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import cz.msebera.android.httpclient.client.methods.RequestBuilder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hulifei on 2018/8/29.
 */

public class HttpUtil {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            callBack.rendView(msg.obj.toString());
        }
    };

    CallBack callBack = null;

    public void send(String url, RequestParams param, CallBack callBack) {
        send(url, null, param, callBack);
    }

    public void send(String url, Headers headers, RequestParams param, CallBack callBack) {
        this.callBack = callBack;

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder()
                .url(url);

        Iterator<Map.Entry<String, String>> iterator = param.entrySet().iterator();
        FormBody.Builder formBuild = new FormBody.Builder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            formBuild.add(entry.getKey(), entry.getValue());
        }

        if (headers != null) {
            builder.headers(headers);
        }

        client.newCall(builder.post(formBuild.build()).build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handler.sendMessage(Message.obtain(handler, 0, null));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handler.sendMessage(Message.obtain(handler, 0, response.body().string()));
            }
        });
    }
}
