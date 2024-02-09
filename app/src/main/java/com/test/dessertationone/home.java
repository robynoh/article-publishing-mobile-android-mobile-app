package com.test.dessertationone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class home extends AppCompatActivity {

    // Declare variables
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;



    ViewPagerFragmentAdapter adapter;

    // array for tab labels
    private String[] labels = new String[]{"Latest","Saved", "Upload"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar=findViewById(R.id.toolbar);


        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");

        if(authorid.isEmpty()){

            Intent a = new Intent(home.this,signin_remind.class);
            startActivity(a);
        }

        // call function to initialize views
        init();

        // bind and set tabLayout to viewPager2 and set labels for every tab
        new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            tab.setText(labels[position]);
        }).attach();

        // set default position to 1 instead of default 0
        viewPager2.setCurrentItem(0, false);
    }

    private void init() {
        // initialize tabLayout
        tabLayout = findViewById(R.id.tablayout);
        // initialize viewPager2
        viewPager2 = findViewById(R.id.viewpager);
        // create adapter instance
        adapter = new ViewPagerFragmentAdapter(this);
        // set adapter to viewPager2
        viewPager2.setAdapter(adapter);

        // remove default elevation of actionbar
        getSupportActionBar().setElevation(0);
    }

    // create adapter to attach fragments to viewpager2 using FragmentStateAdapter
    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        // return fragments at every position
        @NonNull
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new latesbooksFragment(); // calls fragment
                case 1:
                    return new savedbooksFragment(); // chats fragment

                case 2:
                     return new mybooksFragment(); // chats fragment

            }
            return new latesbooksFragment(); //chats fragment
        }

        // return total number of tabs in our case we have 3
        @Override
        public int getItemCount() {
            return labels.length;
        }
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}

