package com.elblasy.navigation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elblasy.navigation.Order;
import com.elblasy.navigation.R;
import com.elblasy.navigation.models.Result;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    private Context context;
    private List<Result> results;
    private int lastPosition = -1;


    public PlacesListAdapter(Context context, List<Result> results) {

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
        Result result = results.get(position);
        holder.textViewName.setText(result.getName());
        holder.textViewAddress.setText(result.getVicinity());
        holder.ratingBar.setNumStars(5);

        setAnimation(holder.cardView, position);

        if (result.getRating() != null)
            holder.ratingBar.setRating(result.getRating().intValue());

        Glide.with(context)
                .load(result.getIcon())
                .skipMemoryCache(false)
                .centerCrop()
                .into(holder.icon);

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
        private CircleImageView icon;
        private RatingBar ratingBar;
        private CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewAddress = itemView.findViewById(R.id.textViewAddress);
            icon = itemView.findViewById(R.id.icon);
            ratingBar = itemView.findViewById(R.id.rating);
            cardView = itemView.findViewById(R.id.layout1);

        }
    }


}
