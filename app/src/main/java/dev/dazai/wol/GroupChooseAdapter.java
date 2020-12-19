package dev.dazai.wol;

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
import java.util.List;

import dev.dazai.wol.databinding.ActionGroupChooseDialogBinding;
import dev.dazai.wol.databinding.ChooseGroupItemBinding;
import dev.dazai.wol.databinding.NetworkScannerListItemBinding;

public class GroupChooseAdapter extends RecyclerView.Adapter<GroupChooseAdapter.MyViewHolder> {

    private Context mContext;
    protected List<Group> mGroupList;
    int size = 0;
    private OnGroupListener mOnGroupListener;

    public GroupChooseAdapter(Context context, OnGroupListener onGroupListener){
        mContext = context;
        mOnGroupListener = onGroupListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ChooseGroupItemBinding itemBinding = ChooseGroupItemBinding.inflate(LayoutInflater.from(mContext), parent, false);
        return new MyViewHolder(itemBinding, mOnGroupListener);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Group group = mGroupList.get(position);
        holder.groupName.setText(group.getGroupName());


    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView groupName;
        CardView itemContainer;
        OnGroupListener onGroupListener;

        public MyViewHolder(@NonNull ChooseGroupItemBinding itemBinding, OnGroupListener onGroupListener) {
            super(itemBinding.getRoot());
            itemContainer = itemBinding.groupCard;
            groupName = itemBinding.groupName;
            this.onGroupListener = onGroupListener;

            itemContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onGroupListener.onGroupClick(mGroupList.get(getAdapterPosition()));

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

    public interface OnGroupListener{
        void onGroupClick(Group group);

    }

}
