package com.example.user.myapplication.volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by hulifei on 2018/4/23.
 */

public class MySingleton {
    private static MySingleton mySingleton;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private static Context context;

    private MySingleton(Context context){
        this.context=context;
        requestQueue=getRequestQueue();

        imageLoader=new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {

                private LruCache<String,Bitmap> cache=new LruCache<>(20);
                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url,bitmap);
                    }
                });
    }

    public static synchronized MySingleton getInstance(Context context){
        if(mySingleton==null){
            mySingleton=new MySingleton(context);
        }

        return mySingleton;
    }

    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }



}
