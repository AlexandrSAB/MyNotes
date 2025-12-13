package com.example.mynotes;

import android.content.SharedPreferences;

public class NoteEditPresenter {
    private final NoteEditView view;
    private final CardSourceImpl model;

    public NoteEditPresenter(NoteEditView view, SharedPreferences pref) {
        this.view = view;
        this.model = new CardSourceImpl(pref).init();
    }

    public void loadCard(int position) {
        CardData card = model.getCardData(position);
        view.showCard(card);
    }

    public void saveCard(int position, String title, String text, boolean important) {
        CardData card = new CardData(title, text, important);
        model.updateCardData(position, card);
        view.showMessage("Сохранено");
        view.close();
    }
}
