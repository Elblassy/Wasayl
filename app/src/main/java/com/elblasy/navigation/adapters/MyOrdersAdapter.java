package com.elblasy.navigation.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.elblasy.navigation.R;
import com.elblasy.navigation.activities.ChooseDriver;
import com.elblasy.navigation.activities.TrackingOrder;
import com.elblasy.navigation.models.OrderModel;

import java.util.ArrayList;

public class MyOrdersAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderModel> listforview;
    private LayoutInflater inflator;
    private View v;
    private ViewHolder holder;

    public MyOrdersAdapter(Context context, ArrayList<OrderModel> listforview) {
        super();
        this.context = context;
        this.listforview = listforview;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listforview.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Log.d("MyOrderAdapter", "on getView Adapter");

        v = convertView;
        OrderModel orders = listforview.get(position);

        if (convertView == null) {
            //inflate the view for each row of listview
            v = inflator.inflate(R.layout.my_orders_list_item, parent, false);
            //ViewHolder object to contain myadapter.xml elements
            holder = new ViewHolder();

            holder.address = v.findViewById(R.id.address);
            holder.details = v.findViewById(R.id.place_name);
            holder.card = v.findViewById(R.id.card);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.address.setText(orders.getForm());
        holder.details.setText(orders.getPlaceName());
        holder.card.setOnClickListener(v -> {
            String token = orders.getToken();
            Intent intent;

            if (orders.getDriversID().equals("unknown"))
                intent = new Intent(context, ChooseDriver.class);
            else
                intent = new Intent(context, TrackingOrder.class);

            intent.putExtra("token", token);
            intent.putExtra("userName", orders.getUserName());
            intent.putExtra("phoneNumber", orders.getPhoneNumber());
            intent.putExtra("placeName", orders.getPlaceName());
            context.startActivity(intent);
        });

        return v;
    }

    private class ViewHolder {
        TextView address;
        TextView details;
        CardView card;

    }
}
