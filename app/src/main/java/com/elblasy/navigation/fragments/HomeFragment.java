package com.elblasy.navigation.fragments;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.R;
import com.elblasy.navigation.SpeackToDriver;
import com.elblasy.navigation.adapters.CategoryAdapter;
import com.elblasy.navigation.adapters.PlacesListAdapter;
import com.elblasy.navigation.api.APIClient;
import com.elblasy.navigation.api.GoogleMapAPI;
import com.elblasy.navigation.models.PlacesResults;
import com.elblasy.navigation.models.Result;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ArrayList<Integer> mImageUrls;
    private ArrayList<String> mNames;
    private GoogleMapAPI googleMapAPI;

    private LocationManager locationManager;
    private List<Result> results;
    private PlacesListAdapter placesListAdapter;
    private CategoryAdapter category;
    private ProgressBar progressBar;
    private TextView list;
    private View rootView;
    //location listener to get list of places
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            String key = getText(R.string.google_maps_key).toString();
            String currentLocation = location.getLatitude() + "," + location.getLongitude();
            int radius = 500;
            Log.i("HOME", "onLocationChanged: " + CategoryAdapter.CATEGORY);

            googleMapAPI = APIClient.getClient().create(GoogleMapAPI.class);
            googleMapAPI.getNearBy(currentLocation, radius, CategoryAdapter.CATEGORY, CategoryAdapter.CATEGORY, key).enqueue(new Callback<PlacesResults>() {
                @Override
                public void onResponse(@NotNull Call<PlacesResults> call, @NotNull Response<PlacesResults> response) {
                    if (response.isSuccessful()) {
                        assert response.body() != null;
                        results.addAll(response.body().getResults());
                        placesListAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        list.setVisibility(View.GONE);

                        Log.e("main", results.toString());
                    } else {
                        Toast.makeText(rootView.getContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PlacesResults> call, @NotNull Throwable t) {
                    Toast.makeText(rootView.getContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    private RecyclerView recyclerView;
    private RecyclerView mRecyclerView;

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
        list = rootView.findViewById(R.id.text3);

        CardView cardView = rootView.findViewById(R.id.card);

        cardView.setOnClickListener(v -> {
            Intent intent = new Intent(rootView.getContext(), SpeackToDriver.class);
            startActivity(intent);
        });

        System.out.println("onCreateView222");
        initImageBitmaps();

        return rootView;
    }

    private void initImageBitmaps() {
        System.out.println("onCreateView6666");

        mImageUrls.add(R.drawable.fast_food);
        mNames.add(getResources().getString(R.string.restaurant));


        mImageUrls.add(R.drawable.groceries);
        mNames.add(getResources().getString(R.string.groceries));

        mImageUrls.add(R.drawable.medicine);
        mNames.add(getResources().getString(R.string.medicine));


        mImageUrls.add(R.drawable.giftbox);
        mNames.add(getResources().getString(R.string.gifts));


        mImageUrls.add(R.drawable.cupcake);
        mNames.add(getResources().getString(R.string.bakery));

        mImageUrls.add(R.drawable.coffee);
        mNames.add(getResources().getString(R.string.cafe));

        mImageUrls.add(R.drawable.fast_food);
        mNames.add(getResources().getString(R.string.restaurant));


        mImageUrls.add(R.drawable.groceries);
        mNames.add(getResources().getString(R.string.groceries));

        mImageUrls.add(R.drawable.medicine1);
        mNames.add(getResources().getString(R.string.medicine));


        mImageUrls.add(R.drawable.shopping);
        mNames.add(getResources().getString(R.string.gifts));


        mImageUrls.add(R.drawable.cupcake);
        mNames.add(getResources().getString(R.string.bakery));

        mImageUrls.add(R.drawable.coffeecup);
        mNames.add(getResources().getString(R.string.cafe));

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
            list.setVisibility(View.VISIBLE);

            placesListAdapter.clear();


            if (ContextCompat.checkSelfPermission(rootView.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(rootView.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                        locationListener, null);
            } else {
                if (ContextCompat.checkSelfPermission(rootView.getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
                }
                if (ContextCompat.checkSelfPermission(rootView.getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
                }
            }
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
