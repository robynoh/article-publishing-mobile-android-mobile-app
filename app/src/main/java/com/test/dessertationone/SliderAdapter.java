package com.test.dessertationone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class SliderAdapter extends PagerAdapter {


    Context context ;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;

    }

    int images[] = {R.drawable.exp_img,R.drawable.slide_3_img,R.drawable.slide_2_img};
    String textTitle[] = {"Do research easily","Create publication","Help others"};
    String textDescription[] = {"Buy credits, upload & learn","Do research easily & create your own publication","chamber7inc@gmail.com"};


    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view= layoutInflater.inflate(R.layout.slides_layout,container,false);
        ImageView imageView=view.findViewById(R.id.imageView);
        imageView.setImageResource(images[position]);

        TextView textTitle1=view.findViewById(R.id.textTitle);
        textTitle1.setText(textTitle[position]);

        TextView textDescription1=view.findViewById(R.id.textDescription);
        textDescription1.setText(textDescription[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
