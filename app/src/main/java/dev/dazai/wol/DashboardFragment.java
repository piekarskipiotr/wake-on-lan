package dev.dazai.wol;

import android.os.Bundle;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        inflater.inflate(R.layout.fragment_dashboard, container, false);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
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
