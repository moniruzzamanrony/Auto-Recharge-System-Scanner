package com.itvillage.ars.services;

import android.content.Context;

import com.itvillage.ars.api.Apis;
import com.itvillage.ars.config.ApiClient;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class ApiServices {
    private final Context context;
    private final String LOGIN_REQUEST_BODY_FORMAT = "{ \"gameNumber\": \"%s\" , \"gameType\": \"%s\" }";

    public ApiServices(Context context) {
        this.context = context;
    }

    public Observable<String> addNewUser(String userId, String phoneNo, String email, String shopName,
                                         String mac, String serial_key, String active_date,
                                         String expaiedDate, String packageName, String price,
                                         String userName, String initialPassword, String shopAddress, String packageValidity,
                                         String role) {

        String body = "{\n" +
                "        \"userId\":\"" + userId + "\",\n" +
                "        \"phoneNo\":\"" + phoneNo + "\",\n" +
                "        \"email\":\"" + email + "\",\n" +
                "        \"shopName\":\"" + shopName + "\",\n" +
                "        \"mac\":\"" + mac + "\",\n" +
                "        \"serial_key\":\"" + serial_key + "\",\n" +
                "        \"active_date\":\"" + active_date + "\",\n" +
                "        \"expaiedDate\":\"" + expaiedDate + "\",\n" +
                "        \"packageName\":\"" + packageName + "\",\n" +
                "        \"price\":\"" + price + "\",\n" +
                "        \"userName\":\"" + userName + "\",\n" +
                "        \"initialPassword\":\"" + initialPassword + "\",\n" +
                "        \"shopAddress\":\"" + shopAddress + "\",\n" +
                "        \"packageValidity\":\"" + packageValidity + "\",\n" +
                "        \"role\":\"" + role + "\"\n" +
                "        }";

        return ApiClient.getClient(context)
                .create(Apis.class)
                .addNewUser(RequestBody.create(MediaType.parse("application/json"), body));
    }
}
