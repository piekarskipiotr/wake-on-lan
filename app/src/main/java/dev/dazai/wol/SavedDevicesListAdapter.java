package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import dev.dazai.wol.databinding.DeviceListItemBinding;

public class SavedDevicesListAdapter extends RecyclerView.Adapter<SavedDevicesListAdapter.MyViewHolder>  {
    private Context mContext;
    private OnMyDeviceListener mOnDeviceListener;

    public SavedDevicesListAdapter(Context context, OnMyDeviceListener onDeviceListener){
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnMyDeviceListener onDeviceListener;

        public MyViewHolder(@NonNull DeviceListItemBinding itemView, OnMyDeviceListener onDeviceListener) {
            super(itemView.getRoot());
            this.onDeviceListener = onDeviceListener;
        }

        @Override
        public void onClick(View v) {
            onDeviceListener.onDeviceClick(getAdapterPosition());
        }
    }

    public interface OnMyDeviceListener{
        void onDeviceClick(int position);

    }
}
