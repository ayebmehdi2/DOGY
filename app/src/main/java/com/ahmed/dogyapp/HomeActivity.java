package com.ahmed.dogyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.ahmed.dogyapp.AUTH.WelcomeBack;
import com.ahmed.dogyapp.databinding.ActivityHomeBinding;
import com.ahmed.dogyapp.databinding.NavHeaderHomeBinding;
import com.ahmed.dogyapp.databinding.WelcomeBackBinding;
import com.ahmed.dogyapp.ui.Services.ServicesFragment;
import com.ahmed.dogyapp.ui.myworks.MyWorkFragment;
import com.ahmed.dogyapp.ui.rating.RatingshowFragment;
import com.ahmed.dogyapp.ui.wishlist.WishListFragment;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeActivity extends AppCompatActivity {


    ActivityHomeBinding binding;

    private String uid;

    FirebaseDatabase database;
    DrawerLayout drawer;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_home, new ServicesFragment()).commit();
        setTitle("Services");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = preferences.getString("uid", "");

        setSupportActionBar(binding.appBarHome.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        setupDrawerContent(navigationView);

        /*
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.services, R.id.rating, R.id.work, R.id.wishlist)
                .setOpenableLayout(drawer)
                .build();


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
      NavigationUI.setupWithNavController(navigationView, navController);
        */
        

        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        String uid = preferences.getString("uid", null);

        reference.child("PROFILES").child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ProfilData data = snapshot.getValue(ProfilData.class);
                        if (data == null) return;
                        ImageView imageView = binding.navView.getHeaderView(0).findViewById(R.id.userimg);
                        Glide.with(HomeActivity.this).load(data.getImage()).into(imageView);
                        TextView textView = binding.navView.getHeaderView(0).findViewById(R.id.username);
                        textView.setText(data.getUsername());
                        TextView textView1 = binding.navView.getHeaderView(0).findViewById(R.id.email);
                        textView1.setText(data.getEmail());

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) { }
                });


        ActionBarDrawerToggle toggle = setupDrawerToggle();
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        drawer.addDrawerListener(toggle);

    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, drawer, binding.appBarHome.toolbar, R.string.drawer_open,  R.string.drawer_close);
    }


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.services:
                fragmentClass = ServicesFragment.class;
                break;
            case R.id.rating:
                fragmentClass = RatingshowFragment.class;
                break;
            case R.id.work:
                fragmentClass = MyWorkFragment.class;
                break;
            case R.id.wishlist:
                fragmentClass = WishListFragment.class;
                break;
            case R.id.about:

                break;    
            case R.id.logout:
                SharedPreferences.Editor preference = PreferenceManager.getDefaultSharedPreferences(HomeActivity.this).edit();
                preference.putString("uid", null);
                preference.apply();
                FirebaseMessaging.getInstance().unsubscribeFromTopic("user_" + uid).addOnSuccessListener(aVoid -> Toast.makeText(getApplicationContext(), "Unsubscribe Success", Toast.LENGTH_LONG).show());
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomeActivity.this, SplachActivity.class));
                drawer.closeDrawer(GravityCompat.START);
                break;    
            default:
                fragmentClass = ServicesFragment.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment_content_home, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        drawer.closeDrawers();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed () {
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        if (drawer.isDrawerOpen(GravityCompat. START )) {
            drawer.closeDrawer(GravityCompat. START ) ;
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            super .onBackPressed() ;
        }
    }


}

