    package com.example.booking_hotel;

    import android.content.Intent;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.app.Activity;
    import android.content.Context;
    import android.view.LayoutInflater;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
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

    public class list_order extends AppCompatActivity {
        private ArrayList<room_list_detail> orderroom_listProperties = new ArrayList<>();
        private FirebaseDatabase mDatabase;
        private DatabaseReference mDataRef;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_list_order);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });


            mDatabase = FirebaseDatabase.getInstance();
            mDataRef = mDatabase.getReference();

            Button btnBack = findViewById(R.id.btn_Back);
            Button btnCreateOrder = findViewById(R.id.btn_ThemDonThue);

            // Set a click listener to navigate to another activity
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an intent to go back to the desired activity (e.g., MainActivity)
                    Intent intent = new Intent(list_order.this, manager_homescreen.class);
                    startActivity(intent);
                }
            });

            btnCreateOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an intent to go back to the desired activity (e.g., MainActivity)
                    Intent intent = new Intent(list_order.this, create_order.class);
                    startActivity(intent);
                }
            });

            Spinner Phong = findViewById(R.id.spn_Room);
            loadDropdownData("trangthai",Phong);

            orderroom_listProperties.add(
                    new room_list_detail("308","Phòng đôi", "20/9/2024 - 22/9/2024","Chưa nhận phòng","room_img"));
            orderroom_listProperties.add(
                    new room_list_detail("309","Phòng đon", "21/9/2024 - 22/9/2024","Đang sử dụng", "room_img"));

            ArrayAdapter<room_list_detail> adapter = new list_orderArrayAdapter(this, 0, orderroom_listProperties);

            ListView listView = (ListView) findViewById(R.id.order_list);
            listView.setAdapter(adapter);
            AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {

                //on click
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    room_list_detail property = orderroom_listProperties.get(position);

                    Intent intent = new Intent(list_order.this, details_order.class);
                    intent.putExtra("room_name", property.getRoomName());
                    intent.putExtra("room_type", property.getRoomType());
                    intent.putExtra("room_trangthai", property.getRoomTrangthai());
                    intent.putExtra("image", property.getRoomImage());
                    startActivity(intent);
                }
            };
    //set the listener to the list view
            listView.setOnItemClickListener(adapterViewListener);

        }


        private void loadDropdownData(String dropdownKey, Spinner spinner) {
            mDataRef.child("dropdown_values").child(dropdownKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) { // Kiểm tra nếu dữ liệu tồn tại
                        List<String> dropdownValues = new ArrayList<>();

                        // Lấy giá trị từ Firebase
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String value = snapshot.getValue(String.class);
                            dropdownValues.add(value);
                        }

                        // Cập nhật Spinner
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(list_order.this, android.R.layout.simple_spinner_item, dropdownValues);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    } else {
                        Toast.makeText(list_order.this, "Không tìm thấy dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Xử lý lỗi nếu có
                    Toast.makeText(list_order.this, "Lỗi khi tải dữ liệu từ Firebase!", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }
    class list_orderArrayAdapter extends ArrayAdapter<room_list_detail> {

        private Context context;
        private List<room_list_detail> orderroom_listProperties;

        public list_orderArrayAdapter(Context context, int resource, List<room_list_detail> objects) {
            super(context, resource, objects);

            this.context = context;
            this.orderroom_listProperties = objects;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            room_list_detail property = orderroom_listProperties.get(position);

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