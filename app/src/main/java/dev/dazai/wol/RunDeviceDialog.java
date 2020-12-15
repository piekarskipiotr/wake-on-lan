package dev.dazai.wol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import dev.dazai.wol.databinding.RunDeviceDialogBinding;

public class RunDeviceDialog extends AppCompatDialogFragment {
    private RunDeviceDialogViewModel runDeviceDialogViewModel;
    AlertDialog alertDialog;
    RunDeviceDialogBinding binding;
    private List<Device> mDeviceList;
    private Device mDevice;
    AlertDialog.Builder builder;
    RunDeviceListAdapter adapter;
    public RunDeviceDialog(List<Device> deviceList){
        mDeviceList = deviceList;

    }

    public RunDeviceDialog(Device device){
        mDevice = device;

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        runDeviceDialogViewModel = ViewModelProviders.of(this).get(RunDeviceDialogViewModel.class);
        binding = RunDeviceDialogBinding.inflate(getLayoutInflater());
        adapter = new RunDeviceListAdapter(getContext());
        builder = new AlertDialog.Builder(getContext());
        builder.setView(binding.getRoot());
        alertDialog = builder.create();

        binding.devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.devicesRecyclerView.setHasFixedSize(true);
        binding.devicesRecyclerView.setAdapter(adapter);
        if(mDeviceList != null){
            adapter.setDevices(mDeviceList);
            for(int i = 0; i < mDeviceList.size(); i++){
                final Device device = mDeviceList.get(i);
                if(device.getReachable()){
                    continue;
                }
                binding.headerText.setText("Uruchamianie: "+device.getDeviceName());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new MagicPacket().send(device.deviceMacAddress, Integer.parseInt(device.getDeviceLanPort()), getContext());
                        new ReachableChecker(RunDeviceDialog.this, device, runDeviceDialogViewModel).execute();
                    }
                },2000);

            }

        }else{
            adapter.setDevice(mDevice);
            binding.headerText.setText("Uruchamianie: "+mDevice.getDeviceName());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new MagicPacket().send(mDevice.deviceMacAddress, Integer.parseInt(mDevice.deviceLanPort), getContext());
                    new ReachableChecker(RunDeviceDialog.this, mDevice, runDeviceDialogViewModel).execute();
                }
            },1000);
        }

        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        return alertDialog;
    }
}
