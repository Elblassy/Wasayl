package com.elblasy.navigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.SharedPref;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.Arrays;
import java.util.List;

public class EditProfile extends AppCompatActivity {

    public EditProfile() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //toolbar define
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.edit_profile));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        SharedPref sharedPref = new SharedPref(this);

        EditText userName = findViewById(R.id.user_name);
        EditText mobile = findViewById(R.id.phone_number);

        MaterialBetterSpinner citySpinner = findViewById(R.id.spinnerCities);

        List<String> citiesList = Arrays.asList(getResources().getStringArray(R.array.Cities));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProfile.this,
                android.R.layout.simple_dropdown_item_1line, citiesList);

        citySpinner.setAdapter(adapter);

        String name = SharedPref.getSessionValue("Name");
        String number = SharedPref.getSessionValue("PhoneNumber");
        String city = SharedPref.getSessionValue("City");

        userName.setText(name);
        mobile.setText(number);

        citySpinner.setText(city);

        Button save = findViewById(R.id.save);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.save_changes));
        builder.setMessage(getResources().getString(R.string.sure_to_save));
        builder.setCancelable(true);

        builder.setPositiveButton(
                getResources().getString(R.string.yes),
                (dialog, id) -> {
                    sharedPref.setPreferName(userName.getText().toString());
                    sharedPref.setPrefPhoneNumber(mobile.getText().toString());
                    sharedPref.setPrefCity(citySpinner.getText().toString());
                    dialog.dismiss();

                    Intent intent = new Intent(EditProfile.this, Home.class);
                    startActivity(intent);
                    finish();
                });

        builder.setNegativeButton(
                getResources().getString(R.string.no),
                (dialog, id) -> dialog.cancel());


        save.setOnClickListener(v -> {
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
