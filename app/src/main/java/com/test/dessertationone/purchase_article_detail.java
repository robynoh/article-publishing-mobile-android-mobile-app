package com.test.dessertationone;

        import androidx.appcompat.app.AlertDialog;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.widget.Toolbar;

        import android.app.DownloadManager;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Build;
        import android.os.Bundle;
        import android.os.Environment;
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

public class purchase_article_detail extends AppCompatActivity {
    RequestQueue queue;
    TextView readstext,categoryTitle,buy_title,buy_work,authorname;
    Button save;
    Button yeslike,buy,buyclose;
    Button nolike;
    Button delivered,nosave,downloadfile,readfile;
    Button okay,saveblocked,likebtn,like2btn,buynow;
    LinearLayout downloadbg,purchase,nopurchase;
    AlertDialog alertDialog;

    DownloadManager manager;

    String filename="";
    String fileTopic="";
    String fileprocess="";

    private String Buyerid,authorid, articleId, topic, articleAmount;

    private BottomSheetDialog bottomSheetDialogbuy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_article_detail);

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
        String filename = getIntent().getStringExtra("filename");

        fileprocess=getIntent().getStringExtra("topic").toLowerCase();

        fileTopic=fileprocess.replace(" ","_")+".pdf";

        TextView titletxt = (TextView) findViewById(R.id.title);
        TextView abstracttext = (TextView) findViewById(R.id.abstracts);
        TextView nametxt = (TextView) findViewById(R.id.author);

        //TextView lastnametxt = (TextView) findViewById(R.id.lastname);
        readfile=findViewById(R.id.readfile);

        TextView likestext = (TextView) findViewById(R.id.likes);
        TextView categorytxt = (TextView) findViewById(R.id.category);

        titletxt.setText(topic);
        abstracttext.setText(abstracts);
        categorytxt.setText(category);
        nametxt.setText(title+" "+firstname+" "+lastname);


        likestext.setText("("+likes+")");
        readstext.setText("("+reads+")");

        purchaseCheck(articleid,authorid);


        downloadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDownload();
            }
        });

        readfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(filename), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } catch (Exception e) {
                    // Error...
                }

            }
        });










    }





    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
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
        String urix =filename;
        //download(purchase_article_detail.this,fileTopic, ".pdf", "Downloads", uri.trim());

        AlertDialog.Builder builder = new AlertDialog.Builder(purchase_article_detail.this);
        builder.setMessage("Choose Download Type");
        builder.setPositiveButton("Using Default Browser", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urix)));
            }
        });
        builder.setNegativeButton("Using Download Manager", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadPdf(urix);
            }
        });
        builder.show();



    }


    private void downloadPdf(String url) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            } else {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("PDF Download");
                request.setDescription("Downloading PDF file");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileTopic);
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            }


        }
    }
//    private void download(Context context, String Filename, String FileExtension, String DesignationDirectory, String url) {
//        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        Uri uri = Uri.parse(url);
//        DownloadManager.Request request = new DownloadManager.Request(uri);
//        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//        request.setDestinationInExternalFilesDir(context, DesignationDirectory, Filename + FileExtension);
//        assert downloadManager != null;
//        downloadManager.enqueue(request);
//        Snackbar snackbar = (Snackbar) Snackbar.make(purchase_article_detail.this.findViewById(android.R.id.content), "Downloading...", Snackbar.LENGTH_LONG);
//        snackbar.show();
//    }
}