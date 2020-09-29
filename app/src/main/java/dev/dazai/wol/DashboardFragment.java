package dev.dazai.wol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import dev.dazai.wol.databinding.DashboardNewDeviceDialogBinding;
import dev.dazai.wol.databinding.DialogNetworkScanningBinding;
import dev.dazai.wol.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements NetworkScannerListAdapter.OnDeviceListener, SavedDevicesListAdapter.OnMyDeviceListener{
    ArrayList<DeviceInNetwork> devicesList = new ArrayList<>();
    NetworkScanner networkScanner;
    BottomSheetDialog bottomSheetDialog;
    FragmentDashboardBinding binding;
    DashboardNewDeviceDialogBinding dialogBinding;
    DialogNetworkScanningBinding dialogNetworkScanningBinding;
    NetworkScannerListAdapter nAdapter;
    SavedDevicesListAdapter sAdapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DeviceDatabase deviceDatabase = DeviceDatabase.getInstance(getContext());
        sAdapter = new SavedDevicesListAdapter(getContext(), deviceDatabase.deviceDao().getAll(), DashboardFragment.this);
        binding.devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.devicesRecyclerView.setHasFixedSize(true);
        binding.devicesRecyclerView.setAdapter(sAdapter);

        binding.newDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding = DashboardNewDeviceDialogBinding.inflate(getLayoutInflater());

                bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()), R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(dialogBinding.getRoot());
                bottomSheetDialog.show();


                dialogBinding.manualInputButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), DevicePanelActivity.class);
                        startActivity(i);
                    }
                });

                dialogBinding.networkScanningButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBinding.getRoot().removeAllViews();

                        dialogNetworkScanningBinding = DialogNetworkScanningBinding.inflate(getLayoutInflater());
                        bottomSheetDialog.setContentView(dialogNetworkScanningBinding.getRoot());
                        nAdapter = new NetworkScannerListAdapter(getContext(), devicesList, DashboardFragment.this);
                        dialogNetworkScanningBinding.devicesScanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                        dialogNetworkScanningBinding.devicesScanRecyclerView.setHasFixedSize(true);
                        dialogNetworkScanningBinding.devicesScanRecyclerView.setAdapter(nAdapter);

                        networkScanner = new NetworkScanner(DashboardFragment.this);
                        //first ip, last ip, timeout
                        networkScanner.execute(500);

                        dialogNetworkScanningBinding.stopNetworkScanningButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkScanner.cancel(true);
                            }
                        });

                        dialogNetworkScanningBinding.manualInputNetworkScanningButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), DevicePanelActivity.class);
                                startActivity(i);
                            }
                        });

                        dialogNetworkScanningBinding.reNetworkScanningButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                devicesList.clear();
                                networkScanner = new NetworkScanner(DashboardFragment.this);
                                networkScanner.execute(500);
                            }
                        });

                    }

                });

                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        devicesList.clear();
                    }
                });
            }
        });

    }

    public void insertDevice(String deviceName, String deviceIp, String deviceMac){
        if(devicesList.size()==0)
            dialogNetworkScanningBinding.line.setVisibility(View.VISIBLE);

        devicesList.add(0, new DeviceInNetwork(deviceName, deviceIp, deviceMac));
        nAdapter.notifyItemInserted(0);
//        devicesList.add(0, new Device(deviceName, deviceIp, deviceMac));
//        adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        dialogBinding = null;
    }

    @Override
    public void onDeviceClick(int position) {
        String name = devicesList.get(position).getName();
        String ipAddress = devicesList.get(position).getIpAddress();
        String macAddress = devicesList.get(position).getMacAddress();

        Intent i = new Intent(getActivity(), DevicePanelActivity.class);
        i.putExtra("DEVICE_NAME", name);
        i.putExtra("DEVICE_IP_ADDRESS", ipAddress);
        i.putExtra("DEVICE_MAC_ADDRESS", macAddress);
        startActivity(i);

    }
}

