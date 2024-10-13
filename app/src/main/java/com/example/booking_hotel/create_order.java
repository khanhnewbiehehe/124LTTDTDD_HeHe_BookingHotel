package com.example.booking_hotel;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
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

        Spinner Phong = findViewById(R.id.spn_Room);
        List<String> listPhong = new ArrayList<>();
        listPhong.add("Phòng");
        listPhong.add("101");
        listPhong.add("201");
        listPhong.add("301");
        ArrayAdapter<String> PhongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPhong);
        PhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Phong.setAdapter(PhongAdapter);
        Phong.setSelection(0);

        Button checkinDateButton = findViewById(R.id.btn_CheckIn);
        Button checkoutDateButton = findViewById(R.id.btn_CheckOut);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        checkinDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    create_order.this,
                    (view, year1, month1, dayOfMonth) -> {
                        checkinDateButton.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    }, year, month, day);
            datePickerDialog.show();
        });


        checkoutDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    create_order.this,
                    (view, year12, month12, dayOfMonth) -> {
                        checkoutDateButton.setText(dayOfMonth + "/" + (month12 + 1) + "/" + year12);
                    }, year, month, day);
            datePickerDialog.show();
        });
    }
}