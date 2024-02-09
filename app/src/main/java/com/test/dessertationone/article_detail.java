package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

public class article_detail extends AppCompatActivity {
    RequestQueue queue;
    TextView readstext,categoryTitle,buy_title,buy_work,authorname;
    Button save;
    Button yeslike,buy,buyclose;
    Button nolike;
    Button delivered,nosave,downloadfile,readfile;
    Button okay,saveblocked,likebtn,like2btn,buynow;
    LinearLayout downloadbg,purchase,nopurchase;
    AlertDialog alertDialog;

    String filename="";
    String fileTopic="";
    String fileprocess="";

    private String Buyerid,authorid, articleId, topic, articleAmount;

    private BottomSheetDialog bottomSheetDialogbuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        Toolbar toolbar=findViewById(R.id.toolbar);
        readstext = (TextView) findViewById(R.id.reads);
        save=findViewById(R.id.save);

        purchase=findViewById(R.id.purchase);
        nopurchase=findViewById(R.id.nopurchase);

        yeslike=findViewById(R.id.yeslike);
        nolike=findViewById(R.id.nolike);

        like2btn=findViewById(R.id.like2);

        downloadbg=findViewById(R.id.downloadbg);
        downloadfile=findViewById(R.id.downloadfile);

        readfile=findViewById(R.id.readfile);


        saveblocked=findViewById(R.id.saveblocked);
        likebtn=findViewById(R.id.like);




        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");

        String topic = getIntent().getStringExtra("topic");
        String abstracts = getIntent().getStringExtra("abstracts");
        String category = getIntent().getStringExtra("category");
        String firstname = getIntent().getStringExtra("firstname");
        String lastname= getIntent().getStringExtra("lastname");
        String title = getIntent().getStringExtra("title");
        String work = getIntent().getStringExtra("work");
        String reads = getIntent().getStringExtra("reads");
        String likes = getIntent().getStringExtra("likes");
        String articleid = getIntent().getStringExtra("articleid");

        fileprocess=getIntent().getStringExtra("topic").toLowerCase();

        fileTopic=fileprocess.replace(" ","_");

        TextView titletxt = (TextView) findViewById(R.id.title);
        TextView abstracttext = (TextView) findViewById(R.id.abstracts);
        TextView nametxt = (TextView) findViewById(R.id.author);

        //TextView lastnametxt = (TextView) findViewById(R.id.lastname);


        TextView likestext = (TextView) findViewById(R.id.likes);
        TextView categorytxt = (TextView) findViewById(R.id.category);

        titletxt.setText(topic);
        abstracttext.setText(abstracts);
        categorytxt.setText(category);
        nametxt.setText(title+" "+firstname+" "+lastname);


        likestext.setText("("+likes+")");
        readstext.setText("("+reads+")");

        purchaseCheck(articleid,authorid);
        savecheck(articleid,authorid);
        likecheck(articleid,authorid);

        readfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        downloadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDownload();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(article_detail.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.save_article_dialogue, viewGroup, false);
                builder.setView(dialogView);

                delivered = dialogView.findViewById(R.id.delivered);
                nosave = dialogView.findViewById(R.id.nosave);
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                delivered.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savepublication(articleid,authorid);
                    }
                });

                nosave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();
                    }
                });
                alertDialog.show();
            }
        });

        likebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(article_detail.this);
                ViewGroup viewGroup = findViewById(android.R.id.content);
                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.like_dialogue, viewGroup, false);
                builder.setView(dialogView);

                yeslike = dialogView.findViewById(R.id.yeslike);
                nolike = dialogView.findViewById(R.id.nolike);
                alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);

                yeslike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        savelike(articleid,authorid);
                        alertDialog.hide();
                    }
                });

                nolike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();
                    }
                });
                alertDialog.show();
            }
        });


        buy=findViewById(R.id.buy);

        bottomSheetDialogbuy = new BottomSheetDialog(article_detail.this, R.style.BottomSheetTheme);
        View sheetviewbuy = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buy_publication, null);
        bottomSheetDialogbuy.setContentView(sheetviewbuy);

        categoryTitle=sheetviewbuy.findViewById(R.id.category);
//        buy_title=sheetviewbuy.findViewById(R.id.buy_title);
//        buy_work=sheetviewbuy.findViewById(R.id.buy_work);
        authorname=sheetviewbuy.findViewById(R.id.authorname);
        buyclose=sheetviewbuy.findViewById(R.id.buyclose);

        buynow=sheetviewbuy.findViewById(R.id.buynow);

        categoryTitle.setText(category);
        buy_title.setText(topic);
        buy_work.setText(work);
        authorname.setText(title+" "+firstname+" "+lastname);

        buyclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogbuy.hide();
            }
        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bottomSheetDialogbuy.show();

            }
        });

        buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }



    public void updatereads(String articleid,String authorid){



        String URL = "https://dissertation1.com/api/update-reads/"+articleid+"/"+authorid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {




                        readstext.setText("("+obj.getString("readcount")+")");

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

    public void savepublication(String articleid,String authorid){



        String URL = "https://dissertation1.com/api/save-publication/"+articleid+"/"+authorid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("saved")) {

                        saveblocked.setVisibility(View.VISIBLE);
                        save.setVisibility(View.GONE);


                        AlertDialog.Builder builder = new AlertDialog.Builder(article_detail.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.save_success_dialogue, viewGroup, false);
                        builder.setView(dialogView);

                        okay = dialogView.findViewById(R.id.okay);
                        AlertDialog alertDialog2 = builder.create();
                        alertDialog2.setCanceledOnTouchOutside(false);

                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alertDialog2.hide();
                                alertDialog.hide();
                            }
                        });
                        alertDialog2.show();

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

    public void savecheck(String articleid,String authorid){



        String URL = "https://dissertation1.com/api/check-saved/"+articleid+"/"+authorid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("saved")) {




                        saveblocked.setVisibility(View.VISIBLE);
                        save.setVisibility(View.GONE);


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


    public void likecheck(String articleid,String authorid){



        String URL = "https://dissertation1.com/api/like-saved/"+articleid+"/"+authorid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("liked")) {




                        likebtn.setVisibility(View.GONE);
                        like2btn.setVisibility(View.VISIBLE);


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

    public void savelike(String articleid,String authorid){



        String URL = "https://dissertation1.com/api/save-like/"+articleid+"/"+authorid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("liked")) {




                        likebtn.setVisibility(View.GONE);
                        like2btn.setVisibility(View.VISIBLE);

                     //   Toast.makeText(article_detail.this,"You like theis articale", Toast.LENGTH_LONG).show();


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

    public void purchaseCheck(String articleid,String authorid){



        String URL = "https://dissertation1.com/api/purchase-check/"+articleid+"/"+authorid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("purchased")) {


                    filename=obj.getString("message");

                        downloadbg.setVisibility(View.VISIBLE);
                        downloadfile.setVisibility(View.VISIBLE);

                        purchase.setVisibility(View.VISIBLE);
                        nopurchase.setVisibility(View.GONE);


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

    private void initDownload() {
        String uri =filename;
        download(article_detail.this,fileTopic, ".pdf", "Downloads", uri.trim());
    }
    private void download(Context context, String Filename, String FileExtension, String DesignationDirectory, String url) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, DesignationDirectory, Filename + FileExtension);
        assert downloadManager != null;
        downloadManager.enqueue(request);
        Snackbar snackbar = (Snackbar) Snackbar.make(article_detail.this.findViewById(android.R.id.content), "Downloading...", Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}