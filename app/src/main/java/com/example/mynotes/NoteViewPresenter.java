package com.example.mynotes;

import android.content.SharedPreferences;

public class NoteViewPresenter {
    private final NoteViewView view;
    private final CardSourceImpl model;

    public NoteViewPresenter(NoteViewView view, SharedPreferences pref) {
        this.view = view;
        this.model = new CardSourceImpl(pref).init();
    }

    public void loadCard(int position) {
        model.init();
        CardData card = model.getCardData(position);
        view.showCard(card);
    }

    public CardSource getDataSource() {
        return model;
    }

    public void deleteCard(int position) {
        model.deleteCardData(position);
    }
}
