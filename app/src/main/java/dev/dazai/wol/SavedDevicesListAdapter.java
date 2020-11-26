package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import dev.dazai.wol.databinding.DeviceListItemBinding;

public class SavedDevicesListAdapter extends RecyclerView.Adapter<SavedDevicesListAdapter.MyViewHolder>  {
    private Context mContext;
    protected List<Device> mDevicesList;
    private onDeviceClick mOnDeviceListener;
    private int size = 0;

    public SavedDevicesListAdapter(Context context, onDeviceClick onDeviceListener){
        mContext = context;
        mOnDeviceListener = onDeviceListener;

    }

    @NonNull
    @Override
    public SavedDevicesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceListItemBinding itemBinding = DeviceListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnDeviceListener);


    }

    @Override
    public void onBindViewHolder(@NonNull SavedDevicesListAdapter.MyViewHolder holder, int position) {
        Device device = mDevicesList.get(position);
        holder.name.setText(device.getDeviceName());
        holder.icon.setText(device.getDeviceIcon());

    }

    @Override
    public int getItemCount() {
        return size;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        onDeviceClick onDeviceListener;
        TextView name, icon;
        CardView itemContainer;
        public MyViewHolder(@NonNull DeviceListItemBinding itemView, onDeviceClick onDeviceListener) {
            super(itemView.getRoot());
            name = itemView.name;
            icon = itemView.icon;
            itemContainer = itemView.itemContainer;
            this.onDeviceListener = onDeviceListener;

            itemContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onDeviceListener.onDeviceCardClick(mDevicesList.get(getAdapterPosition()));
        }
    }

    public void setSavedDevices(List<Device> devices){
        mDevicesList = devices;
        size = mDevicesList.size();
        notifyDataSetChanged();

    }

    public interface onDeviceClick{
        void onDeviceCardClick(Device device);

    }

}
