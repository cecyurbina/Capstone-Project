package com.udacity.surbi.listnow.utils;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ListStructure;

public class DatabaseHelper {
    private DatabaseReference mDatabase;

    public DatabaseHelper(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public String createList(){
        String key = mDatabase.child("lists").push().getKey();
        ListStructure listStructure = new ListStructure();
        listStructure.setName("Test");
        listStructure.setFavorite(false);
        listStructure.setCompleted(false);
        mDatabase.child("lists").child(key).setValue(listStructure);
        return key;
    }

    public void renameList(){

    }

    public void removeList(){

    }

    public void copyList(boolean empty){

    }

    public void addItemToList(String idList){
        String key = mDatabase.child("items").push().getKey();
        Item item = new Item();
        item.setName("Test");
        item.setRejected(false);
        item.setImage(false);
        item.setQuantity(1);
        mDatabase.child("lists").child(idList).child("items").child(key).setValue(item);

    }

    public void updateItem(){

    }

}

