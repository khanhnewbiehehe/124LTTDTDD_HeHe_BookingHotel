package com.example.booking_hotel;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class renew_password extends AppCompatActivity {

    DatabaseReference mDatabase;

    private Button btnCreate;
    private EditText oldpass, newpass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_renew_password);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");

        oldpass = findViewById(R.id.inputPass_renew);
        newpass = findViewById(R.id.inputPass_renew_2);

        Button btnBack = findViewById(R.id.btn_Back);
        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(renew_password.this, profile.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });

        btnCreate = findViewById(R.id.btnCreate);


        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newpass.getText().toString().isEmpty()) {
                    Toast.makeText(renew_password.this, "Vui lòng điền mật khẩu mới", Toast.LENGTH_SHORT).show();
                }
                mDatabase.child("Users").child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        String pass = snapshot.child("Password").getValue(String.class);
                        if (pass.equals(oldpass.getText().toString())) {
                            mDatabase.child("Users").child(user_id).child("Password").setValue(newpass.getText().toString());
                        } else {
                            Toast.makeText(renew_password.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Toast.makeText(renew_password.this, "Error loading room details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


}