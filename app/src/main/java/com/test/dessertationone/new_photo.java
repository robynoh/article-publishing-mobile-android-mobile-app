package com.test.dessertationone;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class new_photo extends AppCompatActivity {
    Button choose,upload;
    Bitmap  profilePhotoBitmap;
    long  profilePhotoimagename;
    String profilePhotopath;
    RequestQueue queue;
    private ProgressBar loadingPB;
    Button delivered;

    ActivityResultLauncher<String> profilephoto = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri

                    if(uri !=null) {
                        try {
                            Context context= new_photo.this;
                            String path =RealPathUtil.getRealPath(context,uri);
                            Random rand = new Random();

                            profilePhotoBitmap=pathToBitmap(path,getFileName(path));

                            profilePhotoimagename =rand.nextInt(20);

                           profilePhotopath = path;
                            ImageView profile_image=findViewById(R.id.profile_image);
                            profile_image.setImageBitmap(getScaledBitmap(path, 800, 800));
//
//
//                            driverLicensetext.setText(getFileName(path));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_photo);

        Toolbar toolbar=findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");


        choose=findViewById(R.id.choose);
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[] {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    } else {
                        imageChooserdriverlicense();
                    }
                }



            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");
        pullprofilePhoto(authorid);



        loadingPB = findViewById(R.id.idLoadingPB);
        upload=findViewById(R.id.upload);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
if(profilePhotopath !=null) {
//    uploadImageAndString("https://dissertation1.com/api/submit-photo", profilePhotoBitmap,authorid);
    submit_author_photo(authorid);
}
            }
        });

    }

    private void submit_author_photo(String authorid) {
        // url to post our data


        loadingPB.setVisibility(View.VISIBLE);

        String url = "https://dissertation1.com/api/submit-photo";
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new_photo.this);
                            ViewGroup viewGroup = findViewById(android.R.id.content);
                            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.update_dialogue, viewGroup, false);
                            builder.setView(dialogView);

                            delivered = dialogView.findViewById(R.id.delivered);
                            AlertDialog alertDialog = builder.create();
                            alertDialog.setCanceledOnTouchOutside(false);

                            delivered.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(new_photo.this,profile.class);
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
                        Toast.makeText(new_photo.this,error.getMessage(), Toast.LENGTH_LONG).show();




                    }
                }){



            @RequiresApi(api = Build.VERSION_CODES.O)
            protected Map<String, String> getParams() throws AuthFailureError {



                // Creating Map String Params.
                Map<String, String> params = new HashMap<String, String>();

                // Adding All values to Params
                params.put("authorid", authorid);
                return params;
            }

            /*
             *pass files using below method
             * */
            @Override

            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();

                    params.put("picture", new DataPart(profilePhotoimagename + ".png", getFileDataFromDrawable(profilePhotoBitmap)));



                return params;
            }

        };



        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue rQueue = Volley.newRequestQueue(new_photo.this);
        rQueue.add(volleyMultipartRequest);

    }

    public void imageChooserdriverlicense(){

        profilephoto.launch("image/*");
    }

    public static String getFileName(String path) {
        try {

            return path != null ? path.substring(path.lastIndexOf("/") + 1) : "unknown";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "unknown";
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private Bitmap pathToBitmap(String path, String imageName) {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(path);
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(image.getAbsolutePath(),bmOptions);


        return bitmap;
    }

    private Bitmap getScaledBitmap(String picturePath, int width, int height) {
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, sizeOptions);

        int inSampleSize = calculateInSampleSize(sizeOptions, width, height);

        sizeOptions.inJustDecodeBounds = false;
        sizeOptions.inSampleSize = inSampleSize;

        return BitmapFactory.decodeFile(picturePath, sizeOptions);
    }
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }


//    private void uploadImageAndString(String url, Bitmap image, String stringParam) {
//
//        // Instantiate the RequestQueue.
//
//        loadingPB.setVisibility(View.VISIBLE);
//        RequestQueue queue = Volley.newRequestQueue(this);
//
//        // Create a multipart request
//        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
//                new Response.Listener<NetworkResponse>() {
//                    @Override
//                    public void onResponse(NetworkResponse response) {
//                         //Toast.makeText(new_photo.this,response.getMessage(), Toast.LENGTH_LONG).show();
//                        // Handle response
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        // Handle error
//                        Toast.makeText(new_photo.this,error.getMessage(), Toast.LENGTH_LONG).show();
//
//                    }
//                }) {
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("User-Agent", "Mozilla/5.0");
//                return headers;
//            }
//            // Add image data to the request
//            @Override
//            protected Map<String, DataPart> getByteData() {
//                Map<String, DataPart> params = new HashMap<>();
//                params.put("picture", new DataPart("image.jpg", getFileDataFromBitmap(image)));
//                return params;
//            }
//
//            // Add string parameter to the request
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<>();
//                params.put("authorid", stringParam);
//                return params;
//            }
//
//        };
//
//        // Add the request to the RequestQueue.
//        queue.add(multipartRequest);
//
//
//
//
//
//    }

    // Helper method to convert Bitmap image to byte array
    private byte[] getFileDataFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }



    public void pullprofilePhoto(String author){



        String URL = "https://dissertation1.com/api/pull-photo/"+author;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {


                        ImageView image = findViewById(R.id.profile_image);
                        if(! obj.getString("picture").isEmpty()) {
                            Picasso.get().load(obj.getString("picture")).into(image);
                          //  Picasso.with(new_photo.this).load(obj.getString("picture")).into(image);
                        }
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

}