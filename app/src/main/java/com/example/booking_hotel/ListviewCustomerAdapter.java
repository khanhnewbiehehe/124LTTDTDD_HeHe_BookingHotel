package com.example.booking_hotel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import com.example.booking_hotel.model.Customer;

public class ListviewCustomerAdapter extends ArrayAdapter<Customer> {

    private Context context;
    private int resource;
    private List<Customer> arrCustomer;


    public ListviewCustomerAdapter(Context context, int resource, ArrayList<Customer> arrCustomer) {
        super(context, resource, arrCustomer);
        this.context = context;
        this.resource = resource;
        this.arrCustomer = arrCustomer;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.customer_row, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.CusID = (TextView) convertView.findViewById(R.id.txt_SDT);
            viewHolder.CusFullname = (TextView) convertView.findViewById(R.id.txt_CusName);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Customer cus = arrCustomer.get(position);
        viewHolder.CusID.setText(cus.getCusPhone());
        viewHolder.CusFullname.setText(cus.getCusFullname());  // Đã sửa tên biến

        return convertView;
    }

    public class ViewHolder {
        TextView CusID, CusFullname;  // Đã sửa tên biến
    }
}
