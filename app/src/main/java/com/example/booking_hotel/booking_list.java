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

public class booking_list extends AppCompatActivity {

    private ArrayList<room_list_detail> room_listProperties = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_list);
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
                Intent intent = new Intent(booking_list.this, customer_home.class);
                startActivity(intent);
            }
        });

        Spinner trangthai = findViewById(R.id.spn_trangthai_danhsach);
        List<String> listTrangThai = new ArrayList<>();
        listTrangThai.add("Trạng thái"); // Thêm phần tử placeholder
        listTrangThai.add("Đang sử dụng");
        listTrangThai.add("Chưa nhận phòng");
        listTrangThai.add("Đã thanh toán");
        ArrayAdapter<String> LoaiPhongAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listTrangThai);
        LoaiPhongAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trangthai.setAdapter(LoaiPhongAdapter);
        trangthai.setSelection(0);

        room_listProperties.add(
                new room_list_detail("308","Phòng đôi", "20/9/2024 - 22/9/2024","Chưa nhận phòng","room_img"));
        room_listProperties.add(
                new room_list_detail("309","Phòng đon", "21/9/2024 - 22/9/2024","Đang sử dụng", "room_img"));

        ArrayAdapter<room_list_detail> adapter = new list_orderArrayAdapter(this, 0, room_listProperties);

        ListView listView = (ListView) findViewById(R.id.room_listview);
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

            //on click
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                room_list_detail property = room_listProperties.get(position);

                Intent intent = new Intent(booking_list.this, booking_details.class);
                intent.putExtra("room_name", property.getRoomName());
                intent.putExtra("room_type", property.getRoomType());
                intent.putExtra("room_date", property.getRoomDate());
                intent.putExtra("room_trangthai", property.getRoomTrangthai());
                intent.putExtra("image", property.getRoomImage());
                startActivity(intent);
            }
        };
//set the listener to the list view
        listView.setOnItemClickListener(adapterViewListener);
    }
}

class room_listArrayAdapter extends ArrayAdapter<room_list_detail> {

    private Context context;
    private List<room_list_detail> room_listProperties;

    public room_listArrayAdapter(Context context, int resource, List<room_list_detail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.room_listProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        room_list_detail property = room_listProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.room_list_item, null);

        TextView roomname = (TextView) view.findViewById(R.id.room_name);
        TextView roomtype = (TextView) view.findViewById(R.id.room_type);
        TextView roomdate = (TextView) view.findViewById(R.id.room_date);
        TextView roomtrangthai = (TextView) view.findViewById(R.id.room_trangthai);
        ImageView image = (ImageView) view.findViewById(R.id.room_img);

        //set price and rental attributes
        roomname.setText("Phòng " + String.valueOf(property.getRoomName()));
        roomtype.setText(String.valueOf(property.getRoomType()));
        roomdate.setText( String.valueOf(property.getRoomDate()));
        roomtrangthai.setText( String.valueOf(property.getRoomTrangthai()));

        //get the image associated with this property
        int imageID = context.getResources().getIdentifier(property.getRoomImage(), "drawable", context.getPackageName());
        image.setImageResource(imageID);

        return view;
    }
}