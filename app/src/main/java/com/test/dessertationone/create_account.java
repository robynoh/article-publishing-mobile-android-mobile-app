package com.test.dessertationone;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class create_account extends AppCompatActivity {

    EditText firstname;
    EditText lastname;
    EditText place_of_work;
    EditText position;
    EditText password;
    EditText cpassword;
    EditText email;
    boolean isAllFieldsChecked = false;
    String titleValue;
    private ProgressBar loadingPB;
    RequestQueue queue;
    Button delivered;
    Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        Toolbar toolbar = findViewById(R.id.toolbar);


        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        spinner = findViewById(R.id.spinner);
        ArrayList<String> arrayListspinner = new ArrayList<>();
        arrayListspinner.add("");
        arrayListspinner.add("Mr");
        arrayListspinner.add("Mrs");
        arrayListspinner.add("Dr");
        arrayListspinner.add("Phd");
        arrayListspinner.add("Engr");
        arrayListspinner.add("Prof.");
        arrayListspinner.add("LLB");

        loadingPB = findViewById(R.id.idLoadingPB);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(create_account.this, android.R.layout.simple_spinner_item, arrayListspinner);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);



        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        place_of_work = findViewById(R.id.place_of_work);
        position = findViewById(R.id.position);
        email = findViewById(R.id.email);

        password=findViewById(R.id.password);
        cpassword=findViewById(R.id.cpassword);

        Button gotosignup =findViewById(R.id.gotosignup);
        gotosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent j = new Intent(create_account.this,sign_in.class);
                startActivity(j);
            }
        });

        Button create_account =findViewById(R.id.submit_account);
        create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isAllFieldsChecked= CheckAllFields();

                // the boolean variable turns to be true then
                // only the user must be proceed to the activity2
                if (isAllFieldsChecked) {

                    loadingPB.setVisibility(View.VISIBLE);

                    String firstnameValue=firstname.getText().toString();
                    String lastnameValue=lastname.getText().toString();
                    String workValue=place_of_work.getText().toString();
                    String positionValue=position.getText().toString();
                    String emailValue=email.getText().toString();
                    String titleValuetxt=spinner.getSelectedItem().toString();;
                    String passwordValue=password.getText().toString();


                     registernewauthor(firstnameValue,lastnameValue,workValue,positionValue,titleValuetxt,passwordValue,emailValue);


                }



            }
        });
    }

    private boolean CheckAllFields() {


        if (firstname.length() == 0) {
            firstname.setError("Enter your firstname");
            return false;
        }
        if (lastname.length() == 0) {
            lastname.setError("Enter your lastname");
            return false;
        }
        if (place_of_work.length() == 0) {
            place_of_work.setError("Enter your work place");
            return false;
        }
        if (position.length() == 0) {
            position.setError("Enter your position");
            return false;
        }

        if (spinner.getSelectedItem() =="") {
            setSpinnerError(spinner,"Please choose a title");
            return false;
        }

        if (password.length() == 0) {
            password.setError("Enter a password you can remember");
            return false;
        }

        if (cpassword.length() == 0) {
            cpassword.setError("Please confirm your password");
            return false;
        }
        if(!password.getText().toString().equals(cpassword.getText().toString())) {

            cpassword.setError("Both passwords does not match");
            return false;
        }
        if(!isValidEmail(email.getText().toString())){
            email.setError("please enter a valid email address");
            return false;

        }


//        if (titleValue.isEmpty()) {
//            setSpinnerError(spinner,"Please choose a title");
//           // return false;
//        }

        // after all validation return true.
        return true;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
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





    public void registernewauthor(String firstname,String lastname, String work, String position, String title, String password, String email){



        String URL = "https://dissertation1.com/api/new-author/"+firstname+"/"+lastname+"/"+work+"/"+position+"/"+title+"/"+password+"/"+email;

        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                JSONObject obj = null;
                try {
                    obj = new JSONObject(response);


                    if (obj.getString("status").equals("ok")) {

                        loadingPB.setVisibility(View.GONE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(create_account.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.success_dialogue, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(create_account.this, sign_in.class);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();



                        // Toast.makeText(introMap.this,"done", Toast.LENGTH_LONG).show();


                    }else if(obj.getString("status").equals("already exist")) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(create_account.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.warning_dialuge, viewGroup, false);
                        builder.setView(dialogView);

                        delivered = dialogView.findViewById(R.id.delivered);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.setCanceledOnTouchOutside(false);

                        delivered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(create_account.this, sign_in.class);
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



    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}