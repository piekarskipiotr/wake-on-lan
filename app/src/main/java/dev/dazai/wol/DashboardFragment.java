package dev.dazai.wol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
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

public class DashboardFragment extends Fragment implements NetworkScannerListAdapter.OnDeviceListener, SavedDevicesListAdapter.OnMyDeviceListener, ActiveDevicesListAdapter.onDeviceClick {
    ArrayList<DeviceInNetwork> devicesInNetworkList = new ArrayList<>();
    NetworkScanner networkScanner;
    BottomSheetDialog bottomSheetDialog;
    FragmentDashboardBinding binding;
    DashboardNewDeviceDialogBinding dialogBinding;
    DialogNetworkScanningBinding dialogNetworkScanningBinding;
    NetworkScannerListAdapter nAdapter;
    SavedDevicesListAdapter sAdapter;
    ActiveDevicesListAdapter aAdapter;
    DeviceDatabase deviceDatabase;
    private DeviceViewModel deviceViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceDatabase = DeviceDatabase.getInstance(getContext());
        deviceViewModel = ViewModelProviders.of(getActivity()).get(DeviceViewModel.class);
//        deviceViewModel = ViewModelProvider(DashboardFragment.class).get(DeviceViewModel.class);
        deviceViewModel.getSavedDevices().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                sAdapter.setSavedDevices(devices);

            }
        });

        deviceViewModel.getActiveDevices().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                aAdapter.setActiveDevices(devices);

            }
        });


        setSavedDevicesAdapter();
        setActiveDevicesAdapter();

        binding.newDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding = DashboardNewDeviceDialogBinding.inflate(getLayoutInflater());

                bottomSheetDialog = new BottomSheetDialog(requireActivity(), R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(dialogBinding.getRoot());
                bottomSheetDialog.show();


                dialogBinding.manualInputButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.dismiss();
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
                        nAdapter = new NetworkScannerListAdapter(getContext(), devicesInNetworkList, DashboardFragment.this);
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
                                bottomSheetDialog.dismiss();
                                Intent i = new Intent(getActivity(), DevicePanelActivity.class);
                                startActivity(i);
                            }
                        });

                        dialogNetworkScanningBinding.reNetworkScanningButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                devicesInNetworkList.clear();
                                networkScanner = new NetworkScanner(DashboardFragment.this);
                                networkScanner.execute(500);
                            }
                        });

                    }

                });

                bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        devicesInNetworkList.clear();
                    }
                });
            }
        });

    }

    public void insertDevice(String deviceName, String deviceIp, String deviceMac){
        if(devicesInNetworkList.size()==0)
            dialogNetworkScanningBinding.line.setVisibility(View.VISIBLE);

        devicesInNetworkList.add(0, new DeviceInNetwork(deviceName, deviceIp, deviceMac));
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

    private void setSavedDevicesAdapter(){
        sAdapter = new SavedDevicesListAdapter(getContext(), DashboardFragment.this);
        binding.devicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.devicesRecyclerView.setHasFixedSize(true);
        binding.devicesRecyclerView.setAdapter(sAdapter);

    }

    private void setActiveDevicesAdapter(){
        aAdapter = new ActiveDevicesListAdapter(getContext(), DashboardFragment.this);
        binding.activeDevicesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.activeDevicesRecyclerView.setHasFixedSize(true);
        binding.activeDevicesRecyclerView.setAdapter(aAdapter);

    }

    @Override
    public void onNewDeviceClick(int position) {
        String name = devicesInNetworkList.get(position).getName();
        String ipAddress = devicesInNetworkList.get(position).getIpAddress();
        String macAddress = devicesInNetworkList.get(position).getMacAddress();

        bottomSheetDialog.dismiss();
        Intent i = new Intent(getActivity(), DevicePanelActivity.class);
        i.putExtra("DEVICE_NAME", name);
        i.putExtra("DEVICE_IP_ADDRESS", ipAddress);
        i.putExtra("DEVICE_MAC_ADDRESS", macAddress);
        startActivity(i);
    }

    @Override
    public void onDeviceClick(int position) {
        Intent i = new Intent(getActivity(), DevicePanelActivity.class);
        i.putExtra("ID", deviceDatabase.deviceDao().getAll().getValue().get(position).getDeviceId());
        startActivity(i);

    }



}