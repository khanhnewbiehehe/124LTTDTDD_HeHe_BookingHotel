package com.example.booking_hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.booking_hotel.Models.Bookings;
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

public class booking_list extends AppCompatActivity {

    private ArrayList<room_list_detail> room_listProperties = new ArrayList<>();
    private ArrayList<Bookings> bookingList = new ArrayList<>();
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_list);

        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<room_list_detail> adapter = new customer_booking_listArrayAdapter(this, 0, room_listProperties);
        ListView roomlistView = (ListView) findViewById(R.id.room_listview);
        roomlistView.setAdapter(adapter);

        Button btnBack = findViewById(R.id.btn_Back);

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(v -> {
            // Create an intent to go back to the desired activity (e.g., MainActivity)
            Intent intent1 = new Intent(booking_list.this, customer_home.class);
            intent1.putExtra("user_id", user_id);
            startActivity(intent1);
        });

        Spinner trangthai = findViewById(R.id.spn_trangthai_danhsach);
        List<String> listTrangThai = new ArrayList<>();
        listTrangThai.add("Trạng thái - Tất cả"); // Thêm phần tử placeholder
        listTrangThai.add("Chờ nhận phòng");
        listTrangThai.add("Đã nhận phòng");
        listTrangThai.add("Chờ thanh toán");
        listTrangThai.add("Đã hoàn thành");
        ArrayAdapter<String> LoaiPhongAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listTrangThai) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // This is the view that shows the selected item
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK); // Set the text color to black
                return tv;
            }
        };
        LoaiPhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trangthai.setAdapter(LoaiPhongAdapter);
        trangthai.setSelection(0);

        trangthai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    String selectedValue = parent.getItemAtPosition(position).toString();
                    BookingList(user_id, selectedValue);

                } else {
                    DatabaseReference roomRef = mDatabase.child("Bookings");
                    roomRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            room_listProperties.clear();
                            for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                                String userid = roomSnapshot.child("UserID").getValue(String.class);
                                if (userid.equals(user_id)) {
                                    String CheckIn = roomSnapshot.child("CheckIn").getValue(String.class);
                                    String CheckOut = roomSnapshot.child("CheckOut").getValue(String.class);
                                    try {
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                        Date date = inputFormat.parse(CheckIn);
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        String formattedDate = outputFormat.format(date);
                                        CheckIn = formattedDate;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                        Date date = inputFormat.parse(CheckOut);
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                                        String formattedDate = outputFormat.format(date);
                                        CheckOut = formattedDate;
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    String roomName = String.valueOf(roomSnapshot.child("RoomCode").getValue(Integer.class));
                                    String bookingID = roomSnapshot.child("Id").getValue(String.class);
                                    String TrangThai = roomSnapshot.child("Status").getValue(String.class);

                                    room_listProperties.add(new room_list_detail(roomName, CheckIn, CheckOut, TrangThai, bookingID));
                                }

                            }
                            adapter.notifyDataSetChanged();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Firebase", "Error: " + error.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Khi không có mục nào được chọn (hiếm khi xảy ra)
            }
        });

        DatabaseReference roomRef = mDatabase.child("Bookings");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                room_listProperties.clear();
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    String userid = roomSnapshot.child("UserID").getValue(String.class);
                    if (userid.equals(user_id)) {
                        String CheckIn = roomSnapshot.child("CheckIn").getValue(String.class);
                        String CheckOut = roomSnapshot.child("CheckOut").getValue(String.class);
                        String roomName = String.valueOf(roomSnapshot.child("RoomCode").getValue(Integer.class));
                        String bookingID = roomSnapshot.child("Id").getValue(String.class);
                        String TrangThai = roomSnapshot.child("Status").getValue(String.class);

                        room_listProperties.add(new room_list_detail(roomName, CheckIn, CheckOut, TrangThai, bookingID));
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });



        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                room_list_detail property = room_listProperties.get(position);
                Intent intent = new Intent(booking_list.this, booking_details.class);
                intent.putExtra("Id", property.getRoomID());
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        };
        roomlistView.setOnItemClickListener(adapterViewListener);
    }

    public void BookingList(String user_id, String filter) {
        ArrayAdapter<room_list_detail> adapter = new customer_booking_listArrayAdapter(this, 0, room_listProperties);
        ListView roomlistView = (ListView) findViewById(R.id.room_listview);
        roomlistView.setAdapter(adapter);

        DatabaseReference roomRef = mDatabase.child("Bookings");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                room_listProperties.clear();
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    String userid = roomSnapshot.child("UserID").getValue(String.class);
                    String TrangThai = roomSnapshot.child("Status").getValue(String.class);
                    if (userid.equals(user_id) && TrangThai.equals(filter)) {
                        String CheckIn = roomSnapshot.child("CheckIn").getValue(String.class);
                        String CheckOut = roomSnapshot.child("CheckOut").getValue(String.class);
                        String roomName = String.valueOf(roomSnapshot.child("RoomCode").getValue(Integer.class));
                        String bookingID = roomSnapshot.child("Id").getValue(String.class);

                        room_listProperties.add(new room_list_detail(roomName, CheckIn, CheckOut, TrangThai, bookingID));
                    }

                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });
    }

}

////////

class customer_booking_listArrayAdapter extends ArrayAdapter<room_list_detail> {

    private Context context;
    private List<room_list_detail> room_listProperties;

    public customer_booking_listArrayAdapter(Context context, int resource, ArrayList<room_list_detail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.room_listProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        room_list_detail property = room_listProperties.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.room_list_item, null);

        TextView room_name = view.findViewById(R.id.room_name);
        TextView room_date = view.findViewById(R.id.room_date);
        TextView room_trangthai = view.findViewById(R.id.room_trangthai);

        room_name.setText("Phòng " + property.getRoomName());
        room_date.setText(property.getRoomCheckIn() + " - " + property.getRoomCheckOut());
        room_trangthai.setText(property.getRoomTrangthai());

        switch (property.getRoomTrangthai()) {
            case "Chờ nhận phòng":
                room_trangthai.setTextColor(Color.parseColor("#FFC107"));
                break;
            case "Đã nhận phòng":
                room_trangthai.setTextColor(Color.parseColor("#4CAF50"));
                break;
            case "Chờ thanh toán":
                room_trangthai.setTextColor(Color.parseColor("#ED3237"));
                break;
            case "Đã hoàn thành":
                room_trangthai.setTextColor(Color.parseColor("#9E9E9E"));
                break;
            default:
                room_trangthai.setTextColor(Color.BLACK);
                break;
        }
        return view;
    }

}
