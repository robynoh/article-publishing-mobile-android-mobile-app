package com.test.dessertationone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<all_articles_list> arrayList;
    ListView lv;
    BottomNavigationView bottomNavigationView;
    RequestQueue queue;
    LinearLayout testdetail;
    TextView username;
    private ProgressBar loadingPB;

    article_list_adapter adapter;


    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second

//    @Override
//    protected void onResume() {
//
//
//        super.onResume();
//        pull_articles();
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        SharedPreferences sharedPref = getSharedPreferences("preactivity", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("showflashscreen", "true");


        // to save our data with key and value.
        editor.apply();






        // assigning ID of the toolbar to a variable
     //Toolbar toolbar = findViewById(R.id.toolbar);
        Toolbar toolbar=findViewById(R.id.toolbar);
        loadingPB = findViewById(R.id.idLoadingPB);
        username=findViewById(R.id.username);
        lv =findViewById(R.id.listView);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        arrayList = new ArrayList<>();


        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");
        String firstname = sharedPreferences.getString("firstname", "");
        String lastname = sharedPreferences.getString("lastname", "");
        username.setText(firstname+" "+lastname);


        pull_articles();

       // bottomNavigationView = findViewById(R.id.bottomNavigationView);





        CardView searchbtn=findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,search_activity.class);
                startActivity(i);

            }
        });

        BottomNavigationView  bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

//        bottomNavigationView.setOnItemSelectedListener(this);
//        bottomNavigationView.setSelectedItemId(R.id.home);
//        bottomNavigationView.setSelectedItemId(R.id.home);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

                // ListView Clicked item index

                String topic = arrayList.get(position).getTopic();
                String abstracts = arrayList.get(position).getAbstracts();
                String category = arrayList.get(position).getCategory();
                String firstname = arrayList.get(position).getFirstname();
                String lastname = arrayList.get(position).getLastname();
                String title = arrayList.get(position).getTitle();
                String work= arrayList.get(position).getWork();
                String reads = arrayList.get(position).getReads();
                String likes = arrayList.get(position).getLikes();
                String articleid = arrayList.get(position).getId();
                String writerid = arrayList.get(position).getAuthorid();





                Intent intent = new Intent(MainActivity.this,article_detail_guest.class);
                intent.putExtra("topic", topic);
                intent.putExtra("abstracts", abstracts);
                intent.putExtra("category",category);
                intent.putExtra("firstname",firstname);
                intent.putExtra("lastname",lastname);
                intent.putExtra("title", title);
                intent.putExtra("work",work);
                intent.putExtra("reads",reads);
                intent.putExtra("likes", likes);
                intent.putExtra("articleid", articleid);
                intent.putExtra("writerid", writerid);
                startActivity(intent);



            }
        });







        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.books:
                        Intent i = new Intent(MainActivity.this,home.class);
                        startActivity(i);

                        break;
                    case R.id.search:
                       Intent j = new Intent(MainActivity.this,search.class);
                        startActivity(j);
                        break;
                    case R.id.profile:
                        Intent k = new Intent(MainActivity.this,profile.class);
                        startActivity(k);
                        break;
                    default:
//                        viewPager.setCurrentItem(0);
                }
                return true; // return true;
            }
        });




        }

//    private void setSupportActionBar(Toolbar toolbar) {
//    }
//
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.side_menu, menu);
    return super.onCreateOptionsMenu(menu);
}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){

            SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this,sign_in.class);
            this.startActivity(intent);

        }
        if(item.getItemId() == R.id.privacy){



        }


                return true;
        }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    private void pull_articles(){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-articles";

        loadingPB.setVisibility(View.VISIBLE);
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("articles");
                    loadingPB.setVisibility(View.GONE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject productObject = jsonArray.getJSONObject(i);


                            arrayList.add(new all_articles_list(
                                    productObject.getString("id"),
                                    productObject.getString("authorid"),
                                    productObject.getString("topic"),
                                    productObject.getString("abstracts"),
                                    productObject.getString("category"),
                                    productObject.getString("firstname"),
                                    productObject.getString("lastname"),
                                    productObject.getString("title"),
                                    productObject.getString("work"),
                                    productObject.getString("date_posted"),
                                    productObject.getString("reads"),
                                    productObject.getString("likes")

                            ));
                        }


                        adapter = new article_list_adapter(MainActivity.this, R.layout.all_article, arrayList);
                        lv.setAdapter(adapter);




                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.toString());
            }
        });
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Toast.makeText(MainActivity.this,"Internet Connection not Present", Toast.LENGTH_LONG).show();
            return false;
        }
    }
}
