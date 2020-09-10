package dev.dazai.wol;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import dev.dazai.wol.databinding.NetworkScannerListItemBinding;

public class NetworkScannerListAdapter extends RecyclerView.Adapter<NetworkScannerListAdapter.MyViewHolder> {

    private ArrayList<Device> devicesList;

    public NetworkScannerListAdapter(ArrayList<Device> devices){
        devicesList = devices;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView headerText, secondaryText;

        public MyViewHolder(@NonNull NetworkScannerListItemBinding itemBinding) {
            super(itemBinding.getRoot());
            headerText = itemBinding.headerText;
            secondaryText = itemBinding.secondaryText;


        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NetworkScannerListItemBinding binding = NetworkScannerListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Device device = devicesList.get(position);
        holder.headerText.setText(device.getName());
        holder.secondaryText.setText("(" + device.getIpAddress() + ")");

    }

    @Override
    public int getItemCount() {
        return devicesList.size();

    }


}
