//package com.example.booking_hotel;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.FrameLayout;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class room_list_k extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_room_list_k);
//
//        Button btn = findViewById(R.id.btn_Back);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent list_home = new Intent(room_list_k.this , customer_home.class);
//                startActivity(list_home);
//            }
//        });
//
//        FrameLayout frl = findViewById(R.id.id_qk);
//        frl.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent  list_rDetail = new Intent(room_list_k.this , room_details.class);
//                startActivity(list_rDetail);
//            }
//        });
//    }
//}



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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class room_list_k extends AppCompatActivity {

    private ArrayList<ManagerRoomListDetail> managerRoomListProperties = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_room_list_k);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Back button
        Button btnBack = findViewById(R.id.btn_Back);
        btnBack.setOnClickListener(view -> {
            Intent intent = new Intent(room_list_k.this, customer_home.class);
            startActivity(intent);
        });

        // Setup Spinners
        setupSpinner(R.id.spn_room_list_k_tien, new String[]{"Gia", "<100k", "100k-500k", "500k-1tr", ">1tr"});
        setupSpinner(R.id.spn_room_list_k_loai, new String[]{"Loại", "Phòng đơn", "Phòng đôi", "Phòng 3 người", "Phòng 4 người"});
        setupSpinner(R.id.spn_room_list_k_view, new String[]{"View", "Không có", "Biển", "Thành phố"});

        // Sample data for ListView
        managerRoomListProperties.add(new ManagerRoomListDetail("308", "Phòng đôi", "300.000VND", "room_img"));
        managerRoomListProperties.add(new ManagerRoomListDetail("309", "Phòng đơn", "150000VND", "room_img"));

        ArrayAdapter<ManagerRoomListDetail> adapter = new ManagerRoomListArrayAdapter(this, 0, managerRoomListProperties);
        ListView roomlistView = findViewById(R.id.room_list_k_listview);
        roomlistView.setAdapter(adapter);

        roomlistView.setOnItemClickListener((parent, view, position, id) -> {
            ManagerRoomListDetail property = managerRoomListProperties.get(position);
            Intent intent = new Intent(room_list_k.this, room_details.class);

            intent.putExtra("room_name", property.getRoomName());
            intent.putExtra("room_type", property.getRoomType());
            intent.putExtra("room_gia", property.getRoomGia());
            intent.putExtra("image", property.getRoomImage());

            startActivity(intent);
        });
    }

    private void setupSpinner(int spinnerId, String[] items) {
        Spinner spinner = findViewById(spinnerId);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }
}

class ManagerRoomListArrayAdapter extends ArrayAdapter<ManagerRoomListDetail> {

    private final Context context;
    private final List<ManagerRoomListDetail> managerRoomListProperties;

    public ManagerRoomListArrayAdapter(Context context, int resource, ArrayList<ManagerRoomListDetail> objects) {
        super(context, resource, objects);
        this.context = context;
        this.managerRoomListProperties = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ManagerRoomListDetail property = managerRoomListProperties.get(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.room_list_k_item, null);

        TextView roomname = view.findViewById(R.id.room_list_k_name);
        TextView roomtype = view.findViewById(R.id.room_list_k_type);
        TextView roomtrangthai = view.findViewById(R.id.room_list_k_price);
        ImageView image = view.findViewById(R.id.room_list_k_img);

        roomname.setText("Phòng " + property.getRoomName());
        roomtype.setText(property.getRoomType());
        roomtrangthai.setText(property.getRoomGia());

        int imageID = context.getResources().getIdentifier(property.getRoomImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}

class ManagerRoomListDetail {
    private final String roomName;
    private final String roomType;
    private final String roomGia;
    private final String roomImage;

    public ManagerRoomListDetail(String roomName, String roomType, String roomGia, String roomImage) {
        this.roomName = roomName;
        this.roomType = roomType;
        this.roomGia = roomGia;
        this.roomImage = roomImage;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getRoomGia() {
        return roomGia;
    }

    public String getRoomImage() {
        return roomImage;
    }
}
