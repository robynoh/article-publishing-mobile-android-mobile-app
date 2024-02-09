package com.test.dessertationone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class saved_articles_adapter extends ArrayAdapter<all_saved_articles> {
    RequestQueue queue;
    RequestQueue queue2;
    ArrayList<all_saved_articles> products;
    Context context;
    int resource;


    public saved_articles_adapter(Context context, int resource, List<all_saved_articles> products) {
        super(context, resource, products);
        this.products = (ArrayList<all_saved_articles>) products;
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.all_saved_articles, null, true);

        }
        all_saved_articles product = getItem(position);

//        ImageView image = (ImageView) convertView.findViewById(R.id.newclientimg);
//        Picasso.with(getContext()).load(product.getImage()).into(image);


        TextView title = (TextView) convertView.findViewById(R.id.article_title);
        title.setText(product.getTopic());

        TextView work = (TextView) convertView.findViewById(R.id.work);
        work.setText(product.getWork());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(product.getTitle()+" "+product.getFirstname()+" "+product.getLastname());

        Button openBottom=convertView.findViewById(R.id.openBottom);


        openBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);
                View sheetview = LayoutInflater.from(getContext()).inflate(R.layout.delete_saved_dialogue, null);
                bottomSheetDialog.setContentView(sheetview);



                Button deleteArticle=sheetview.findViewById(R.id.deleteArticle);

                deleteArticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        ViewGroup viewGroup = view.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.remove_this_save, viewGroup, false);
                        builder.setView(dialogView);
                        AlertDialog alertDialog = builder.create();

                        TextView realTopic=dialogView.findViewById(R.id.topic);
                        realTopic.setText(product.getTopic());

                        Button yesdelete =dialogView.findViewById(R.id.yesdelete);
                        yesdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                //Toast.makeText(getContext(),product.getId(), Toast.LENGTH_LONG).show();
                                deleteArticle(product.getId());

                               // notifyDataSetChanged();

                                alertDialog.hide();

                            }
                        });

                        Button nodelete =dialogView.findViewById(R.id.nodelete);
                        nodelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                alertDialog.hide();
                            }
                        });

                        alertDialog.show();
                    }
                });


                bottomSheetDialog.show();

            }
        });

//        TextView dateposted= (TextView) convertView.findViewById(R.id.postdate);
//        dateposted.setText(product.getDate_posted());


        return convertView;
    }





    private void deleteArticle(String article){

        SharedPreferences sharedPreferences = context.getSharedPreferences("myKey", context.MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");

        String URL = "https://dissertation1.com/api/remove-article/"+article+"/"+authorid;

        queue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {



                        // Toast.makeText(introMap.this,"done", Toast.LENGTH_LONG).show();


                    }else if(obj.getString("status").equals("does not exist")) {


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

}



