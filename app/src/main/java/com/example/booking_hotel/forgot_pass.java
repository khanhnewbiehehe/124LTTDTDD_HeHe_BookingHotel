package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class forgot_pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_pass);


        Button btn = findViewById(R.id.btnChangePass);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fogot_sign = new Intent(forgot_pass.this , sign_in.class);
                startActivity(fogot_sign);
            }
        });

        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(forgot_pass.this, sign_in.class);
                startActivity(intent);
            }
        });
    }
}