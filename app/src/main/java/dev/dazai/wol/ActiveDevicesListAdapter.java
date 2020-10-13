package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import dev.dazai.wol.databinding.ActiveDeviceListItemBinding;

public class ActiveDevicesListAdapter extends RecyclerView.Adapter<ActiveDevicesListAdapter.MyViewHolder>  {
    private Context mContext;
    protected List<Device> mActiveDevicesList;
    private OnMyActiveDeviceListener mOnActiveDeviceListener;

    public ActiveDevicesListAdapter(Context context, List<Device> devicesList, OnMyActiveDeviceListener onDeviceListener){
        mContext = context;
        mActiveDevicesList = devicesList;
        mOnActiveDeviceListener = onDeviceListener;

    }

    @NonNull
    @Override
    public ActiveDevicesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ActiveDeviceListItemBinding itemBinding = ActiveDeviceListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnActiveDeviceListener);


    }

    @Override
    public void onBindViewHolder(@NonNull ActiveDevicesListAdapter.MyViewHolder holder, int position) {
        Device device = mActiveDevicesList.get(position);
        holder.name.setText(device.getDeviceName());
        holder.icon.setText(device.getDeviceIcon());

    }

    @Override
    public int getItemCount() {
        return mActiveDevicesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnMyActiveDeviceListener onDeviceListener;
        TextView name, icon;
        public MyViewHolder(@NonNull ActiveDeviceListItemBinding itemView, OnMyActiveDeviceListener onDeviceListener) {
            super(itemView.getRoot());
            name = itemView.name;
            icon = itemView.icon;
            this.onDeviceListener = onDeviceListener;
        }

        @Override
        public void onClick(View v) {
            onDeviceListener.onActiveDeviceClick(getAdapterPosition());
        }
    }

    public interface OnMyActiveDeviceListener{
        void onActiveDeviceClick(int position);

    }


}
