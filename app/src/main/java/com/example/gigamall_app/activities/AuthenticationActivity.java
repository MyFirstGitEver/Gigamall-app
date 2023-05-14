package com.example.gigamall_app.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gigamall_app.R;
import com.example.gigamall_app.tools.Tools;
import com.example.gigamall_app.entities.UserEntity;
import com.example.gigamall_app.fragments.PasswordFragment;
import com.example.gigamall_app.fragments.VerificationFragment;
import com.example.gigamall_app.services.UserService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationActivity extends AppCompatActivity
{
    private int registeredUserId = 0;
    private CallbackManager manager;

    private final ActivityResultLauncher<IntentSenderRequest> registerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                if(result.getResultCode() == Activity.RESULT_OK){

                    try{
                        SignInCredential credential = Identity.getSignInClient(AuthenticationActivity.this)
                                .getSignInCredentialFromIntent(result.getData());

                        UserEntity user = new UserEntity(
                                credential.getId(), "", credential.getDisplayName(), 100);

                        UserService.service.register(user, 1).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                VerificationFragment fragment = new VerificationFragment();
                                Bundle bundle = new Bundle();
                                bundle.putString("user", credential.getId());
                                bundle.putInt("id", response.body());

                                fragment.setArguments(bundle);
                                fragment.show(getSupportFragmentManager(), "verification");
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {
                                Log.d("error", "wut?");
                            }
                        });
                    }catch (ApiException e) {
                        Toast.makeText(AuthenticationActivity.this,
                                "Failed to sign in :(", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            }
    );

    private final View.OnClickListener googleLoginListener = v ->{
        signIn();
    };

    private final View.OnClickListener facebookLoginListener = v ->{
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
    };

    private EditText userNameEditTxt, passwordEditTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        getSupportActionBar().hide();

        if(savedInstanceState != null){
            registeredUserId = savedInstanceState.getInt("registeredUserId");
        }

        userNameEditTxt = findViewById(R.id.userNameEditTxt);
        passwordEditTxt = findViewById(R.id.passwordEditTxt);

        AppCompatButton loginBtn = findViewById(R.id.loginBtn);
        ImageButton googleLoginBtn = findViewById(R.id.googleLoginBtn);
        ImageButton facebookLoginBtn = findViewById(R.id.facebookLoginBtn);

        loginBtn.setOnClickListener(v ->{
            String userName = userNameEditTxt.getText().toString();
            String password = passwordEditTxt.getText().toString();

            UserService.service.login(password, userName).enqueue(new Callback<UserEntity>() {
                @Override
                public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                    handleLoginResult(response, userName, password);
                }

                @Override
                public void onFailure(Call<UserEntity> call, Throwable t) {
                    Log.d("ada", ":dasdas");
                }
            });
        });

        googleLoginBtn.setOnClickListener(googleLoginListener);
        facebookLoginBtn.setOnClickListener(facebookLoginListener);

        processFragmentResults();
        setUpFacebookLoginCallback();
        loginUsingSaved();
    }

    private void processFragmentResults() {
        getSupportFragmentManager().setFragmentResultListener("verified", this, (key, result) ->{
            PasswordFragment fragment = new PasswordFragment();
            fragment.show(getSupportFragmentManager(), "password");

            registeredUserId = result.getInt("userId");
        });

        getSupportFragmentManager().setFragmentResultListener("last step", this, (key, result) ->{
            UserService.service.complete(result.getString("pass"), registeredUserId).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    userNameEditTxt.setText(response.body());
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Log.d("dasda", "dadas");
                }
            });
        });
    }

    private void signIn() {
        GetSignInIntentRequest request =
                GetSignInIntentRequest.builder()
                        .setServerClientId(getString(R.string.server_client_id))
                        .build();


        Identity.getSignInClient(this)
                .getSignInIntent(request)
                .addOnSuccessListener(
                        result -> {
                            registerLauncher.launch(new IntentSenderRequest.Builder(result.getIntentSender()).build());
                        })
                .addOnFailureListener(
                        e -> {
                            Log.d("adasd", "Google Sign-in failed");
                        });
    }

    private void setUpFacebookLoginCallback() {
        manager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(manager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Profile profile = Profile.getCurrentProfile();

                        UserEntity user = new UserEntity(
                                Tools.removeAccent(profile.getLastName()),
                                "",
                                profile.getFirstName() + " " + profile.getMiddleName() + " " + profile.getLastName(),
                                100);

                        UserService.service.register(user, 0).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(Call<Integer> call, Response<Integer> response) {
                                PasswordFragment fragment = new PasswordFragment();
                                fragment.show(getSupportFragmentManager(), "last step");

                                registeredUserId = response.body();
                            }

                            @Override
                            public void onFailure(Call<Integer> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onCancel() {
                        Log.d("adas", "dadas");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.d("adas", "dadas");
                    }
                });
    }

    private void saveUserInfo(String userName, String password){
        File file = new File(getFilesDir(), "login");

        try {
            file.createNewFile();

            FileOutputStream fOut = new FileOutputStream(file);

            fOut.write((userName + "\t" + password).getBytes(StandardCharsets.UTF_8));

            fOut.close();
        } catch (IOException e) {
            Toast.makeText(AuthenticationActivity.this,
                    "Can't save your login information", Toast.LENGTH_SHORT).show();
        }
    }

    private void loginUsingSaved() {
        try {
            File file = new File(getFilesDir(), "login");

            if(!file.exists()){
                return;
            }

            FileInputStream fIn = new FileInputStream(file);

            byte[] loginInfo = new byte[(int) file.length()];
            fIn.read(loginInfo);
            String info = new String(loginInfo);

            int index = info.indexOf('\t');
            String password = info.substring(0, index);
            String userName = info.substring(index + 1);

            UserService.service.login(userName, password).enqueue(new Callback<UserEntity>() {
                @Override
                public void onResponse(Call<UserEntity> call, Response<UserEntity> response) {
                    handleLoginResult(response, null, null);
                }

                @Override
                public void onFailure(Call<UserEntity> call, Throwable t) {
                    Log.d("dasdas", "dasd");
                }
            });

            fIn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLoginResult(Response<UserEntity> response, String userName, String password){
        if(response.code() == HttpURLConnection.HTTP_NOT_FOUND){
            Toast.makeText(AuthenticationActivity.this,
                    "Please check your user name or password!", Toast.LENGTH_SHORT).show();

            return;
        }
        else if(response.code() == 226){
            int confirmationId = response.body().getId();

            VerificationFragment fragment = new VerificationFragment();
            Bundle bundle = new Bundle();
            bundle.putString("user", userName);
            bundle.putInt("id", confirmationId);
            bundle.putBoolean("setCountDown", false);

            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), "verification");
        }
        else{
            Intent intent = new Intent(AuthenticationActivity.this, GigaMallActivity.class);
            startActivity(intent);

            if(userName != null){
                saveUserInfo(userName, password);
            }

            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("registeredUserId", registeredUserId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        manager.onActivityResult(requestCode, resultCode, data);
    }
}