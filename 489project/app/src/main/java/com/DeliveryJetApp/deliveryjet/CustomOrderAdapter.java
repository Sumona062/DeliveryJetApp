package com.DeliveryJetApp.deliveryjet;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.util.ArrayList;

public class CustomOrderAdapter extends ArrayAdapter<Order> {
    private final Context context;
    private final ArrayList<Order> orders;


    public CustomOrderAdapter(@NonNull Context context, @NonNull ArrayList<Order> objects) {
        super(context, -1, objects);
        this.context = context;
        this.orders = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_order_row, parent, false);

        TextView orderId = rowView.findViewById(R.id.tvOrderId);
        TextView dueDate = rowView.findViewById(R.id.tvOrderDueDate);
        TextView status = rowView.findViewById(R.id.tvOrderStatus);

        orderId.setText(orders.get(position).orderId);
        dueDate.setText(orders.get(position).date);
        status.setText(orders.get(position).status);


        return rowView;
    }

}