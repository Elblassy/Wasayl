package com.elblasy.navigation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseVehicle extends AppCompatActivity {

    @BindView(R.id.light_vichel)
    LinearLayout lightVichel;
    @BindView(R.id.hard_vichel)
    LinearLayout hardVichel;

    public ChooseVehicle() {
        LocaleUtils.updateConfig(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_vehicle);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.light_vichel, R.id.hard_vichel})
    public void onViewClicked(View view) {
        Intent intent = new Intent(ChooseVehicle.this, Order.class);
        switch (view.getId()) {
            case R.id.light_vichel:
                intent.putExtra("kind_of_order", "light");
                startActivity(intent);
                finish();
                break;
            case R.id.hard_vichel:
                intent.putExtra("kind_of_order", "heavy");
                startActivity(intent);
                finish();
                break;
        }
    }
}
