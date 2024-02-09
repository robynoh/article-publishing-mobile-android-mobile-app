package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class forgot_password2 extends AppCompatActivity {

    EditText resetcode,password,cpassword;
    Button createpassword;
    private ProgressBar loadingPB;
    boolean isAllFieldsChecked = false;
    RequestQueue queue;
    Button delivered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password2);

        resetcode=findViewById(R.id.resetcode);
        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);
        createpassword=findViewById(R.id.createpassword);
        loadingPB = findViewById(R.id.idLoadingPB);


        String trackid = getIntent().getStringExtra("trackid");


        createpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked= CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                    loadingPB.setVisibility(View.VISIBLE);

                    String resetValue=resetcode.getText().toString();
                    String passwordValue=password.getText().toString();





                    changePassword(resetValue,passwordValue,trackid);



                }

            }
        });
    }

    private boolean CheckAllFields() {


        if (password.length() == 0) {
            password.setError("Enter password");
            return false;
        }
        if (cpassword.length() == 0) {
            cpassword.setError("Enter confirm password");
            return false;
        }
        if(!password.getText().toString().equals(cpassword.getText().toString())) {

            cpassword.setError("Both passwords does not match");
            return false;
        }

        return true;
    }

    public void changePassword(String resetValue, String passwordValue,String trackid){



        String URL = "https://dissertation1.com/api/changepassword/"+resetValue+"/"+passwordValue+"/"+trackid;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")){



                        loadingPB.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(forgot_password2.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.password_reset_success, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(forgot_password2.this,forgot_password2.class);
                                i.putExtra("trackid",trackid);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();


                    }else{

                        loadingPB.setVisibility(View.GONE);

                        Toast.makeText(forgot_password2.this,"Invalid reset code", Toast.LENGTH_LONG).show();
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