package com.example.mynotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static com.example.mynotes.Constants.*;
import android.widget.CheckBox;

public class NoteEditActivity extends AppCompatActivity{

    private SharedPreferences sharedPref = null;
    private CardSource data;

    private CheckBox checkBoxImportant;

    private EditText titleEditText;
    private EditText textEditText;
    private int notePosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.note_editing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        noteEditActivityUpdate();

        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
                finish();

            }
        });
    }

    private void noteEditActivityUpdate() {
        sharedPref = getSharedPreferences("MyNotesPreferences", MODE_PRIVATE);
        data = new CardSourceImpl(sharedPref).init();
        notePosition = getIntent().getExtras().getInt(POSITION);

        titleEditText = findViewById(R.id.editTitle);
        textEditText = findViewById(R.id.editNoteText);
        checkBoxImportant = findViewById(R.id.editImportant);

        CardData cardData = data.getCardData(notePosition);

        titleEditText.setText(cardData.getTitle());
        textEditText.setText(cardData.getNoteText());
        checkBoxImportant.setChecked(cardData.getImportant());
    }

    private void saveNote() {
        String title = titleEditText.getText().toString().trim();
        String text = textEditText.getText().toString().trim();
        boolean isImportant = checkBoxImportant.isChecked();

        CardData updatedCardData = new CardData(title, text, isImportant);
        if (title.isEmpty()) {
            title = "Без названия";
        }
        data.updateCardData(notePosition, updatedCardData);
        data.saveData();

    }
}
