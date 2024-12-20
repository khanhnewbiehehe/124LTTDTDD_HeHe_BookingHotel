package com.example.booking_hotel;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.booking_hotel.Models.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class customer_home extends AppCompatActivity {

    FloatingActionButton fab;

    DrawerLayout dra ;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    DatabaseReference mDatabase;

    private ArrayList<voucher_list_detail> voucher_listProperties = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home);
        Intent intent = getIntent();
        String user_id = intent.getStringExtra("user_id");
        mDatabase = FirebaseDatabase.getInstance().getReference();



        mDatabase.child("Users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.d("firebase", "Error getting data: " + task.getException());
                    TextView txtHello = findViewById(R.id.hello);
                    txtHello.setText("Error loading user data");
                } else {
                    DataSnapshot dataSnapshot = task.getResult();
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        TextView txtHello = findViewById(R.id.hello);
                        txtHello.setText("Xin chào, " + user.getName());
                    } else {
                        Log.d("firebase", "No user data found.");
                        TextView txtHello = findViewById(R.id.hello);
                        txtHello.setText("Xin chao!");
                    }
                }
            }
        });

        Button checkinDateButton = findViewById(R.id.btn_CheckIn);
        Button checkoutDateButton = findViewById(R.id.btn_CheckOut);


        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        checkinDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    customer_home.this,
                    (view, year1, month1, dayOfMonth) -> {
                        checkinDateButton.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                    }, year, month, day);
            datePickerDialog.show();
        });


        checkoutDateButton.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    customer_home.this,
                    (view, year12, month12, dayOfMonth) -> {
                        checkoutDateButton.setText(dayOfMonth + "/" + (month12 + 1) + "/" + year12);
                    }, year, month, day);
            datePickerDialog.show();
        });

        Button btnNav = findViewById(R.id.btn_nav);
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Button btn_check_room = findViewById(R.id.btn_check_room);
        btn_check_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String checkInDate = checkinDateButton.getText().toString();
                String checkOutDate = checkoutDateButton.getText().toString();

                if (checkInDate.equals("Ngày nhận phòng")  || checkOutDate.equals("Ngày trả phòng")) {
                    Toast.makeText(customer_home.this, "Vui lòng chọn ngày check in và check out", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(customer_home.this, room_list_k.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("checkInDate", checkInDate);
                    intent.putExtra("checkOutDate", checkOutDate);
                    startActivity(intent);
                }
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_logout) {
                Log.d("NavigationView", "Đã đăng xuất");
                Intent intent1 = new Intent(customer_home.this, sign_in.class);
                startActivity(intent1);
                finish();
            } else if (id == R.id.nav_home) {
                Intent intent1 = new Intent(customer_home.this, profile.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            } else if (id == R.id.nav_settings) {
                Intent intent1 = new Intent(customer_home.this, booking_list.class);
                intent1.putExtra("user_id", user_id);
                startActivity(intent1);
            }

            // Close the drawer after item is selected
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        ArrayAdapter<voucher_list_detail> adapter = new voucher_listArrayAdapter(this, 0, voucher_listProperties);
        ListView voucherlistView = (ListView) findViewById(R.id.voucher_listview);
        voucherlistView.setAdapter(adapter);

        DatabaseReference roomRef = mDatabase.child("Vouchers");
        roomRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                voucher_listProperties.clear();
                for (DataSnapshot roomSnapshot : snapshot.getChildren()) {
                    String vouchername = roomSnapshot.child("Name").getValue(String.class);
                    String voucherdescription = roomSnapshot.child("Description").getValue(String.class);
                    String voucherdiscount = String.valueOf(roomSnapshot.child("Discount").getValue(Double.class));
                    String image = roomSnapshot.child("ImageUrl").getValue(String.class);
                    String vouchercode = roomSnapshot.child("Code").getValue(String.class);

                    voucher_listProperties.add(new voucher_list_detail(vouchername, voucherdescription, voucherdiscount, image, vouchercode));
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error: " + error.getMessage());
            }
        });

    }

    public void onBackPressed()  {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }


}
class voucher_listArrayAdapter extends ArrayAdapter<voucher_list_detail> {

    private Context context;
    private List<voucher_list_detail> room_listProperties;

    public voucher_listArrayAdapter(Context context, int resource, ArrayList<voucher_list_detail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.room_listProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.voucher_list_item, parent, false);
            holder = new ViewHolder();
            holder.voucherName = convertView.findViewById(R.id.voucher_item_name);
            holder.voucherDescription = convertView.findViewById(R.id.voucher_item_des);
            holder.voucherDiscount = convertView.findViewById(R.id.voucher_item_dis);
            holder.voucherCode = convertView.findViewById(R.id.voucher_item_code);
            holder.image = convertView.findViewById(R.id.voucher_item_img);
            holder.btnCopy = convertView.findViewById(R.id.btnCopy);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        voucher_list_detail property = getItem(position);
        holder.voucherName.setText(property.getVoucherName());
        holder.voucherDescription.setText(property.getVoucherDescription());
        holder.voucherDiscount.setText(property.getVoucherDiscount() + " %");
        holder.voucherCode.setText(property.getVoucherCode());
        Glide.with(context).load(property.getVoucherImage()).placeholder(R.drawable.corner_img_slider).into(holder.image);

        // Set up btnCopy click listener
        holder.btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Voucher Code", property.getVoucherCode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Đã lưu vào clipboard!", Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    static class ViewHolder {
        TextView voucherName;
        TextView voucherDescription;
        TextView voucherDiscount;
        TextView voucherCode;
        ImageView image;
        Button btnCopy;
    }

}