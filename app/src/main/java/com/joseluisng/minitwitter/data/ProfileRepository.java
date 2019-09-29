package com.joseluisng.minitwitter.data;

import android.arch.lifecycle.MutableLiveData;
import android.widget.Toast;

import com.joseluisng.minitwitter.common.Constantes;
import com.joseluisng.minitwitter.common.MyApp;
import com.joseluisng.minitwitter.common.SharedPreferencesManager;
import com.joseluisng.minitwitter.retrofit.AuthTwitterClient;
import com.joseluisng.minitwitter.retrofit.AuthTwitterService;
import com.joseluisng.minitwitter.retrofit.request.RequestUserProfile;
import com.joseluisng.minitwitter.retrofit.response.ResponseUploadPhoto;
import com.joseluisng.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {

    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;
    MutableLiveData<String> photoProfile;

    ProfileRepository(){

        authTwitterClient = AuthTwitterClient.getInstace();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if(photoProfile == null){
            photoProfile = new MutableLiveData<>();
        }

    }

    public MutableLiveData<String> getPhotoProfile(){
        return photoProfile;
    }

    public MutableLiveData<ResponseUserProfile> getProfile(){
        if(userProfile == null){
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(),"Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });


        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile){
        Call<ResponseUserProfile> call = authTwitterService.updateProfile(requestUserProfile);

        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if(response.isSuccessful()){
                    userProfile.setValue(response.body());
                }else{
                    Toast.makeText(MyApp.getContext(),"Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void uploadPhoto(String photoPath){
        File file = new File(photoPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        Call<ResponseUploadPhoto> call = authTwitterService.uploadProfilePhoto(requestBody);

        call.enqueue(new Callback<ResponseUploadPhoto>() {
            @Override
            public void onResponse(Call<ResponseUploadPhoto> call, Response<ResponseUploadPhoto> response) {
                if(response.isSuccessful()){
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());

                }else{
                    Toast.makeText(MyApp.getContext(),"Algo ha salido mal", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseUploadPhoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(),"Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
