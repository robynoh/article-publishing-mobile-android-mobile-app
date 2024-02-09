package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class buy_credit extends AppCompatActivity {
    double amount;

    Button stripPay;
    String Customerid;
    String EphemeralKey;
    String ClientSecret;

    Button okay,saveblocked,likebtn,like2btn,buynow,refundPolicy;
    AlertDialog alertDialog;

    String credit;



    String tutorialsName;


    ProgressDialog progressDialog;



    String authorid;
    RequestQueue queue;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_credit);


        Toolbar toolbar=findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");




        Spinner spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayListspinner = new ArrayList<>();

        TextView computeAmount= findViewById(R.id.computeAmount);
        stripPay=findViewById(R.id.payButton);

        arrayListspinner.add("1");
        arrayListspinner.add("2");
        arrayListspinner.add("3");
        arrayListspinner.add("4");
        arrayListspinner.add("5");
        arrayListspinner.add("6");
        arrayListspinner.add("7");
        arrayListspinner.add("8");
        arrayListspinner.add("9");
        arrayListspinner.add("10");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(buy_credit.this, android.R.layout.simple_spinner_item, arrayListspinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        authorid = sharedPreferences.getString("authorid", "");


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tutorialsName = parent.getItemAtPosition(position).toString();
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);;
               // Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
          amount = Integer.valueOf(tutorialsName) *20.23;

          credit=tutorialsName;

                computeAmount.setText("$"+amount);


                stripPay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (spinner.getSelectedItem() =="") {
                            setSpinnerError(spinner,"Please choose the number of credit you want to buy");

                        }else {

                            Intent i = new Intent(buy_credit.this, pay_online.class);
                            i.putExtra("amount", String.valueOf(amount));
                            i.putExtra("credit", tutorialsName);
                            i.putExtra("buyer", authorid);
                            startActivity(i);
                            finish();
                        }

                    }
                });


            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {

                computeAmount.setText("");
            }
        });

    }







    private void saveCredit(String buyer, String credit) {



        String URL = "https://dissertation1.com/api/save-credit/"+buyer+"/"+credit;

        queue = Volley.newRequestQueue(this);

        progressDialog.dismiss();
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("done")) {


                        progressDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(buy_credit.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.credit_bought, viewGroup, false);
                        builder.setView(dialogView);

                        okay = dialogView.findViewById(R.id.okay);
                        AlertDialog alertDialog2 = builder.create();
                        alertDialog2.setCanceledOnTouchOutside(false);

                        okay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(buy_credit.this,profile.class);
                                startActivity(i);
                                finish();
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


    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}