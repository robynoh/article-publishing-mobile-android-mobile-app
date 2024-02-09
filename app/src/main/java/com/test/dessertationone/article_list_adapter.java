package com.test.dessertationone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class article_list_adapter extends ArrayAdapter<all_articles_list> {
    RequestQueue queue;
    RequestQueue queue2;
    ArrayList<all_articles_list> products;
    Context context;
    int resource;


    public article_list_adapter(Context context, int resource, ArrayList<all_articles_list> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) getContext()
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.all_article, null, true);

        }
        all_articles_list product = getItem(position);

//        ImageView image = (ImageView) convertView.findViewById(R.id.newclientimg);
//        Picasso.with(getContext()).load(product.getImage()).into(image);


        TextView title = (TextView) convertView.findViewById(R.id.article_title);
        title.setText(product.getTopic());

        TextView work = (TextView) convertView.findViewById(R.id.work);
        work.setText(product.getWork());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(product.getTitle()+" "+product.getFirstname()+" "+product.getLastname());

//        TextView dateposted= (TextView) convertView.findViewById(R.id.postdate);
//        dateposted.setText(product.getDate_posted());


        return convertView;
    }







}

