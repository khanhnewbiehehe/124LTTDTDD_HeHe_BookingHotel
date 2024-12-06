package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booking_hotel.model.Customer;

public class customer_information extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_information);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        String cusName = intent.getStringExtra("txt_CusName");
        String cusPhone = intent.getStringExtra("txt_SDT");
        String cusEmail = intent.getStringExtra("txt_Email");


        TextView txtCusName = findViewById(R.id.txt_CusName);
        TextView txtCusPhone = findViewById(R.id.txt_CusPhone);
        TextView txtCusEmail = findViewById(R.id.txt_cusEmail);


        txtCusName.setText("Họ tên: " + cusName);
        txtCusPhone.setText("SĐT: " + cusPhone);
        txtCusEmail.setText("Email: " + cusEmail);



        Button btnBack = findViewById(R.id.btn_Back);
        Button btnDoiMK = findViewById(R.id.btn_DoiMatKhau);
        Button btnCapNhat = findViewById(R.id.btn_ThayDoiThongTin);


        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(customer_information.this, list_customer.class);
                startActivity(intent);
            }
        });
        btnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(customer_information.this, change_password.class);
                intent.putExtra("cusPhone", cusPhone);
                intent.putExtra("cusEmail", cusEmail);

                startActivity(intent);
            }
        });
        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(customer_information.this, update_customer.class);
                intent.putExtra("cusPhone", cusPhone);
                intent.putExtra("cusEmail", cusEmail);
                intent.putExtra("cusFullName", cusName);
                startActivity(intent);
            }
        });
    }
}