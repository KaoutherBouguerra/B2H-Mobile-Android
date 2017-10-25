package com.example.macbook.box2homeclient;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.macbook.box2homeclient.application.BaseApplication;

public class SplashScreen extends AppCompatActivity {
    ImageView relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        relative = (ImageView) findViewById(R.id.image);


        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{


                    if (BaseApplication.session.isLoggedIn())
                     BaseApplication.session.checkLogin();
                    else{
                        finish();
                        Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(intent);

                    }



                    // ActivityTransitionLauncher.with(SplashScreen.this).from(imgLogo.getRootView() ).launch(intent);

                    // overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );
                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
