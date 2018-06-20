package com.kart.track24;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class utils {
        public static String test(){
       return "1234" ;
    }

    public static Map getUserName(){
    // English to Russian Dictionary
    Map<String,String> engRus = new HashMap<String,String>();

    //добавление элементов
        engRus.put("Monday", "Понедельник");
        engRus.put("Tuesday", "Вторник");
        engRus.put("Wednesday", "Среда");
        engRus.put("Thursday", "Четверг");
        engRus.put("Friday", "Пятница");
        return engRus;
    }



    public static void postRequest(String postUrl, String postBody) throws IOException {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, postBody);

        Request request = new Request.Builder()
                .url(postUrl)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                Log.d("TAG", response.body().string());
            }
        });
    }



}