package com.richify.iamrich;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Set;

public class HomeActivity extends FragmentActivity {

    private static final String TAG = "HomeActivity";
    private static final String DIALOG_TAG = "dialog";

    // Declare database ref
    private DatabaseReference mDatabaseRef;

    ProfileTracker profileTracker;
    CallbackManager callbackManager;
    ImageView profilePicture;
    TextView id;
    TextView infoLabel;
    TextView info;
    TextView locationLabel;
    TextView location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        profilePicture = findViewById(R.id.profile_image);
        id = findViewById(R.id.id);
        infoLabel = findViewById(R.id.info_label);
        info = findViewById(R.id.info);
        locationLabel = findViewById(R.id.location_label);
        location = findViewById(R.id.location);

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (currentProfile != null) {
                    displayProfileInfo(currentProfile);
                }
            }
        };

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            Profile currentProfile = Profile.getCurrentProfile();
            if (currentProfile != null) {
                displayProfileInfo(currentProfile);
            } else {
                Profile.fetchProfileForCurrentAccessToken();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Read from the database
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.startTracking();
    }

    public void onLogout(View view) {
        LoginManager.getInstance().logOut();
        launchLoginActivity();

        setResult(RESULT_OK);
        finish();
    }

    private void displayProfileInfo(Profile profile) {

        callbackManager = CallbackManager.Factory.create();
        Set permissions = AccessToken.getCurrentAccessToken().getPermissions();
        if (permissions.contains("user_location")) {
            fetchLocation();
        } else {
            LoginManager loginManager = LoginManager.getInstance();
            loginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    fetchLocation();
                }

                @Override
                public void onCancel() {
                    String permissionMessage = getResources()
                            .getString(R.string.location_permission_message);
                    Toast.makeText(HomeActivity.this, permissionMessage, Toast.LENGTH_LONG)
                            .show();
                }

                @Override
                public void onError(FacebookException error) {

                }
            });
            loginManager.logInWithReadPermissions(this, Arrays.asList("user_location"));
        }

        String profileId = profile.getId();
        id.setText(profileId);

        String name = profile.getName();
        info.setText(name);
        infoLabel.setText(R.string.name_label);

        Uri profilePictureUri = profile.getProfilePictureUri(100, 100);
        displayProfilePicture(profilePictureUri);
    }

    private void displayProfilePicture(Uri uri) {
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(HomeActivity.this)
                .load(uri)
                .transform(transformation)
                .into(profilePicture);
    }

    private void fetchLocation() {
        Bundle parameters = new Bundle();
        parameters.putString("fields", "location");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                parameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        if (response.getError() != null) {
                            Toast.makeText(HomeActivity.this,
                                    response.getError().getErrorMessage(),
                                    Toast.LENGTH_LONG).show();
                            return;
                        }

                        JSONObject jsonResponse = response.getJSONObject();
                        try {
                            JSONObject locationObj = jsonResponse.getJSONObject("location");
                            String locationString = locationObj.getString("name");
                            location.setText(locationString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
        ).executeAsync();
    }

    private void launchLoginActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
