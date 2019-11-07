package com.elblasy.navigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.adapters.DriverAdapter;
import com.elblasy.navigation.models.DriverModel;
import com.elblasy.navigation.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChooseDriver extends AppCompatActivity {

    ArrayList<DriverModel> driverList;
    DatabaseReference reference;

    public ChooseDriver() {
        LocaleUtils.updateConfig(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_driver);

        //toolbar define
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.choose_driver));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        driverList = new ArrayList<>();

        TextView textView = findViewById(R.id.text);


        Intent intent1 = getIntent();

        String placeName = intent1.getStringExtra("placeName");
        String mobile = intent1.getStringExtra("phoneNumber");
        String token = intent1.getStringExtra("token");

        Log.i("placeName", placeName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose_driver));
        builder.setMessage(getResources().getString(R.string.sure_to_choose));
        builder.setCancelable(true);

        builder.setPositiveButton(
                getResources().getString(R.string.yes),
                (dialog, id) -> {
                    Intent intent = new Intent(ChooseDriver.this, TrackingOrder.class);
                    intent.putExtra("placeName", placeName);
                    intent.putExtra("phoneNumber", mobile);
                    intent.putExtra("token", token);

                    startActivity(intent);
                    finish();
                });

        builder.setNegativeButton(
                getResources().getString(R.string.no),
                (dialog, id) -> dialog.cancel());



        //recyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        DriverAdapter driverAdapter = new DriverAdapter(this, driverList, (v, position) -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("orders");
            DatabaseReference db2 = FirebaseDatabase.getInstance().getReference("ClientOrders").child(mobile);

            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        OrderModel orders = dataSnapshot1.getValue(OrderModel.class);
                        if (orders != null) {
                            if (orders.getPlaceName().equals(placeName) && orders.getPhoneNumber().equals(mobile)) {
                                dataSnapshot1.getRef().child("driversID").setValue("010");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            };

            db.addValueEventListener(valueEventListener);
            db2.addValueEventListener(valueEventListener);

            AlertDialog alert = builder.create();
            alert.show();
        });


        RecyclerView.LayoutManager layoutManager =
                new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        recyclerView.setAdapter(driverAdapter);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userKey = user.getPhoneNumber();

        Intent intent = getIntent();
        String place = intent.getStringExtra("placeName");

        //fireBase reference
        reference = FirebaseDatabase.getInstance().getReference("driversAgree")
                .child(userKey)
                .child(place);

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                DriverModel driverModel = dataSnapshot.getValue(DriverModel.class);

                driverList.add(driverModel);
                driverAdapter.notifyDataSetChanged();
                textView.setVisibility(View.GONE);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
