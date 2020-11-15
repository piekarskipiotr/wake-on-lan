package dev.dazai.wol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import dev.dazai.wol.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ActivityMainBinding binding;
    NavigationView navigationView;
    private DeviceViewModel deviceViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        navigationView = binding.navigationView;
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        if(savedInstanceState == null){
            navigationView.setCheckedItem(navigationView.getMenu().getItem(0));
            getSupportFragmentManager()
                    .beginTransaction().
                    replace((R.id.fragmentContainer), new DashboardFragment())
                    .commit();
        }

        binding.menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.END);

            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch(item.getItemId()){
            case R.id.dashboard:
                fragment = new DashboardFragment();
                break;
            case R.id.groups:
                fragment = new MyGroupsFragment();
                break;
            case R.id.events:
                fragment = new EventsFragment();
                break;
            case R.id.help:
                fragment = new HelpFragment();
                break;

        }
        binding.navigationView.setCheckedItem(item);
        getSupportFragmentManager()
                .beginTransaction()
                .replace((R.id.fragmentContainer), fragment)
                .commit();
        binding.drawerLayout.closeDrawer(GravityCompat.END);
        return true;
    }



    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.END)){
            binding.drawerLayout.closeDrawer(GravityCompat.END);
        }else{
            super.onBackPressed();
        }

    }

}


