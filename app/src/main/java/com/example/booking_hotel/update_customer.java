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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_customer extends AppCompatActivity {


    public EditText editFullName;
    public EditText editPhone;
    public EditText editEmail;
    public Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        intent = getIntent();
        String cusName = intent.getStringExtra("cusFullName");
        String cusPhone = intent.getStringExtra("cusPhone");
        String cusEmail = intent.getStringExtra("cusEmail");

        editFullName = findViewById(R.id.txt_HoTenKhachHang);
        editPhone = findViewById(R.id.txt_SDT);
        editEmail = findViewById(R.id.txt_Email);

        Button btnBack = findViewById(R.id.btn_Back);
        Button btnSave = findViewById(R.id.btn_TaoKhachHang);

        btnSave.setOnClickListener(v -> updateCustomerInfo());
        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(update_customer.this, customer_information.class);
                startActivity(intent);
            }
        });



        if (cusName != null) {
            editFullName.setText(" " +cusName);
        }
        if (cusPhone != null) {
            editPhone.setText(" "+cusPhone);
        }
        if (cusEmail != null) {
            editEmail.setText(" "+cusEmail);
        }
    }

    private void updateCustomerInfo() {
        String updatedFullName = editFullName.getText().toString().trim();
        String updatedPhone = editPhone.getText().toString().trim();
        String updatedEmail = editEmail.getText().toString().trim();
        String cusPhone = intent.getStringExtra("cusPhone");

        if (updatedFullName.isEmpty() || updatedPhone.isEmpty() || updatedEmail.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference("customers");
        customerRef.orderByChild("cusPhone").equalTo(cusPhone).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String customerId = snapshot.getKey(); // Lấy customerId từ key của Firebase


                        customerRef.child(customerId).child("cusFullname").setValue(updatedFullName);
                        customerRef.child(customerId).child("cusPhone").setValue(updatedPhone);
                        customerRef.child(customerId).child("cusEmail").setValue(updatedEmail);


                        Toast.makeText(update_customer.this, "Cập nhật thông tin thành công!", Toast.LENGTH_SHORT).show();

                        // Quay lại trang customer_information
                        Intent intent = new Intent(update_customer.this, list_customer.class);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(update_customer.this, "Lỗi khi cập nhật: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}