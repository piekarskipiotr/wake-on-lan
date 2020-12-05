package dev.dazai.wol;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dev.dazai.wol.databinding.FragmentMyGroupsBinding;

public class MyGroupsFragment extends Fragment implements GroupListAdapter.onNavigationArrowClick{
    FragmentMyGroupsBinding binding;
    GroupListAdapter adapter;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new GroupListAdapter(getContext(), MyGroupsFragment.this);
        binding.groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.groupRecyclerView.setHasFixedSize(true);
        binding.groupRecyclerView.setAdapter(adapter);



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
    public void onNavigationArrow(Group group) {

    }
}