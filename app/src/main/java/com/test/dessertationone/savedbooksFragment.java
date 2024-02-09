package com.test.dessertationone;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;


public class savedbooksFragment extends Fragment {

    ArrayList<all_saved_articles> arrayList;
    ListView lv;
    RequestQueue queue;
    String authorid;

    private List<all_saved_articles> filteredDataList = new ArrayList<all_saved_articles>();

    saved_articles_adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_savedbooks, container, false);


    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("myKey", Context.MODE_PRIVATE);
        authorid = sharedPreferences.getString("authorid", "");


        lv = (ListView) view.findViewById(R.id.SavedlistView);


        arrayList = new ArrayList<>();

        pull_saved_articles(authorid);

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





                Intent intent = new Intent(getActivity(),article_detail_guest.class);
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


    private void pull_saved_articles(String authorid){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-saved-articles/"+authorid;


        queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);


                        arrayList.add(new all_saved_articles(
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



                    adapter = new saved_articles_adapter(getContext(), R.layout.all_saved_articles, filteredDataList);


                    filteredDataList.addAll(arrayList);

                    lv.setAdapter(adapter);

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