package dev.dazai.wol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import dev.dazai.wol.databinding.RunDeviceDialogBinding;

public class RunDeviceDialog extends AppCompatDialogFragment {
    private RunDeviceDialogViewModel runDeviceDialogViewModel;
    AlertDialog alertDialog;
    RunDeviceDialogBinding binding;
    private List<Device> mDeviceList, mNonReachableDeviceList = new ArrayList<>();
    private Device mDevice;
    private AlertDialog.Builder builder;
    RunDeviceListAdapter adapter;
    private ReachableChecker reachableChecker;
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
        alertDialog.setCanceledOnTouchOutside(false);

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
                mNonReachableDeviceList.add(device);
                binding.headerText.setText("Uruchamianie: "+device.getDeviceName());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        String secureOn;
                        if(device.getDeviceSecureOn().isEmpty())
                            secureOn = "00:00:00:00:00:00";
                        else
                            secureOn = device.getDeviceSecureOn();



                        new MagicPacket().send(device.deviceMacAddress, Integer.parseInt(device.getDeviceLanPort()), secureOn, getContext());

                    }
                },2000);

            }
            reachableChecker = new ReachableChecker(RunDeviceDialog.this, mNonReachableDeviceList, runDeviceDialogViewModel);
            reachableChecker.execute();

        }else{
            adapter.setDevice(mDevice);
            binding.headerText.setText("Uruchamianie: "+mDevice.getDeviceName());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    String secureOn;
                    if(mDevice.getDeviceSecureOn().isEmpty())
                        secureOn = "00:00:00:00:00:00";
                    else
                        secureOn = mDevice.getDeviceSecureOn();


                    new MagicPacket().send(mDevice.deviceMacAddress, Integer.parseInt(mDevice.deviceLanPort), secureOn, getContext());
                    reachableChecker = new ReachableChecker(RunDeviceDialog.this, mDevice, runDeviceDialogViewModel);
                    reachableChecker.execute();
                }
            },1000);
        }


        binding.doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reachableChecker.cancel(true);
                alertDialog.dismiss();

            }
        });

        return alertDialog;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
