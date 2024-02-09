package com.test.dessertationone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class subcategory extends AppCompatActivity {

    String category;
    String categoryid;
    String categoryValue;

    ArrayList<all_subcategory> arrayList;
    ListView lv;
    RequestQueue queue;

    private ProgressBar loadingPB;

    all_subcategory_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subcategory);

        category = getIntent().getStringExtra("category");
        categoryValue = getIntent().getStringExtra("categoryValue");
        categoryid = getIntent().getStringExtra("categoryid");
        loadingPB = findViewById(R.id.idLoadingPB);

        lv =findViewById(R.id.listView);
        TextView categorytitle =findViewById(R.id.categorytitle);

        categorytitle.setText(categoryValue);

        Toolbar toolbar=findViewById(R.id.toolbar);
        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        arrayList = new ArrayList<>();

        pull_subcategory(categoryid);

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





                Intent intent = new Intent(subcategory.this,article_detail_guest.class);
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
    }


    private void pull_subcategory(String category){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-sub-articles/"+category;

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


                        arrayList.add(new all_subcategory(
                                productObject.getString("id"),
                                productObject.getString("topic"),
                                productObject.getString("authorid"),
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


                    adapter = new all_subcategory_adapter(subcategory.this,R.layout.subcategory_layout, arrayList);
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