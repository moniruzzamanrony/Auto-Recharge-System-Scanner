package com.itvillage.ars.config;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

    private static Retrofit retrofit = null;
    private static int REQUEST_TIMEOUT = 60;
    private static OkHttpClient okHttpClient;
    //private static String BASE_URL = "http://10.0.2.2:9020/";
    private static String BASE_URL = "https://arwm-server-deploy.herokuapp.com/";
    //private static String BASE_URL = "http://123.49.62.227:9020/gdm/";
    //private static String BASE_URL = "http://10.0.2.15:9020";
    //private static String BASE_URL = "http://192.168.0.100:9020";

    public static Retrofit getClient(Context context) {

        if (okHttpClient == null) {
            initOkHttp(context);
        }

        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context) {

        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        final JavaNetCookieJar cookieJar = new JavaNetCookieJar(cookieManager);

        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .cookieJar(cookieJar)
                .connectTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(REQUEST_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json");


                List<Cookie> cookieList = cookieJar.loadForRequest(original.url());
                Log.e("ApiClient", cookieList.toString());

          /*  for (Cookie cookie : cookieList) {
                if ("XSRF-TOKEN".equals(cookie.name())) {

                    requestBuilder.addHeader("X-XSRF-TOKEN", cookie.value());
                    break;
                }
            }*/

//            ApplicationSharedPreferencesUtil perfUtil = new ApplicationSharedPreferencesUtil(context);
//
//            if (perfUtil.getAccessToken() != null) {
//
//                Log.d("perfUtil AccessToken", perfUtil.getAccessToken());
//                requestBuilder.addHeader("Authorization", "Bearer " + perfUtil.getAccessToken());
//            } else {
//                Log.e("AccessToken", "Not Found Access Token");
//            }
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient = httpClient.build();
    }

}
