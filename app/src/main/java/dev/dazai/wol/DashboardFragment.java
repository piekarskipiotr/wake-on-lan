package dev.dazai.wol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;

import dev.dazai.wol.databinding.DashboardNewDeviceDialogBinding;
import dev.dazai.wol.databinding.DialogNetworkScanningBinding;
import dev.dazai.wol.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment implements NetworkScannerListAdapter.OnDeviceListener, SavedDevicesListAdapter.onDeviceClick, ActiveDevicesListAdapter.onDeviceClick{
    ArrayList<DeviceInNetwork> devicesInNetworkList = new ArrayList<>();
    List<Device> allDevices;
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
    private DeviceReachableHandler deviceReachableHandler;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        deviceDatabase = DeviceDatabase.getInstance(getContext());
        setSavedDevicesAdapter();
        setActiveDevicesAdapter();

        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        //deviceViewModel = ViewModelProvider(requireActivity()).get(DeviceViewModel.class);

        deviceViewModel.getAllDevices().observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                if(devices != null){
                    allDevices = devices;
                    runReachableCheck.run();

                }

            }
        });

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

        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                runReachableCheck.run();
                binding.swipeRefresh.setRefreshing(false);

            }
        });

        binding.listOfAllActiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListOfDevicesActivity.class);
                i.putExtra("STATE", true);
                startActivity(i);


            }
        });

        binding.listOfAllSavedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListOfDevicesActivity.class);
                i.putExtra("STATE", false);
                startActivity(i);
            }
        });


    }

    private Runnable runReachableCheck = new Runnable() {
        @Override
        public void run() {
            deviceReachableHandler = new DeviceReachableHandler(DashboardFragment.this, allDevices);
            deviceReachableHandler.execute();

        }
    };

    public void insertDevice(String deviceName, String deviceIp, String deviceMac){
        if(devicesInNetworkList.size()==0)
            dialogNetworkScanningBinding.line.setVisibility(View.VISIBLE);

        devicesInNetworkList.add(0, new DeviceInNetwork(deviceName, deviceIp, deviceMac));
        nAdapter.notifyItemInserted(0);

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
    public void onDeviceCardClick(Device device) {
        Intent i = new Intent(getActivity(), DevicePanelActivity.class);
        i.putExtra("ID", device.getDeviceId());
        startActivity(i);

    }

    public void updateDeviceReachableStatus(Device device){
        deviceViewModel.update(device);

    }

}