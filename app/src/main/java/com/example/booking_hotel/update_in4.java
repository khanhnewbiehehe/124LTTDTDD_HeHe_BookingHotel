package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_in4 extends AppCompatActivity {

    DatabaseReference mDatabase;
    private EditText name, email, number;
    private Button btnCapNhat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_in4);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        name = findViewById(R.id.inputName);
        email = findViewById(R.id.inputEmail);
        number = findViewById(R.id.inputNumberCall);
        btnCapNhat =findViewById(R.id.btnCapNhat);

        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(update_in4.this, profile.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });

        mDatabase.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String Email = snapshot.child("Email").getValue(String.class);
                String Name = snapshot.child("Name").getValue(String.class);
                String PhoneNumber = snapshot.child("PhoneNumber").getValue(String.class);

                name.setText(Name);
                email.setText(Email);
                number.setText(PhoneNumber);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(update_in4.this, "Error loading room details", Toast.LENGTH_SHORT).show();
            }
        });

        btnCapNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updatedName = name.getText().toString().trim();
                String updatedEmail = email.getText().toString().trim();
                String updatedPhoneNumber = number.getText().toString().trim();

                if (updatedName.isEmpty() || updatedEmail.isEmpty() || updatedPhoneNumber.isEmpty()) {
                    Toast.makeText(update_in4.this, "Vui lòng không để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase.child("Users").child(user_id).child("Name").setValue(updatedName);
                mDatabase.child("Users").child(user_id).child("Email").setValue(updatedEmail);
                mDatabase.child("Users").child(user_id).child("PhoneNumber").setValue(updatedPhoneNumber);

                Toast.makeText(update_in4.this, "Đã cập nhật thành công", Toast.LENGTH_SHORT).show();

            }
        });
    }
}