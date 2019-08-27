package com.elblasy.navigation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.CustomItemClickListener;
import com.elblasy.navigation.R;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private static final String TAG = "CategoryAdapter";

    private ArrayList<String> mTitles;
    private ArrayList<Integer> mImage;
    public static String CATEGORY;
    private CustomItemClickListener listener;
    private Context context;

    public CategoryAdapter(Context context, ArrayList<String> mTitles, ArrayList<Integer> mImage
            , CustomItemClickListener listener) {

        this.context = context;
        this.mTitles = mTitles;
        this.mImage = mImage;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "on onCreateViewHolder called.");

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card,
                parent, false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "on BindingViewHolder called.");

        holder.textView.setText(mTitles.get(position));

        holder.imageView.setImageResource(mImage.get(position));


        holder.layout.setOnClickListener(view -> {
            listener.onItemClick(view, position);
            switch (position) {
                case 0:
                    CATEGORY = "restaurant";
                    Log.i(TAG, "onBindViewHolder: " + CATEGORY);
                    break;
                case 1:
                    CATEGORY = "supermarket";
                    Log.i(TAG, "onBindViewHolder: " + CATEGORY);

                    break;
                case 2:
                    CATEGORY = "pharmacy";
                    Log.i(TAG, "onBindViewHolder: " + CATEGORY);

                    break;
                case 3:
                    CATEGORY = "store";
                    Log.i(TAG, "onBindViewHolder: " + CATEGORY);

                    break;
                case 4:
                    CATEGORY = "bakery";
                    break;
                case 5:
                    CATEGORY = "cafe";
                    break;

            }
        });

    }

    @Override
    public int getItemCount() {
        return mImage.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        RelativeLayout layout;

        ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.image);
            layout = view.findViewById(R.id.layout);

        }
    }

//    final private Random mRandom = new Random(System.currentTimeMillis());
//
//    private int generateRandomColor() {
//        // This is the base color which will be mixed with the generated one
//        final int baseColor = Color.WHITE;
//
//        final int baseRed = Color.red(baseColor);
//        final int baseGreen = Color.green(baseColor);
//        final int baseBlue = Color.blue(baseColor);
//
//        final int red = (baseRed + mRandom.nextInt(256)) / 2;
//        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
//        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;
//
//        return Color.rgb(red, green, blue);
//    }
}
