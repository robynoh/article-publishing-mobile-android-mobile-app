package com.test.dessertationone;




import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;







public class article_detail_guest extends AppCompatActivity {
    RequestQueue queue;

    RequestQueue requestQueue;

    TextView readstext,categoryTitle,buy_title,buy_work,authorname;
    Button save;
    Button yeslike,buy,buyclose;
    Button nolike;
    Button delivered,nosave;
    Button okay,saveblocked,likebtn,like2btn,buynow,refundPolicy;
    AlertDialog alertDialog;
    private BottomSheetDialog bottomSheetDialogbuy;
    private BottomSheetDialog bottomSheetDialogbuy2;
    ProgressDialog progressDialog;
    private BottomSheetDialog bottomSheetDialogbuy3;




    private BottomSheetDialog bottomSheetDialogbuy4;

    String buyerid;
    String writerId;
    String acticleId;
    String articletopic,firstname,lastname,title;
    String refNumber;
    String price ="20.23";



    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail_guest);



        Toolbar toolbar=findViewById(R.id.toolbar);




        readstext = (TextView) findViewById(R.id.reads);
        save=findViewById(R.id.save);



        yeslike=findViewById(R.id.yeslike);
        nolike=findViewById(R.id.nolike);

        like2btn=findViewById(R.id.like2);


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
        firstname = getIntent().getStringExtra("firstname");
        lastname= getIntent().getStringExtra("lastname");
        title = getIntent().getStringExtra("title");
        String work = getIntent().getStringExtra("work");
        String reads = getIntent().getStringExtra("reads");
        String likes = getIntent().getStringExtra("likes");
        String articleid = getIntent().getStringExtra("articleid");
        String writerid = getIntent().getStringExtra("writerid");

        buyerid = authorid;
        writerId =  writerid;
        acticleId = articleid;
        articletopic = topic;

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

        updatereads(articleid,writerid);
        savecheck(articleid,authorid);
        likecheck(articleid,authorid);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(article_detail_guest.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(article_detail_guest.this);
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

        bottomSheetDialogbuy = new BottomSheetDialog(article_detail_guest.this, R.style.BottomSheetTheme);
        View sheetviewbuy = LayoutInflater.from(getApplicationContext()).inflate(R.layout.buy_publication, null);
        bottomSheetDialogbuy.setContentView(sheetviewbuy);

        categoryTitle=sheetviewbuy.findViewById(R.id.category);
//        buy_title=sheetviewbuy.findViewById(R.id.buy_title);
//        buy_work=sheetviewbuy.findViewById(R.id.buy_work);
        authorname=sheetviewbuy.findViewById(R.id.authorname);
        buyclose=sheetviewbuy.findViewById(R.id.buyclose);

        buynow=sheetviewbuy.findViewById(R.id.buynow);
        refundPolicy=sheetviewbuy.findViewById(R.id.refundPolicy);

//        categoryTitle.setText(category);
//        buy_title.setText(topic);
//        buy_work.setText(work);
//        authorname.setText(title+" "+firstname+" "+lastname);

        buyclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialogbuy.hide();
            }
        });

        refundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(article_detail_guest.this,refund_policy.class);
                startActivity(i);

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
                bottomSheetDialogbuy.hide();

                progressDialog = ProgressDialog.show(article_detail_guest.this,"Your purchase is processing. Please wait...",null,true,true);
                progressDialog.setCancelable(false);


                updateArticlesales(buyerid,writerId,acticleId,refNumber,price,"This payment is made for article purchase");


//                Intent intent = new Intent(article_detail_guest.this,paymentActivity.class);
//                intent.putExtra("buyerid", authorid);
//                intent.putExtra("writerid", writerid);
//                intent.putExtra("acticleid",articleid);
//                intent.putExtra("articletopic",topic);
//                startActivity(intent);

               // makePayment();


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


                        AlertDialog.Builder builder = new AlertDialog.Builder(article_detail_guest.this);
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


//    private void makePayment() {
//
//        new RavePayManager(this)
//                .setAmount(Double.parseDouble(price))
//                .setEmail("robinsonagaga@gmail.com")
//              //  .setCountry("USA")
//                .setCurrency("USD")
//                .setfName(firstname)
//                .setlName(lastname)
//                .setNarration("Purchase of an article in Dissertation one")
//                .setPublicKey("FLWPUBK_TEST-f5302848d8f53a7b782041e73157ba65-X")
//                .setEncryptionKey("FLWSECK_TEST5ef24e08a971")
//                .setTxRef(refNumber)
//                .acceptAccountPayments(true)
//                .acceptCardPayments(true)
//                .acceptMpesaPayments(true)
//                .onStagingEnv(true)
//                .shouldDisplayFee(true)
//                .showStagingLabel(true)
//                .initialize();
//
//
//    }

    //@Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == RaveConstants.RAVE_REQUEST_CODE && data != null) {
//            String message = data.getStringExtra("response");
//            if (resultCode == RavePayActivity.RESULT_SUCCESS) {
//
//
//              updateArticlesales(buyerid,writerId,acticleId,refNumber,price);
//
//
//
//
//
//
//           //     Toast.makeText(this, "SUCCESS " + message, Toast.LENGTH_LONG).show();
//            }
//            else if (resultCode == RavePayActivity.RESULT_ERROR) {
//                Toast.makeText(this, "ERROR " + message, Toast.LENGTH_LONG).show();
//            }
//            else if (resultCode == RavePayActivity.RESULT_CANCELLED) {
//                Toast.makeText(this, "CANCELLED " + message, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

     public void updateArticlesales(String buyerid, String writerId, String articleId, String refNumber,String price,String description){



        String URL = "https://dissertation1.com/api/update-article-sale/"+buyerid+"/"+writerId+"/"+articleId+"/"+refNumber+"/"+price+"/"+description;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {

                        progressDialog.dismiss();

                        Intent intent = new Intent(article_detail_guest.this,payment_success.class);
                intent.putExtra("firstname", firstname);
                intent.putExtra("lastname", lastname);
                intent.putExtra("title",title);
                intent.putExtra("articletopic",articletopic);
                startActivity(intent);




                    }

                    if (obj.getString("status").equals("not ok")) {


                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(article_detail_guest.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.no_credit_buy_dialogue, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(article_detail_guest.this,profile.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();




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
