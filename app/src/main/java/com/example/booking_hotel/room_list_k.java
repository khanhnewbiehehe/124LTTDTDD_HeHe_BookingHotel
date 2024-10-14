package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class room_list_k extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_list_k);

        Button btn = findViewById(R.id.btn_Back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list_home = new Intent(room_list_k.this , customer_home.class);
                startActivity(list_home);
            }
        });

        FrameLayout frl = findViewById(R.id.id_qk);
        frl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  list_rDetail = new Intent(room_list_k.this , room_details.class);
                startActivity(list_rDetail);
            }
        });
    }
}