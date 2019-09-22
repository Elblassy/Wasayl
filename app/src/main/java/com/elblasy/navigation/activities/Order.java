package com.elblasy.navigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.JsonObjectRequest;
import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.adapters.EditAdapter;
import com.elblasy.navigation.models.Message;
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

    String address, placeName;
    private StringBuilder details;
    private boolean active = true;
    FirebaseAuth firebaseAuth;
    ListView listView;
    EditAdapter editAdapter;
    ProgressBar progressBar;

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAMgUYmxc:APA91bHLBfsU1MbJwOCcpW-6g5cg11cQZ901bbPVY83Dh2Td_Q79B7V-YDchmdg4qC9S92TRLPjRnxArXCySyZ1Pw4QNQlZHI4q_iNVeHlMcGltltCrMjEZFqBpblE1J_ECPkDiS0AiP";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;
    String fcmToken;
    EditText recieve, home, placeNameEdit;
    String userKey;
    private DatabaseReference mdatabase, ordersDatabase, reference;

    public Order() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        if (intent.hasExtra("Address") && intent.hasExtra("place_name")) {
            address = intent.getStringExtra("Address");
            placeName = intent.getStringExtra("place_name");
        } else {
            address = "";
            placeName = "";
        }


        home = findViewById(R.id.to);
        recieve = findViewById(R.id.from);
        placeNameEdit = findViewById(R.id.place_name);

        placeNameEdit.setText(placeName);
        recieve.setText(address);

        mdatabase = FirebaseDatabase.getInstance().getReference("ClientOrders");
        ordersDatabase = FirebaseDatabase.getInstance().getReference("orders");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        userKey = user.getPhoneNumber();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        listView = findViewById(R.id.list_item);
        editAdapter = new EditAdapter(this);
        listView.setAdapter(editAdapter);

        FirebaseMessaging.getInstance().subscribeToTopic("wasaylclient");

        reference = FirebaseDatabase.getInstance().getReference("chats").child(userKey).child(placeName);


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(Order.this, instanceIdResult -> {

            fcmToken = instanceIdResult.getToken();
            Log.e("token", fcmToken);

        });


        Button placeOrder = findViewById(R.id.place_order);


        placeOrder.setOnClickListener(v -> {
            if (TextUtils.isEmpty(recieve.getText().toString()) || TextUtils.isEmpty(home.getText().toString())
                    || TextUtils.isEmpty(placeNameEdit.getText().toString())) {
                Toast.makeText(getApplicationContext(), R.string.valid_location, Toast.LENGTH_LONG).show();
                return;
            }
            details = new StringBuilder();
            progressBar.setVisibility(View.VISIBLE);

            for (int i = 0; i < listView.getChildCount() - 1; i++) {
                View view = listView.getChildAt(i);
                EditText et = view.findViewById(R.id.editText1);
                EditText et2 = view.findViewById(R.id.editText2);
                details.append(et2.getText().toString()).append("-");
                details.append(et.getText().toString()).append(" ");
            }

            OrderModel orderModel = new OrderModel(active, details.toString(), recieve.getText().toString(),
                    placeNameEdit.getText().toString(), "Elblasy",
                    fcmToken, userKey, home.getText().toString(), "unknown", "nothing");

            assert userKey != null;
            mdatabase.child(userKey).push().setValue(orderModel);
            ordersDatabase.child(placeNameEdit.getText().toString() + userKey).setValue(orderModel);


            TOPIC = "/topics/portsaid"; //topic must match with what the receiver subscribed to
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
            sendMessage("Client", "Driver", details.toString());
            sendNotification(notification);

        });
    }

    private void sendMessage(String sender, final String receiver, String message) {

        Message message1 = new Message();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("driverNumber", "unknown");

        reference.child("Order").setValue(hashMap);

        message1.setTitle(sender);
        message1.setMessage(message);
        message1.setSelf(true);

    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
                    Intent intent = new Intent(Order.this, ChooseDriver.class);
                    intent.putExtra("placeName", placeNameEdit.getText().toString());
                    intent.putExtra("phoneNumber", userKey);
                    intent.putExtra("token", fcmToken);
                    startActivity(intent);
                    progressBar.setVisibility(View.GONE);
                    finish();
                },
                error -> {
                    Toast.makeText(Order.this, "Request error" + error, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    Log.i(TAG, "onErrorResponse: Didn't work  " + error);
                }) {

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

}
