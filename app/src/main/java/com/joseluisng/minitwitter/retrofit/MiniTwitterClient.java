package com.joseluisng.minitwitter.retrofit;

import com.google.gson.Gson;
import com.joseluisng.minitwitter.common.Constantes;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MiniTwitterClient {

    private static MiniTwitterClient instace = null;
    private MiniTwitterService miniTwitterService;
    private Retrofit retrofit;

    public MiniTwitterClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        miniTwitterService = retrofit.create(MiniTwitterService.class);
    }

    public static MiniTwitterClient getInstace(){
        if(instace == null){
            instace = new MiniTwitterClient();
        }

        return instace;
    }

    public MiniTwitterService getMiniTwitterService(){
        return miniTwitterService;
    }


}
