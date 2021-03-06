package com.elblasy.navigation.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.JsonObjectRequest;
import com.elblasy.navigation.LocaleUtils;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpeackToDriver extends AppCompatActivity {

    final String TAG = "NOTIFICATION TAG";
    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "your key";
    final private String contentType = "application/json";


    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC, token, myToken;

    EditText editText;
    ListView listView;
    DatabaseReference reference, orderReference;
    private MessageListAdapter mAdapter;
    private ArrayList<Message> messageList;

    public SpeackToDriver() {
        LocaleUtils.updateConfig(this);
    }

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

        send.setVisibility(View.GONE);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0)
                    send.setVisibility(View.GONE);
                else
                    send.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
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
        token = intent.getStringExtra("token");


        //database reference
        reference = FirebaseDatabase.getInstance().getReference("chats").child(userKey).child(placeName);
        orderReference = FirebaseDatabase.getInstance().getReference("orders").child(placeName + mobile);

        //Load past message
        loadMessages();

        //get my token
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this,
                instanceIdResult -> {
                    myToken = instanceIdResult.getToken();
                    Log.e("myToken", myToken);

                });

        send.setOnClickListener(v -> {

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
        hashMap.put("token", myToken);

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
                assert messages != null;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.call, menu);
        return true;
    }

    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.call) {
            if (ContextCompat.checkSelfPermission(SpeackToDriver.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(SpeackToDriver.this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:01060279201"));
                startActivity(callIntent);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
