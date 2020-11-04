package com.itvillage.ars;


import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;


public class ServerConnector {


    public void requestSend(String url, Context context) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response response = client.newCall(request).execute();
            Log.e("Result", response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
