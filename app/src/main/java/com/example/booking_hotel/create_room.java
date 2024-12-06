package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class create_room extends AppCompatActivity {
    public DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_room);

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
                Intent intent = new Intent(create_room.this, manager_roomlist.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        EditText txtMaPhong = findViewById(R.id.txt_MaPhong);
        EditText txtGiaTien = findViewById(R.id.txt_GiaTien);
        Spinner spnLoaiPhong = findViewById(R.id.spn_LoaiPhong);
        Spinner spnLoaiView = findViewById(R.id.spn_LoaiView);
        Spinner spnLoaiGiuong = findViewById(R.id.spn_LoaiGiuong);


        Spinner loaiPhong = findViewById(R.id.spn_LoaiPhong);
        Spinner loaiView = findViewById(R.id.spn_LoaiView);
        Spinner loaiGiuong = findViewById(R.id.spn_LoaiGiuong);


        loadDropdownData("loaiPhong", loaiPhong);
        loadDropdownData("loaiView", loaiView);
        loadDropdownData("loaiGiuong", loaiGiuong);

        Button btn_tao = findViewById(R.id.btn_CapNhat);
        btn_tao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String maPhong = txtMaPhong.getText().toString();
                String giaTien = txtGiaTien.getText().toString();
                String loaiPhong = spnLoaiPhong.getSelectedItem().toString();
                String loaiView = spnLoaiView.getSelectedItem().toString();
                String loaiGiuong = spnLoaiGiuong.getSelectedItem().toString();

                // Kiểm tra các trường dữ liệu
                if (maPhong.isEmpty() || giaTien.isEmpty() || loaiPhong.equals("Loại phòng") || loaiView.equals("Loại khung cảnh") || loaiGiuong.equals("Loại giường")) {
                    Toast.makeText(create_room.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }


                Map<String, Object> roomData = new HashMap<>();
                roomData.put("maPhong", maPhong);
                roomData.put("giaTien", giaTien);
                roomData.put("loaiPhong", loaiPhong);
                roomData.put("loaiView", loaiView);
                roomData.put("loaiGiuong", loaiGiuong);
                roomData.put("trangthai", "Trống");
                roomData.put("img" , "room_img" );


                mDatabase.child("rooms").child(maPhong).setValue(roomData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(create_room.this, "Thêm phòng thành công", Toast.LENGTH_SHORT).show();


                        Intent intent = new Intent(create_room.this, manager_roomlist.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(create_room.this, "Có lỗi xảy ra, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }



    private void loadDropdownData(String dropdownKey, Spinner spinner) {
        DatabaseReference dropdownRef = mDatabase.child("dropdown_values").child(dropdownKey);

        dropdownRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> dropdownValues = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    dropdownValues.add(value);
                }


                ArrayAdapter<String> adapter = new ArrayAdapter<>(create_room.this, android.R.layout.simple_spinner_item, dropdownValues);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(create_room.this, "Lỗi khi tải dữ liệu từ Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
