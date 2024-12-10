package com.example.booking_hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.booking_hotel.Models.Bookings;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class room_list_k extends AppCompatActivity {

    private ArrayList<customer_roomlist_detail> customer_room_listProperties = new ArrayList<>();
    private ArrayList<Bookings> bookingList = new ArrayList<>();
    private String selectedRoomType = "Loại - Tất cả";
    private String selectedPriceRange = "Giá - Tất cả";
    DatabaseReference mDatabase;

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
                startActivity(list_home);
            }
        });

        Spinner gia = findViewById(R.id.spn_rooomlist_gia);
        List<String> listGia = new ArrayList<>();
        listGia.add("Giá - Tất cả"); // Thêm phần tử placeholder
        listGia.add("> 100,000");
        listGia.add("> 200,000");
        listGia.add("> 300,000");
        listGia.add("> 400,000");
        ArrayAdapter<String> GiaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listGia);
        GiaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gia.setAdapter(GiaAdapter);
        gia.setSelection(0);

        Spinner loaiPhong = findViewById(R.id.spn_rooomlist_loai);
        List<String> listLoaiPhong = new ArrayList<>();
        ArrayAdapter<String> LoaiPhongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiPhong);
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

        ////////////////////////////


        DatabaseReference roomRef = mDatabase.child("Rooms");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customer_room_listProperties.clear();
                for (DataSnapshot roomSnapshot  : snapshot.getChildren()) {
                    String roomName = String.valueOf(roomSnapshot.child("Code").getValue(Integer.class));
                    String roomType = roomSnapshot.child("TypeName").getValue(String.class);
                    String roomPrice = String.valueOf(roomSnapshot.child("Price").getValue(Integer.class));
                    String roomID = roomSnapshot.child("Id").getValue(String.class);
                    String firstImagePath = null;
                    DataSnapshot imageUrlsSnapshot = roomSnapshot.child("Images");
                    if (imageUrlsSnapshot.exists()) {
                        for (DataSnapshot urlSnapshot : imageUrlsSnapshot.getChildren()) {
                            firstImagePath = urlSnapshot.getValue(String.class);
                            break; // Get only the first image path
                        }
                    }
                    customer_room_listProperties.add(new customer_roomlist_detail(roomName, roomType, roomPrice, firstImagePath, roomID));
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
                customer_roomlist_detail property = customer_room_listProperties.get(position);
                Intent intent = new Intent(room_list_k.this, room_details.class);
                intent.putExtra("Id", property.getRoomID());
                intent.putExtra("user_id", user_id);
                intent.putExtra("CheckIn",checkin);
                intent.putExtra("CheckOut", checkout);
                startActivity(intent);
            }
        };
        roomlistView.setOnItemClickListener(adapterViewListener);
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
