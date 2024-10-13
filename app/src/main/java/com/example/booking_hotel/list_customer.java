package com.example.booking_hotel;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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

        listCus = findViewById(R.id.list_KH);

        ArrayList<Customer> arrCustomer = new ArrayList<>();
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn A"));
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn B"));
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn C"));
        arrCustomer.add(new Customer("0123123123", "Nguyễn Văn D"));

        ListviewCustomerAdapter customAdapter = new ListviewCustomerAdapter(this, R.layout.customer_row, arrCustomer);
        listCus.setAdapter(customAdapter);
    }
}