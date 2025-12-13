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

public class NoteEditActivity extends AppCompatActivity implements NoteEditView{

    private SharedPreferences sharedPref = null;

    private NoteEditPresenter presenter;


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

        sharedPref = getSharedPreferences("MyNotesPreferences", MODE_PRIVATE);
        presenter = new NoteEditPresenter(this, sharedPref);

        notePosition = getIntent().getExtras().getInt(POSITION);

        initViews();
        presenter.loadCard(notePosition);



        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> finish());

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> {
            presenter.saveCard(
                    notePosition,
                    titleEditText.getText().toString().trim(),
                    textEditText.getText().toString().trim(),
                    checkBoxImportant.isChecked()
            );
        });
    }

    private void initViews() {

        titleEditText = findViewById(R.id.editTitle);
        textEditText = findViewById(R.id.editNoteText);
        checkBoxImportant = findViewById(R.id.editImportant);
    }

    @Override
    public void showCard(CardData card) {
        titleEditText.setText(card.getTitle());
        textEditText.setText(card.getNoteText());
        checkBoxImportant.setChecked(card.getImportant());
    }

    @Override
    public void showMessage(String msg) {

    }

    @Override
    public void close() {
        finish();
    }


}
