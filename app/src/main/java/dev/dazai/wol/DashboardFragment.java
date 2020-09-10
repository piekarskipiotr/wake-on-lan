package dev.dazai.wol;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.Objects;
import dev.dazai.wol.databinding.DashboardNewDeviceDialogBinding;
import dev.dazai.wol.databinding.DialogNetworkScanningBinding;
import dev.dazai.wol.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment{
    ArrayList<Device> devicesList = new ArrayList<>();
    NetworkScanner networkScanner;
    BottomSheetDialog bottomSheetDialog;
    FragmentDashboardBinding binding;
    DashboardNewDeviceDialogBinding dialogBinding;
    DialogNetworkScanningBinding dialogNetworkScanningBinding;
    NetworkScannerListAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.newDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding = DashboardNewDeviceDialogBinding.inflate(getLayoutInflater());

                bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()), R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(dialogBinding.getRoot());
                bottomSheetDialog.show();

                dialogBinding.networkScanningButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialogBinding.getRoot().removeAllViews();

                        dialogNetworkScanningBinding = DialogNetworkScanningBinding.inflate(getLayoutInflater());
                        bottomSheetDialog.setContentView(dialogNetworkScanningBinding.getRoot());
                        adapter = new NetworkScannerListAdapter(devicesList);
                        dialogNetworkScanningBinding.devicesScanRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        dialogNetworkScanningBinding.devicesScanRecyclerView.setHasFixedSize(true);
                        dialogNetworkScanningBinding.devicesScanRecyclerView.setAdapter(adapter);

                        networkScanner = new NetworkScanner(DashboardFragment.this);
                        //first ip, last ip, timeout
                        networkScanner.execute(500);

                        dialogNetworkScanningBinding.stopNetworkScanningButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                networkScanner.cancel(true);
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

            }
        });

    }

    public void insertDevice(String deviceName, String deviceIp, String deviceMac){
        devicesList.add(new Device(deviceName, deviceIp, deviceMac));
        adapter.notifyDataSetChanged();

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

}

