package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class booking_details extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private  List<img_slider> mListPhoto;
    private Button btnLeft, btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(booking_details.this, booking_list.class);
                startActivity(intent);
            }
        });

        mViewPager = findViewById(R.id.img_slider);
        mCircleIndicator = findViewById(R.id.circle_indicator);
        btnLeft = findViewById(R.id.icon_left);
        btnRight = findViewById(R.id.icon_right);

        mListPhoto = getListPhoto();
        img_slider_adapter adapter = new img_slider_adapter(mListPhoto);
        mViewPager.setAdapter(adapter);

        mCircleIndicator.setViewPager(mViewPager);

        btnLeft.setOnClickListener(v -> navigateSlider(-1));
        btnRight.setOnClickListener(v -> navigateSlider(1));
    }

    private List<img_slider> getListPhoto() {
        List<img_slider> list = new ArrayList<>();
        list.add(new img_slider(R.drawable.room_img));
        list.add(new img_slider(R.drawable.room_img));
        list.add(new img_slider(R.drawable.room_img));
        list.add(new img_slider(R.drawable.room_img));

        return list;
    }

    private void navigateSlider(int direction) {
        int currentPosition = mViewPager.getCurrentItem();
        int totalItems = mListPhoto.size();

        int newPosition = currentPosition + direction;

        if (newPosition >= 0 && newPosition < totalItems) {
            mViewPager.setCurrentItem(newPosition);
        }
    }

}