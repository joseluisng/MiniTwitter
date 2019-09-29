package com.joseluisng.minitwitter.ui.profile;

import android.Manifest;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.joseluisng.minitwitter.common.Constantes;
import com.joseluisng.minitwitter.data.ProfileViewModel;
import com.joseluisng.minitwitter.R;
import com.joseluisng.minitwitter.retrofit.request.RequestUserProfile;
import com.joseluisng.minitwitter.retrofit.response.ResponseUserProfile;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    ImageView ivAvatar;
    EditText etUsername, etEmail, etPassword, etWebsite, etDescripcion;
    Button btnSave, btnChangePassword;
    boolean loadingData = true;
    PermissionListener allPermissionsListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileViewModel = ViewModelProviders.of(getActivity()).get(ProfileViewModel.class);
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        ivAvatar = v.findViewById(R.id.imageViewAvatar);
        etUsername = v.findViewById(R.id.editTextUserNameProfile);
        etEmail = v.findViewById(R.id.editTextEmailProfile);
        etPassword = v.findViewById(R.id.editTextCurrentPasswordProfile);
        etWebsite = v.findViewById(R.id.editTextWebsite);
        etDescripcion = v.findViewById(R.id.editTextDescripcion);
        btnSave = v.findViewById(R.id.buttonSave);
        btnChangePassword = v.findViewById(R.id.buttonChangePassword);

        // Eventos
        btnSave.setOnClickListener( view -> {
            String username = etUsername.getText().toString();
            String email = etEmail.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String website = etWebsite.getText().toString();
            String password = etPassword.getText().toString();
            if(username.isEmpty()){
                etUsername.setError("El campo de Usuario es requerido");
            }else if(email.isEmpty()){
                etEmail.setError("El campo de Email es requerido");
            }else if(password.isEmpty()){
                etPassword.setError("La Constraseña es requerido");
            }else{
                RequestUserProfile requestUserProfile = new RequestUserProfile(username, email,  descripcion,  website,  password);
                profileViewModel.updateProfile(requestUserProfile);
                Toast.makeText(getActivity(), "Enviando información al servidor", Toast.LENGTH_SHORT).show();
                btnSave.setEnabled(false);
            }
        });

        btnChangePassword.setOnClickListener( view -> {
            Toast.makeText(getActivity(), "Click change password", Toast.LENGTH_SHORT).show();
        });

        ivAvatar.setOnClickListener( view -> {
            // Invocar a la seleccion de la fotografía
            // Invocamos al método de comprobación de permisos
            checkPermissions();
        });

        // ViewModel
        profileViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(@Nullable ResponseUserProfile responseUserProfile) {
                loadingData = false;
                etUsername.setText(responseUserProfile.getUsername());
                etEmail.setText(responseUserProfile.getEmail());
                etWebsite.setText(responseUserProfile.getWebsite());
                etDescripcion.setText(responseUserProfile.getDescripcion());

                //btnSave.setEnabled(false);
                if(!responseUserProfile.getPhotoUrl().isEmpty()){
                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILE_URL + responseUserProfile.getPhotoUrl())
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }

                if(!loadingData){
                    btnSave.setEnabled(true);
                    Toast.makeText(getActivity(), "Datos Guardados Correctamente", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileViewModel.photoProfile.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                if(!photo.isEmpty()){
                    Glide.with(getActivity())
                            .load(Constantes.API_MINITWITTER_FILE_URL + photo)
                            .dontAnimate()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .centerCrop()
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
            }
        });

        return v;
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle("Permisos necesarios para poder seleccionar foto")
                .withMessage("Los permisos solicitados son necesarios para seleccionar la foto de perfil")
                .withButtonText("Aceptar")
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener(
                (PermissionListener) getActivity(),
                dialogOnDeniedPermissionListener
        );

        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check();

    }


}
