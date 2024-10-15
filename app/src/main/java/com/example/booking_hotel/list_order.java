package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class list_order extends AppCompatActivity {
    private ArrayList<room_list_detail> room_listProperties = new ArrayList<>();
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

        Button btnBack = findViewById(R.id.btn_Back);
        Button btnCreateOrder = findViewById(R.id.btn_ThemDonThue);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(list_order.this, manager_homescreen.class);
                startActivity(intent);
            }
        });

        btnCreateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(list_order.this, create_order.class);
                startActivity(intent);
            }
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

        room_listProperties.add(
                new room_list_detail("308","Phòng đôi", "20/9/2024 - 22/9/2024","Chưa nhận phòng","room_img"));
        room_listProperties.add(
                new room_list_detail("309","Phòng đon", "21/9/2024 - 22/9/2024","Đang sử dụng", "room_img"));

        ArrayAdapter<room_list_detail> adapter = new ListviewOrderAdapter(this, 0, room_listProperties);

        ListView listView = (ListView) findViewById(R.id.order_list);
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                room_list_detail property = room_listProperties.get(position);

                Intent intent = new Intent(list_order.this, details_order.class);
                intent.putExtra("room_name", property.getRoomName());
                intent.putExtra("room_type", property.getRoomType());
                intent.putExtra("room_trangthai", property.getRoomTrangthai());
                intent.putExtra("image", property.getRoomImage());
                startActivity(intent);
            }
        };
//set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);

    }
}