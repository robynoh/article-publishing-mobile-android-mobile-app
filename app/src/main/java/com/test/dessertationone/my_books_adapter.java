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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class my_books_adapter extends ArrayAdapter<all_my_books> {
    RequestQueue queue;
    RequestQueue queue2;
    ArrayList<all_my_books> products;
    Context context;
    int resource;


    public my_books_adapter(Context context, int resource, ArrayList<all_my_books> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.all_mybooks, null, true);

        }
        all_my_books product = getItem(position);

//        ImageView image = (ImageView) convertView.findViewById(R.id.newclientimg);
//        Picasso.with(getContext()).load(product.getImage()).into(image);


        TextView title = (TextView) convertView.findViewById(R.id.article_title);
        title.setText(product.getTopic());

        TextView work = (TextView) convertView.findViewById(R.id.work);
        work.setText(product.getWork());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(product.getTitle()+" "+product.getFirstname()+" "+product.getLastname());

        Button openBottom=convertView.findViewById(R.id.openBottom);
        TextView status=convertView.findViewById(R.id.status);

        if(product.getStatus().equals("0")){

            openBottom.setVisibility(View.VISIBLE);
            status.setVisibility(View.GONE);

        }
        if(product.getStatus().equals("1")){

            openBottom.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);

        }

//        TextView dateposted= (TextView) convertView.findViewById(R.id.postdate);
//        dateposted.setText(product.getDate_posted());








        openBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetTheme);
                View sheetview = LayoutInflater.from(getContext()).inflate(R.layout.edit_bottom_dialogue, null);
                bottomSheetDialog.setContentView(sheetview);

                Button editArticle=sheetview.findViewById(R.id.editArticle);

                editArticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getContext(),edit_article.class);
                        i.putExtra("topic",product.getTopic());
                        i.putExtra("category", product.getCategory());
                        i.putExtra("pabstract",product.getAbstracts());
                        i.putExtra("filename",product.getFilename());
                        i.putExtra("coauthor",product.getCoauthors());
                        i.putExtra("id",product.getId());
                        view.getContext().startActivity(i);

                    }
                });

                Button deleteArticle=sheetview.findViewById(R.id.deleteArticle);

                deleteArticle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        ViewGroup viewGroup = view.findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.delete_article_dialogue, viewGroup, false);
                        builder.setView(dialogView);
                        AlertDialog alertDialog = builder.create();

                        TextView realTopic=dialogView.findViewById(R.id.topic);
                        realTopic.setText(product.getTopic());

                        Button yesdelete =dialogView.findViewById(R.id.yesdelete);
                        yesdelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                              //  Toast.makeText(getContext(),product.getId(), Toast.LENGTH_LONG).show();
                               deleteArticle(product.getId());

                                notifyDataSetChanged();

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


        return convertView;
    }



    private void deleteArticle(String article){

        String URL = "https://dissertation1.com/api/delete-article/"+article;

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




