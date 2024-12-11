package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class tb_dptc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tb_dptc);
        Button btn = findViewById(R.id.btn_trang_chu);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        btn.setOnClickListener(view -> {
            Intent tb_home = new Intent(tb_dptc.this , customer_home.class);
            tb_home.putExtra("user_id", user_id);
            startActivity(tb_home);
            finish();
        });
    }
}