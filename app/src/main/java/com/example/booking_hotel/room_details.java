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
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;


public class room_details extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private List<img_slider_2> mListPhoto;
    DatabaseReference mDatabase;
    private TextView roomName, roomType, roomPrice, roomCheckin, roomCheckout, roomTotal, roomPrice2, user_name, voucher_discount, voucher_id;
    private img_slider_adapter_2 adapter;
    private EditText input_magiamgia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("Id");
        String checkin = intent.getStringExtra("CheckIn");
        String checkout = intent.getStringExtra("CheckOut");
        String user_id = intent.getStringExtra("user_id");


        roomName = findViewById(R.id.room_name);
        roomType = findViewById(R.id.loaiphong);
        roomPrice = findViewById(R.id.giatien);
        roomPrice2 = findViewById(R.id.giatien2);
        roomCheckin = findViewById(R.id.txt_checkin);
        roomCheckout = findViewById(R.id.txt_checkout);
        input_magiamgia = findViewById(R.id.inputMagiamgia);
        user_name = findViewById(R.id.username);
        voucher_discount = findViewById(R.id.voucher_discount);
        voucher_id = findViewById(R.id.voucher_id);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mViewPager = findViewById(R.id.img_slider);
        mCircleIndicator = findViewById(R.id.circle_indicator);

        mListPhoto = new ArrayList<>();
        adapter = new img_slider_adapter_2(mListPhoto);
        mViewPager.setAdapter(adapter);
        mCircleIndicator.setViewPager(mViewPager);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Fetch user data based on user_id
        usersRef.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    // Retrieve the user name from the data snapshot
                    String userName = snapshot.child("Name").getValue(String.class);

                    // Set the user name to the TextView
                    user_name.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any errors in fetching user data
                Toast.makeText(room_details.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });
        DatabaseReference voucherRef = FirebaseDatabase.getInstance().getReference("Vouchers");

        voucherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    String voucherid = Snapshot.getKey();
                    if (Snapshot.child("Code").getValue(String.class).equals(input_magiamgia.getText().toString())) {
                        String voucherDiscount = String.valueOf(Snapshot.child("Discount").getValue(Double.class));
                        voucher_discount.setText(voucherDiscount);
                        voucher_id.setText(voucherid);
                    }
                    break;
                }
                voucher_discount.setText("0.0");
                voucher_id.setText(null);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle any errors in fetching user data
                Toast.makeText(room_details.this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });


        // Load room details and images
        mDatabase.child("Rooms").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = String.valueOf(snapshot.child("Code").getValue(Integer.class));
                String type = snapshot.child("TypeName").getValue(String.class);
                Double price = snapshot.child("Price").getValue(Double.class);
                Double roomdiscount = snapshot.child("Discount").getValue(Double.class);


                // Update UI
                roomName.setText("Phòng " + name);
                roomType.setText("Loại phòng : " + type);

                if (roomdiscount == 0.0 ) {
                    roomPrice.setText("Giá tiền : " + price + " VND /ngày");
                } else {
                    Double price_dis = price - (roomdiscount/100)*price;
                    String text1 = "Giá tiền : " + price ;
                    String text2 = price_dis + " VND /ngày" ;
                    SpannableString spannableString = new SpannableString(text1);
                    spannableString.setSpan(new StrikethroughSpan(), 11, text1.length(), 0);
                    roomPrice.setText(spannableString);
                    roomPrice2.setText(text2);
                }

                roomCheckin.setText("Ngày nhận phòng : " + checkin);
                roomCheckout.setText("Ngày trả phòng : " + checkout);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(room_details.this, "Error loading room details", Toast.LENGTH_SHORT).show();
            }
        });
        loadRoomImages(id);

        Button btn = findViewById(R.id.btn_Back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rDetail_list = new Intent(room_details.this, room_list_k.class);
                startActivity(rDetail_list);
            }
        });

        Button btn_datphong = findViewById(R.id.btn_DatPhong);
        btn_datphong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

                String CheckIn = checkin;
                String CheckOut = checkout;
                String Id = bookingsRef.push().getKey();
                Double Price = Double.parseDouble(roomPrice.getText().toString().replace("Giá tiền : ", "").trim());
                Integer RoomCode = Integer.valueOf(roomName.getText().toString().replace("Phòng ", "").trim());
                Double RoomDiscount = Double.parseDouble(roomPrice2.getText().toString().replace(" VND /ngày", "").trim());
                RoomDiscount = (Price - RoomDiscount) / Price * 100;
                String RoomID = id;
                String Status = "Chờ nhận phòng";
                Double Total = Price; //chua
                String UserID = user_id;
                String UserName = user_name.getText().toString();
                String VoucherCode = input_magiamgia.getText().toString();
                Double VoucherDiscount = Double.valueOf(voucher_discount.getText().toString()) ;
                String VoucherID = voucher_id.getText().toString();

                HashMap<String, Object> bookingMap = new HashMap<>();
                bookingMap.put("CheckIn", CheckIn);
                bookingMap.put("CheckOut", CheckOut);
                bookingMap.put("Id", Id);
                bookingMap.put("Price", Price);
                bookingMap.put("RoomCode", RoomCode);
                bookingMap.put("RoomDiscount", RoomDiscount);
                bookingMap.put("RoomID", RoomID);
                bookingMap.put("Status", Status);
                bookingMap.put("Total", Total);
                bookingMap.put("UserID", UserID);
                bookingMap.put("UserName", UserName);
                bookingMap.put("VoucherCode", VoucherCode);
                bookingMap.put("VoucherDiscount", VoucherDiscount);
                bookingMap.put("VoucherID", VoucherID);

                if (Id != null) {
                    bookingsRef.child(Id).setValue(bookingMap).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent start = new Intent(room_details.this, tb_dptc.class);
                            startActivity(start);
                            finish();
                        } else {
                            Toast.makeText(room_details.this, "thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(room_details.this, "Thất bại.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void loadRoomImages(String roomId) {
        mDatabase.child("Rooms").child(roomId).child("Images").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                    mListPhoto.clear(); // Clear the list before adding new items
                    for (DataSnapshot imageSnapshot : snapshot.getChildren()) {
                        String imageUrl = imageSnapshot.getValue(String.class);
                        if (imageUrl != null) {
                            mListPhoto.add(new img_slider_2(imageUrl));
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter of data changes
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(room_details.this, "Error loading room images", Toast.LENGTH_SHORT).show();
            }
        });
    }

}