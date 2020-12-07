package dev.dazai.wol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import dev.dazai.wol.databinding.GroupItemBinding;

public class GroupListAdapter extends RecyclerView.Adapter<GroupListAdapter.MyViewHolder>{
    private Context mContext;
    protected List<Group> mGroupList;
    private int size = 0;
    private onNavigationArrowClick mOnNavigationArrowClick;
    private GroupItemBinding itemBinding;

    public GroupListAdapter(Context context, onNavigationArrowClick onNavigationArrowClick){
        mContext = context;
        mOnNavigationArrowClick = onNavigationArrowClick;


    }

    @NonNull
    @Override
    public GroupListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemBinding = GroupItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnNavigationArrowClick);

    }

    @Override
    public void onBindViewHolder(@NonNull GroupListAdapter.MyViewHolder holder, int position) {
        Group group = mGroupList.get(position);
        holder.groupName.setText(group.getGroupName());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        onNavigationArrowClick onNavigationArrowClick;
        TextView groupName;
        ImageView arrowButton;
        LinearLayout devicesContainer;
        public MyViewHolder(@NonNull GroupItemBinding itemView, onNavigationArrowClick onNavigationArrowClick) {
            super(itemView.getRoot());
            groupName = itemView.groupCardName;
            arrowButton = itemView.groupCardArrowNavigation;
            devicesContainer = itemView.listOfDevicesContainer;
            this.onNavigationArrowClick = onNavigationArrowClick;
            arrowButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onNavigationArrowClick.onNavigationArrow(mGroupList.get(getAdapterPosition()), itemBinding);

        }
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public void setGroups(List<Group> groups){
        mGroupList = groups;
        size = mGroupList.size();
        notifyDataSetChanged();

    }

    public interface onNavigationArrowClick{
        void onNavigationArrow(Group group, GroupItemBinding itemView);

    }


}
