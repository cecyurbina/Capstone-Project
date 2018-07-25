package com.udacity.surbi.listnow.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.data.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    private List<Item> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cl_item_check_list)
        View viewItem;
        @BindView(R.id.tv_name)
        TextView tvName;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public CheckListAdapter(List<Item> myDataset) {
        mDataset = myDataset;
    }

    @NonNull
    @Override
    public CheckListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_check_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Item currentItem = mDataset.get(holder.getAdapterPosition());
        holder.tvName.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}
