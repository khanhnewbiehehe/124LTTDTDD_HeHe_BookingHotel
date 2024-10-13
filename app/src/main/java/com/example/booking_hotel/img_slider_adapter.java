package com.example.booking_hotel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;
public class img_slider_adapter extends PagerAdapter {

    private List<img_slider> mListPhoto;

    public img_slider_adapter(List<img_slider> mListPhoto) {
        this.mListPhoto = mListPhoto;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_img_slider,container, false);
        ImageView imgPhoto = view.findViewById(R.id.img_photo);

        img_slider photo = mListPhoto.get(position);
        imgPhoto.setImageResource(photo.getResourceId());

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        if(mListPhoto !=null) {
            return  mListPhoto.size();
        }
        return 0;
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
