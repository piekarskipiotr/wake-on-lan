package dev.dazai.wol.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import dev.dazai.wol.R;
import dev.dazai.wol.databinding.NetworkScannerListItemBinding;
import dev.dazai.wol.network.DeviceInNetwork;

public class NetworkScannerListAdapter extends RecyclerView.Adapter<NetworkScannerListAdapter.MyViewHolder> {

    private Context mContext;
    protected ArrayList<DeviceInNetwork> devicesList;
    private OnDeviceListener mOnDeviceListener;


    public NetworkScannerListAdapter(Context context, ArrayList<DeviceInNetwork> deviceInNetworks, OnDeviceListener onDeviceListener){
        mContext = context;
        devicesList = deviceInNetworks;
        mOnDeviceListener = onDeviceListener;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headerText, secondaryText;
        CardView itemContainer;
        OnDeviceListener onDeviceListener;

        public MyViewHolder(@NonNull NetworkScannerListItemBinding itemBinding, OnDeviceListener onDeviceListener) {
            super(itemBinding.getRoot());
            itemContainer = itemBinding.itemContainer;
            headerText = itemBinding.headerText;
            secondaryText = itemBinding.secondaryText;
            this.onDeviceListener = onDeviceListener;

            itemContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onDeviceListener.onNewDeviceClick(getAdapterPosition());

        }
    }

    public interface OnDeviceListener{
        void onNewDeviceClick(int position);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NetworkScannerListItemBinding itemBinding = NetworkScannerListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnDeviceListener);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DeviceInNetwork deviceInNetwork = devicesList.get(position);
        holder.itemContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale));
        holder.headerText.setText(deviceInNetwork.getName());
        holder.secondaryText.setText("(" + deviceInNetwork.getIpAddress() + ")");



    }

    @Override
    public int getItemCount() {
        return devicesList.size();

    }


}
