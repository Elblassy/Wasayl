package com.elblasy.navigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.models.OrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackingOrder extends AppCompatActivity {

    DatabaseReference db;

    public TrackingOrder() {
        LocaleUtils.updateConfig(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_order);

        Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        String placeName = intent.getStringExtra("placeName");
        String mobile = intent.getStringExtra("phoneNumber");

        View onWayToPlace = findViewById(R.id.circle_way_to_place);
        View onWayToHome = findViewById(R.id.on_way_to_home);
        View delivered = findViewById(R.id.delivered);


        db = FirebaseDatabase.getInstance().getReference("ClientOrders").child(mobile);

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    OrderModel orders = dataSnapshot1.getValue(OrderModel.class);
                    assert orders != null;
                    if (orders.getPlaceName().equals(placeName)) {
                        switch (orders.getStatus()) {
                            case "way_to_place":
                                onWayToPlace.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                                onWayToHome.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                                delivered.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                                break;
                            case "way_to_home":
                                onWayToPlace.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                                onWayToHome.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                                delivered.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                                break;
                            case "delivered":
                                onWayToPlace.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                                onWayToHome.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                                delivered.setBackground(getResources().getDrawable(R.drawable.solid_circle));
                                break;

                            default:
                                onWayToPlace.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                                onWayToHome.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                                delivered.setBackground(getResources().getDrawable(R.drawable.strock_circle));
                                break;
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        db.addValueEventListener(valueEventListener);

        Button speakToDriver = findViewById(R.id.speak_driver);
        speakToDriver.setOnClickListener(v -> {
            Intent intent1 = new Intent(TrackingOrder.this, SpeackToDriver.class);
            intent1.putExtra("token", token);
            intent1.putExtra("placeName", placeName);
            intent1.putExtra("phoneNumber", mobile);
            startActivity(intent1);
        });
    }
}
