package com.test.dessertationone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class flash_screen extends AppCompatActivity {
    private static  int SPLASH_SCREEN=5000;



    String showonboardscreen = "false";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_screen);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        SharedPreferences sharedPreferences = getSharedPreferences("preactivity", MODE_PRIVATE);
        showonboardscreen = sharedPreferences.getString("showflashscreen", "");

        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){



                Intent intent = new Intent(this, onboarding.class);
                startActivity(intent);
                finish();


        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(flash_screen.this,onboarding.class);
                    startActivity(intent);
                    finish();
                }
            },SPLASH_SCREEN);
        }

//        if(showonboardscreen.equals("true")) {
//            Intent intent = new Intent(this,sign_up.class);
//            startActivity(intent);
//            finish();
//        }






//        SharedPreferences sharedPref = getSharedPreferences("preactivity", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//
//        editor.putString("showflashscreen", "true");
//
//
//        // to save our data with key and value.
//        editor.apply();

    }


}