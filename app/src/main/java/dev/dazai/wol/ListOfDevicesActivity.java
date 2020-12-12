package dev.dazai.wol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import dev.dazai.wol.databinding.ActivityListOfDevicesBinding;

public class ListOfDevicesActivity extends AppCompatActivity implements ActiveDevicesListAdapter.onDeviceClick, SavedDevicesListAdapter.onDeviceClick {
    ActivityListOfDevicesBinding binding;
    ListOfDevicesViewModel listOfDevicesViewModel;
    ActiveDevicesListAdapter aAdapter;
    SavedDevicesListAdapter sAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListOfDevicesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.listOfDevicesRecycleView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        binding.listOfDevicesRecycleView.setHasFixedSize(true);

        listOfDevicesViewModel = ViewModelProviders.of(this).get(ListOfDevicesViewModel.class);

        Bundle extras = getIntent().getExtras();
        if(extras.getBoolean("STATE")){
            binding.devicesStateText.setText("Aktywne\nurządzenia");
            aAdapter = new ActiveDevicesListAdapter(getApplicationContext(), this);
            binding.listOfDevicesRecycleView.setAdapter(aAdapter);
            listOfDevicesViewModel.getActiveDevices().observe(this, new Observer<List<Device>>() {
                @Override
                public void onChanged(List<Device> devices) {
                    aAdapter.setActiveDevices(devices);

                }
            });

        }else{
            Log.d("CHUJ", extras.getBoolean("STATE")+"x");
            binding.devicesStateText.setText("Dostępne\nurządzenia");
            sAdapter = new SavedDevicesListAdapter(getApplicationContext(), this);
            binding.listOfDevicesRecycleView.setAdapter(sAdapter);
            listOfDevicesViewModel.getSavedDevices().observe(this, new Observer<List<Device>>() {
                @Override
                public void onChanged(List<Device> devices) {
                    sAdapter.setSavedDevices(devices);

                }
            });

        }

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onDeviceCardClick(Device device) {
        Intent i = new Intent(this, DevicePanelActivity.class);
        i.putExtra("ID", device.getDeviceId());
        startActivity(i);

    }
}