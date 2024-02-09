package com.test.dessertationone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class onboarding extends AppCompatActivity {


    //Variables
    ViewPager viewPager;
    LinearLayout dotsLayout;
    SliderAdapter sliderAdapter;
    TextView[] dots;
    Button btnNext;
    Button btnStart,skipNext;
    Animation animation;
    int currentPos;

    String showonboardscreen = "false";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_onboarding);
        // hide the action bar


//        SharedPreferences sharedPreferences = getSharedPreferences("preactivity", MODE_PRIVATE);
//        showonboardscreen = sharedPreferences.getString("showflashscreen", "");
//
//        if(showonboardscreen.equals("true")) {
//            Intent intent = new Intent(this,sign_up.class);
//            startActivity(intent);
//            finish();
//        }


        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");



        SharedPreferences pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
        if(pref.getBoolean("activity_executed", false)){


            if(!authorid.isEmpty()){

                Intent a = new Intent(this,MainActivity.class);
                startActivity(a);
                finish();
            }else {
                Intent intent = new Intent(this, sign_up.class);
                startActivity(intent);
                finish();
            }

        } else {
            SharedPreferences.Editor ed = pref.edit();
            ed.putBoolean("activity_executed", true);
            ed.commit();
        }


        //getSupportActionBar().hide();

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



        skipNext = (Button)findViewById(R.id.skip_btn);
//        btnNext = findViewById(R.id.next_btn);
//        btnNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                viewPager.setCurrentItem(currentPos + 1);
//
//                if(currentPos>0){
//                    btnStart.setVisibility(View.VISIBLE);
//                    btnNext.setVisibility(View.INVISIBLE);
//
//                }
//
//
//
//            }
//        });
        btnStart = (Button)findViewById(R.id.start_btn);



        btnStart.setVisibility(View.INVISIBLE);
        //Hooks
        viewPager = findViewById(R.id.slider);
        dotsLayout = findViewById(R.id.dots);


        //Call adapter
        sliderAdapter = new SliderAdapter(this);
        viewPager.setAdapter(sliderAdapter);

        //Dots
        addDots(0);
        viewPager.addOnPageChangeListener(changeListener);
    }

    // public void skip(View view) {
    //    startActivity(new Intent(this, UserDashboard.class));
    //    finish();
    //}

    public void start(View view){

        Intent i = new Intent(onboarding.this,sign_up.class);
        startActivity(i);
    }

    public void next(View view) {
        viewPager.setCurrentItem(currentPos + 1);
    }

    private void addDots(int position) {

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);

            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[position].setTextColor(getResources().getColor(R.color.black));
        }

    }


    ViewPager.OnPageChangeListener changeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            currentPos = position;

            if (position == 1) {

                btnStart.setVisibility(View.INVISIBLE);
//               btnNext.setVisibility(View.INVISIBLE);
                skipNext.setVisibility(View.VISIBLE);
            }
            else if (position == 0) {
                btnStart.setVisibility(View.INVISIBLE);
//               btnNext.setVisibility(View.VISIBLE);
                skipNext.setVisibility(View.INVISIBLE);
            }
            else if (position == 2) {
                btnStart.setVisibility(View.VISIBLE);
//               btnNext.setVisibility(View.INVISIBLE);
                skipNext.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

//    public void moveToSecondary(){
//        // use an intent to travel from one activity to another.
//        Intent intent = new Intent(this,sign_up.class);
//        startActivity(intent);
//    }


}