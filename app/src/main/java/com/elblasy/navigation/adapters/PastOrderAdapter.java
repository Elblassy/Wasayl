package com.elblasy.navigation.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.elblasy.navigation.R;
import com.elblasy.navigation.activities.Order;
import com.elblasy.navigation.models.OrderModel;

import java.util.ArrayList;

public class PastOrderAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<OrderModel> listforview;
    private LayoutInflater inflator;
    private View v;
    private ViewHolder holder;

    public PastOrderAdapter(Context context, ArrayList<OrderModel> listforview) {
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
            v = inflator.inflate(R.layout.past_orders_list, parent, false);
            //ViewHolder object to contain myadapter.xml elements
            holder = new ViewHolder();

            holder.address = v.findViewById(R.id.address);
            holder.details = v.findViewById(R.id.place_name);
            holder.orderAgain = v.findViewById(R.id.order_again);


            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.address.setText(orders.getFrom());
        holder.details.setText(orders.getPlaceName());
        holder.orderAgain.setOnClickListener(v -> {
            Intent intent = new Intent(context, Order.class);
            intent.putExtra("Address", holder.address.getText().toString());
            intent.putExtra("place_name", holder.details.getText().toString());
            context.startActivity(intent);

        });

        return v;
    }

    private class ViewHolder {
        TextView address;
        TextView details;
        Button orderAgain;

    }
}
