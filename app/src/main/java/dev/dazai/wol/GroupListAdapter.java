package dev.dazai.wol;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dev.dazai.wol.databinding.GroupItemBinding;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> implements DeviceListInGroupAdapter.onDeviceClick{
    private Context mContext;
    protected List<GroupWithDevices> mGroupWithDevicesList;
    private int size = 0;
    private onNavigationArrowClick mOnNavigationArrowClick;
    private GroupItemBinding itemBinding;
    DeviceListInGroupAdapter deviceAdapter;

    public GroupListAdapter(Context context, onNavigationArrowClick onNavigationArrowClick){
        mContext = context;
        mOnNavigationArrowClick = onNavigationArrowClick;

    }

    @NonNull
    @Override
    public GroupListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemBinding = GroupItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        itemBinding.DevicesInGroupRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        itemBinding.DevicesInGroupRecyclerView.setHasFixedSize(true);

        return new MyViewHolder(itemBinding, mOnNavigationArrowClick);

    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.MyViewHolder holder, int position) {
        Group group = mGroupWithDevicesList.get(position).group;
        List<Device> deviceList = mGroupWithDevicesList.get(position).devices;
        deviceAdapter = new DeviceListInGroupAdapter(mContext, GroupListAdapter.this);
        itemBinding.DevicesInGroupRecyclerView.setAdapter(deviceAdapter);

        holder.groupName.setText(group.getGroupName());
        if(deviceList != null)
            deviceAdapter.setDevicesInGroup(deviceList);

    }

    //onDevice from DeviceListInGroupAdapter
    @Override
    public void onDeviceCardClick(Device device) {
        Intent i = new Intent(mContext, DevicePanelActivity.class);
        i.putExtra("ID", device.getDeviceId());
        mContext.startActivity(i);

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        onNavigationArrowClick onNavigationArrowClick;
        TextView groupName;
        GroupItemBinding mItemView;
        ImageView arrowButton;
        LinearLayout devicesContainer;
        public MyViewHolder(@NonNull GroupItemBinding itemView, onNavigationArrowClick onNavigationArrowClick) {
            super(itemView.getRoot());
            groupName = itemView.groupCardName;
            arrowButton = itemView.groupCardArrowNavigation;
            devicesContainer = itemView.listOfDevicesContainer;
            mItemView = itemView;
            this.onNavigationArrowClick = onNavigationArrowClick;
            arrowButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNavigationArrowClick.onNavigationArrow(mItemView);

        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public Group getGroup(int position){
        return mGroupWithDevicesList.get(position).group;

    }

    public void setGroupWithDevices(List<GroupWithDevices> groupWithDevicesList){
        mGroupWithDevicesList = groupWithDevicesList;
        size = groupWithDevicesList.size();
        notifyDataSetChanged();
    }

    public interface onNavigationArrowClick{
        void onNavigationArrow(GroupItemBinding itemView);

    }


}
