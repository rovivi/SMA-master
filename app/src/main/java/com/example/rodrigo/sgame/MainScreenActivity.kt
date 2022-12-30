package com.example.rodrigo.sgame;

import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.rodrigo.sgame.CommonGame.CustomSprite.ThreadSprite;



public class MainScreenActivity extends AppCompatActivity implements View.OnClickListener {
    //private FirebaseAuth mAuth;
    ImageView startButton, settingsButton;
    private int RC_SIGN_IN = 523;
    private static final String TAG = "MainScreen";
    //GoogleSignInClient mGoogleSignInClient;
    VideoView bgLoop;
    TextView taptostart, tv___start;
    MediaPlayer mp = new MediaPlayer();
    Animation fadeOut, fadeIn;

    //Sprite testSprite, testSprite2, testSprite3, testSprite4, testSprite5, testSprite6;
    ThreadSprite threadSprite;
    //private AdView mAdView;

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
        ConstraintLayout constraintLayout = findViewById(R.id.constraintlogo);
       // SignInButton sing = findViewById(R.id.sign_in_google);
        //sing.setSize(SignInButton.SIZE_STANDARD);
        bgLoop = findViewById(R.id.bg_loop);
        startButton = findViewById(R.id.startGameButton);
        taptostart = findViewById(R.id.taptostart);
        settingsButton = findViewById(R.id.imageViewSetting);
        tv___start = findViewById(R.id.tv___start);
      //  mAdView = findViewById(R.id.adbaner1);

        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out_start);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeIn.setDuration(250);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                showLoadingAnimation();
                releaseMediaPlayer();
                startGame();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startButton.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/font.ttf");
        taptostart.setTypeface(custom_font);

        bgLoop.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVolume(0, 0);
        });

        bgLoop.setOnErrorListener((mp, what, extra) -> {
            //    Log.d("video", "setOnErrorListener ");
            return true;
        });


        View.OnClickListener listenerStart = v -> {
            startButton.setVisibility(View.VISIBLE);
            startButton.setAnimation(fadeIn);
            //showLoadingAnimation();


        };

        bgLoop.setOnClickListener(listenerStart);
        startButton.setOnClickListener(listenerStart);
        tv___start.setOnClickListener(listenerStart);
        taptostart.setOnClickListener(listenerStart);
        constraintLayout.setOnClickListener(listenerStart);

        startButton.setOnClickListener(v -> {

        });

        settingsButton.setOnClickListener(v -> showEditFragment());

    //    sing.setOnClickListener(v -> signIn());
//
//        //Google login
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//
//        //Firebase login

// ...
//// Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();
//        //login FB


        // If using in a fragment
        // loginButton.setFragment(this);

        // Callback registration


        mp = MediaPlayer.create(this, R.raw.title_loop);
//        mAdView.setAdListener(new AdListener() {
//
//            @Override
//            public void onAdLoaded() {
//                Toast.makeText(getBaseContext(), "se ha cargado", Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                Toast.makeText(getBaseContext(), "no ha cargado error :" + errorCode, Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//            }
//
//
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//
//        try {
//            FirebaseUser currentUser = mAuth.getCurrentUser();
//            updateUI(currentUser);
//            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//            updateUIAccount(account);
//            updateUI(currentUser);
//        } catch (Exception e) {
//        }
        //set and start video
        Uri uriVideoBg = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.bgmain2);
        bgLoop.setVideoURI(uriVideoBg);


        //set animation
        //new AnimationUtils();

        Animation controller = AnimationUtils.loadAnimation(this, R.anim.zoom_splash);
        startButton.startAnimation(fadeOut);
        taptostart.setAnimation(controller);
        if (mp != null) {
            mp.setLooping(true);
            mp.start();
        }
//
//        MobileAds.initialize(this, "ca-app-pub-9689972331812584~5469764045");
//
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//
//        mAdView.loadAd(adRequest);
//

    }

//    private void updateUIAccount(GoogleSignInAccount account) {
//
//        if (account == null) {
//
//        } else {
//            //startButton.setText(account.toJson());
//        }
//    }


    public void showEditFragment() {

        if (mp != null) {
            mp.pause();

        }
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        /*
        SettingsDialogGeneric newFragment = new SettingsDialogGeneric();
        newFragment.lista = levelArrayList;
        newFragment.setSongList(this);*/
        //newFragment.show(getFragmentManager(), "");
        //  newFragment.show
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
    }



//
//    private void updateUI(FirebaseUser user) {
//        if (user != null) {
//            // startButton.setText(user.getDisplayName());
//        } else {
//            //  startButton.setText("");
//        }
//
//    }
//
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//
//
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            // Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }

    @Override
    protected void onRestart() {
        super.onRestart();


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (bgLoop != null) {
            bgLoop.start();
        }
    }

//    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
//        try {
//            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
//
//            // Signed in successfully, show authenticated UI.
//            updateUIAccount(account);
//        } catch (ApiException e) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
//        }
//    }

    private void startGame() {
        Intent i = new Intent(this, SongList.class);
        startActivity(i);
        finish();
    }


    @Override
    public void onClick(View v) {
    //    startGame();


    }


//    private class LoadSongs extends AsyncTask<SongList, SongList, SongList> {
//
//
//        @Override
//        protected void onPostExecute(SongList songList) {
//            super.onPostExecute(songList);
//        }
//
//        @Override
//        protected SongList doInBackground(SongList... songLists) {
//            return null;
//        }
//    }


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


    public void showLoadingAnimation() {
        FragmenStartMenu newFragment = new FragmenStartMenu();//newFragment.setSongList(this);
        newFragment.loadingScreen = true;
        newFragment.show(getFragmentManager(), "");

    }
}
