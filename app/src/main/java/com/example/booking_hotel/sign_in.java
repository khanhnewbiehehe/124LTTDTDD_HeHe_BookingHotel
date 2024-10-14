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

public class sign_in extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);


        TextView textView_register = findViewById(R.id.textViewRegister);
        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  sign_register = new Intent(sign_in.this , register_hotel.class);
                startActivity(sign_register);
            }
        });

        TextView textView_forgot = findViewById(R.id.forgotPass);
        textView_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent forgot_pass = new Intent(sign_in.this, forgot_pass.class);
                startActivity(forgot_pass);
            }
        });

        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_home = new Intent(sign_in.this, customer_home.class);
                startActivity(sign_home);
            }
        });
    }
}