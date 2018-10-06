package com.example.rodrigo.sgame;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import io.fabric.sdk.android.services.concurrency.AsyncTask;

public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    ImageView startButton, settingsButton;
    private int RC_SIGN_IN = 523;
    private static final String TAG = "MainScreen";
    GoogleSignInClient mGoogleSignInClient;
    VideoView bgLoop;
    TextView taptostart;
    MediaPlayer mp=new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            this.getSupportActionBar().hide();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

            View mContentView = findViewById(R.id.fullscreen_content);
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        } catch (Exception e) {

        }

        setContentView(R.layout.activity_main_screen);
        ConstraintLayout constraintLayout= findViewById(R.id.constraintlogo);
        SignInButton sing = findViewById(R.id.sign_in_google);
        sing.setSize(SignInButton.SIZE_STANDARD);
        bgLoop = findViewById(R.id.bg_loop);
        startButton = findViewById(R.id.startGameButton);
        taptostart = findViewById(R.id.taptostart);
        settingsButton = findViewById(R.id.imageViewSetting);

        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        taptostart.setTypeface(custom_font);

        bgLoop.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
                mp.setVolume(0, 0);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //mp.setPlaybackParams(mp.getPlaybackParams().setSpeed(0.7f));// Esto será para el rush
                }
            }
        });

        bgLoop.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                //    Log.d("video", "setOnErrorListener ");
                return true;
            }
        });


        View.OnClickListener listenerStart = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
                releaseMediaPlayer();
            }
        };

        bgLoop.setOnClickListener(listenerStart);
        startButton.setOnClickListener(listenerStart);
        taptostart.setOnClickListener(listenerStart);
        constraintLayout.setOnClickListener(listenerStart   );

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFragment();
            }
        });

        sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        //Google login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Firebase login

// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();


        //login FB

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        callbackManager = CallbackManager.Factory.create();


        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email", "public_profile");

        // If using in a fragment
        // loginButton.setFragment(this);

        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:onError", exception);
            }
        });

        mp= MediaPlayer.create(this,R.raw.title_loop);
        mp.setLooping(true);
        mp.start();

    }

    @Override
    protected void onStart() {
        super.onStart();


        try {
            FirebaseUser currentUser = mAuth.getCurrentUser();
            updateUI(currentUser);
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            updateUIAccount(account);
            updateUI(currentUser);
        } catch (Exception e) {
        }
        //set and start video
        Uri uriVideoBg = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bgmain2);
        bgLoop.setVideoURI(uriVideoBg);
        bgLoop.start();

        //set animation
        new AnimationUtils();

        Animation controller = AnimationUtils.loadAnimation(this, R.anim.zoom_splash);
        startButton.startAnimation(controller);

        taptostart.setAnimation(controller);


       ;
        //mp.setDataSource();
    }

    private void updateUIAccount(GoogleSignInAccount account) {

        if (account == null) {

        } else {
            //startButton.setText(account.toJson());
        }
    }


    public void showEditFragment() {
        SettingsDialogGeneric newFragment = new SettingsDialogGeneric();
        /*newFragment.lista = levelArrayList;
        newFragment.setSongList(this);*/
        newFragment.show(getFragmentManager(), "");
        //  newFragment.show
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }


    }


    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainScreenActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // startButton.setText(user.getDisplayName());
        } else {
            //  startButton.setText("");
        }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUIAccount(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void startGame() {
        Intent i = new Intent(this, SongList.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onClick(View v) {
        startGame();


    }





    private class LoadSongs extends AsyncTask<SongList,SongList,SongList> {


        @Override
        protected void onPostExecute(SongList songList) {
            super.onPostExecute(songList);
        }

        @Override
        protected SongList doInBackground(SongList... songLists) {
            return null;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    private void releaseMediaPlayer() {
        try {
            if (mp != null) {
                if (mp.isPlaying())
                    mp.stop();
                mp.release();
                mp = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
