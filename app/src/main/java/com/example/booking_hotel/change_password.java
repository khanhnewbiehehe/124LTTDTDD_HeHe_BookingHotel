package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class change_password extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_change_password);

        Button btnSave = findViewById(R.id.btnChangePass);
        EditText inputPass = findViewById(R.id.inputPass);
        EditText inputCPass = findViewById(R.id.inputCPass);


        Intent intent = getIntent();
        String cusPhone = intent.getStringExtra("cusPhone");
        String cusEmail = intent.getStringExtra("cusEmail");




        EditText inputNumberCall = findViewById(R.id.inputNumberCall_F);
        EditText inputEmail = findViewById(R.id.inputEmail);


        if (cusPhone != null) {
            inputNumberCall.setText(" "+cusPhone);
        }
        if (cusEmail != null) {
            inputEmail.setText(" "+cusEmail);
        }

        Button btn = findViewById(R.id.btn_Back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(change_password.this , customer_information.class);
                startActivity(intent);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newPassword = inputPass.getText().toString().trim();
                String confirmPassword = inputCPass.getText().toString().trim();


                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(change_password.this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(change_password.this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
                } else if (newPassword.length() < 6) {
                    Toast.makeText(change_password.this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show();
                } else {

                    updatePasswordToFirebase(newPassword);
                }
            }
        });
    }
    private void updatePasswordToFirebase(String newPassword) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference customerRef = database.getReference("customers");


        Intent intent = getIntent();
        String cusPhone = intent.getStringExtra("cusPhone");

        if (cusPhone != null) {
            customerRef.orderByChild("cusPhone").equalTo(cusPhone)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    snapshot.getRef().child("cusPass").setValue(newPassword)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(change_password.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();

                                                    Intent backToInfo = new Intent(change_password.this, list_customer.class);
                                                    startActivity(backToInfo);
                                                } else {
                                                    Toast.makeText(change_password.this, "Đổi mật khẩu thất bại. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            } else {
                                Toast.makeText(change_password.this, "Không tìm thấy khách hàng với số điện thoại này!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(change_password.this, "Lỗi kết nối: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(this, "Không tìm thấy số điện thoại người dùng!", Toast.LENGTH_SHORT).show();
        }
    }

}