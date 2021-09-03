package com.DeliveryJetApp.deliveryjet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomServiceAdapter extends ArrayAdapter<Order> {
    private final Context context;
    private final ArrayList<Order> orders;


    public CustomServiceAdapter(@NonNull Context context, @NonNull ArrayList<Order> objects) {
        super(context, -1, objects);
        this.context = context;
        this.orders = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_service_row, parent, false);

        TextView orderId = rowView.findViewById(R.id.tvOrderId);
        TextView dueDate = rowView.findViewById(R.id.tvOrderDueDate);
        TextView company = rowView.findViewById(R.id.tvCompany);

        orderId.setText(orders.get(position).orderId);
        dueDate.setText("Due Date: "+orders.get(position).date);
        company.setText(orders.get(position).email);


        return rowView;
    }

}