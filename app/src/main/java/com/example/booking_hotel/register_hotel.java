package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class register_hotel extends AppCompatActivity {

    private EditText inputName, inputEmail, inputNumberCall, inputPass;
    private Button btnRegister;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_hotel);

        // Initialize Firebase Database
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI components
        inputName = findViewById(R.id.inputName);
        inputEmail = findViewById(R.id.inputEmail);
        inputNumberCall = findViewById(R.id.inputNumberCall_R);
        inputPass = findViewById(R.id.inputPass);
        btnRegister = findViewById(R.id.btnRegister);

        // Set Register Button Click Listener
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        TextView textView_haveAccount = findViewById(R.id.haveAccount);
        textView_haveAccount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_haveAccount.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        textView_haveAccount.setTextColor(getResources().getColor(android.R.color.white));
                        break;
                }
                return false;
            }
        });

        textView_haveAccount.setOnClickListener(view -> {
            Intent start_sign = new Intent(register_hotel.this, sign_in.class);
            startActivity(start_sign);
        });
    }

    private void registerUser() {
        // Get user input
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String phoneNumber = inputNumberCall.getText().toString().trim();
        String password = inputPass.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Sai địa chỉ gmail.");
            inputEmail.requestFocus();
            return;
        }


        // Save user data to Firebase
        String userId = mDatabase.child("Users").push().getKey(); // Generate a unique ID for the user

        HashMap<String, Object> userMap = new HashMap<>();
        userMap.put("AvatarUrl", null);
        userMap.put("Email", email);
        userMap.put("Id", userId);
        userMap.put("Name", name);
        userMap.put("Password", password);
        userMap.put("PhoneNumber", phoneNumber);
        userMap.put("Role", "Client");
        userMap.put("Vip", 0);

        if (userId != null) {
            mDatabase.child(userId).setValue(userMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(register_hotel.this, "Đăng ký thành công hãy quay lại trang đăng nhập!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(register_hotel.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Đăng ký thất bại.", Toast.LENGTH_SHORT).show();
        }
    }
}
