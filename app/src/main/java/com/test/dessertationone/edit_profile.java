package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class edit_profile extends AppCompatActivity {
    RequestQueue queue;
    private ProgressBar loadingPB;
    Button delivered;

    EditText firstnameedit;
    EditText lastnameedit;
    EditText workplaceedit;
    EditText positionedit,current_degree_edit,schoolnameedit;
    Spinner spinner;
    boolean isAllFieldsChecked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");



        String firstname = getIntent().getStringExtra("firstname");
        String lastname = getIntent().getStringExtra("lastname");
        String work = getIntent().getStringExtra("work");
        String position = getIntent().getStringExtra("position");
        String title = getIntent().getStringExtra("title");
        String current_degree = getIntent().getStringExtra("current_degree");
        String schoolname = getIntent().getStringExtra("schoolname");
       // String accountnumber = getIntent().getStringExtra("accountnumber");

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String authorid = sharedPreferences.getString("authorid", "");


        EditText firstnameValue=findViewById(R.id.firstname);
        EditText lastnameValue=findViewById(R.id.lastname);
        EditText positionValue=findViewById(R.id.position);
        EditText workValue=findViewById(R.id.workplace);
        EditText current_degree_Value=findViewById(R.id.current_degree);
        EditText schoolname_value=findViewById(R.id.schoolname);
       // EditText accountnumberValue=findViewById(R.id.accountnumber);

        firstnameedit = findViewById(R.id.firstname);
        lastnameedit = findViewById(R.id.lastname);
        workplaceedit = findViewById(R.id.workplace);
        positionedit = findViewById(R.id.position);
        current_degree_edit = findViewById(R.id.current_degree);
        schoolnameedit = findViewById(R.id.schoolname);
       // accountnumberedit = findViewById(R.id.accountnumber);



        loadingPB = findViewById(R.id.idLoadingPB);

         spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayListspinner = new ArrayList<>();
        arrayListspinner.add(title);
        arrayListspinner.add("Mr");
        arrayListspinner.add("Mrs");
        arrayListspinner.add("Dr");
        arrayListspinner.add("Phd");
        arrayListspinner.add("Engr");
        arrayListspinner.add("Prof.");
        arrayListspinner.add("LLB");

        firstnameValue.setText(firstname);
        lastnameValue.setText(lastname);
        positionValue.setText(position);
        workValue.setText(work);
        current_degree_Value.setText(current_degree);
        schoolname_value.setText(schoolname);

       // accountnumberValue.setText(accountnumber);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(edit_profile.this, android.R.layout.simple_spinner_item, arrayListspinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);


        Button update=findViewById(R.id.update);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                isAllFieldsChecked= CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                    loadingPB.setVisibility(View.VISIBLE);


                    String firstnameValuetxt=firstnameedit.getText().toString();
                    String lastnameValuetxt=lastnameedit.getText().toString();
                    String workValuetxt=workplaceedit.getText().toString();
                    String positionValuetxt=positionedit.getText().toString();
                    String titleValuetxt2=spinner.getSelectedItem().toString();
                    String current_degree_Valuetxt=current_degree_edit.getText().toString();
                    String schoolname_Valuetxt=schoolnameedit.getText().toString();
                  //  String accountnumberValuetxt=accountnumberedit.getText().toString();



                    updateprofile(firstnameValuetxt,lastnameValuetxt,workValuetxt,positionValuetxt,titleValuetxt2,authorid,current_degree_Valuetxt,schoolname_Valuetxt);

                }




            }
        });


    }


    public void updateprofile(String firstname,String lastname, String work, String position, String title, String authorid,String current_degree_Valuetxt,String schoolname_Valuetxt){



        String URL = "https://dissertation1.com/api/update-author/"+firstname+"/"+lastname+"/"+work+"/"+position+"/"+title+"/"+authorid+"/"+current_degree_Valuetxt+"/"+schoolname_Valuetxt;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {

                        loadingPB.setVisibility(View.GONE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(edit_profile.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.update_dialogue, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(edit_profile.this,profile.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();



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

    private boolean CheckAllFields() {


        if (firstnameedit.length() == 0) {
            firstnameedit.setError("Enter your firstname");
            return false;
        }
        if (lastnameedit.length() == 0) {
            lastnameedit.setError("Enter your lastname");
            return false;
        }
        if (workplaceedit.length() == 0) {
            workplaceedit.setError("Enter your work place");
            return false;
        }
        if (positionedit.length() == 0) {
            positionedit.setError("Enter your position");
            return false;
        }

        if (spinner.getSelectedItem() =="") {
            setSpinnerError(spinner,"Please choose a title");
            return false;
        }

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

    public void pullprofile(String author){



        String URL = "https://dissertation1.com/api/pull-author/"+author;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {




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