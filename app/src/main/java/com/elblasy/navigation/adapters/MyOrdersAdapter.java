package com.elblasy.navigation.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.elblasy.navigation.R;
import com.elblasy.navigation.SharedPref;
import com.elblasy.navigation.activities.ChooseDriver;
import com.elblasy.navigation.activities.TrackingOrder;
import com.elblasy.navigation.models.OrderModel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
            holder.button = v.findViewById(R.id.order);
            holder.delete = v.findViewById(R.id.delete);
            holder.pushID = v.findViewById(R.id.push_id);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.address.setText(orders.getFrom());
        holder.details.setText(orders.getPlaceName());
        holder.pushID.setText(orders.getPushID());

        holder.button.setOnClickListener(v -> {
//            String token = orders.getToken();
            Intent intent;

            if (orders.getDriversID().equals("unknown"))
                intent = new Intent(context, ChooseDriver.class);
            else
                intent = new Intent(context, TrackingOrder.class);

//            intent.putExtra("token", token);
            intent.putExtra("userName", orders.getUserName());
            intent.putExtra("phoneNumber", orders.getPhoneNumber());
            intent.putExtra("placeName", orders.getPlaceName());
            context.startActivity(intent);
        });

        holder.delete.setOnClickListener(view -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("حذف الطلب");
            builder.setMessage("هل انت متأكد من حذف طلبك ؟");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    context.getResources().getString(R.string.yes),
                    (dialog, id) -> {
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("ClientOrders")
                                .child(SharedPref.getSessionValue("PhoneNumber")).child(holder.pushID.getText().toString());
                        db.removeValue();
                        notifyDataSetChanged();
                    });

            builder.setNegativeButton(
                    context.getResources().getString(R.string.no),
                    (dialog, id) -> dialog.cancel());

            AlertDialog alert = builder.create();
            alert.show();
        });

        return v;
    }

    private class ViewHolder {
        TextView address, details, pushID;
        CardView card;
        Button button;
        ImageView delete;

    }
}
