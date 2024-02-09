package com.test.dessertationone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class search extends AppCompatActivity {

    ArrayList<all_categories> arrayList;
    ListView lv;
    private ProgressBar loadingPB;

    all_categories_adapter adapter;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar=findViewById(R.id.toolbar);

        // using toolbar as ActionBar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("");
        toolbar.setSubtitle("");





        loadingPB = findViewById(R.id.idLoadingPB);

        lv =findViewById(R.id.listView);

        arrayList = new ArrayList<>();

        pull_category();



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onItemClick(AdapterView<?> parent, View v, int position, long id){

                // ListView Clicked item index

                String category = arrayList.get(position).getCategory();
                String categoryValue = arrayList.get(position).getCategoryValue();
                String categoryid = arrayList.get(position).getCategoryId();







                Intent intent = new Intent(search.this,subcategory.class);
                intent.putExtra("category",category);
                intent.putExtra("categoryValue",categoryValue);
                intent.putExtra("categoryid",categoryid);
                startActivity(intent);



            }
        });

//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String tutorialsName = parent.getItemAtPosition(position).toString();
//                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);;
//                // Toast.makeText(parent.getContext(), "Selected: " + tutorialsName,          Toast.LENGTH_LONG).show();
//
//                if(tutorialsName.equals("Day")){
//
//                    // calender class's instance and get current date , month and year from calender
//                    final Calendar c = Calendar.getInstance();
//                    int mYear = c.get(Calendar.YEAR); // current year
//                    int mMonth = c.get(Calendar.MONTH); // current month
//                    int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
//                    // date picker dialog
//                    DatePickerDialog datePickerDialog = new DatePickerDialog(cash_payment.this,
//                            new DatePickerDialog.OnDateSetListener() {
//
//                                @Override
//                                public void onDateSet(DatePicker view, int year,
//                                                      int monthOfYear, int dayOfMonth) {
//                                    // set day of month , month and year value in the edit text
////                                    expirydate.setText(dayOfMonth + "/"
////                                            + (monthOfYear + 1) + "/" + year);
//                                    Intent a = new Intent(cash_payment.this,daily_earning.class);
//                                    a.putExtra("day", String.valueOf(dayOfMonth));
//                                    a.putExtra("month", String.valueOf(monthOfYear));
//                                    a.putExtra("year", String.valueOf(year));
//                                    startActivity(a);
//                                    //finish();
//
//
//                                    // pull_all_cash_payment_daily(riderid, dayOfMonth,monthOfYear,year);
//
//                                }
//                            }, mYear, mMonth, mDay);
//                    datePickerDialog.show();
//
//                }
//
//                if(tutorialsName.equals("Month")){
//
//                    Intent b = new Intent(cash_payment.this,monthly_earning.class);
//                    startActivity(b);
//                    // finish();
//
//                    // pull_all_cash_payment_monthly(riderid);
//
//                }
//
//                if(tutorialsName.equals("Week")){
//
//                    Intent c = new Intent(cash_payment.this,week_sort.class);
//                    startActivity(c);
//                    //finish();
//
//                }
//
//
//
//
//            }
//            @Override
//            public void onNothingSelected(AdapterView <?> parent) {
//            }
//        });
    }




    private void pull_category(){



        // Building the url to the web service
        String URL = "https://dissertation1.com/api/pull-categories";

        loadingPB.setVisibility(View.VISIBLE);
        queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject obj = null;

                try {
                    obj = new JSONObject(response);
                    JSONArray jsonArray = obj.getJSONArray("articles");
                    loadingPB.setVisibility(View.GONE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject productObject = jsonArray.getJSONObject(i);


                        arrayList.add(new all_categories(
                                productObject.getString("category"),
                                productObject.getString("categoryvalue"),
                                productObject.getString("categoryid")


                        ));
                    }


                    adapter = new all_categories_adapter(search.this, R.layout.all_category_layout, arrayList);
                    lv.setAdapter(adapter);




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