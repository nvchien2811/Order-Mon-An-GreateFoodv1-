package com.sict.ordermonan;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class Main2Activity extends AppCompatActivity {
    public androidx.appcompat.widget.Toolbar toolbar;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        mAuth = FirebaseAuth.getInstance();
        String ten = mAuth.getCurrentUser().getEmail();
        String[] part = ten.split("@");
        String tenhienthi = part[0];
        toolbar.setTitle("Xin chào\n"+tenhienthi.toUpperCase());
        setSupportActionBar(toolbar);


        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frament_container, new HomeFragment()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedfragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_home :
                            selectedfragment = new HomeFragment();
                            break;
                        case R.id.nav_favorites :
                            selectedfragment = new FavoritesFragment();
                            break;
                        case R.id.nav_search :
                            selectedfragment = new BanAnFragment();
                            break;
                        case R.id.nav_cart:
                            selectedfragment = new CartFragment();
                            break;
                    }
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.replace(R.id.frament_container,selectedfragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                    return true;
                }
            };


    public void setSupportActionBar(Toolbar toolbar) {


    }
}
