package com.joseluisng.minitwitter.ui.auth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.joseluisng.minitwitter.ui.DashboardActivity;
import com.joseluisng.minitwitter.R;
import com.joseluisng.minitwitter.common.Constantes;
import com.joseluisng.minitwitter.common.SharedPreferencesManager;
import com.joseluisng.minitwitter.retrofit.MiniTwitterClient;
import com.joseluisng.minitwitter.retrofit.MiniTwitterService;
import com.joseluisng.minitwitter.retrofit.request.RequestSignup;
import com.joseluisng.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSignUp;
    TextView tvGoLogin;
    EditText etUsername, etEmail, etPassword;
    MiniTwitterClient miniTwitterClient;
    MiniTwitterService miniTwitterService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();


        retrofitInit();
        findViews();
        events();
        
    }

    private void retrofitInit() {
        miniTwitterClient = MiniTwitterClient.getInstace();
        miniTwitterService = miniTwitterClient.getMiniTwitterService();
    }

    private void findViews() {
        btnSignUp = findViewById(R.id.buttonSignUp);
        tvGoLogin = findViewById(R.id.textViewGoLogin);
        etUsername = findViewById(R.id.editTextUsername);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
    }

    private void events() {
        btnSignUp.setOnClickListener(this);
        tvGoLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.buttonSignUp:
                goToSignUp();
                break;
            case R.id.textViewGoLogin:
                goToLogin();
                break;
        }

    }

    private void goToSignUp() {
        String username = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if(username.isEmpty()){
            etUsername.setError("El nombre de usuario es requerido");
        }else if(email.isEmpty()){
            etEmail.setError("El correo es requerido");
        }else if(password.isEmpty()){
            etPassword.setError("La contraseña es requerido y debe tener al menos 4 caracteres");
        }else{
            String code = "UDEMYANDROID";
            RequestSignup requestSignup = new RequestSignup(username, email, password, code);
            Call<ResponseAuth> call = miniTwitterService.doSignup(requestSignup);

            call.enqueue(new Callback<ResponseAuth>() {
                @Override
                public void onResponse(Call<ResponseAuth> call, Response<ResponseAuth> response) {
                    if(response.isSuccessful()){

                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_TOKEN, response.body().getToken());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_USERNAME, response.body().getUsername());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_EMAIL, response.body().getEmail());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getPhotoUrl());
                        SharedPreferencesManager.setSomeStringValue(Constantes.PREF_CREATED, response.body().getCreated());
                        SharedPreferencesManager.setSomeBooleanValue(Constantes.PREF_ACTIVE, response.body().getActive());

                        Intent i = new Intent(SignUpActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        Toast.makeText(SignUpActivity.this, "Algo ha salido mal, revise su conexión a internet", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseAuth> call, Throwable t) {
                    Toast.makeText(SignUpActivity.this, "Error en la conexión", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void goToLogin() {
        Intent i = new Intent(SignUpActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

}
