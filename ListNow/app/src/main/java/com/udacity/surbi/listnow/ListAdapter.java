package com.udacity.surbi.listnow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<ItemList> mDataset;
    private OnItemSelectedListener listener;

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.cl_item_list)
        View viewItem;
        @BindView(R.id.tv_title)
        TextView tvTile;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.ib_more)
        ImageButton ibImage;
        @BindView(R.id.iv_fav)
        ImageView ivFav;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }

    public ListAdapter(List<ItemList> myDataset, OnItemSelectedListener onItemSelectedListener) {
        mDataset = myDataset;
        listener = onItemSelectedListener;
    }

    @NonNull
    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final ItemList currentItem = mDataset.get(holder.getAdapterPosition());
        Context context = holder.itemView.getContext();
        holder.tvTile.setText(currentItem.getTitle());
        String status = (currentItem.isCompleted()) ? context.getString(R.string.home_list_status_completed) : context.getString(R.string.home_list_status_pending);
        holder.tvStatus.setText(status);
        holder.ibImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectedItemMenu(currentItem, holder.ibImage);
                }
            }
        });

        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectedItem(currentItem);
                }
            }
        });

        holder.ivFav.setVisibility((currentItem.isFavorite()) ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface OnItemSelectedListener {
        void onSelectedItem(ItemList itemList);

        void onSelectedItemMenu(ItemList itemList, View view);
    }
}
