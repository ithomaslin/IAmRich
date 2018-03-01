package com.richify.iamrich;

import java.util.Arrays;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.richify.iamrich.models.User;
import com.richify.iamrich.surface.SurfaceTextAnimation;
import su.levenetc.android.textsurface.TextSurface;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String EMAIL = "email";
    private TextSurface textSurface;
    private FirebaseAuth mAuth;

    // Declare database ref
    private DatabaseReference mDatabaseRef;

    AppEventsLogger logger;
    LoginButton fbLoginButton;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        com.facebook.AccessToken loginToken = com.facebook.AccessToken.getCurrentAccessToken();
        if (loginToken != null) {
            launchHomeActivity();
        }

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        logger = AppEventsLogger.newLogger(this);
        fbLoginButton = findViewById(R.id.facebook_login_button);
        fbLoginButton.setReadPermissions(Arrays.asList(EMAIL));

        // Login button callback registration
        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                LoginOperation loginOperation = new LoginOperation();
                loginOperation.execute(loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                String toastMessage = error.getMessage();
                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_LONG).show();
            }
        });

        textSurface = findViewById(R.id.text_surface);
        textSurface.postDelayed(new Runnable() {
            @Override
            public void run() {
                show();
            }
        }, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void launchHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void show() {
        textSurface.reset();
        SurfaceTextAnimation.play(textSurface, getAssets(), MainActivity.this);
    }

    private class LoginOperation extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String token = strings[0];
            AuthCredential credential = FacebookAuthProvider.getCredential(token);
            mAuth.signInWithCredential(credential);

            FirebaseUser user = mAuth.getCurrentUser();
            final String uid = user.getUid();
            final String fullName = user.getDisplayName();
            final String email = user.getEmail();
            final Double wealth = 199.99;

            mDatabaseRef.child("users").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        writeNewUser(uid, fullName, email, wealth);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            launchHomeActivity();
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        private void writeNewUser(String uid, String fullName, String email, Double wealth) {
            User user = new User(fullName, email, wealth);
            mDatabaseRef.child("users").child(uid).setValue(user);
        }
    }
}
