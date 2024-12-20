package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class booking_details extends AppCompatActivity {

    private ViewPager mViewPager;
    private CircleIndicator mCircleIndicator;
    private  List<img_slider_2> mListPhoto;
    private Button btnLeft, btnRight, btnHuyDatPhong;
    private TextView bookingName, bookingType, bookingPrice, bookingStatus, bookingCheckIn, bookingCheckOut, bookingDiscount, roomID;
    DatabaseReference mDatabase;
    private img_slider_adapter_2 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_details);
        Intent intent = getIntent();
        String id = intent.getStringExtra("Id");
        String user_id = intent.getStringExtra("user_id");

        bookingName = findViewById(R.id.room_name);
        bookingType = findViewById(R.id.loaiphong);
        bookingPrice = findViewById(R.id.giatien);
        bookingStatus = findViewById(R.id.trangthai);
        bookingCheckIn = findViewById(R.id.ngaynhan);
        bookingCheckOut = findViewById(R.id.ngaytra);
        bookingDiscount = findViewById(R.id.voucher);
        roomID = findViewById(R.id.roomID);
        btnHuyDatPhong = findViewById(R.id.btn_TraPhong);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(v -> {
            // Create an intent to go back to the desired activity (e.g., MainActivity)
            Intent intent1 = new Intent(booking_details.this, booking_list.class);
            intent1.putExtra("user_id", user_id);
            startActivity(intent1);
            finish();
        });

        mViewPager = findViewById(R.id.img_slider);
        mCircleIndicator = findViewById(R.id.circle_indicator);
        mListPhoto = new ArrayList<>();
        adapter = new img_slider_adapter_2(mListPhoto);
        mViewPager.setAdapter(adapter);
        mCircleIndicator.setViewPager(mViewPager);
        btnLeft = findViewById(R.id.icon_left);
        btnRight = findViewById(R.id.icon_right);


        btnLeft.setOnClickListener(v -> navigateSlider(-1));
        btnRight.setOnClickListener(v -> navigateSlider(1));

        mDatabase.child("Bookings").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String name = String.valueOf(snapshot.child("RoomCode").getValue(Integer.class));
                Double price = snapshot.child("Price").getValue(Double.class);
                String status = snapshot.child("Status").getValue(String.class);
                String checkin = snapshot.child("CheckIn").getValue(String.class);
                String checkout = snapshot.child("CheckOut").getValue(String.class);
                String voucher = snapshot.child("VoucherCode").getValue(String.class);
                String roomid = snapshot.child("RoomID").getValue(String.class);
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = inputFormat.parse(checkin);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = outputFormat.format(date);
                    checkin = formattedDate;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = inputFormat.parse(checkout);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                    String formattedDate = outputFormat.format(date);
                    checkout = formattedDate;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                // Update UI
                bookingName.setText("Phòng " + name);
                bookingPrice.setText("Giá tiền : " + price + " VND /ngày");
                bookingStatus.setText("Trạng thái : " + status);
                bookingDiscount.setText(voucher);
                roomID.setText(roomid);
                bookingCheckIn.setText("Ngày nhận phòng : " + checkin);
                bookingCheckOut.setText("Ngày trả phòng : " + checkout);
                loadRoomImages(roomid);
                loadRoomDetail(roomid);


            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(booking_details.this, "Error loading room details", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void navigateSlider(int direction) {
        int currentPosition = mViewPager.getCurrentItem();
        int totalItems = mListPhoto.size();

        int newPosition = currentPosition + direction;

        if (newPosition >= 0 && newPosition < totalItems) {
            mViewPager.setCurrentItem(newPosition);
        }
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
                Toast.makeText(booking_details.this, "Error loading room images", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadRoomDetail(String roomId) {
        // Load room details and images
        mDatabase.child("Rooms").child(roomId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String type = snapshot.child("TypeName").getValue(String.class);
                // Update UI
                bookingType.setText("Loại phòng : " + type);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(booking_details.this, "Error loading room details", Toast.LENGTH_SHORT).show();
            }
        });
    }

}