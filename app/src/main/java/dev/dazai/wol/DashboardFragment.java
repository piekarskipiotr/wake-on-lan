package dev.dazai.wol;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.dazai.wol.databinding.ActivityMainBinding;
import dev.dazai.wol.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    FragmentDashboardBinding binding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


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
