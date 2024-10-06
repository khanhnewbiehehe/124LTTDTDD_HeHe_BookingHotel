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

public class create_order extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_order);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner loaiPhong = findViewById(R.id.spn_LoaiPhong);
        List<String> listLoaiPhong = new ArrayList<>();
        listLoaiPhong.add("Loại phòng:");
        listLoaiPhong.add("Phòng đôi");
        listLoaiPhong.add("Phòng đơn");
        listLoaiPhong.add("Phòng gia đình");
        ArrayAdapter<String> LoaiPhongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiPhong);
        LoaiPhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiPhong.setAdapter(LoaiPhongAdapter);
        loaiPhong.setSelection(0);

        Spinner loaiView = findViewById(R.id.spn_View);
        List<String> listKhungCanh = new ArrayList<>();
        listKhungCanh.add("Loại khung cảnh:");
        listKhungCanh.add("Thành phố");
        listKhungCanh.add("Biển");
        listKhungCanh.add("Cửa sổ");
        listKhungCanh.add("Không có");
        ArrayAdapter<String> KhungCanhAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listKhungCanh);
        KhungCanhAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiView.setAdapter(KhungCanhAdapter);
        loaiView.setSelection(0);

        Spinner loaiGiuong = findViewById(R.id.spn_LoaiGiuong);
        List<String> listLoaiGiuong = new ArrayList<>();
        listLoaiGiuong.add("Loại giường:");
        listLoaiGiuong.add("1 giường đơn");
        listLoaiGiuong.add("2 giường đơn");
        listLoaiGiuong.add("1 giường đôi");
        listLoaiGiuong.add("2 giường đôi");
        ArrayAdapter<String> LoaiGiuongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiGiuong);
        LoaiGiuongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiGiuong.setAdapter(LoaiGiuongAdapter);
        loaiGiuong.setSelection(0);
    }
}