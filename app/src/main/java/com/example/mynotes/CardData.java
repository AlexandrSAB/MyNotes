package com.example.mynotes;

class CardData {

    private String title;
    private String noteText;
    private boolean isImportant;

    public CardData(String title, String noteText, boolean isImportant) {
        this.title = title;
        this.noteText = noteText;
        this.isImportant = isImportant;
    }

    public String getTitle() {
        return title;
    }

    public String getNoteText() {
        return noteText;
    }

    public boolean getImportant() {
        return isImportant;
    }



}
