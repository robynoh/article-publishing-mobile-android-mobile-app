package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class forgot_password1 extends AppCompatActivity {

    Button submitbtn;
    EditText emailaddress;
    boolean isAllFieldsChecked = false;
    private ProgressBar loadingPB;
    RequestQueue queue;
    Button delivered;
    String trackid="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password1);

        submitbtn=findViewById(R.id.submitbtn);
        emailaddress = findViewById(R.id.emailaddress);
        loadingPB = findViewById(R.id.idLoadingPB);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked= CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                    loadingPB.setVisibility(View.VISIBLE);

                    String emailValue=emailaddress.getText().toString();

                    submitEmail(emailValue);



                }

            }
        });
    }

    private boolean CheckAllFields() {


        if (emailaddress.length() == 0) {
            emailaddress.setError("Enter your email");
            return false;
        }

        if(!isValidEmail(emailaddress.getText().toString())){
            emailaddress.setError("please enter a valid email address");
            return false;

        }

        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }



    public void submitEmail(String email){



        String URL = "https://dissertation1.com/api/submitemail/"+email;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")){

                        trackid=obj.getString("trackid");

                        loadingPB.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(forgot_password1.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.password_reset_dialogue, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(forgot_password1.this,forgot_password2.class);
                                i.putExtra("trackid",trackid);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();


                    }else{

                        loadingPB.setVisibility(View.GONE);

                        Toast.makeText(forgot_password1.this,obj.getString("message"), Toast.LENGTH_LONG).show();
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