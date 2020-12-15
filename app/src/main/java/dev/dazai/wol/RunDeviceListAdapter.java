package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.dazai.wol.databinding.RunDeviceDialogItemBinding;

public class RunDeviceListAdapter extends RecyclerView.Adapter<RunDeviceListAdapter.MyViewHolder> {
    private int size = 0;
    private List<Device> mDevicesList;
    private Device mDevice;
    private Context mContext;

    public RunDeviceListAdapter(Context context){
        mContext = context;
    }

    @NonNull
    @Override
    public RunDeviceListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RunDeviceDialogItemBinding binding = RunDeviceDialogItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RunDeviceListAdapter.MyViewHolder holder, int position) {
        if(mDevicesList != null){
            holder.deviceName.setText(mDevicesList.get(position).getDeviceName());
            if(mDevicesList.get(position).getReachable())
                holder.deviceStatus.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_circle, null));
            else
                holder.deviceStatus.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_circle_red, null));

        }else{
            holder.deviceName.setText(mDevice.getDeviceName());
            if(mDevice.getReachable())
                holder.deviceStatus.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_circle, null));
            else
                holder.deviceStatus.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), R.drawable.ic_circle_red, null));

        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        ImageView deviceStatus;
        public MyViewHolder(@NonNull RunDeviceDialogItemBinding itemView) {
            super(itemView.getRoot());
            deviceName = itemView.deviceName;
            deviceStatus = itemView.statusIcon;


        }
    }

    public void setDevices(List<Device> devices){
        mDevicesList = devices;
        size = devices.size();
        notifyDataSetChanged();
    }

    public void setDevice(Device device){
        mDevice = device;
        size = 1;
        notifyDataSetChanged();
    }
}
