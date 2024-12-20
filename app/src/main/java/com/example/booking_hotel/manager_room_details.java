package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class manager_room_details extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private List<img_slider> mListPhoto;
    private Button btnLeft, btnRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_room_details);

        mViewPager = findViewById(R.id.img_slider);
        mCircleIndicator = findViewById(R.id.circle_indicator);

        mListPhoto = getListPhoto();
        img_slider_adapter adapter = new img_slider_adapter(mListPhoto);
        mViewPager.setAdapter(adapter);

        mCircleIndicator.setViewPager(mViewPager);

        Button btn = findViewById(R.id.btn_Back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rDetail_list = new Intent(manager_room_details.this , manager_roomlist.class);
                startActivity(rDetail_list);
            }
        });

        Button btn_capnhat = findViewById(R.id.btn_CapNhatPhong);
        btn_capnhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rDetail_list = new Intent(manager_room_details.this , update_room.class);
                startActivity(rDetail_list);
            }
        });

    }



    private List<img_slider> getListPhoto() {
        List<img_slider> list = new ArrayList<>();
        list.add(new img_slider(R.drawable.room_img));
        list.add(new img_slider(R.drawable.room_img));
        list.add(new img_slider(R.drawable.room_img));
        list.add(new img_slider(R.drawable.room_img));

        return list;
    }


}