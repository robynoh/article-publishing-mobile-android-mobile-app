package com.test.dessertationone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class edit_article extends AppCompatActivity {

    RequestQueue queue;
    private ProgressBar loadingPB;
    Button delivered,downloadfile;

    EditText title;
    EditText coauthor;
    EditText pabstract;
    Button uploadArticle;
    String filePath;
    String filePath2;
    TextView filename;
    ImageButton choose_file;

    long initialFilename;
    boolean isAllFieldsChecked = false;



    String authorid;

    private Spinner spinner;
    private ArrayList<String> students;



    ActivityResultLauncher<String> articleDoc = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    // Handle the returned Uri


                    String path =uri.getPath();

                    Random rand = new Random();

                    if(uri.getPath().contains("document/raw:")){
                        filePath = uri.getPath().replace("/document/raw:","");
                    }else {

                        filePath = uri.getPath();
                    }
                    String fileName = getFileName(filePath);

                    initialFilename =rand.nextInt(20);


                    filename.setText(fileName);

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_article);

        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        spinner= findViewById(R.id.spinner);
         title=findViewById(R.id.title);
        coauthor=findViewById(R.id.coauthor);
        pabstract=findViewById(R.id.pabstract);

        downloadfile=findViewById(R.id.downloadfile);

        students = new ArrayList<String>();


        String parseTopic = getIntent().getStringExtra("topic");
        String parsePabstract = getIntent().getStringExtra("pabstract");
        String parseCategory = getIntent().getStringExtra("category");
        String parseCoauthor = getIntent().getStringExtra("coauthor");
        String parseFilename = getIntent().getStringExtra("filename");
        String parseId = getIntent().getStringExtra("id");

        title.setText(parseTopic);
        coauthor.setText(parseCoauthor);
        pabstract.setText(parsePabstract);


        uploadArticle= findViewById(R.id.uploadArticle);
        spinner= findViewById(R.id.spinner);
        title= findViewById(R.id.title);
        coauthor= findViewById(R.id.coauthor);
        pabstract= findViewById(R.id.pabstract);
        choose_file= findViewById(R.id.choose_file);
        filename= findViewById(R.id.filename);
        loadingPB = findViewById(R.id.idLoadingPB);

        choose_file= findViewById(R.id.choose_file);

        choose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    } else {
                        fileChooserdriverlicense();
                    }
                }
            }
        });


        uploadArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    isAllFieldsChecked= CheckAllFields();

                    if (isAllFieldsChecked) {

                        loadingPB.setVisibility(View.VISIBLE);

                        String titleValue=title.getText().toString();
                        String coauthorValue=coauthor.getText().toString();
                        String categoryValue=spinner.getSelectedItem().toString();;
                        String abstractValue=pabstract.getText().toString();

                        try {
                            // uploadNewArticle(titleValue,coauthorValue,categoryValue,abstractValue,authorid);

                           if(filePath !=null) {
                               uploadDocFile("https://dissertation1.com/api/update-new-article", filePath, titleValue, coauthorValue, categoryValue, abstractValue, parseId);
                           }else{
                               uploadDocNotFile( titleValue, coauthorValue, categoryValue, abstractValue, parseId);

                           }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }






            }
        });


        downloadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(parseFilename), "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                } catch (Exception e) {
                    // Error...
                }




//                File file = new File(parseFilename);
//                Uri path = Uri.fromFile(file);
//                Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
//                pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                pdfOpenintent.setDataAndType(path, "application/pdf");
//                try {
//                    startActivity(pdfOpenintent);
//                }
//                catch (ActivityNotFoundException e) {
//
//                }
            }
        });


        students.add(parseCategory);
        getData();
    }

    public void fileChooserdriverlicense(){


        try {
            articleDoc.launch("application/pdf");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFileName(String path) {
        try {

            return path != null ? path.substring(path.lastIndexOf("/") + 1) : "unknown";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "unknown";
    }

    private boolean CheckAllFields() {


        if (title.length() == 0) {
            title.setError("Enter article title");
            return false;
        }
        if (coauthor.length() == 0) {
            coauthor.setError("Enter nil if there is no coauthor");
            return false;
        }
        if (pabstract.length() == 0) {
            pabstract.setError("Enter type in your abstract");
            return false;
        }
        if (spinner.getSelectedItem() =="") {
            setSpinnerError(spinner,"Please choose category");
            return false;
        }

//        if (titleValue.isEmpty()) {
//            setSpinnerError(spinner,"Please choose a title");
//           // return false;
//        }

        // after all validation return true.
        return true;
    }
    private void setSpinnerError(Spinner spinner, String error){
        View selectedView = spinner.getSelectedView();
        if (selectedView != null && selectedView instanceof TextView) {
            spinner.requestFocus();
            TextView selectedTextView = (TextView) selectedView;
            selectedTextView.setError("error"); // any name of the error will do
            selectedTextView.setTextColor(Color.RED); //text color in which you want your error message to be displayed
            selectedTextView.setText(error); // actual error message
            spinner.performClick(); // to open the spinner list if error is found.

        }
    }

    private void getData(){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-categories";


        queue = Volley.newRequestQueue(edit_article.this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("articles");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);


                        students.add(productObject.getString("categoryvalue"));
                    }


                    spinner.setAdapter(new ArrayAdapter<String>(edit_article.this, android.R.layout.simple_spinner_dropdown_item, students));





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




    private void uploadDocNotFile(String title, String coauthor, String category, String pabstract, String articleid) {
        // url to post our data


        loadingPB.setVisibility(View.VISIBLE);

        String url = "https://dissertation1.com/api/update-article-no-file";
// Creating string request with post method.
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        // Hiding the progress dialog after all task complete.
                        loadingPB.setVisibility(View.GONE);
                        String resultResponse = new String(response.data);

                        //Toast.makeText(contact_activity.this,resultResponse, Toast.LENGTH_LONG).show();
                        // Showing response message coming from server.
                        if(resultResponse.equals("ok")) {

                            loadingPB.setVisibility(View.GONE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(edit_article.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.update_dialogue_article, viewGroup, false);
                            builder.setView(dialogView);

                            delivered = dialogView.findViewById(R.id.delivered);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCanceledOnTouchOutside(false);

                            delivered.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(edit_article.this,profile.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                            try {
                                alertDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        loadingPB.setVisibility(View.GONE);
                        // Showing error message if something goes wrong.
                        Toast.makeText(edit_article.this,error.getMessage(), Toast.LENGTH_LONG).show();




                    }
                }){



            @RequiresApi(api = Build.VERSION_CODES.O)
            protected Map<String, String> getParams() throws AuthFailureError {



                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params
                params.put("title",title);
                params.put("coauthor", coauthor);
                params.put("category", category);
                params.put("pabstract", pabstract);
                params.put("articleid", articleid);
                return params;
            }

            /*
             *pass files using below method
             * */
                  };



        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(edit_article.this);
        rQueue.add(volleyMultipartRequest);

    }











    private void uploadDocFile(String url, String filePath, String title, String coauthor, String category, String pabstract, String authorid) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String boundary = "------------------------" + System.currentTimeMillis();
        String mimeType = "multipart/form-data;boundary=" + boundary;
        File sd = Environment.getExternalStorageDirectory();
        final File file = new File(filePath);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

            // Add string variables to the request
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"title\"\r\n\r\n" +title + "\r\n");
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"coauthor\"\r\n\r\n" + coauthor + "\r\n");
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"category\"\r\n\r\n" + category + "\r\n");
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"pabstract\"\r\n\r\n" + pabstract + "\r\n");
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"articleid\"\r\n\r\n" + authorid + "\r\n");

            // Add file to the request
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"pdFfile\";filename=\"" + file.getName() + "\"\r\n");
            dataOutputStream.writeBytes("Content-Type: application/pdf\r\n\r\n");


            try{
                FileInputStream fileInputStream = new FileInputStream(file);


                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                    dataOutputStream.write(buffer, 0, bytesRead);
                }

                dataOutputStream.writeBytes("\r\n--" + boundary + "--\r\n");

                fileInputStream.close();
                dataOutputStream.flush();



            }
            catch (FileNotFoundException e) {
                // Handle the exception
                System.out.println("The file could not be found: " + e.getMessage());


            }


            byte[] multipartBody = byteArrayOutputStream.toByteArray();

            // Create the request using Volley
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            // Handle successful response

                            // Hiding the progress dialog after all task complete.
                            loadingPB.setVisibility(View.GONE);
                            String resultResponse = new String(response.data);

                            //Toast.makeText(contact_activity.this,resultResponse, Toast.LENGTH_LONG).show();
                            // Showing response message coming from server.
                            if(resultResponse.equals("ok")) {

                                loadingPB.setVisibility(View.GONE);

                                AlertDialog.Builder builder = new AlertDialog.Builder(edit_article.this);
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.update_dialogue_article, viewGroup, false);
                                builder.setView(dialogView);

                                delivered = dialogView.findViewById(R.id.delivered);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCanceledOnTouchOutside(false);

                                delivered.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alertDialog.hide();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error response
                            error.printStackTrace();
                            loadingPB.setVisibility(View.GONE);
                            // Showing error message if something goes wrong.
                            Toast.makeText(edit_article.this,error.getMessage(), Toast.LENGTH_LONG).show();



                        }
                    }
            ) {
                @Override
                public String getBodyContentType() {
                    return mimeType;
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return multipartBody;
                }
            };

            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(multipartRequest);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}