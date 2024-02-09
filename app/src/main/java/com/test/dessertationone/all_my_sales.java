package com.test.dessertationone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class all_my_sales extends AppCompatActivity {
    ArrayList<all_sales> arrayList;
    ListView lv;
    RequestQueue queue;

    my_sales_adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_my_sales);

        Toolbar toolbar=findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);


        String authorid = sharedPreferences.getString("authorid", "");

        lv = findViewById(R.id.saleslistView);


        arrayList = new ArrayList<>();

        pull_my_sales(authorid);


    }


    private void pull_my_sales(String authorid){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-my-sales/"+ authorid;


        queue = Volley.newRequestQueue(all_my_sales.this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);


                        arrayList.add(new all_sales(
                                productObject.getString("id"),
                                productObject.getString("topic"),
                                productObject.getString("abstracts"),
                                productObject.getString("category"),
                                productObject.getString("firstname"),
                                productObject.getString("lastname"),
                                productObject.getString("title"),
                                productObject.getString("work"),
                                productObject.getString("date_posted"),
                                productObject.getString("reads"),
                                productObject.getString("likes"),
                                productObject.getString("status"),
                                productObject.getString("coauthor"),
                                productObject.getString("filename")


                        ));
                    }


                    adapter = new my_sales_adapter(all_my_sales.this, R.layout.sales_layout, arrayList);
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

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}