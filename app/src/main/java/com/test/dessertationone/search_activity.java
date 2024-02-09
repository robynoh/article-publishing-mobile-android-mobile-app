package com.test.dessertationone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;

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
import java.util.List;

public class search_activity extends AppCompatActivity {

    // List View object
    ListView listView;
    RequestQueue queue;
    private ProgressBar loadingPB;
    // Define array adapter for ListView
    all_search_adapter adapter;

    // Define array List for List View data
    ArrayList<all_search> mylist;

    private List<all_search> filteredDataList = new ArrayList<all_search>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);


        Toolbar toolbar=findViewById(R.id.toolbar);
        loadingPB = findViewById(R.id.idLoadingPB);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        // initialise ListView with id
        listView = findViewById(R.id.listView);


        // Add items to Array List
        mylist = new ArrayList<>();

         pull_articles();
       // pull_articles();
        // Set adapter to ListView





        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

                // ListView Clicked item index

                String topic =  mylist.get(position).getTopic();
                String abstracts =  mylist.get(position).getAbstracts();
                String category =  mylist.get(position).getCategory();
                String firstname =  mylist.get(position).getFirstname();
                String lastname =  mylist.get(position).getLastname();
                String title =  mylist.get(position).getTitle();
                String work=  mylist.get(position).getWork();
                String reads =  mylist.get(position).getReads();
                String likes =  mylist.get(position).getLikes();
                String articleid =  mylist.get(position).getId();
                String writerid =  mylist.get(position).getAuthorid();





                Intent intent = new Intent(search_activity.this,article_detail_guest.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu with items using MenuInflator
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        //pull_articles();
        // Initialise menu item search bar
        // with id and take its object
        MenuItem searchViewItem = menu.findItem(R.id.search_bar);
       // SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        SearchView searchView = (SearchView) searchViewItem.getActionView();

        // attach setOnQueryTextListener
        // to search view defined above
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                filteredDataList.clear();
                if (TextUtils.isEmpty(s)) {
                    filteredDataList.addAll(mylist);
                } else {
                    s = s.toLowerCase();
                    for (all_search data : mylist) {
                        if (data.getTopic().toLowerCase().contains(s) ||
                                data.getWork().toLowerCase().contains(s)) {
                            filteredDataList.add(data);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                return true;

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
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


                        mylist.add(new all_search(
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


                    adapter = new all_search_adapter(search_activity.this, R.layout.all_search_layout, (ArrayList<all_search>) filteredDataList);


                    filteredDataList.addAll(mylist);


                    listView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();




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
}