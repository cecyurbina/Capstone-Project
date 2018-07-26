package com.udacity.surbi.listnow.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ItemList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    private List<Item> mDataset;
    private OnItemSelectedListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cl_item_check_list)
        View viewItem;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.iv_upload_image)
        ImageView ivUploadImage;
        @BindView(R.id.tv_reject)
        TextView tvReject;
        @BindView(R.id.checkBox)
        TextView cbCheck;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public CheckListAdapter(List<Item> myDataset, OnItemSelectedListener onItemSelectedListener) {
        mDataset = myDataset;
        listener = onItemSelectedListener;
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
        Context context = holder.viewItem.getContext();
        holder.tvName.setText(currentItem.getName());

        //quantity
        if (currentItem.getQuantity() != null) {
            holder.tvQuantity.setVisibility(View.VISIBLE);
            holder.tvQuantity.setText(String.valueOf(currentItem.getQuantity()));
        } else {
            holder.tvQuantity.setVisibility(View.GONE);
        }

        //unit
        if (currentItem.getUnit() != null && !currentItem.getUnit().equals("")) {
            holder.tvUnit.setVisibility(View.VISIBLE);
            holder.tvUnit.setText(currentItem.getUnit());
        } else {
            holder.tvUnit.setVisibility(View.GONE);
        }

        //image
        if (currentItem.getImage()) {
            holder.ivUploadImage.setVisibility(View.VISIBLE);
            holder.ivUploadImage.setImageResource(R.drawable.circle_background);
        } else {
            holder.ivUploadImage.setVisibility(View.GONE);
        }

        //reject
        if (currentItem.isRejected()) {
            holder.tvReject.setVisibility(View.VISIBLE);
            holder.tvReject.setText(context.getString(R.string.check_list_reject));
        } else {
            holder.tvReject.setVisibility(View.GONE);
        }

        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectedItem(currentItem, holder.viewItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemSelectedListener {
        void onSelectedItem(Item item, View view);
    }
}
