package dev.dazai.wol;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import dev.dazai.wol.databinding.FragmentMyGroupsBinding;

public class MyGroupsFragment extends Fragment implements GroupListAdapter.onNavigationArrowClick, DeviceListInGroupAdapter.onDeviceClick{
    FragmentMyGroupsBinding binding;
    GroupListAdapter adapter;
    MyGroupsViewModel myGroupsViewModel;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new GroupListAdapter(getContext(), MyGroupsFragment.this);
        binding.groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.groupRecyclerView.setHasFixedSize(true);
        binding.groupRecyclerView.setAdapter(adapter);

        myGroupsViewModel = ViewModelProviders.of(this).get(MyGroupsViewModel.class);
        Group x = new Group();
        x.setGroupId(1);
        x.setGroupName("Moja pierwsza grupa");
        myGroupsViewModel.insert(x);
        myGroupsViewModel.getAllGroups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                adapter.setGroups(groups);

            }
        });

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyGroupsBinding.inflate(getLayoutInflater());
        return binding.getRoot();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onNavigationArrow(Group group, GroupItemBinding itemView) {
        itemView.listOfDevicesContainer.setVisibility(View.VISIBLE);
        itemView.DevicesInGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        itemView.DevicesInGroupRecyclerView.setHasFixedSize(true);
        final DeviceListInGroupAdapter dadapter = new DeviceListInGroupAdapter(getContext(), MyGroupsFragment.this);
        myGroupsViewModel.getDevicesByGroupId(1).observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                dadapter.setDevicesInGroup(devices);
            }
        });
        itemView.DevicesInGroupRecyclerView.setAdapter(dadapter);

        Toast.makeText(getContext(), group.getGroupName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeviceCardClick(Device device) {

    }
}