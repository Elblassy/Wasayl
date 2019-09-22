package com.elblasy.navigation.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.SharedPref;
import com.elblasy.navigation.activities.EditProfile;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    private View rootView;

    private AlertDialog alertDialog;
    private CharSequence[] values = {" English ", " العربية "};
    private SharedPref sharedPref;

    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);

        ListView listView = rootView.findViewById(R.id.list_view);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(getResources().getString(R.string.edit_profile));
        arrayList.add(getResources().getString(R.string.language));

        sharedPref = new SharedPref(rootView.getContext());

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(rootView.getContext(),
                R.layout.settings_item, arrayList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    Intent intent = new Intent(rootView.getContext(), EditProfile.class);
                    startActivity(intent);
                    break;
                case 1:
                    alertDialogChooseLang();
                    break;
            }
        });
        return rootView;
    }

    private void alertDialogChooseLang() {


        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());

        builder.setTitle(getResources().getString(R.string.choose_language));

        builder.setSingleChoiceItems(values, -1, (dialog, item) -> {

            switch (item) {
                case 0:
                    sharedPref.setPrefLang(LocaleUtils.LAN_ENGLISH);
                    break;
                case 1:
                    sharedPref.setPrefLang(LocaleUtils.LAN_ARABIC);
                    break;

            }
            alertDialog.dismiss();

            Toast.makeText(rootView.getContext(), getResources().getString(R.string.toost_to_restart),
                    Toast.LENGTH_LONG).show();
        });
        alertDialog = builder.create();
        alertDialog.show();

    }


}
