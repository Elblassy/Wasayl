package com.elblasy.navigation.fragments;


import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.R;
import com.elblasy.navigation.activities.ChooseVehicle;
import com.elblasy.navigation.adapters.CategoryAdapter;
import com.elblasy.navigation.adapters.PlacesListAdapter;
import com.elblasy.navigation.models.PlaceModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<Integer> mImageUrls;
    private ArrayList<String> mNames;

    private LocationManager locationManager;
    private List<PlaceModel> results;
    private PlacesListAdapter placesListAdapter;
    private CategoryAdapter category;
    private ProgressBar progressBar;
    private TextView listText;
    private View rootView;

    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;
    private DatabaseReference db;
    private String categoryText = "";


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        results = new ArrayList<>();
        mImageUrls = new ArrayList<>();
        mNames = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        mRecyclerView = rootView.findViewById(R.id.recyclerView2);

        progressBar = rootView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        listText = rootView.findViewById(R.id.text3);


        CardView cardView = rootView.findViewById(R.id.card);

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(rootView.getContext(), ChooseVehicle.class);
            startActivity(intent);
        });

        System.out.println("onCreateView222");
        initImageBitmaps();

        return rootView;
    }

    private void initImageBitmaps() {
        System.out.println("onCreateView6666");

        mImageUrls.add(R.drawable.burger);
        mNames.add(getResources().getString(R.string.restaurant));


        mImageUrls.add(R.drawable.groceries);
        mNames.add(getResources().getString(R.string.groceries));


        mImageUrls.add(R.drawable.bread);
        mNames.add(getResources().getString(R.string.bakery));

        mImageUrls.add(R.drawable.coffee);
        mNames.add(getResources().getString(R.string.cafe));

        mImageUrls.add(R.drawable.clothes);
        mNames.add(getResources().getString(R.string.clothe));


        mImageUrls.add(R.drawable.book);
        mNames.add(getResources().getString(R.string.book));

        mImageUrls.add(R.drawable.lamp);
        mNames.add(getResources().getString(R.string.electric));


        mImageUrls.add(R.drawable.rose);
        mNames.add(getResources().getString(R.string.florist));


        initRecyclerView();
    }

    private void initRecyclerView() {

        System.out.println("initRecyclerView");


        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(rootView.getContext(), resId);

        placesListAdapter = new PlacesListAdapter(rootView.getContext(), results);

        category = new CategoryAdapter(getContext(), mNames, mImageUrls, (v, position) -> {
            System.out.println("initRecyclerView22");
            System.out.println(position + "  HOMEClick");
            progressBar.setVisibility(View.VISIBLE);
            listText.setVisibility(View.VISIBLE);
            placesListAdapter.clear();

            switch (position) {
                case 0:
                    categoryText = "restaurant";
                    break;
                case 1:
                    categoryText = "supermarket";

                    break;
                case 2:
                    categoryText = "bakery";

                    break;
                case 3:
                    categoryText = "cafe";
                    break;
                case 4:
                    categoryText = "clothes";
                    break;
                case 5:
                    categoryText = "books";
                    break;
                case 6:
                    categoryText = "electronics_store";
                    break;
                case 7:
                    categoryText = "florist";
                    break;

            }

            db = FirebaseDatabase.getInstance().getReference("places").child("portsaid").child(categoryText);

            db.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    PlaceModel places = dataSnapshot.getValue(PlaceModel.class);
//                    System.out.print("erorrrrr : " + position);
//                    System.out.print("erorrrrr : " + dataSnapshot);
                    System.out.print("erorrrrr : " + places.getName());


                    if (places != null) {
                        results.add(places);
                        progressBar.setVisibility(View.GONE);
                        listText.setVisibility(View.GONE);
                    } else {
                        progressBar.setVisibility(View.GONE);
                        listText.setText("مفيش محلات متوفرة حاليًا");
                    }

                    placesListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            System.out.println("erorrrrr : " + categoryText);

        });


        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(category);

        System.out.println("initRecyclerView33");
        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getActivity().getBaseContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setAdapter(placesListAdapter);


    }


}
