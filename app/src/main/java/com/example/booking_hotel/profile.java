package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.booking_hotel.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class profile extends AppCompatActivity {

    DatabaseReference mDatabase;
    private TextView name, email, number;
    ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.profile_ten);
        email = findViewById(R.id.profile_email);
        number = findViewById(R.id.profile_sdt);
        img = findViewById(R.id.profile_img);

        Button btnChange = findViewById(R.id.profile_btn_change);
        Button btnChangePass = findViewById(R.id.profile_btn_change_password);
        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(profile.this, update_in4.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(profile.this, renew_password.class);
                startActivity(intent);
            }
        });

        btnBack.setOnClickListener(v -> {
            // Create an intent to go back to the desired activity (e.g., MainActivity)
            Intent intent1 = new Intent(profile.this, customer_home.class);
            intent1.putExtra("user_id", user_id);
            startActivity(intent1);
        });

        mDatabase.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String Ava = snapshot.child("AvatarUrl").getValue(String.class);
                String Email = snapshot.child("Email").getValue(String.class);
                String Name = snapshot.child("Name").getValue(String.class);
                String PhoneNumber = snapshot.child("PhoneNumber").getValue(String.class);

                name.setText("Họ tên : " + Name);
                email.setText("Email : " + Email);
                number.setText("Số điện thoại : " + PhoneNumber);

                Glide.with(profile.this)
                        .load(Ava)
                        .placeholder(R.drawable.circle_corner) // Default image
                        .into(img);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(profile.this, "Error loading room details", Toast.LENGTH_SHORT).show();
            }
        });
    }
}