package com.example.mynotes;

public interface CardView {
    void showCards();
    void showEmptyState();
    void showError(String message);
    void showMessage(String message);
}
