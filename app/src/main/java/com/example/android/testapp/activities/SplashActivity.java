package com.example.android.testapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.testapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SplashActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    boolean flag = false;
    TextView ad;
    TextView ur;
    ImageView cupImage;
    TextView cup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_splash);
        ad = (TextView) findViewById(R.id.ad);
        ur = (TextView) findViewById(R.id.ur);
        cupImage = (ImageView) findViewById(R.id.cup_image);
        cup = (TextView) findViewById(R.id.cup);
        authStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                FirebaseUser user = mAuth.getCurrentUser();
                flag = (user != null);
            }
        };

        startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(flag)
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                else
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            }
        }, 3000);



    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);

    }

    public void startAnimation() {
        mAuth.addAuthStateListener(authStateListener);

        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
        fadeIn.setDuration(2500);
        AnimationSet animation = new AnimationSet(false); //change to false
        animation.addAnimation(fadeIn);
        cupImage.setAnimation(animation);
        Animation leftToRight = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.left_to_right);
        Animation rightToLeft = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.right_to_left);
        ad.startAnimation(leftToRight);
        cup.startAnimation(rightToLeft);


    }

}
