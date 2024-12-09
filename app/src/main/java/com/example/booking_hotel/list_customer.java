package com.example.booking_hotel;

import android.content.Intent;
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

import com.example.booking_hotel.Models.Customer;

import java.util.ArrayList;

public class list_customer extends AppCompatActivity {

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

        // Set a click listener to navigate to another activity
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(list_customer.this, manager_homescreen.class);
                startActivity(intent);
            }
        });

        btnTaoKH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to go back to the desired activity (e.g., MainActivity)
                Intent intent = new Intent(list_customer.this, create_customer.class);
                startActivity(intent);
            }
        });

        listCus = findViewById(R.id.list_KH);

        ArrayList<Customer> arrCustomer = new ArrayList<>();
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn A"));
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn B"));
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn C"));
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn D"));

        ListviewCustomerAdapter customAdapter = new ListviewCustomerAdapter(this, R.layout.customer_row, arrCustomer);
        listCus.setAdapter(customAdapter);

        AdapterView.OnItemClickListener adapterViewListener = new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer property = arrCustomer.get(position);
                Intent intent = new Intent(list_customer.this, customer_information.class);
                intent.putExtra("txt_CusName", property.getCusFullname());
                intent.putExtra("txt_SDT", property.getCusID());
                startActivity(intent);
            }
        };
        listCus.setOnItemClickListener(adapterViewListener);
    }
}