package com.test.dessertationone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

public class sign_up extends AppCompatActivity {
Button continuebtn,signinbtn,signupbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        continuebtn=findViewById(R.id.continuebtn);
        signinbtn=findViewById(R.id.signinbtn);
        signupbtn=findViewById(R.id.signupbtn);

//        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
//        String authorid = sharedPreferences.getString("authorid", "");
//
//        if(!authorid.isEmpty()){
//
//            Intent a = new Intent(sign_up.this,MainActivity.class);
//            startActivity(a);
//            finish();
//        }

//        List<Integer> imageIds = Arrays.asList(R.drawable.exp_img, R.drawable.slide_2_img, R.drawable.slide_3_img);
//        List<String> titles = Arrays.asList("Use order research work as guide", "Upload your reserch work and earn money ", "Reasearch work made easy");
//        List<String> descriptions = Arrays.asList("Use order research work as guide", "Upload your reserch work and earn money", "Reasearch work made easy");
//
//        ImagePagerAdapter adapter = new ImagePagerAdapter(imageIds,titles,descriptions);
//        ViewPager viewPager = findViewById(R.id.view_pager);
//        viewPager.setAdapter(adapter);


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(sign_up.this,MainActivity.class);
                startActivity(j);
            }
        });


        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent z = new Intent(sign_up.this,sign_in.class);
                startActivity(z);
            }
        });

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent y = new Intent(sign_up.this,create_account.class);
                startActivity(y);
            }
        });


    }


}