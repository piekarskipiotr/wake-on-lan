package dev.dazai.wol;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.Objects;

import dev.dazai.wol.databinding.ActivityMainBinding;
import dev.dazai.wol.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    FragmentDashboardBinding binding;
    BottomSheetDialog bottomSheetDialog;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.newDeviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()), R.style.BottomSheetDialogTheme);
                View bottomResultSheetView = LayoutInflater.from(getActivity()).inflate(
                        R.layout.dashboard_new_device_dialog, (LinearLayout) Objects.requireNonNull(getView()).findViewById(R.id.dialog_container));
                bottomSheetDialog.setContentView(bottomResultSheetView);
                bottomSheetDialog.show();
            }
        });


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
    }
}

//        NetworkScanner networkScanner = new NetworkScanner(getApplicationContext());
//        networkScanner.delegate = this;
//        //first ip, last ip, timeout
//        networkScanner.execute(0, 50, 200);
