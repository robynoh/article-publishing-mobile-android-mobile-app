package com.test.dessertationone;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;

public class all_categories_adapter extends ArrayAdapter<all_categories> {
    RequestQueue queue;
    RequestQueue queue2;
    ArrayList<all_categories> products;
    Context context;
    int resource;


    public all_categories_adapter(Context context, int resource, ArrayList<all_categories> products) {
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
            convertView = layoutInflater.inflate(R.layout.all_category_layout, null, true);

        }
        all_categories product = getItem(position);

//        ImageView image = (ImageView) convertView.findViewById(R.id.newclientimg);
//        Picasso.with(getContext()).load(product.getImage()).into(image);


        TextView category = (TextView) convertView.findViewById(R.id.categoryname);
        category.setText(product.getCategory());



        return convertView;
    }







}






