package com.joseluisng.minitwitter.retrofit;

import com.joseluisng.minitwitter.common.Constantes;
import com.joseluisng.minitwitter.common.SharedPreferencesManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_TOKEN);
        Request request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+token).build();
        return chain.proceed(request);
    }
}
