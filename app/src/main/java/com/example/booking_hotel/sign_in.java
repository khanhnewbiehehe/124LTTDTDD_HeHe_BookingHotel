package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sign_in extends AppCompatActivity {

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView textView_register = findViewById(R.id.textViewRegister);
        textView_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sign_register = new Intent(sign_in.this, register_hotel.class);
                startActivity(sign_register);
            }
        });

        textView_register.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        textView_register.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        textView_register.setTextColor(ContextCompat.getColor(view.getContext(), R.color.yellow));
                        break;
                }
                return false;
            }
        });


        EditText sdt = findViewById(R.id.inputName);
        EditText pass = findViewById(R.id.inputPass);
        Button btn = findViewById(R.id.btnLogin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = sdt.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    // Show error message if any field is empty
                    Toast.makeText(sign_in.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate credentials (e.g., using Firebase)
                validateCredentials(username, password);
            }
        });
    }

    private void validateCredentials(String username, String password) {
        DatabaseReference usersRef = mDatabase.child("Users");

        // Show a loading indicator
        Toast.makeText(sign_in.this, "Checking...", Toast.LENGTH_SHORT).show();

        usersRef.orderByChild("PhoneNumber").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String dbPassword = userSnapshot.child("Password").getValue(String.class);
                        String role = userSnapshot.child("Role").getValue(String.class);

                        if (dbPassword != null && dbPassword.equals(password)) {
                            // Check user role and navigate accordingly
                            if ("Client".equalsIgnoreCase(role)) {
                                Intent clientHome = new Intent(sign_in.this, customer_home.class);
                                String user_id = userSnapshot.child("Id").getValue(String.class);
                                clientHome.putExtra("user_id", user_id);
                                startActivity(clientHome);
                                Toast.makeText(sign_in.this, "Chào mừng quý khách!", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent managerHome = new Intent(sign_in.this, manager_homescreen.class);
                                startActivity(managerHome);
                                Toast.makeText(sign_in.this, "Welcome!", Toast.LENGTH_SHORT).show();
                            }
                            finish(); // Prevent returning to login screen
                            return;
                        }
                    }
                    // Password incorrect
                    Toast.makeText(sign_in.this, "Sai mật khẩu hoặc số điện thoại", Toast.LENGTH_SHORT).show();
                } else {
                    // User not found
                    Toast.makeText(sign_in.this, "Sai mật khẩu hoặc số điện thoại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(sign_in.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

