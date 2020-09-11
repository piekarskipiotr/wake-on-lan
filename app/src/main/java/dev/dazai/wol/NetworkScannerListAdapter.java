package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import dev.dazai.wol.databinding.NetworkScannerListItemBinding;

public class NetworkScannerListAdapter extends RecyclerView.Adapter<NetworkScannerListAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<Device> devicesList;


    public NetworkScannerListAdapter(Context context, ArrayList<Device> devices){
        mContext = context;
        devicesList = devices;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView headerText, secondaryText;
        private CardView itemContainer;

        public MyViewHolder(@NonNull NetworkScannerListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            itemContainer = itemBinding.itemContainer;
            headerText = itemBinding.headerText;
            secondaryText = itemBinding.secondaryText;


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NetworkScannerListItemBinding binding = NetworkScannerListItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Device device = devicesList.get(position);
        holder.itemContainer.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_scale));
        holder.headerText.setText(device.getName());
        holder.secondaryText.setText("(" + device.getIpAddress() + ")");



    }

    @Override
    public int getItemCount() {
        return devicesList.size();

    }


}
