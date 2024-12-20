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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.booking_hotel.Models.Bookings;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class room_list_k extends AppCompatActivity {

    private ArrayList<customer_roomlist_detail> customer_room_listProperties = new ArrayList<>();
    private ArrayList<Bookings> bookingList = new ArrayList<>();
    DatabaseReference mDatabase;
    private DatabaseReference roomRef;
    private DatabaseReference bookingRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_list_k);
        Intent intent = getIntent();
        String checkin = intent.getStringExtra("checkInDate");
        String checkout = intent.getStringExtra("checkOutDate");
        String user_id = intent.getStringExtra("user_id");


        mDatabase = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<customer_roomlist_detail> adapter = new customer_room_listArrayAdapter(this, 0, customer_room_listProperties);
        ListView roomlistView = (ListView) findViewById(R.id.customer_room_listview);
        roomlistView.setAdapter(adapter);

        Button btn = findViewById(R.id.btn_Back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent list_home = new Intent(room_list_k.this , customer_home.class);
                list_home.putExtra("user_id", user_id);
                startActivity(list_home);
            }
        });

        Spinner gia = findViewById(R.id.spn_rooomlist_gia);
        List<String> listGia = new ArrayList<>();
        listGia.add("Giá - Tất cả"); // Thêm phần tử placeholder
        listGia.add("Tăng dần");
        listGia.add("Giảm dần");
        ArrayAdapter<String> GiaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listGia) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // This is the view that shows the selected item
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK); // Set the text color to black
                return tv;
            }
        };
        GiaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gia.setAdapter(GiaAdapter);
        gia.setSelection(0);


        Spinner loaiPhong = findViewById(R.id.spn_rooomlist_loai);
        List<String> listLoaiPhong = new ArrayList<>();
        ArrayAdapter<String> LoaiPhongAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listLoaiPhong){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // This is the view that shows the selected item
                TextView tv = (TextView) super.getView(position, convertView, parent);
                tv.setTextColor(Color.BLACK); // Set the text color to black
                return tv;
            }
        };
        LoaiPhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loaiPhong.setAdapter(LoaiPhongAdapter);

        DatabaseReference roomTypesRef = mDatabase.child("RoomTypes");

        Query roomTypesQuery = roomTypesRef.orderByChild("People").startAt(1);
        roomTypesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listLoaiPhong.clear();
                listLoaiPhong.add("Loại - Tất cả");

                for (DataSnapshot data : snapshot.getChildren()) {
                    String roomTypeName = data.child("Name").getValue(String.class);
                    if (roomTypeName != null) {
                        listLoaiPhong.add(roomTypeName);
                    }
                }
                LoaiPhongAdapter.notifyDataSetChanged();
                loaiPhong.setSelection(0);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(room_list_k.this, "Failed to load room types: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        gia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedPriceRange = parent.getItemAtPosition(position).toString(); // Get selected option
                String selectedRoomType = loaiPhong.getSelectedItem() != null
                        ? loaiPhong.getSelectedItem().toString()
                        : "Loại - Tất cả";
                roomListShowFilter(checkin, checkout, selectedPriceRange, selectedRoomType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        loaiPhong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRoomType = parent.getItemAtPosition(position).toString(); // Get selected room type option
                String selectedPriceRange = gia.getSelectedItem() != null
                        ? gia.getSelectedItem().toString()
                        : "Giá - Tất cả"; // Default value if null

                // Call your filter method
                roomListShowFilter(checkin, checkout, selectedPriceRange, selectedRoomType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        ////////////////////////////

        roomListShow(checkin,checkout);

        //on click
        AdapterView.OnItemClickListener adapterViewListener = (parent, view, position, id) -> {
            customer_roomlist_detail property = customer_room_listProperties.get(position);
            Intent intent1 = new Intent(room_list_k.this, room_details.class);
            intent1.putExtra("Id", property.getRoomID());
            intent1.putExtra("user_id", user_id);
            intent1.putExtra("CheckIn",checkin);
            intent1.putExtra("CheckOut", checkout);
            startActivity(intent1);
        };
        roomlistView.setOnItemClickListener(adapterViewListener);
    }

    public void roomListShow(String checkin, String checkout) {
        ArrayAdapter<customer_roomlist_detail> adapter = new customer_room_listArrayAdapter(this, 0, customer_room_listProperties);
        ListView roomlistView = (ListView) findViewById(R.id.customer_room_listview);
        roomlistView.setAdapter(adapter);

        roomRef = mDatabase.child("Rooms");
        bookingRef = mDatabase.child("Bookings");

        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                List<String> bookedRoomIds = new ArrayList<>();

                for (DataSnapshot bookingData : bookingSnapshot.getChildren()) {
                    String status = bookingData.child("Status").getValue(String.class);
                    String bookingCheckIn = bookingData.child("CheckIn").getValue(String.class);
                    String bookingCheckOut = bookingData.child("CheckOut").getValue(String.class);

                    if (!status.equals("Đã hoàn thành") && !status.equals("Bị hủy")) {
                        if (isDateOverlap(bookingCheckIn, bookingCheckOut, checkin, checkout)) {
                            String roomid = bookingData.child("RoomID").getValue(String.class);
                            bookedRoomIds.add(roomid);
                        }
                    }
                }

                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        customer_room_listProperties.clear();

                        for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                            String roomid = roomSnapshot.child("Id").getValue(String.class);
                            String roomStatus = roomSnapshot.child("Status").getValue(String.class);

                            if (!bookedRoomIds.contains(roomid) && "Sẵn sàng".equals(roomStatus)) {
                                String roomName = String.valueOf(roomSnapshot.child("Code").getValue(Integer.class));
                                String roomType = roomSnapshot.child("TypeName").getValue(String.class);
                                String roomPrice = String.valueOf(roomSnapshot.child("Price").getValue(Integer.class));
                                String roomID = roomSnapshot.child("Id").getValue(String.class);

                                String firstImagePath = null;
                                DataSnapshot imageUrlsSnapshot = roomSnapshot.child("Images");
                                if (imageUrlsSnapshot.exists()) {
                                    for (DataSnapshot urlSnapshot : imageUrlsSnapshot.getChildren()) {
                                        firstImagePath = urlSnapshot.getValue(String.class);
                                        break;
                                    }
                                }

                                customer_room_listProperties.add(new customer_roomlist_detail(roomName, roomType, roomPrice, firstImagePath, roomID));
                            }
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error fetching Rooms: " + error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching Bookings: " + error.getMessage());
            }
        });
    }

    private boolean isDateOverlap(String bookingStart, String bookingEnd, String inputStart, String inputEnd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date bStart = sdf.parse(bookingStart);
            Date bEnd = sdf.parse(bookingEnd);
            Date iStart = sdf.parse(inputStart);
            Date iEnd = sdf.parse(inputEnd);

            return (bStart != null && bEnd != null && iStart != null && iEnd != null) &&
                    (bStart.compareTo(iEnd) <= 0 && bEnd.compareTo(iStart) >= 0);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void roomListShowFilter(String checkin, String checkout, String gia, String loai) {
        ArrayAdapter<customer_roomlist_detail> adapter = new customer_room_listArrayAdapter(this, 0, customer_room_listProperties);
        ListView roomlistView = (ListView) findViewById(R.id.customer_room_listview);
        roomlistView.setAdapter(adapter);

        roomRef = mDatabase.child("Rooms");
        bookingRef = mDatabase.child("Bookings");

        bookingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot bookingSnapshot) {
                List<String> bookedRoomIds = new ArrayList<>();

                for (DataSnapshot bookingData : bookingSnapshot.getChildren()) {
                    String status = bookingData.child("Status").getValue(String.class);
                    String bookingCheckIn = bookingData.child("CheckIn").getValue(String.class);
                    String bookingCheckOut = bookingData.child("CheckOut").getValue(String.class);

                    if (!status.equals("Đã hoàn thành") && !status.equals("Bị hủy")) {
                        if (isDateOverlap(bookingCheckIn, bookingCheckOut, checkin, checkout)) {
                            String roomid = bookingData.child("RoomID").getValue(String.class);
                            bookedRoomIds.add(roomid);
                        }
                    }
                }

                roomRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        customer_room_listProperties.clear();

                        for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                            String roomid = roomSnapshot.child("Id").getValue(String.class);
                            String roomStatus = roomSnapshot.child("Status").getValue(String.class);
                            String roomType = roomSnapshot.child("TypeName").getValue(String.class);

                            if (!bookedRoomIds.contains(roomid) && "Sẵn sàng".equals(roomStatus)) {
                                if (roomType.equals(loai) || loai.equals("Loại - Tất cả")) {
                                    String roomName = String.valueOf(roomSnapshot.child("Code").getValue(Integer.class));
                                    String roomPrice = String.valueOf(roomSnapshot.child("Price").getValue(Integer.class));
                                    String roomID = roomSnapshot.child("Id").getValue(String.class);

                                    String firstImagePath = null;
                                    DataSnapshot imageUrlsSnapshot = roomSnapshot.child("Images");
                                    if (imageUrlsSnapshot.exists()) {
                                        for (DataSnapshot urlSnapshot : imageUrlsSnapshot.getChildren()) {
                                            firstImagePath = urlSnapshot.getValue(String.class);
                                            break;
                                        }
                                    }

                                    customer_room_listProperties.add(new customer_roomlist_detail(roomName, roomType, roomPrice, firstImagePath, roomID));
                                }
                            }
                        }
                        if (gia.equals("Tăng dần")) {
                            Collections.sort(customer_room_listProperties, new Comparator<customer_roomlist_detail>() {
                                @Override
                                public int compare(customer_roomlist_detail o1, customer_roomlist_detail o2) {
                                    int price1 = Integer.parseInt(o1.getRoomPrice());
                                    int price2 = Integer.parseInt(o2.getRoomPrice());
                                    return Integer.compare(price1, price2);
                                }
                            });
                        } else if (gia.equals("Giảm dần")) {
                            Collections.sort(customer_room_listProperties, new Comparator<customer_roomlist_detail>() {
                                @Override
                                public int compare(customer_roomlist_detail o1, customer_roomlist_detail o2) {
                                    int price1 = Integer.parseInt(o1.getRoomPrice());
                                    int price2 = Integer.parseInt(o2.getRoomPrice());
                                    return Integer.compare(price2, price1);
                                }
                            });
                        }

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error fetching Rooms: " + error.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching Bookings: " + error.getMessage());
            }
        });
    }

}

////////

class customer_room_listArrayAdapter extends ArrayAdapter<customer_roomlist_detail> {

    private Context context;
    private List<customer_roomlist_detail> room_listProperties;

    public customer_room_listArrayAdapter(Context context, int resource, ArrayList<customer_roomlist_detail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.room_listProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        customer_roomlist_detail property = room_listProperties.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.customer_room_list_item, null);

        TextView roomname = view.findViewById(R.id.customer_room_name);
        TextView roomtype = view.findViewById(R.id.customer_room_type);
        TextView roomprice = view.findViewById(R.id.customer_room_giatien);
        ImageView image = view.findViewById(R.id.customer_room_img);

        roomname.setText("Phòng " + property.getRoomName());
        roomtype.setText(property.getRoomType());
        roomprice.setText(property.getRoomPrice());

        // Load the image dynamically using Glide
        Glide.with(context)
                .load(property.getRoomImage()) // Pass the image URL here
                .placeholder(R.drawable.lefttop_leftbottom_rounded) // Placeholder image resource
                .into(image);

        return view;
    }

}
