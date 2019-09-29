package com.joseluisng.minitwitter.retrofit;

import com.joseluisng.minitwitter.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {

    private static AuthTwitterClient instace = null;
    private AuthTwitterService miniTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient(){
        // Incluir en la cabezera de la petición el TOKEN que autoriza el usuario
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient client = okHttpClientBuilder.build();


        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        miniTwitterService = retrofit.create(AuthTwitterService.class);
    }

    public static AuthTwitterClient getInstace(){
        if(instace == null){
            instace = new AuthTwitterClient();
        }

        return instace;
    }

    public AuthTwitterService getAuthTwitterService(){
        return miniTwitterService;
    }


}
