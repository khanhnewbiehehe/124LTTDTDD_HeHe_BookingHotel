package com.example.booking_hotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;

import java.util.List;

public class img_slider_adapter_2 extends PagerAdapter {

    private List<img_slider_2> mListPhoto;

    public img_slider_adapter_2(List<img_slider_2> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        // Inflate the layout for each page
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_img_slider, container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        img_slider_2 photo = mListPhoto.get(position);

        // Use Glide to load the image dynamically (whether it's a URL or resource ID)
        if (photo.getImageUrl() != null && !photo.getImageUrl().isEmpty()) {
            // If the image is a URL, load it with Glide
            Glide.with(container.getContext())
                    .load(photo.getImageUrl()) // Image URL
                    .into(imgPhoto);
        } else {
            // If the image is a drawable resource, load it using setImageResource
            imgPhoto.setImageResource(photo.getResourceId());
        }

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return (mListPhoto != null) ? mListPhoto.size() : 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
