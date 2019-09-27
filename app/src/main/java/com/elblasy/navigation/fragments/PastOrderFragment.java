package com.elblasy.navigation.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.elblasy.navigation.R;
import com.elblasy.navigation.adapters.PastOrderAdapter;
import com.elblasy.navigation.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrderFragment extends Fragment {


    private DatabaseReference databaseReference;
    private PastOrderAdapter ordersAdapter;
    private ArrayList<OrderModel> ordersList;

    public PastOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ordersList = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userKey = user.getPhoneNumber();

        databaseReference = FirebaseDatabase.getInstance().getReference("ClientOrders").child(userKey);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_past_order, container, false);

        ListView listView = rootView.findViewById(R.id.list_item_my_order);
        ordersAdapter = new PastOrderAdapter(rootView.getContext(), ordersList);
        listView.setAdapter(ordersAdapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                OrderModel orders = dataSnapshot.getValue(OrderModel.class);
                assert orders != null;
                if (!orders.isActive())
                    ordersList.add(orders);

                ordersAdapter.notifyDataSetChanged();
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

        // Inflate the layout for this fragment
        return rootView;
    }

}
