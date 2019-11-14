package com.elblasy.navigation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.elblasy.navigation.LocaleUtils;
import com.elblasy.navigation.R;
import com.elblasy.navigation.SharedPref;
import com.elblasy.navigation.fragments.AboutUsFragment;
import com.elblasy.navigation.fragments.HomeFragment;
import com.elblasy.navigation.fragments.MyOrdersFragment;
import com.elblasy.navigation.fragments.PastOrderFragment;
import com.elblasy.navigation.fragments.SettingFragment;
import com.google.android.material.navigation.NavigationView;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import org.jetbrains.annotations.NotNull;


public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    public Home() {
        LocaleUtils.updateConfig(this);
    }

    Toolbar toolbar;


//    @Override
//    protected void onStart() {
//        SharedPref sharedPref = new SharedPref(this);
//        sharedPref.setPrefLang("ar");
//        String lan = sharedPref.getSessionValue("Language");
//        Locale locale = new Locale(lan);
//        Locale.setDefault(locale);
//        Configuration config = new Configuration();//get Configuration
//        config.locale = locale;//set config locale as selected locale
//        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
//        invalidateOptionsMenu();
//
//        System.out.println(Locale.getDefault());
//        super.onStart();
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //toolbar define
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAccent2));
//        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colorAccent2));
        setSupportActionBar(toolbar);


        AdvanceDrawerLayout drawer = findViewById(R.id.drawer_layout);

        if (SharedPref.getSessionValue("Language").matches("ar")) {
            drawer.setRadius(Gravity.END, 55);
            drawer.setViewScale(GravityCompat.END, 0.9f);
            drawer.setViewElevation(GravityCompat.END, 20);
            System.out.println("menu" + "Right");
        } else {
            drawer.setRadius(Gravity.START, 55);
            drawer.setViewScale(GravityCompat.START, 0.9f);
            drawer.setViewElevation(GravityCompat.START, 20);
            System.out.println("menu" + "left");

        }


        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // load the store fragment by default
        toolbar.setTitle(getResources().getString(R.string.title_activity_home));
        loadFragment(new HomeFragment());


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //to make this is main activity and don't back to sign in
            Intent startMain = new Intent(Home.this, Home.class);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // load the store fragment by default
            toolbar.setTitle(getResources().getString(R.string.menu_home));
            loadFragment(new HomeFragment());
        } else if (id == R.id.nav_order) {
            // load the store fragment by default
            toolbar.setTitle(getResources().getString(R.string.menu_orders));
            loadFragment(new MyOrdersFragment());
        } else if (id == R.id.nav_history) {
            toolbar.setTitle(getResources().getString(R.string.menu_history));
            loadFragment(new PastOrderFragment());

        } else if (id == R.id.nav_tools) {
            toolbar.setTitle(getResources().getString(R.string.menu_settings));
            loadFragment(new SettingFragment());
        } else if (id == R.id.nav_about_us) {
            toolbar.setTitle(getResources().getString(R.string.menu_about_us));
            loadFragment(new AboutUsFragment());
        } else if (id == R.id.nav_send) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

