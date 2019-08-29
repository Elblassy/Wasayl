package com.elblasy.navigation.activities;

import android.content.Intent;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.volley.toolbox.JsonObjectRequest;
import com.elblasy.navigation.R;
import com.elblasy.navigation.adapters.MessageListAdapter;
import com.elblasy.navigation.models.Message;
import com.elblasy.navigation.models.MySingleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpeackToDriver extends AppCompatActivity {

    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAMgUYmxc:APA91bHLBfsU1MbJwOCcpW-6g5cg11cQZ901bbPVY83Dh2Td_Q79B7V-YDchmdg4qC9S92TRLPjRnxArXCySyZ1Pw4QNQlZHI4q_iNVeHlMcGltltCrMjEZFqBpblE1J_ECPkDiS0AiP";
    final private String contentType = "application/json";


    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC, token;

    EditText editText;
    ListView listView;
    DatabaseReference reference, orderReference;
    private MessageListAdapter mAdapter;
    private ArrayList<Message> messageList;
    ConstraintLayout constraintLayout;
    ConstraintSet constraintSet;
    int mPosition = -1;
    private LinearLayout chatLayout, selectLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speack_to_driver);

        //toolbar define
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        editText = findViewById(R.id.edit_text);
        ImageView send = findViewById(R.id.send);
        listView = findViewById(R.id.list_view);
        chatLayout = findViewById(R.id.linearLayout);
        selectLayout = findViewById(R.id.linearLayout2);
        constraintLayout = findViewById(R.id.constraint);

        chatLayout.setVisibility(View.GONE);
        selectLayout.setVisibility(View.VISIBLE);

        Button agree = findViewById(R.id.agree);
        Button show = findViewById(R.id.show);

        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        agree.setOnClickListener(v -> {
            if (mPosition == -1) {
                Toast.makeText(SpeackToDriver.this, "Please choose one of diver's offers",
                        Toast.LENGTH_LONG).show();
                return;
            }
            constraintSet.connect(listView.getId(), ConstraintSet.BOTTOM, chatLayout.getId(),
                    ConstraintSet.TOP, 0);
            constraintSet.applyTo(constraintLayout);
            chatLayout.setVisibility(View.VISIBLE);
            selectLayout.setVisibility(View.GONE);

            listView.setSelector(new StateListDrawable());

            Message message = messageList.get(mPosition);

            orderReference.child("driverName").setValue(message.getSender());


        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            mPosition = position;

        });



        messageList = new ArrayList<>();

        mAdapter = new MessageListAdapter(this, messageList);
        listView.setAdapter(mAdapter);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String userKey = user.getPhoneNumber();

        Intent intent = getIntent();
        String placeName = intent.getStringExtra("placeName");
        String mobile = intent.getStringExtra("phoneNumber");

        Log.i("SPEAK", placeName);

        reference = FirebaseDatabase.getInstance().getReference("chats").child(userKey).child(placeName);
        orderReference = FirebaseDatabase.getInstance().getReference("orders").child(placeName + mobile);


        loadMessages();

        send.setOnClickListener(v -> {
            if (!messageList.isEmpty() && !editText.getText().toString().matches("")) {
                TOPIC = "/topics/wasayldrivers"; //topic must match with what the receiver subscribed to
                NOTIFICATION_TITLE = "Driver";
                NOTIFICATION_MESSAGE = editText.getText().toString();

                sendMessage("Client", "Driver", editText.getText().toString());

                JSONObject notification = new JSONObject();
                JSONObject notifcationBody = new JSONObject();
                try {
                    notifcationBody.put("title", NOTIFICATION_TITLE);
                    notifcationBody.put("message", NOTIFICATION_MESSAGE);

                    notification.put("to", token);
                    notification.put("data", notifcationBody);

                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: " + e.getMessage());
                }

                System.out.println(notification);
                sendNotification(notification);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void sendMessage(String sender, final String receiver, String message) {

        Message message1 = new Message();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.push().setValue(hashMap);

        message1.setTitle(sender);
        message1.setMessage(message);
        message1.setSelf(true);

        editText.setText("");

    }


    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> {
                    Log.i(TAG, "onResponse: " + response.toString());
//                    Intent intent = new Intent(Main.this, Home.class);
//                    startActivity(intent);
//                    finish();
                },
                error -> {
                    Toast.makeText(SpeackToDriver.this, "Request error", Toast.LENGTH_LONG).show();
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

    private void loadMessages() {

        Query messageQuery = reference.limitToLast(10);

        messageQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message messages = dataSnapshot.getValue(Message.class);
                if (messages.getSender().matches("Driver")) {
                    token = messages.getToken();
                } else {
                    messages.setSelf(true);
                }

                messages.setTitle(messages.getSender());
                messageList.add(messages);
                mAdapter.notifyDataSetChanged();

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
}
