package com.example.mynotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity implements Constants{
    private static final int MY_DEFAULT_DURATION = 1000;
    private CardSource data;
    private MyAdapter adapter;
    private RecyclerView recyclerView;

    private SharedPreferences sharedPref = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*setSplashScreenLoadingParameters();*/



        sharedPref = getSharedPreferences("MyNotesPreferences", MODE_PRIVATE);


        setSupportActionBar(findViewById(R.id.toolbar));

        initView();


    }

    /*private void setSplashScreenLoadingParameters() {


        // Set up an OnPreDrawListener to the root view.
        final View content = findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        // Check whether the initial data is ready.
                        if (isMainActivityReady()) {
                            // The content is ready. Start drawing.
                            content.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        } else {
                            // The content isn't ready. Suspend.
                            return false;
                        }
                    }
                });
    }

    private boolean isMainActivityReady() {
        View mainContent = findViewById(R.id.main);
        return mainContent != null && mainContent.isLaidOut();
    }*/

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cards_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            data.addCardData(new CardData("Заголовок " + data.size(),
                    "Описание " + data.size(),
                    R.drawable.img,
                    false));
            adapter.notifyItemInserted(data.size() - 1);
            //recyclerView.scrollToPosition(data.size() - 1);
            recyclerView.smoothScrollToPosition(data.size() - 1);
            return true;
        } else if (item.getItemId() == R.id.action_clear) {
            data.clearCardData();
            adapter.notifyDataSetChanged();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    private void initView() {
        recyclerView = findViewById(R.id.recycler_view_lines);
        // Получим источник данных для списка
        data = new CardSourceImpl(sharedPref).init();
        initRecyclerView();
        ImageButton buttonCreateNew = findViewById(R.id.buttonCreateNew);

        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.addCardData(new CardData("Без названия",
                        "",
                        false));
                adapter.notifyItemInserted(data.size() - 1);
                //recyclerView.scrollToPosition(data.size() - 1);
                recyclerView.smoothScrollToPosition(data.size() - 1);
            }
        });
    }

    private void initRecyclerView(/*RecyclerView recyclerView, CardSource data*/) {

        // Эта установка служит для повышения производительности системы
        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        /*final MyAdapter*/
        adapter = new MyAdapter(data, this);
        recyclerView.setAdapter(adapter);

        //обавляем декоратор/разделитель карточек
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        itemDecoration.setDrawable(getResources().getDrawable(R.drawable.separator, null));
        recyclerView.addItemDecoration(itemDecoration);

        // Установим анимацию. А чтобы было хорошо заметно, сделаем анимацию долгой
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setAddDuration(MY_DEFAULT_DURATION);
        animator.setRemoveDuration(MY_DEFAULT_DURATION);
        recyclerView.setItemAnimator(animator);

        // Установим слушателя
        adapter.SetOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onItemClick(View view, int position) {
                CardData cardData = data.getCardData(position);

                Intent runNoteViewActivity = new Intent(MainActivity.this, NoteViewActivity.class);
                runNoteViewActivity.putExtra(TITLE, cardData.getTitle().toString());
                runNoteViewActivity.putExtra(NOTETEXT, cardData.getNoteText().toString());
                runNoteViewActivity.putExtra(ISIMPORTANT, cardData.getImportant());
                runNoteViewActivity.putExtra(POSITION, position);
                startActivity(runNoteViewActivity);
            }
        });
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.card_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getMenuPosition();
        if (item.getItemId() == R.id.action_edit) {
            Intent runNoteEditActivity = new Intent(MainActivity.this, NoteEditActivity.class);
            runNoteEditActivity.putExtra(POSITION, position);
            startActivity(runNoteEditActivity);
        } else if (item.getItemId() == R.id.action_delete) {
            data.deleteCardData(position);
            adapter.notifyItemRemoved(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        data = new CardSourceImpl(sharedPref).init();
        adapter.setCardSourse(data);
        adapter.notifyDataSetChanged();
    }

}