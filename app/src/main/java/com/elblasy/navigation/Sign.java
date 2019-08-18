package com.elblasy.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Sign extends AppCompatActivity {

    Button button;
    EditText phoneNumber, username;
    FirebaseAuth auth;
    DatabaseReference users;

    private static final String TAG = Sign.class.getSimpleName();

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        phoneNumber = findViewById(R.id.phone_number);
//        username = findViewById(R.id.username);
        button = findViewById(R.id.signin);


//        Locale[] locales = Locale.getAvailableLocales();
//        final ArrayList<String> countries = new ArrayList<>();
//        for (Locale locale : locales) {
//            String country = locale.getDisplayCountry();
//            if (country.trim().length() > 0 && !countries.contains(country)) {
//                countries.add(country);
//            }
//        }
//        Collections.sort(countries);
//

//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_dropdown_item_1line, countries);
//        spinnerCountries.setAdapter(adapter);
//
//        resetCities();


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        users = database.getReference("users");


        button.setOnClickListener(v -> {

            String mPhoneNumber = phoneNumber.getText().toString().trim();


            if (TextUtils.isEmpty(mPhoneNumber) || mPhoneNumber.length() < 11) {
                Toast.makeText(getApplicationContext(), "Please enter a valid mobile", Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent1 = new Intent(Sign.this, VerifyPhoneNumber.class);
            intent1.putExtra("mobile", mPhoneNumber);

            startActivity(intent1);

        });


    }
}
//    private void resetCities() {
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Sign.this,
//                android.R.layout.simple_dropdown_item_1line, citiesList);
//        spinnerCities.setAdapter(adapter1);
//
//    }
//
//    private String loadJSONFromAsset() {
//        String json = null;
//        try {
//            InputStream is = getAssets().open("countries.json");
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            json = new String(buffer, StandardCharsets.UTF_8);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//        return json;
//    }
