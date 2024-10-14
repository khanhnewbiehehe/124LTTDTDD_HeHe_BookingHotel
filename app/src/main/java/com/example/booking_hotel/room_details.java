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

public class room_details extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private List<img_slider> mListPhoto;
    private Button btnLeft, btnRight;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_details);

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
                Intent rDetail_list = new Intent(room_details.this , room_list_k.class);
                startActivity(rDetail_list);
            }
        });

        Button btn_datphong = findViewById(R.id.datphong);
        btn_datphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent datphong_tb = new Intent(room_details.this , tb_dptc.class);
                startActivity(datphong_tb);
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