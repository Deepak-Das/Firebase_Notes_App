package com.example.notes;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class LoginAndRegister extends AppCompatActivity {

    MaterialButton buttonLogin;
    private static final String TAG = "LoginAndRegister";
    private int REQUEST_SIGN_IN=10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);


        buttonLogin=findViewById(R.id.button_login);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginAndRegisterHandler();
            }
        });
    }

    private void loginAndRegisterHandler() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                REQUEST_SIGN_IN);
    }

    public void startMainActivity(){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_SIGN_IN){
            if(resultCode==RESULT_OK){
                //SIGN IN SUCCESSFULLY.
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                Log.d(TAG, "onActivityResult: "+user.getEmail());

                if(user.getMetadata().getCreationTimestamp()==user.getMetadata().getLastSignInTimestamp()){

                    Toast.makeText(this, "Welcome new user", Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(this, "Welcome back Again", Toast.LENGTH_SHORT).show();
                }

                startMainActivity();


            }else{
                //SIGN IN FAIL.
                IdpResponse response=IdpResponse.fromResultIntent(data);
                if(response==null){
                    Log.i(TAG, "onActivityResult: User cancelled the sign request.");
                    Toast.makeText(this, "email cancelled", Toast.LENGTH_SHORT).show();
                }else {
                    Log.i(TAG, "onActivityResult: ",response.getError());
                }
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startMainActivity();
    }
}