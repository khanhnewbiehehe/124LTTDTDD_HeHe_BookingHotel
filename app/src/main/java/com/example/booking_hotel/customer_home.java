package com.example.booking_hotel;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import com.example.booking_hotel.Models.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class customer_home extends AppCompatActivity {

    FloatingActionButton fab;

    DrawerLayout dra ;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    DatabaseReference mDatabase;


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
                        txtHello.setText("User data is not available.");
                    }
                }
            }
        });


//        NavigationView navigationView = findViewById(R.id.nav_view);

        Button btnNav = findViewById(R.id.btn_nav);
        btnNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Button btn = findViewById(R.id.btn_check_room);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home_list = new Intent(customer_home.this , room_list_k.class);
                startActivity(home_list);
            }
        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_logout) {
                    Log.d("NavigationView", "Đã đăng xuất");
                    Intent intent = new Intent(customer_home.this, sign_in.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_home) {
                    Intent intent = new Intent(customer_home.this, profile.class);
                    startActivity(intent);
                } else if (id == R.id.nav_settings) {
                    Intent intent = new Intent(customer_home.this, booking_list.class);
                    startActivity(intent);
                }

                // Close the drawer after item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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