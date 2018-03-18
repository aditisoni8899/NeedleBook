package com.needle.soniaditi380.books.Activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.tasks.Task;
import com.needle.soniaditi380.books.R;
import com.needle.soniaditi380.books.SaveSharedPrefrence;

public class SignIn extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_sign_in);



        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(Drive.SCOPE_FILE)
                .requestServerAuthCode("62897478377-lqovsp4j12rdj1nvjr4leegechjgucos.apps.googleusercontent.com")
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("SignInActivity", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {

        if (account != null) {
            // if user login successful then set UserName to shared pref
            SaveSharedPrefrence.setUserName(SignIn.this, account.getEmail().toString());
            Log.d("pref", "updateUI: "+SaveSharedPrefrence.getUserName(this));
            // if user login successful then set UserId to shared pref
            SaveSharedPrefrence.setUserId(SignIn.this, account.getId().toString());
            Log.d("pref", "updateUI: "+SaveSharedPrefrence.getUserName(this));
             if(account.getPhotoUrl()!=null) {
                 // if user login successful then set UserId to shared pref
                 SaveSharedPrefrence.setPhoto(SignIn.this, account.getPhotoUrl());
             }
            // if user login successful then set UserId to shared pref
            SaveSharedPrefrence.setName(SignIn.this, account.getDisplayName());

             //setting token value to the shared pref
            SaveSharedPrefrence.setAuthCode(SignIn.this, account.getServerAuthCode());
            Log.d("pref", "updateUI: "+SaveSharedPrefrence.getAuthCode(getApplicationContext()));

            Intent intent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


}
