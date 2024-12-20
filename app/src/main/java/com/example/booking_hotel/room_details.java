package com.example.booking_hotel;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

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
    private Button btnVoucher;

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
        btnVoucher = findViewById(R.id.btnVoucher);
        roomTotal = findViewById(R.id.txt_total);

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
                Double total = (price - (roomdiscount/100)*price)*dayRange(checkin,checkout);
                roomTotal.setText("Tổng cộng : " + total );

                voucher_discount.setText("0.0");
                voucher_id.setText(null);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(room_details.this, "Error loading room details", Toast.LENGTH_SHORT).show();
            }
        });
        loadRoomImages(id);

        btnVoucher.setOnClickListener(view -> {
            String ma = input_magiamgia.getText().toString();
            checkVoucher(ma, checkin,checkout);
        });

        Button btn = findViewById(R.id.btn_Back);
        btn.setOnClickListener(view -> {
            Intent rDetail_list = new Intent(room_details.this, room_list_k.class);
            rDetail_list.putExtra("user_id", user_id);
            rDetail_list.putExtra("checkInDate",checkin);
            rDetail_list.putExtra("checkOutDate", checkout);
            startActivity(rDetail_list);
            finish();
        });

        Button btn_datphong = findViewById(R.id.btn_DatPhong);
        btn_datphong.setOnClickListener(view -> {
            DatabaseReference bookingsRef = FirebaseDatabase.getInstance().getReference("Bookings");

            String CheckIn = checkin;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = inputFormat.parse(checkin);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String formattedDate = outputFormat.format(date);
                CheckIn = formattedDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String CheckOut = checkout;
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date date = inputFormat.parse(checkout);
                SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String formattedDate = outputFormat.format(date);
                CheckOut = formattedDate;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String Id = bookingsRef.push().getKey();
            Double Price = Double.parseDouble(roomPrice.getText().toString().replace("Giá tiền : ", "").trim());
            Integer RoomCode = Integer.valueOf(roomName.getText().toString().replace("Phòng ", "").trim());
            Double RoomDiscount = Double.parseDouble(roomPrice2.getText().toString().replace(" VND /ngày", "").trim());
            RoomDiscount = (Price - RoomDiscount) / Price * 100;
            String RoomID = id;
            String Status = "Chờ nhận phòng";
            Double Total = Double.parseDouble(roomTotal.getText().toString().replace("Tổng cộng : ","").trim());
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
                        start.putExtra("user_id", user_id);
                        startActivity(start);
                        finish();
                    } else {
                        Toast.makeText(room_details.this, "thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(room_details.this, "Thất bại.", Toast.LENGTH_SHORT).show();
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

    public int dayRange(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);

            if (start != null && end != null) {
                long differenceInMillis = end.getTime() - start.getTime();
                long differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24);

                return (int) differenceInDays;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0; // Trả về 0 nếu có lỗi
    }

    public void checkVoucher(String code,String checkin, String checkout) {
        DatabaseReference voucherRef = mDatabase.child("Vouchers");
        Query query = voucherRef.orderByChild("Code").equalTo(code);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot voucherSnapshot : snapshot.getChildren()) {
                        Double discount = voucherSnapshot.child("Discount").getValue(Double.class);
                        String id = voucherSnapshot.child("Id").getValue(String.class);
                        if (discount != null) {
                            Double roomDiscount = 0.0;
                            Double price = Double.parseDouble(roomPrice.getText().toString().replace("Giá tiền : ", "").trim());
                            String roomDiscountRaw = roomPrice2.getText().toString().replace(" VND /ngày", "").trim();
                            if (!roomDiscountRaw.isEmpty()) {
                                roomDiscount = Double.parseDouble(roomDiscountRaw);
                            }
                            Double discountAmount = (price - roomDiscount) / price * 100;
                            Double total = (price - (discountAmount/100)*price - (discount/100)*price)*dayRange(checkin,checkout);
                            roomTotal.setText("Tổng cộng : " + total);
                            voucher_discount.setText(String.valueOf(discount));
                            voucher_id.setText(id);
                        } else {
                            Toast.makeText(room_details.this, "Voucher is valid but has no discount.", Toast.LENGTH_SHORT).show();
                            Double roomDiscount = 0.0;
                            Double price = Double.parseDouble(roomPrice.getText().toString().replace("Giá tiền : ", "").trim());
                            String roomDiscountRaw = roomPrice2.getText().toString().replace(" VND /ngày", "").trim();
                            if (!roomDiscountRaw.isEmpty()) {
                                roomDiscount = Double.parseDouble(roomDiscountRaw);
                            }
                            Double discountAmount = (price - roomDiscount) / price * 100;
                            Double total = (price - (discountAmount/100)*price)*dayRange(checkin,checkout);
                            roomTotal.setText("Tổng cộng : " + total);
                            voucher_discount.setText("0.0");
                            voucher_id.setText(null);
                        }
                        break;
                    }
                } else {
                    Toast.makeText(room_details.this, "Invalid voucher code.", Toast.LENGTH_SHORT).show();
                    Double roomDiscount = 0.0;
                    Double price = Double.parseDouble(roomPrice.getText().toString().replace("Giá tiền : ", "").trim());
                    String roomDiscountRaw = roomPrice2.getText().toString().replace(" VND /ngày", "").trim();
                    if (!roomDiscountRaw.isEmpty()) {
                        roomDiscount = Double.parseDouble(roomDiscountRaw);
                    }
                    Double discountAmount = (price - roomDiscount) / price * 100;
                    Double total = (price - (discountAmount/100)*price)*dayRange(checkin,checkout);
                    roomTotal.setText("Tổng cộng : " + total);
                    voucher_discount.setText("0.0");
                    voucher_id.setText(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(room_details.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}