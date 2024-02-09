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

public class all_subcategory_adapter extends ArrayAdapter<all_subcategory> {
    RequestQueue queue;
    RequestQueue queue2;
    ArrayList<all_subcategory> products;
    Context context;
    int resource;


    public all_subcategory_adapter(Context context, int resource, ArrayList<all_subcategory> products) {
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
            convertView = layoutInflater.inflate(R.layout.subcategory_layout, null, true);

        }
        all_subcategory product = getItem(position);

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






