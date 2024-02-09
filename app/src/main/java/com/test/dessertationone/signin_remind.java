package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class signin_remind extends AppCompatActivity {
    EditText emailaddress;
    EditText password;
    boolean isAllFieldsChecked = false;
    private ProgressBar loadingPB;
    RequestQueue queue;
    Button delivered,continuebtn,gotosignup2,forgot_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_remind); Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar.setTitle("");
        toolbar.setSubtitle("");

        emailaddress = findViewById(R.id.emailaddress);
        password = findViewById(R.id.password);
        continuebtn=findViewById(R.id.continuebtn);
        gotosignup2= findViewById(R.id.gotosignup2);
        forgot_password = findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(signin_remind.this,forgot_password1.class);
                startActivity(a);

            }
        });

        loadingPB = findViewById(R.id.idLoadingPB);

        Button loginnow=findViewById(R.id.loginnow);
        loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked= CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                    loadingPB.setVisibility(View.VISIBLE);

                    String emailValue=emailaddress.getText().toString();
                    String passwordValue=password.getText().toString();

                    signingauthor(emailValue,passwordValue);



                }

            }
        });


        continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(signin_remind.this,MainActivity.class);
                startActivity(j);
            }
        });

        gotosignup2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent a = new Intent(signin_remind.this,create_account.class);
                startActivity(a);

            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private boolean CheckAllFields() {


        if (emailaddress.length() == 0) {
            emailaddress.setError("Enter your email");
            return false;
        }
        if (password.length() == 0) {
            password.setError("Enter your password");
            return false;
        }
        if(!isValidEmail(emailaddress.getText().toString())){
            emailaddress.setError("please enter a valid email address");
            return false;

        }

        return true;
    }


    public void signingauthor(String email,String password){



        String URL = "https://dissertation1.com/api/signin-author/"+email+"/"+password;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {

                        loadingPB.setVisibility(View.GONE);

                        SharedPreferences sharedPref = getSharedPreferences("myKey", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        editor.putString("firstname", obj.getString("firstname"));
                        editor.putString("lastname", obj.getString("lastname"));
                        editor.putString("work",  obj.getString("work"));
                        editor.putString("position",  obj.getString("position"));
                        editor.putString("title", obj.getString("title"));
                        editor.putString("email",  obj.getString("email"));
                        editor.putString("authorid",  obj.getString("authorid"));

                        // to save our data with key and value.
                        editor.apply();

                        Intent i = new Intent(signin_remind.this,MainActivity.class);
                        startActivity(i);
                        finish();




                        // Toast.makeText(introMap.this,"done", Toast.LENGTH_LONG).show();


                    }else if(obj.getString("status").equals("does not exist")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(signin_remind.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.notexist_dialogue, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(signin_remind.this, create_account.class);
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }
}