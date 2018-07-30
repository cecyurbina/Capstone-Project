package com.udacity.surbi.listnow.utils;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ListStructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DatabaseHelper {
    private DatabaseReference mDatabase;

    public DatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * CREATE LIST
     *
     * @return id
     */
    public ListStructure createList(String userId) {
        String key = mDatabase.child("lists").push().getKey();
        ListStructure listStructure = new ListStructure();
        listStructure.setName("New list");
        listStructure.setFavorite(false);
        listStructure.setCompleted(false);
        listStructure.setId(key);
        List<String> users = new ArrayList<>();
        users.add(userId);
        listStructure.setUsers(users);
        mDatabase.child("lists").child(Objects.requireNonNull(key)).setValue(listStructure);
        return listStructure;
    }

    public void renameList(String key, String name) {
        mDatabase.child("lists").child(key).child("name").setValue(name);
    }

    public void removeList(String key) {
        mDatabase.child("lists").child(key).removeValue();
    }

    public void completeList(String key, boolean isCompleted) {
        mDatabase.child("lists").child(key).child("completed").setValue(isCompleted);
    }

    public void favoriteList(ListStructure item, Context context) {
        //mDatabase.child("lists").child(key).child("favorite").setValue(isFav);
        Utils.saveList(context, item);
    }

    public ListStructure copyList(ListStructure listStructure, String userId) {
        ListStructure newList = new ListStructure();
        try {
            newList = (ListStructure) listStructure.clone();
            String key = mDatabase.child("lists").push().getKey();
            newList.setId(key);
            newList.setName(listStructure.getName() + " Copy");
            List<String> user = new ArrayList<>();
            user.add(userId);
            newList.setUsers(user);
            newList.setDataSnapshot(null);
            mDatabase.child("lists").child(Objects.requireNonNull(key)).setValue(newList);

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return listStructure;
    }

    /**
     * ADD ITEM
     *
     * @param idList list id
     * @param item   item
     */
    public void addItemToList(String idList, Item item) {
        String key = mDatabase.child("items").push().getKey();
        item.setKey(key);
        mDatabase.child("lists").child(idList).child("items").child(Objects.requireNonNull(key)).setValue(item);
    }

    /**
     * UPDATE ITEM
     *
     * @param idList list id
     * @param item   item
     */
    public void updateItem(String idList, Item item) {
        mDatabase.child("lists").child(idList).child("items").child(item.getKey()).setValue(item);
    }

    /**
     * REMOVE ITEM
     *
     * @param idList list id
     * @param idItem item id
     */
    public void removeItemFromList(String idList, String idItem) {
        mDatabase.child("lists").child(idList).child("items").child(idItem).removeValue();
    }

    /**
     * REJECT ITEM
     *
     * @param idList list id
     * @param idItem item id
     */
    public void rejectItemFromList(String idList, String idItem) {
        mDatabase.child("lists").child(idList).child("items").child(idItem).child("rejected").setValue(true);
    }

    public void checkItem(String idList, Item item) {
        mDatabase.child("lists").child(idList).child("items").child(item.getKey()).child("checked").setValue(!item.isChecked());
    }

    public void setImageItem(String idList, Item item) {
        mDatabase.child("lists").child(idList).child("items").child(item.getKey()).child("imageUrl").setValue(item.getImageUrl());
    }


    public void addUserList(List<String> users, String inputText) {
        mDatabase.child("lists").child(inputText).child("users").setValue(users);
    }
}

