package com.udacity.surbi.listnow.adapter;

import android.view.View;

import com.udacity.surbi.listnow.data.Item;

public interface PreviewListListener {
    void onSelectedItem(Item item, View view);
    void onCheck(Item item);
}