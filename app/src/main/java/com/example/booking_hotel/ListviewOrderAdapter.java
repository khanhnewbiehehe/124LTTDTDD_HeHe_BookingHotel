package com.example.booking_hotel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ListviewOrderAdapter extends ArrayAdapter<room_list_detail> {
    private Context context;
    private List<room_list_detail> room_listProperties;

    public ListviewOrderAdapter(Context context, int resource, List<room_list_detail> objects) {
        super(context, resource, objects);

        this.context = context;
        this.room_listProperties = objects;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        //get the property we are displaying
        room_list_detail property = room_listProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.order_row, null);

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
