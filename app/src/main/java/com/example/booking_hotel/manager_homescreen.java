package com.example.booking_hotel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class manager_homescreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_homescreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button checkinDateButton = findViewById(R.id.btn_manager_checkin);
        Button checkoutDateButton = findViewById(R.id.btn_manager_checkout);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        checkinDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    manager_homescreen.this,
                    (view, year1, month1, dayOfMonth) -> {
                        checkinDateButton.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    }, year, month, day);
            datePickerDialog.show();
        });


        checkoutDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    manager_homescreen.this,
                    (view, year12, month12, dayOfMonth) -> {
                        checkoutDateButton.setText(dayOfMonth + "/" + (month12 + 1) + "/" + year12);
                    }, year, month, day);
            datePickerDialog.show();
        });

        Button btnQLKH = findViewById(R.id.btn_manager_quanlykhachhang);
        Button btnQLP = findViewById(R.id.btn_manager_quanlyphong);
        Button btnQLDDP = findViewById(R.id.btn_manager_quanlydondatphong);
        Button btnSearchRoom = findViewById(R.id.btn_manager_search_room);

        btnSearchRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manager_homescreen.this, manager_roomlist.class);
                startActivity(intent);
            }
        });

        // Set a click listener to navigate to another activity
        btnQLKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(manager_homescreen.this, list_customer.class);
                startActivity(intent);
            }
        });

        btnQLP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(manager_homescreen.this, manager_roomlist.class);
                startActivity(intent);
            }
        });

        btnQLDDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(manager_homescreen.this, list_order.class);
                startActivity(intent);
            }
        });

    }
}