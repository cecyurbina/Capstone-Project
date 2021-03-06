package com.udacity.surbi.listnow.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.data.Item;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CheckListAdapter extends RecyclerView.Adapter<CheckListAdapter.ViewHolder> {
    private List<Item> mDataset;
    private PreviewListListener listener;

    public CheckListAdapter(List<Item> myDataset, PreviewListListener onItemSelectedListener) {
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
            if (currentItem.getBitmap() != null) {
                holder.ivUploadImage.setImageBitmap(currentItem.getBitmap());

            } else if (currentItem.getImageUrl() != null) {
                Glide.with(context).load(currentItem.getImageUrl()).apply(new RequestOptions().placeholder(R.drawable.ic_file_upload_black_24dp).centerCrop().dontAnimate().dontTransform()).into(holder.ivUploadImage);

            } else {
                holder.ivUploadImage.setImageResource(R.drawable.ic_file_upload_black_24dp);
            }
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
        holder.cbCheck.setOnCheckedChangeListener(null);
        if (currentItem.isChecked() != null) {
            holder.cbCheck.setChecked(currentItem.isChecked());
            if (currentItem.isChecked()){
                holder.cbCheck.setContentDescription(holder.cbCheck.getContext().getString(R.string.check_item));
            } else {
                holder.cbCheck.setContentDescription(holder.cbCheck.getContext().getString(R.string.uncheck_item));
            }
        } else {
            holder.cbCheck.setChecked(false);
        }

        holder.viewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onSelectedItem(currentItem, holder.viewItem);
                }
            }
        });

        holder.cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                listener.onCheck(currentItem);
                if (isChecked){
                    holder.cbCheck.setContentDescription(holder.cbCheck.getContext().getString(R.string.check_item));
                } else {
                    holder.cbCheck.setContentDescription(holder.cbCheck.getContext().getString(R.string.uncheck_item));
                }
            }
        });

        holder.ivUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectImage(currentItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

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
        CheckBox cbCheck;


        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }


}
