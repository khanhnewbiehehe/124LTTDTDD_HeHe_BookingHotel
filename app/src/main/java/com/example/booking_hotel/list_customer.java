package com.example.booking_hotel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.booking_hotel.model.Customer;

import java.util.ArrayList;

public class list_customer extends AppCompatActivity {

    private ArrayList<Customer> arrCustomer;
    private ListviewCustomerAdapter customAdapter;
    private DatabaseReference databaseReference;
    ListView listCus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_customer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnBack = findViewById(R.id.btn_Back);
        Button btnTaoKH = findViewById(R.id.btn_TaoKH);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(list_customer.this, manager_homescreen.class);
                startActivity(intent);
            }
        });

        btnTaoKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(list_customer.this, create_customer.class);
                startActivity(intent);
            }
        });

        listCus = findViewById(R.id.list_KH);

        arrCustomer = new ArrayList<>();
        customAdapter = new ListviewCustomerAdapter(this, R.layout.customer_row, arrCustomer);
        listCus.setAdapter(customAdapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("customers");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrCustomer.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Customer customer = snapshot.getValue(Customer.class);
                    arrCustomer.add(customer);
                }
                customAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.err.println("Lỗi Firebase: " + databaseError.getMessage());
            }
        });


        listCus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer = arrCustomer.get(position);
                Intent intent = new Intent(list_customer.this, customer_information.class);
                intent.putExtra("txt_CusName", customer.getCusFullname());
                intent.putExtra("txt_SDT", customer.getCusPhone());
                intent.putExtra("txt_Email", customer.getCusEmail());
                System.out.println("t" + customer.getCusEmail());
                startActivity(intent);
            }
        });
    }

}

