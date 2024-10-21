package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class update_room extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_room);

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
                Intent intent = new Intent(update_room.this, manager_room_details.class);
                startActivity(intent);
            }
        });

        // Spinner Loại phòng
        Spinner loaiPhong = findViewById(R.id.spn_LoaiPhong);
        List<String> listLoaiPhong = new ArrayList<>();
        listLoaiPhong.add("Loại phòng"); // Thêm phần tử placeholder
        listLoaiPhong.add("Phòng đôi");
        listLoaiPhong.add("Phòng đơn");
        listLoaiPhong.add("Phòng gia đình");
        ArrayAdapter<String> LoaiPhongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiPhong);
        LoaiPhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiPhong.setAdapter(LoaiPhongAdapter);
        loaiPhong.setSelection(0); // Hiển thị giá trị placeholder ban đầu

        // Spinner Loại khung cảnh
        Spinner loaiView = findViewById(R.id.spn_LoaiView);
        List<String> listKhungCanh = new ArrayList<>();
        listKhungCanh.add("Loại khung cảnh"); // Thêm phần tử placeholder
        listKhungCanh.add("Thành phố");
        listKhungCanh.add("Biển");
        listKhungCanh.add("Cửa sổ");
        listKhungCanh.add("Không có");
        ArrayAdapter<String> KhungCanhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listKhungCanh);
        KhungCanhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiView.setAdapter(KhungCanhAdapter);
        loaiView.setSelection(0); // Hiển thị giá trị placeholder ban đầu

        // Spinner Loại giường
        Spinner loaiGiuong = findViewById(R.id.spn_LoaiGiuong);
        List<String> listLoaiGiuong = new ArrayList<>();
        listLoaiGiuong.add("Loại giường"); // Thêm phần tử placeholder
        listLoaiGiuong.add("1 giường đơn");
        listLoaiGiuong.add("2 giường đơn");
        listLoaiGiuong.add("1 giường đôi");
        listLoaiGiuong.add("2 giường đôi");
        ArrayAdapter<String> LoaiGiuongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiGiuong);
        LoaiGiuongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiGiuong.setAdapter(LoaiGiuongAdapter);
        loaiGiuong.setSelection(0); // Hiển thị giá trị placeholder ban đầu
    }

}
