package com.example.mynotes;

import android.content.Intent;
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

import android.content.SharedPreferences;

public class NoteViewActivity extends AppCompatActivity implements NoteViewView{

    private SharedPreferences sharedPref = null;

    private NoteViewPresenter presenter;

    private CheckBox checkBoxImportant;

    private TextView titleViewText;
    private TextView textViewText;
    private int notePosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.note_viewing);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPref = getSharedPreferences("MyNotesPreferences", MODE_PRIVATE);
        presenter = new NoteViewPresenter(this, sharedPref);

        notePosition = getIntent().getExtras().getInt(POSITION);

        initViews();



        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

        Button buttonDelete = findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(v -> {
            presenter.deleteCard(notePosition);
            finish();
        });

        Button buttonEdit = findViewById(R.id.buttonEdit);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent runNoteEditActivity = new Intent(NoteViewActivity.this, NoteEditActivity.class);
                runNoteEditActivity.putExtra(POSITION, notePosition);
                startActivity(runNoteEditActivity);
            }
        });
    }

    public void initViews() {

        titleViewText = findViewById(R.id.title);
        textViewText = findViewById(R.id.noteText);
        checkBoxImportant = findViewById(R.id.important);

        checkBoxImportant.setClickable(false);
        checkBoxImportant.setFocusable(false);
    }

    @Override
    public void showCard(CardData card) {
        titleViewText.setText(card.getTitle());
        textViewText.setText(card.getNoteText());
        checkBoxImportant.setChecked(card.getImportant());
    }
    @Override
    public void onResume() {
        super.onResume();
        presenter.loadCard(notePosition);
    }
}
