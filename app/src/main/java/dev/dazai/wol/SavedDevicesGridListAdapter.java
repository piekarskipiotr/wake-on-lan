package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.dazai.wol.databinding.DeviceGridListItemBinding;


public class SavedDevicesGridListAdapter extends RecyclerView.Adapter<SavedDevicesGridListAdapter.MyViewHolder>  {
    private Context mContext;
    protected List<Device> mDevicesList;
    private onDeviceClick mOnDeviceListener;
    private int size = 0;

    public SavedDevicesGridListAdapter(Context context, onDeviceClick onDeviceListener){
        mContext = context;
        mOnDeviceListener = onDeviceListener;

    }

    @NonNull
    @Override
    public SavedDevicesGridListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        DeviceGridListItemBinding itemBinding = DeviceGridListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnDeviceListener);


    }

    @Override
    public void onBindViewHolder(@NonNull SavedDevicesGridListAdapter.MyViewHolder holder, int position) {
        Device device = mDevicesList.get(position);
        holder.name.setText(device.getDeviceName());
        holder.icon.setText(device.getDeviceIcon());



    }

    @Override
    public int getItemCount() {
        return size;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        onDeviceClick onDeviceListener;
        TextView name, icon;
        CardView itemContainer;
        public MyViewHolder(@NonNull DeviceGridListItemBinding itemView, onDeviceClick onDeviceListener) {
            super(itemView.getRoot());
            name = itemView.name;
            icon = itemView.icon;
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

    public void setSavedDevices(List<Device> devices){
        mDevicesList = devices;
        size = mDevicesList.size();
        notifyDataSetChanged();

    }

    public interface onDeviceClick{
        void onDeviceCardClick(Device device);
        void onDeviceCardLongClick(Device device);

    }

}
