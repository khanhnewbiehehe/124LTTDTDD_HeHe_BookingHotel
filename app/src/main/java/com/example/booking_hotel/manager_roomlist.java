package com.example.booking_hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;


public class manager_roomlist extends AppCompatActivity {

    private ArrayList<manager_roomlist_detail> manager_room_listProperties = new ArrayList<>();
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDataRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manager_roomlist);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button btnBack = findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manager_roomlist.this, manager_homescreen.class);
                startActivity(intent);
            }
        });

        Button btnCreateRoom = findViewById(R.id.btn_themphong);
        btnCreateRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(manager_roomlist.this, create_room.class);
                startActivity(intent);
            }
        });

        mDatabase = FirebaseDatabase.getInstance();
        mDataRef = mDatabase.getReference("dropdown_values");

        Spinner trangthai = findViewById(R.id.spn_manager_rooomlist_trangthai);
        Spinner loaiPhong = findViewById(R.id.spn_manager_rooomlist_loai);
        Spinner view = findViewById(R.id.spn_manager_rooomlist_view);

        loadDropdownData("trangthai", trangthai);
        loadDropdownData("loaiPhong", loaiPhong);
        loadDropdownData("loaiView", view);

        manager_room_listProperties.add(
                new manager_roomlist_detail("308","Phòng đôi", "Trống","room_img"));
        manager_room_listProperties.add(
                new manager_roomlist_detail("309","Phòng đon", "Đang sử dụng", "room_img"));

        ArrayAdapter<manager_roomlist_detail> adapter = new manager_room_listArrayAdapter(this, 0, manager_room_listProperties);

        ListView roomlistView = (ListView) findViewById(R.id.manager_room_listview);
        roomlistView.setAdapter(adapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                manager_roomlist_detail property = manager_room_listProperties.get(position);

                Intent intent = new Intent(manager_roomlist.this, manager_room_details.class);

                intent.putExtra("room_name", property.getRoomName());
                intent.putExtra("room_type", property.getRoomType());
                intent.putExtra("room_trangthai", property.getRoomTrangthai());
                intent.putExtra("image", property.getRoomImage());

                startActivity(intent);
            }
        };
//        loadRoomDataFromFirebase();

//set the listener to the list view
        roomlistView.setOnItemClickListener(adapterViewListener);
    }
    private void loadRoomDataFromFirebase() {
        mDataRef = mDatabase.getReference("rooms");
        mDataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                manager_room_listProperties.clear();  // Xóa dữ liệu cũ
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    manager_roomlist_detail room = snapshot.getValue(manager_roomlist_detail.class);  // Lấy dữ liệu phòng
                    if (room != null) {
                        manager_room_listProperties.add(room);  // Thêm vào danh sách
                    }
                }

                // Cập nhật ListView
                ((ArrayAdapter) ((ListView) findViewById(R.id.manager_room_listview)).getAdapter()).notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(manager_roomlist.this, "Lỗi khi tải dữ liệu từ Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadDropdownData(String dropdownKey, Spinner spinner) {
        mDataRef.child(dropdownKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> dropdownValues = new ArrayList<>();

                // Lấy giá trị từ Firebase
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String value = snapshot.getValue(String.class);
                    dropdownValues.add(value);
                }

                // Cập nhật Spinner
                ArrayAdapter<String> adapter = new ArrayAdapter<>(manager_roomlist.this, android.R.layout.simple_spinner_item, dropdownValues);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(manager_roomlist.this, "Lỗi khi tải dữ liệu từ Firebase!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class manager_room_listArrayAdapter extends ArrayAdapter<manager_roomlist_detail> {

    private Context context;
    private List<manager_roomlist_detail> manager_room_listProperties;

    public manager_room_listArrayAdapter(Context context, int resource, ArrayList<manager_roomlist_detail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.manager_room_listProperties = objects;
    }



    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        manager_roomlist_detail property = manager_room_listProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.manager_room_list_item, null);

        TextView roomname = (TextView) view.findViewById(R.id.manager_room_name);
        TextView roomtype = (TextView) view.findViewById(R.id.manager_room_type);
        TextView roomtrangthai = (TextView) view.findViewById(R.id.manager_room_trangthai);
        ImageView image = (ImageView) view.findViewById(R.id.manager_room_img);

        //set price and rental attributes
        roomname.setText("Phòng " + String.valueOf(property.getRoomName()));
        roomtype.setText(String.valueOf(property.getRoomType()));
        roomtrangthai.setText( String.valueOf(property.getRoomTrangthai()));

        //get the image associated with this property
        int imageID = context.getResources().getIdentifier(property.getRoomImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}