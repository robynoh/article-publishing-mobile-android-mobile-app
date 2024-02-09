package com.test.dessertationone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class profile extends AppCompatActivity {
    Button openpub, new_publication,edit,publicationclose;
    TextView authorname,authorposition,totalpub,totalreads,totalpurchase,totalsales,totalcredit;
    RequestQueue queue;
    private BottomSheetDialog bottomSheetDialog;

    String firstnametext;
    String lastnametext;
    String worktext;
    String positiontext;
    String titletext;
    String current_degree;
    String schoolname;


    ImageButton change_img_btn;

    CardView ViewPublication,purchase,sales,buyCredit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar=findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

//        new_publication=findViewById(R.id.new_publication);


        totalcredit=findViewById(R.id.totalcredit);
        totalpub=findViewById(R.id.totalpub);
        totalreads=findViewById(R.id.totalreads);
        totalsales=findViewById(R.id.totalsales);
        totalpurchase=findViewById(R.id.totalpurchase);

        purchase=findViewById(R.id.purchase);
//        sales=findViewById(R.id.sales);

        buyCredit=findViewById(R.id.buyCredit);

        ViewPublication=findViewById(R.id.ViewPublication);

        ViewPublication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(profile.this,all_published_articles.class);
                startActivity(i);
            }
        });


        buyCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(profile.this,buy_credit.class);
                startActivity(i);

            }
        });



//        sales.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(profile.this,all_my_sales.class);
//                startActivity(i);
//            }
//        });



        purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(profile.this,all_my_purchase.class);
                startActivity(i);
            }
        });



        openpub=findViewById(R.id.openpub);



        openpub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog = new BottomSheetDialog(profile.this, R.style.BottomSheetTheme);
                View sheetview = LayoutInflater.from(getApplicationContext()).inflate(R.layout.all_publication_bottom_sheet, null);
                bottomSheetDialog.setContentView(sheetview);
                bottomSheetDialog.show();

            }
        });

        CardView newpubcard=findViewById(R.id.newpubcard);
        newpubcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(profile.this,new_publication.class);
                startActivity(i);

            }
        });


        edit=findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(profile.this,edit_profile.class);
                i.putExtra("firstname",firstnametext);
                i.putExtra("lastname", lastnametext);
                i.putExtra("work", worktext);
                i.putExtra("position", positiontext);
                i.putExtra("title",titletext);
                i.putExtra("current_degree",current_degree);
                i.putExtra("schoolname",schoolname);
                startActivity(i);
                finish();

            }
        });



        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);

        String firstname = sharedPreferences.getString("firstname", "");
        String lastname = sharedPreferences.getString("lastname", "");
        String work = sharedPreferences.getString("work", "");
        String position = sharedPreferences.getString("position", "");
        String title = sharedPreferences.getString("title", "");

        String authorid = sharedPreferences.getString("authorid", "");

        if(authorid.isEmpty()){

            Intent a = new Intent(profile.this,signin_remind.class);
            startActivity(a);
        }

        pullprofile(authorid);

        pullprofilePhoto(authorid);

        authorname=findViewById(R.id.authorname);
        authorposition=findViewById(R.id.authorposition);
        publicationclose=findViewById(R.id.publicationclose);

        change_img_btn=findViewById(R.id.change_img_btn);
        change_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(profile.this,new_photo.class);
                startActivity(i);

            }
        });




//        new_publication.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(profile.this,new_publication.class);
//                startActivity(i);
//
//
//            }
//        });
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");

        pullprofile(authorid);

        pullprofilePhoto(authorid);

        pullauthorstat(authorid);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout){

            SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(this,signin_remind.class);
            this.startActivity(intent);

        }
        if(item.getItemId() == R.id.privacy){



        }
        return true;
    }


    public void pullprofilePhoto(String author){



        String URL = "https://dissertation1.com/api/pull-photo/"+author;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {

                        ImageView image = findViewById(R.id.profile_image);
                        if(! obj.getString("picture").isEmpty()) {

                            Picasso.get().load(obj.getString("picture")).into(image);
             //               Picasso.with(profile.this).load(obj.getString("picture")).into(image);
                        }
                        // Toast.makeText(introMap.this,"done", Toast.LENGTH_LONG).show();


                    }
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


    public void pullprofile(String author){



        String URL = "https://dissertation1.com/api/pull-author/"+author;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {

                        firstnametext=obj.getString("firstname");
                        lastnametext=obj.getString("lastname");
                        worktext=obj.getString("work");
                        positiontext=obj.getString("position");
                        titletext=obj.getString("title");
                        current_degree=obj.getString("current_degree");
                        schoolname=obj.getString("schoolname");
                       // accountnumber=obj.getString("accountnumber");



                        authorname.setText(obj.getString("title")+" "+obj.getString("firstname")+" "+obj.getString("lastname"));
                        authorposition.setText(obj.getString("position"));


                        // Toast.makeText(introMap.this,"done", Toast.LENGTH_LONG).show();


                    }
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


    public void pullauthorstat(String author){



        String URL = "https://dissertation1.com/api/author-stat/"+author;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    JSONArray jsonArray = obj.getJSONArray("stat");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);

                        totalpub.setText( productObject.getString("articleCount"));
                        totalreads.setText(productObject.getString("readCount"));
                        totalsales.setText(productObject.getString("salesCount"));
                        totalpurchase.setText(productObject.getString("purchaseCount"));

                        totalcredit.setText(productObject.getString("totalUnit"));



                        // Toast.makeText(introMap.this,"done", Toast.LENGTH_LONG).show();


                    }
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

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }


}