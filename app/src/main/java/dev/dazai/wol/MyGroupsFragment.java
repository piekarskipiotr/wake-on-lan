package dev.dazai.wol;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.List;
import java.util.Objects;
import dev.dazai.wol.databinding.EditGroupNameBinding;
import dev.dazai.wol.databinding.FragmentMyGroupsBinding;
import dev.dazai.wol.databinding.GroupItemBinding;
import dev.dazai.wol.databinding.MyGroupsNewGroupDialogBinding;

public class MyGroupsFragment extends Fragment implements GroupListAdapter.onClickGroup {
    FragmentMyGroupsBinding binding;
    GroupListAdapter groupAdapter;
    MyGroupsViewModel myGroupsViewModel;
    BottomSheetDialog bottomSheetDialog;
    MyGroupsNewGroupDialogBinding dialogBinding;
    EditGroupNameBinding editGroupNameBinding;
    List<GroupWithDevices> groupWithDevicesList;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupAdapter = new GroupListAdapter(getContext(), MyGroupsFragment.this);
        bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme);
        binding.groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.groupRecyclerView.setHasFixedSize(true);
        binding.groupRecyclerView.setAdapter(groupAdapter);

        myGroupsViewModel = ViewModelProviders.of(this).get(MyGroupsViewModel.class);
        myGroupsViewModel.getAllGroupsAndDevices().observe(getViewLifecycleOwner(), new Observer<List<GroupWithDevices>>() {
            @Override
            public void onChanged(List<GroupWithDevices> groupWithDevices) {
                groupWithDevicesList = groupWithDevices;
                groupAdapter.setGroupWithDevices(groupWithDevices);

            }
        });


        binding.newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBinding = MyGroupsNewGroupDialogBinding.inflate(getLayoutInflater());
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


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                myGroupsViewModel.delete(groupAdapter.getGroup(viewHolder.getAdapterPosition()));
                List<Device> devices = groupWithDevicesList.get(viewHolder.getAdapterPosition()).devices;
                Device d;
                for(int i = 0; i < devices.size(); i++){
                    d = devices.get(i);
                    d.setGroupId(0);
                    myGroupsViewModel.update(d);
                    Toast.makeText(getContext(), "Usunięto grupę!", Toast.LENGTH_SHORT).show();
                }


            }
        }).attachToRecyclerView(binding.groupRecyclerView);

    }

    @Override
    public void onNavigationArrow(GroupItemBinding itemView) {
        if(Objects.equals(itemView.groupCardArrowNavigation.getDrawable().getConstantState(),
                Objects.requireNonNull(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_baseline_arrow, null)).getConstantState())
        ){
            itemView.listOfDevicesContainer.setVisibility(View.GONE);
            itemView.groupCardArrowNavigation.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_baseline_arrow_left, null));

        }else{
            itemView.listOfDevicesContainer.setVisibility(View.VISIBLE);
            itemView.groupCardArrowNavigation.setImageDrawable(ResourcesCompat.getDrawable(requireContext().getResources(), R.drawable.ic_baseline_arrow, null));

        }

    }

    @Override
    public void onLongClickCard(final Group group) {
        editGroupNameBinding = EditGroupNameBinding.inflate(getLayoutInflater());
        bottomSheetDialog.setContentView(editGroupNameBinding.getRoot());
        bottomSheetDialog.show();

        editGroupNameBinding.groupTextInput.setText(group.getGroupName());
        editGroupNameBinding.saveGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editGroupNameBinding.groupTextInput.getText().toString().trim().isEmpty()){
                    Toast.makeText(getContext(), "Pole nie może być puste!", Toast.LENGTH_SHORT).show();

                }else{
                    group.setGroupName(editGroupNameBinding.groupTextInput.getText().toString().trim());
                    myGroupsViewModel.update(group);
                    bottomSheetDialog.dismiss();
                }
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

}