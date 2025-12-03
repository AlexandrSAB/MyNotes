package com.example.mynotes;

import android.content.SharedPreferences;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import com.google.gson.GsonBuilder;



public class CardSourceImpl implements CardSource {

    private SharedPreferences sharedPref = null;

    public static final String KEY = "key";

    private List<CardData> dataSource;
/*    private Resources resources;*/

    CardSourceImpl(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
        dataSource = new ArrayList<>(10);
/*        this.resources = resources;*/
    }

    public CardSourceImpl init() {
/*        String[] titles = resources.getStringArray(R.array.titles);
        String[] texts = resources.getStringArray(R.array.noteText);
        String[] importants = resources.getStringArray(R.array.isImportant);
        for (int i = 0; i < titles.length; i++) {
            dataSource.add(new CardData(titles[i], texts[i], importants[i].equals("1")));
        }*/

        String savedCards = sharedPref.getString(KEY, null);
        if (savedCards != null && !savedCards.isEmpty()) {
            try {
                Type type = new TypeToken<ArrayList<CardData>>() {}.getType();
                List<CardData> loadedData = new GsonBuilder().create().fromJson(savedCards, type);
                if (loadedData != null) {
                    dataSource = loadedData;
                }
            } catch (JsonSyntaxException e) {
                dataSource.clear();
            }
        }

        return this;
    }

    private void saveData() {
        String jsonCards = new GsonBuilder().create().toJson(dataSource);
        sharedPref.edit().putString(KEY, jsonCards).apply();
    }




    @Override
    public CardData getCardData(int position) {
        return dataSource.get(position);
    }

    @Override
    public int size() {
        return dataSource.size();
    }

    @Override
    public void deleteCardData(int position) {

        dataSource.remove(position);
        saveData();
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        dataSource.set(position, cardData);
        saveData();
    }

    @Override
    public void addCardData(CardData cardData) {

        dataSource.add(cardData);
        saveData();
    }

    @Override
    public void clearCardData() {
        dataSource.clear();
        saveData();
    }

}
