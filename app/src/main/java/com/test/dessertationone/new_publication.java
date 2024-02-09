package com.test.dessertationone;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class new_publication extends AppCompatActivity {
    private Spinner spinner;
    private ArrayList<String> students;
    RequestQueue queue;

    EditText title,coauthor,pabstract;
    ImageButton choose_file;

    TextView filename;
    TextView errorMsg;
    Bitmap fileBitmap;
    String filepath;
    File fileToUpload;
    long initialFilename;
    boolean isAllFieldsChecked = false;
    private ProgressBar loadingPB;
    Bitmap  realFile;
    long  filenameout;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    Button uploadArticle;
    String filePath;

    Uri uripath;

    String filePath2;
    Button delivered,refundPolicy;

    String authorid;



    String refNumber;
    String price ="20";

    String titleValue;
    String coauthorValue;
    String categoryValue;
    String abstractValue;

    final Handler handler = new Handler();
    final int delay = 1000; // 1000 milliseconds == 1 second

    int paid=0;




    private static final int PICKFILE_REQUEST_CODE = 1;
    private static final int PICK_IMAGE = 456; // Whatever number you like
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL = 28528; // Whatever number you like
    public static final String FILE_TEMP_NAME = "temp_file"; //


    ActivityResultLauncher<String> articleDoc = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {

                    // Handle the returned Uri

                    uripath=uri;
                    String path =uri.getPath();

                    Random rand = new Random();

                    if(uri.getPath().contains("document/raw:")){
                        filePath = uri.getPath().replace("/document/raw:","");
                    }else {

                        filePath = uri.getPath();
                    }
                    String fileName = getFileName(filePath);

                    initialFilename =rand.nextInt(20);

                    File tmpDir = new File(filePath);

                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName); // Set Your File Name
                    if (file.exists()) {
                        filename.setText(fileName);
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(new_publication.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.wrong_upload, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //alertDialog.hide();

                                alertDialog.hide();
                            }
                        });
                        alertDialog.show();


                    }




                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_publication);




        students = new ArrayList<String>();
        Toolbar toolbar=findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        handler.postDelayed(new Runnable() {
            public void run() {
                checkInternetConnection();
                handler.postDelayed(this, delay);
            }
        }, delay);


        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        authorid = sharedPreferences.getString("authorid", "");






        uploadArticle= findViewById(R.id.uploadArticle);

        refundPolicy= findViewById(R.id.refundPolicy);

         spinner= findViewById(R.id.spinner);
        title= findViewById(R.id.title);
        coauthor= findViewById(R.id.coauthor);
        pabstract= findViewById(R.id.pabstract);
        choose_file= findViewById(R.id.choose_file);
        filename= findViewById(R.id.filename);
        loadingPB = findViewById(R.id.idLoadingPB);

        errorMsg = findViewById(R.id.errorMsg);

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


        refundPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(new_publication.this,refund_policy.class);
                startActivity(i);

            }
        });


        uploadArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                    isAllFieldsChecked= CheckAllFields();

                    if (isAllFieldsChecked) {

                       // loadingPB.setVisibility(View.VISIBLE);

                        titleValue=title.getText().toString();
                        coauthorValue=coauthor.getText().toString();
                        categoryValue=spinner.getSelectedItem().toString();;
                        abstractValue=pabstract.getText().toString();

                        if(filePath !=null) {

                            progressDialog = ProgressDialog.show(new_publication.this,"Article uploading. Please wait...",null,true,true);
                            progressDialog.setCancelable(false);

                            try {
                                uploadNewArticle(titleValue,coauthorValue,categoryValue,abstractValue,authorid,refNumber);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                        }else{

                            errorMsg.setText("Please ensure you select a file to upload");

                        }
                    }






            }
        });




        getData();
    }





//    private void pickFile() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");
//        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (resultCode == RESULT_OK && requestCode == PICKFILE_REQUEST_CODE) {
//            Uri uri = data.getData();
//            String filePath = getRealPathFromURI(uri);
//            // Call uploadDocFile method with filePath parameter
//            uploadDocFile(url, filePath, string1, string2, string3, string4, string5);
//        }
//    }


    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }

    private Bitmap pathToBitmap(String path, String imageName) {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd+path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);


        return bitmap;
    }

    private void getData(){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-categories";


        queue = Volley.newRequestQueue(new_publication.this);
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


                    spinner.setAdapter(new ArrayAdapter<String>(new_publication.this, android.R.layout.simple_spinner_dropdown_item, students));





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

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }



    private void uploadNewArticle(String title,String coauthor,String category,String pabstract,String authorid,String refNumber) throws IOException {
        // url to post our data


        loadingPB.setVisibility(View.VISIBLE);

        InputStream iStream = null;

        iStream = getContentResolver().openInputStream(uripath);
        final byte[] inputData = getBytes(iStream);


        String url = "https://dissertation1.com/api/submit-new-article";
// Creating string request with post method.
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST,url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {

                        // Hiding the progress dialog after all task complete.
                        loadingPB.setVisibility(View.GONE);
                        String resultResponse = new String(response.data);

                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(resultResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //Toast.makeText(contact_activity.this,resultResponse, Toast.LENGTH_LONG).show();
                        // Showing response message coming from server.
                        try {
                            if( obj.getString("status").equals("ok")) {

                                loadingPB.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(new_publication.this);
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.submit_article_dialogue, viewGroup, false);
                                builder.setView(dialogView);

                                delivered = dialogView.findViewById(R.id.delivered);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCanceledOnTouchOutside(false);

                                delivered.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(new_publication.this,profile.class);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            if(obj.getString("status").equals("not ok")) {

                                loadingPB.setVisibility(View.GONE);
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(new_publication.this);
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.submit_article_dialogue_no_credit, viewGroup, false);
                                builder.setView(dialogView);

                                delivered = dialogView.findViewById(R.id.delivered);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCanceledOnTouchOutside(false);

                                delivered.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent i = new Intent(new_publication.this,profile.class);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        error.printStackTrace();
                        loadingPB.setVisibility(View.GONE);


                        Log.i(TAG,"Error :" + error.toString());
                        // Showing error message if something goes wrong.
                        Toast.makeText(new_publication.this,error.toString(), Toast.LENGTH_LONG).show();




                    }
                }){



            @RequiresApi(api = Build.VERSION_CODES.O)
            protected Map<String, String> getParams() throws AuthFailureError {



                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params
                params.put("authorid", authorid);
                params.put("title", title);
                params.put("coauthor", coauthor);
                params.put("category", category);
                params.put("pabstract", pabstract);
                params.put("refNumber", refNumber);
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String pdfName = System.currentTimeMillis() + ".pdf";
                params.put("pdFfile", new DataPart(pdfName, inputData));

                return params;
            }

        };



        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(new_publication.this);
        rQueue.add(volleyMultipartRequest);

    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // test for connection
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            Toast.makeText(new_publication.this,"Internet Connection not Present", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private void uploadDocFile(String url, String filePath, String title, String coauthor, String category, String pabstract, String authorid,String refNumber) {
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
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"authorid\"\r\n\r\n" + authorid + "\r\n");
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"refNumber\"\r\n\r\n" + refNumber + "\r\n");

            // Add file to the request
            dataOutputStream.writeBytes("--" + boundary + "\r\n");
            dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"pdFfile\";filename=\"" + file.getName() + "\"\r\n");
            dataOutputStream.writeBytes("Content-Type: application/pdf\r\n\r\n");


try{
            FileInputStream fileInputStream = new FileInputStream(file);

    int maxBufferSize = 1 * 1024 * 1024;
    byte[] buffer = new byte[maxBufferSize];
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
                            progressDialog.dismiss();
                            String resultResponse = new String(response.data);

                            //Toast.makeText(contact_activity.this,resultResponse, Toast.LENGTH_LONG).show();
                            // Showing response message coming from server.
                            if(resultResponse.equals("ok")) {

                                progressDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(new_publication.this);
                                ViewGroup viewGroup = findViewById(android.R.id.content);
                                View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.submit_publication_dialogue, viewGroup, false);
                                builder.setView(dialogView);

                                delivered = dialogView.findViewById(R.id.delivered);
                                AlertDialog alertDialog = builder.create();
                                alertDialog.setCanceledOnTouchOutside(false);

                                delivered.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        //alertDialog.hide();

                                        Intent i = new Intent(new_publication.this,profile.class);
                                        startActivity(i);
                                        finish();
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
                            progressDialog.dismiss();
                            // Showing error message if something goes wrong.
                            Toast.makeText(new_publication.this,error.getMessage(), Toast.LENGTH_LONG).show();



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

    private byte[] getFileData(String filePath) {

        File pdf = new File(filePath);
        int size = (int) pdf.length();
        byte[] bytes = new byte[size];
        byte[] tmpBuff = new byte[size];

        try (FileInputStream inputStream = new FileInputStream(pdf)) {
            int read = inputStream.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = inputStream.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }









    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }


}