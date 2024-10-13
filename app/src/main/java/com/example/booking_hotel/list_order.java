package com.example.booking_hotel;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class list_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner Phong = findViewById(R.id.spn_Room);
        List<String> listPhong = new ArrayList<>();
        listPhong.add("Trạng thái");
        listPhong.add("Chưa nhận phòng");
        listPhong.add("Đang sử dụng");
        listPhong.add("Đã thanh toán");
        ArrayAdapter<String> PhongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPhong);
        PhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Phong.setAdapter(PhongAdapter);
        Phong.setSelection(0);
    }
}