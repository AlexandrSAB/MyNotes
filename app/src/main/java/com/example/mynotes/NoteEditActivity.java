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
    private boolean isImportant;

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

        data = new CardSourceImpl(sharedPref).init();

        int notePosition = getIntent().getExtras().getInt(POSITION);



        EditText title = findViewById(R.id.editTitle);
        EditText text = findViewById(R.id.editNoteText);
        checkBoxImportant = findViewById(R.id.editImportant);

        CardData cardData = data.getCardData(notePosition);

        title.setText(cardData.getTitle());
        text.setText(cardData.getNoteText());
        checkBoxImportant.setChecked(cardData.getImportant());



        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
