package dev.dazai.wol;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dev.dazai.wol.databinding.GroupItemBinding;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder> implements DeviceListInGroupAdapter.onDeviceClick{
    private Context mContext;
    protected List<GroupWithDevices> mGroupWithDevicesList;
    private int size = 0;
    private onClickGroup mOnClickGroup;
    private GroupItemBinding itemBinding;
    DeviceListInGroupAdapter deviceAdapter;

    public GroupListAdapter(Context context, onClickGroup onClickGroup){
        mContext = context;
        mOnClickGroup = onClickGroup;

    }

    @NonNull
    @Override
    public GroupListAdapter.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        itemBinding = GroupItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        itemBinding.DevicesInGroupRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        itemBinding.DevicesInGroupRecyclerView.setHasFixedSize(true);
        return new MyViewHolder(itemBinding, mOnClickGroup);

    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.MyViewHolder holder, int position) {
        Group group = mGroupWithDevicesList.get(position).group;
        final List<Device> deviceList = mGroupWithDevicesList.get(position).devices;
        deviceAdapter = new DeviceListInGroupAdapter(mContext, GroupListAdapter.this);
        holder.devicesInGroupRecyclerView.setAdapter(deviceAdapter);

        holder.groupName.setText(group.getGroupName());
        if(deviceList != null){
            deviceAdapter.setDevicesInGroup(deviceList);
            holder.runAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new RunDeviceDialog(deviceList).show(((FragmentActivity) mContext).getSupportFragmentManager(), "RunDeviceDialog");
                }
            });
        }


        holder.runAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MagicPacket magicPacket = new MagicPacket();
                for(int i = 0; i < deviceList.size(); i++){
                    String deviceMac = deviceList.get(i).getDeviceMacAddress();
                    int devicePort = Integer.parseInt(deviceList.get(i).getDeviceLanPort());
                    magicPacket.send(deviceMac, devicePort, mContext);

                }
            }
        });


    }

    //onDevice from DeviceListInGroupAdapter
    @Override
    public void onDeviceCardClick(Device device) {
        Intent i = new Intent(mContext, DevicePanelActivity.class);
        i.putExtra("ID", device.getDeviceId());
        mContext.startActivity(i);

    }

    @Override
    public void onDeviceCardLongClick(Device device) {
        new RunDeviceDialog(device).show(((FragmentActivity)mContext).getSupportFragmentManager(), "RunDeviceDialog");

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        onClickGroup onClickGroup;
        CardView groupCard, runAll;
        TextView groupName;
        GroupItemBinding mItemView;
        ImageView arrowButton;
        LinearLayout devicesContainer;
        RecyclerView devicesInGroupRecyclerView;
        public MyViewHolder(@NonNull GroupItemBinding itemView, onClickGroup onClickGroup) {
            super(itemView.getRoot());
            groupCard = itemView.itemContainer;
            groupName = itemView.groupCardName;
            arrowButton = itemView.groupCardArrowNavigation;
            devicesContainer = itemView.listOfDevicesContainer;
            devicesInGroupRecyclerView = itemView.DevicesInGroupRecyclerView;
            runAll = itemView.runAllGroupDevicesButton;
            mItemView = itemView;
            this.onClickGroup = onClickGroup;
            arrowButton.setOnClickListener(this);
            groupCard.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onClickGroup.onNavigationArrow(mItemView);

        }

        @Override
        public boolean onLongClick(View v) {
            onClickGroup.onLongClickCard(mGroupWithDevicesList.get(getAdapterPosition()).group);
            return true;
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

    public interface onClickGroup {
        void onNavigationArrow(GroupItemBinding itemView);
        void onLongClickCard(Group group);

    }

}
