package com.elblasy.navigation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.R;
import com.elblasy.navigation.activities.Order;
import com.elblasy.navigation.models.PlaceModel;

import java.util.List;


public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private Context context;
    private List<PlaceModel> results;
    private int lastPosition = -1;


    public PlacesListAdapter(Context context, List<PlaceModel> results) {

        this.context = context;
        this.results = results;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.place_row_layout, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        PlaceModel result = results.get(position);
        holder.textViewName.setText(result.getName());
        holder.textViewAddress.setText(result.getLocation());

        setAnimation(holder.cardView, position);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, Order.class);
            intent.putExtra("Address", holder.textViewAddress.getText());
            intent.putExtra("place_name", holder.textViewName.getText());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return results.size();
    }

    public void clear() {
        int size = results.size();
        results.clear();
        notifyItemRangeRemoved(0, size);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (lastPosition > position)
            lastPosition = -1;
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewAddress;
        private CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            cardView = itemView.findViewById(R.id.layout1);

        }
    }


}
