package com.elblasy.navigation;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.adapters.CategoryAdapter;
import com.elblasy.navigation.adapters.PlacesListAdapter;
import com.elblasy.navigation.api.APIClient;
import com.elblasy.navigation.api.GoogleMapAPI;
import com.elblasy.navigation.models.PlacesResults;
import com.elblasy.navigation.models.Result;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.infideap.drawerbehavior.Advance3DDrawerLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<Integer> mImageUrls = new ArrayList<>();
    private ArrayList<String> mNames = new ArrayList<>();
    GoogleMapAPI googleMapAPI;

    private LocationManager locationManager;
    List<Result> results;
    PlacesListAdapter placesListAdapter;
    CategoryAdapter category;
    ProgressBar progressBar;
    ObjectAnimator animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //toolbar define
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent2));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorAccent2));
        setSupportActionBar(toolbar);


        results = new ArrayList<>();
        placesListAdapter = new PlacesListAdapter(Home.this, results);

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        Advance3DDrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_RTL)
            drawer.setViewRotation(Gravity.END, 15);
        else
            drawer.setViewRotation(Gravity.START, 15);


        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        initImageBitmaps();


    }

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

                        Log.e("main", results.toString());
                    } else {
                        Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NotNull Call<PlacesResults> call, @NotNull Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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


    private void initImageBitmaps() {

        mImageUrls.add(R.drawable.hamburger);
        mNames.add(getResources().getString(R.string.restaurant));


        mImageUrls.add(R.drawable.bread);
        mNames.add(getResources().getString(R.string.groceries));

        mImageUrls.add(R.drawable.medicine1);
        mNames.add(getResources().getString(R.string.medicine));


        mImageUrls.add(R.drawable.shopping);
        mNames.add(getResources().getString(R.string.gifts));


        mImageUrls.add(R.drawable.cupcake);
        mNames.add(getResources().getString(R.string.bakery));

        mImageUrls.add(R.drawable.coffeecup);
        mNames.add(getResources().getString(R.string.cafe));

        mImageUrls.add(R.drawable.hamburger);
        mNames.add(getResources().getString(R.string.restaurant));


        mImageUrls.add(R.drawable.bread);
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

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView mRecyclerView = findViewById(R.id.recyclerView2);

        int resId = R.anim.layout_animation_fall_down;
        LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(Home.this, resId);

        category = new CategoryAdapter(Home.this, mNames, mImageUrls, (v, position) -> {

            System.out.println(position + "  HOMEClick");
            progressBar.setVisibility(View.VISIBLE);
            placesListAdapter.clear();


            if (ContextCompat.checkSelfPermission(Home.this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(Home.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,
                        locationListener, null);
            } else {
                if (ContextCompat.checkSelfPermission(Home.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Home.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12);
                }
                if (ContextCompat.checkSelfPermission(Home.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Home.this,
                            new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 13);
                }
            }
        });


        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);


        recyclerView.setLayoutAnimation(animation);
        recyclerView.setAdapter(category);


        RecyclerView.LayoutManager layoutManager =
                new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setAdapter(placesListAdapter);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //to make this is main activity and don't back to sign in
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(Home.this, Order.class);
            startActivity(intent);
        } else if (id == R.id.action_sign_out) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Home.this, Sign.class);
            startActivity(intent);
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

