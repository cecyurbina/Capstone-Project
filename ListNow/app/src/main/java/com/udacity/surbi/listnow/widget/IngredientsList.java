package com.udacity.surbi.listnow.widget;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.surbi.listnow.R;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IngredientsList implements RemoteViewsService.RemoteViewsFactory {
    private List<Item> items = new ArrayList<>();
    private Context ctxt;

    public IngredientsList(Context ctxt, Intent intent) {
        this.ctxt = ctxt;
        try {
            items = Utils.getIngredients(ctxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        return (items.size());
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews row = new RemoteViews(ctxt.getPackageName(),
                R.layout.item_ingredient);
        row.setTextViewText(R.id.tv_ingredient, items.get(position).getName());
        if (items.get(position).isChecked()) {
            row.setViewVisibility(R.id.iv_check, View.VISIBLE);
        } else {
            row.setViewVisibility(R.id.iv_check, View.GONE);
        }
        return (row);
    }

    @Override
    public RemoteViews getLoadingView() {
        return (null);
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public boolean hasStableIds() {
        return (true);
    }

    @Override
    public void onDataSetChanged() {
        try {
            items = Utils.getIngredients(ctxt);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}