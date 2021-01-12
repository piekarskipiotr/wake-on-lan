package dev.dazai.wol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dev.dazai.wol.data.Device;
import dev.dazai.wol.R;
import dev.dazai.wol.databinding.DeviceInGroupCardBinding;

public class DeviceListInGroupAdapter extends RecyclerView.Adapter<DeviceListInGroupAdapter.MyViewHolder>{
    private Context mContext;
    protected List<Device> mDevicesList;
    private onDeviceClick mOnDeviceListener;
    private int size = 0;

    public DeviceListInGroupAdapter(Context context, onDeviceClick onDeviceListener){
        mContext = context;
        mOnDeviceListener = onDeviceListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceInGroupCardBinding itemBinding = DeviceInGroupCardBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnDeviceListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Device device = mDevicesList.get(position);
        if(device.getDeviceIcon() == null)
            holder.name.setText(device.getDeviceName());
        else
            holder.name.setText(device.getDeviceIcon() + device.getDeviceName());

        if(device.getReachable())
            holder.statusIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_circle));
        else
            holder.statusIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_circle_red));

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        onDeviceClick onDeviceListener;
        TextView name;
        ImageView statusIcon;
        CardView itemContainer;
        public MyViewHolder(@NonNull DeviceInGroupCardBinding itemView, onDeviceClick onDeviceListener) {
            super(itemView.getRoot());
            name = itemView.deviceCardName;
            statusIcon = itemView.statusIcon;
            itemContainer = itemView.itemContainer;

            this.onDeviceListener = onDeviceListener;
            itemContainer.setOnClickListener(this);
            itemContainer.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onDeviceListener.onDeviceCardClick(mDevicesList.get(getAdapterPosition()));

        }


        @Override
        public boolean onLongClick(View v) {
            onDeviceListener.onDeviceCardLongClick(mDevicesList.get(getAdapterPosition()));
            return true;
        }
    }

    public void setDevicesInGroup(List<Device> devices){
        mDevicesList = devices;
        size = mDevicesList.size();
        notifyDataSetChanged();

    }

    public interface onDeviceClick{
        void onDeviceCardClick(Device device);
        void onDeviceCardLongClick(Device device);
    }
}
