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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ktx.Firebase;
import com.example.booking_hotel.model.Customer;


public class create_customer extends AppCompatActivity {


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("customers");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(create_customer.this, list_customer.class);
                startActivity(intent);
            }
        });


        EditText edtHoTen, edtSDT;
        Button btnTaoKhachHang;



        edtHoTen = findViewById(R.id.txt_HoTenKhachHang);
        edtSDT = findViewById(R.id.txt_SDT);
        btnTaoKhachHang = findViewById(R.id.btn_TaoKhachHang);
        btnTaoKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hoTen = edtHoTen.getText().toString().trim();
                String sdt = edtSDT.getText().toString().trim();


                if (hoTen.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(create_customer.this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                } else {
                    // Gọi phương thức lưu dữ liệu vào Firebase
                    saveCustomerData(sdt , hoTen);
                    Intent intent = new Intent(create_customer.this, list_customer.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void saveCustomerData(String hoTen, String sdt) {


        Customer newCustomer = new Customer(hoTen, sdt , "" , "");


        myRef.push().setValue(newCustomer);


        Toast.makeText(create_customer.this, "Tạo khách hàng thành công!", Toast.LENGTH_SHORT).show();
    }
}