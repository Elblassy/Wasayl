package com.elblasy.navigation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.CustomItemClickListener;
import com.elblasy.navigation.R;
import com.elblasy.navigation.models.DriverModel;

import java.util.ArrayList;

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.ViewHolder> {

    private ArrayList<DriverModel> list;
    private Context context;

    private CustomItemClickListener customItemClickListener;

    public DriverAdapter(Context context, ArrayList<DriverModel> list, CustomItemClickListener customItemClickListener) {
        this.context = context;
        this.list = list;
        this.customItemClickListener = customItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.drirver_list,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        DriverModel driverModel = list.get(position);

        holder.title.setText(driverModel.getMessage());
        holder.name.setText(driverModel.getName());
        holder.ratingBar.setRating(driverModel.getRat());

        holder.agree.setOnClickListener(v -> customItemClickListener.onItemClick(v, position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, name;
        RatingBar ratingBar;
        Button agree;

        ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            name = view.findViewById(R.id.name);
            ratingBar = view.findViewById(R.id.rating);
            agree = view.findViewById(R.id.agree);

        }
    }
}
