package dev.dazai.wol;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
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
    GroupListAdapter groupAdapter;
    DeviceListInGroupAdapter deviceAdapter;
    MyGroupsViewModel myGroupsViewModel;
    BottomSheetDialog bottomSheetDialog;
    MyGroupsNewGroupDialogBinding dialogBinding;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupAdapter = new GroupListAdapter(getContext(), MyGroupsFragment.this);
        deviceAdapter = new DeviceListInGroupAdapter(getContext(), MyGroupsFragment.this);

        binding.groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.groupRecyclerView.setHasFixedSize(true);
        binding.groupRecyclerView.setAdapter(groupAdapter);

        myGroupsViewModel = ViewModelProviders.of(this).get(MyGroupsViewModel.class);
        Group x = new Group();
        x.setGroupId(1);
        x.setGroupName("Moja pierwsza grupa");
        myGroupsViewModel.insert(x);
        myGroupsViewModel.getAllGroups().observe(getViewLifecycleOwner(), new Observer<List<Group>>() {
            @Override
            public void onChanged(List<Group> groups) {
                groupAdapter.setGroups(groups);

            }
        });


        binding.newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding = MyGroupsNewGroupDialogBinding.inflate(getLayoutInflater());
                bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
                bottomSheetDialog.setContentView(dialogBinding.getRoot());
                bottomSheetDialog.show();

                dialogBinding.addGroup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(Objects.requireNonNull(dialogBinding.groupTextInput.getText()).toString().isEmpty()){
                            Toast.makeText(getContext(), "Pole nie może być puste!", Toast.LENGTH_SHORT).show();
                        }else{
                            Group nGroup = new Group();
                            nGroup.setGroupName(dialogBinding.groupTextInput.getText().toString().trim());
                            myGroupsViewModel.insert(nGroup);
                            bottomSheetDialog.dismiss();
                        }

                    }
                });
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
        if(Objects.equals(itemView.groupCardArrowNavigation.getDrawable().getConstantState(),
                Objects.requireNonNull(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_baseline_arrow, null)).getConstantState())
        ){
            itemView.listOfDevicesContainer.setVisibility(View.GONE);
            itemView.groupCardArrowNavigation.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_baseline_arrow_left, null));

        }else{
            itemView.listOfDevicesContainer.setVisibility(View.VISIBLE);
            itemView.groupCardArrowNavigation.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_baseline_arrow, null));

        }

        itemView.DevicesInGroupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        itemView.DevicesInGroupRecyclerView.setHasFixedSize(true);
        itemView.DevicesInGroupRecyclerView.setAdapter(deviceAdapter);

        myGroupsViewModel.getDevicesByGroupId(group.getGroupId()).observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                deviceAdapter.setDevicesInGroup(devices);

            }
        });

        myGroupsViewModel.getDevicesByGroupId(group.getGroupId()).observe(getViewLifecycleOwner(), new Observer<List<Device>>() {
            @Override
            public void onChanged(List<Device> devices) {
                deviceAdapter.setDevicesInGroup(devices);

            }
        });
        itemView.DevicesInGroupRecyclerView.setAdapter(dadapter);

        Toast.makeText(getContext(), group.getGroupName(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDeviceCardClick(Device device) {

    }
}