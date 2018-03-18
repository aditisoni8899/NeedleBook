package com.needle.soniaditi380.books.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.needle.soniaditi380.books.Activity.SignIn;
import com.needle.soniaditi380.books.R;

/**
 * Created by Aditya on 23-Jan-18.
 */

//splash Screen
public class SplashScreen extends Activity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        ImageView imageView = findViewById(R.id.spl);
        //setting animation to the image for fading effect
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);

        //code to disappear splash screen after 3 sec by threading
        Thread timer = new Thread(){

     @Override

            public void run() {
         try {

                    sleep(1000);
                    //after 3 sec MainActivity will  get open with the help of intent
                    Intent intent = new Intent(getApplicationContext(),SignIn.class);
                    startActivity(intent);

                    super.run();

                } catch (InterruptedException e) {

                    e.printStackTrace();

                }
                finally {
                    finish();
                }

            }

        };



        timer.start(); // run() method will invoke from this statement
    }
}
