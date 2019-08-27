package com.elblasy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.elblasy.navigation.adapters.EditAdapter;
import com.elblasy.navigation.models.MySingleton;
import com.elblasy.navigation.models.OrderModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Order extends AppCompatActivity {

    TextView cobon;
    String address, placeName;
    private StringBuilder details;
    private boolean active = true;
    FirebaseAuth firebaseAuth;
    ListView listView;
    EditAdapter editAdapter;
    ProgressBar progressBar;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "your key";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String fcmToken;
    private DatabaseReference mdatabase, ordersDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        address = intent.getStringExtra("Address");
        placeName = intent.getStringExtra("place_name");

        mdatabase = FirebaseDatabase.getInstance().getReference("ClientOrders");
        ordersDatabase = FirebaseDatabase.getInstance().getReference("orders");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String userKey = user.getPhoneNumber();

        cobon = findViewById(R.id.text_cobon);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        listView = findViewById(R.id.list_item);
        editAdapter = new EditAdapter(this);
        listView.setAdapter(editAdapter);

        FirebaseMessaging.getInstance().subscribeToTopic("wasaylclient");

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Order.this, instanceIdResult -> {

            fcmToken = instanceIdResult.getToken();
            Log.e("token", fcmToken);

        });


        Button placeOrder = findViewById(R.id.place_order);


        placeOrder.setOnClickListener(v -> {
            details = new StringBuilder();
            progressBar.setVisibility(View.VISIBLE);
            for (int i = 0; i < listView.getChildCount() - 1; i++) {
                View view = listView.getChildAt(i);
                EditText et = view.findViewById(R.id.editText1);
                EditText et2 = view.findViewById(R.id.editText2);
                details.append(et2.getText().toString()).append("-");
                details.append(et.getText().toString()).append(" ");
            }
            OrderModel orderModel = new OrderModel(active, details.toString(), address, placeName,
                    "Elblasy", fcmToken, userKey);
            assert userKey != null;
            mdatabase.child(userKey).push().setValue(orderModel);
            ordersDatabase.push().setValue(orderModel);


            TOPIC = "/topics/wasayldrivers"; //topic must match with what the receiver subscribed to
            NOTIFICATION_TITLE = getResources().getString(R.string.notification_title);
            NOTIFICATION_MESSAGE = details.toString();

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE);
                notifcationBody.put("message", NOTIFICATION_MESSAGE);

                notification.put("to", TOPIC);
                notification.put("data", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
            }

            System.out.println(notification);
            sendNotification(notification);

        });
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
                    Intent intent = new Intent(Order.this, Home.class);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                    finish();
                },
                error -> {
                    Toast.makeText(Order.this, "Request error", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                System.out.println("TRUE");
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
