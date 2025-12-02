package com.example.mynotes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.CheckBox;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import static com.example.mynotes.Constants.*;

public class NoteViewActivity extends AppCompatActivity {

    private CheckBox checkBoxImportant;
    private boolean isImportant;

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

        TextView title = findViewById(R.id.title);
        TextView text = findViewById(R.id.noteText);
        checkBoxImportant = findViewById(R.id.important);


        String noteTitle = getIntent().getExtras().getString(TITLE);
        String noteText = getIntent().getExtras().getString(NOTETEXT);
        isImportant = getIntent().getBooleanExtra(ISIMPORTANT, false);



        title.setText(noteTitle);
        text.setText(noteText);
        checkBoxImportant.setChecked(isImportant);


        /*String name = getIntent().getExtras().getString(YOUR_NAME);
        String age = getIntent().getExtras().getString(YOUR_AGE);

        String sayCongratulations = getString(R.string.say_congratulations) + name + "!";
        String sayAge = getString(R.string.say_age) + age + getString(R.string.say_years);

        txtCongratulations.setText(sayCongratulations);
        txtAge.setText(sayAge);*/

        Button buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
