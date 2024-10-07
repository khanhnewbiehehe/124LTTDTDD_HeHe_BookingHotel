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

public class register_hotel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_hotel);


        Button btn = findViewById(R.id.btnRegister);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_register = new Intent(register_hotel.this , sign_in.class);
                startActivity(intent_register);
            }
        });

        TextView textView_haveAccount = findViewById(R.id.haveAccount);
        textView_haveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  start_sign = new Intent(register_hotel.this , sign_in.class);
                startActivity(start_sign);
            }
        });

        TextView khanh_404 = findViewById(R.id.Khanh_2725);
        khanh_404.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  Khanh2725 = new Intent(register_hotel.this , register_hotel_1.class);
                startActivity(Khanh2725);
            }
        });
    }
}