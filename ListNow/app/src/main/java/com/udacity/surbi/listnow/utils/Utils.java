package com.udacity.surbi.listnow.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.surbi.listnow.data.Item;
import com.udacity.surbi.listnow.data.ListStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final String BASE_URL = "https://google.com";
    private static final String KEY_INGREDIENTS = "key_ingredients";



    public static List<Item> getIngredients(Context context) throws IOException {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(KEY_INGREDIENTS, null);

        if (json != null) {
            ObjectMapper mapper = new ObjectMapper();
            ListStructure user = new ListStructure();
            user = mapper.readValue(json, ListStructure.class);
            return ((user.getItems().size() > 0)? user.getItems() : new ArrayList<Item>());
        } else {
            return new ArrayList<>();
        }

    }

    public static void saveIngredients(Context context, ListStructure ingredients){
        ObjectMapper mapper = new ObjectMapper();
        //Object to JSON in String
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(ingredients);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_INGREDIENTS, jsonInString);
        editor.apply();
    }

}