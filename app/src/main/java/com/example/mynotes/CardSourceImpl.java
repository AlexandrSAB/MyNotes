package com.example.mynotes;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

public class CardSourceImpl implements CardSource {

    private List<CardData> dataSource;
    private Resources resources;

    CardSourceImpl(Resources resources) {
        dataSource = new ArrayList<>(10);
        this.resources = resources;
    }

    public CardSourceImpl init() {
        String[] titles = resources.getStringArray(R.array.titles);
        String[] texts = resources.getStringArray(R.array.noteText);
        String[] importants = resources.getStringArray(R.array.isImportant);
        for (int i = 0; i < titles.length; i++) {
            dataSource.add(new CardData(titles[i], texts[i], importants[i].equals("1")));
        }
        return this;
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
    }

    @Override
    public void updateCardData(int position, CardData cardData) {
        dataSource.set(position, cardData);
    }

    @Override
    public void addCardData(CardData cardData) {
        dataSource.add(cardData);
    }

    @Override
    public void clearCardData() {
        dataSource.clear();
    }

}
