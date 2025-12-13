package com.example.mynotes;

import android.content.Context;
import android.content.SharedPreferences;

public class CardPresenter {
    private final CardView view;
    private final CardSourceImpl model;

    public CardPresenter(CardView view, SharedPreferences sharedPreferences) {
        this.view = view;

        this.model = new CardSourceImpl(sharedPreferences).init();
    }

    public void loadCards() {

        if (model.size() == 0) {
            view.showEmptyState();
        } else {
            view.showCards();
        }
    }

    public void onAddCardClicked() {
        model.addCardData(new CardData("Без названия", "", false));
        model.saveData();
        view.showCards();
    }

    public void deleteCard(int position) {
        model.deleteCardData(position);

        if (model.size() == 0) {
            view.showEmptyState();
        } else {
            view.showCards();
        }
    }

    public CardSource getDataSource() {
        return model;
    }
}
