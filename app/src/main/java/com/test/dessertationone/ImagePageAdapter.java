package com.test.dessertationone;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

class ImagePagerAdapter extends PagerAdapter {

    private final List<Integer> imageIds;
    private final List<String> titles;
    private final List<String> descriptions;

    public ImagePagerAdapter(List<Integer> imageIds,List<String> titles,List<String> descriptions) {
        this.imageIds = imageIds;
        this.titles = titles;
        this.descriptions = descriptions;
    }

    @Override
    public int getCount() {
        return imageIds.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.slide_layout, container, false);
        ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(imageIds.get(position));

        TextView textTitle1=view.findViewById(R.id.textTitle);
        textTitle1.setText(titles.get(position));

        TextView textDescription1=view.findViewById(R.id.textDescription);
        textDescription1.setText(descriptions.get(position));



        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
