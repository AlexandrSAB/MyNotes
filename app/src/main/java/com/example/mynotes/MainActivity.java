package com.example.mynotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

public class MainActivity extends AppCompatActivity implements Constants{

    private static final int MY_DEFAULT_DURATION = 1000;

    private CardSource data;
    private MyAdapter adapter;
    private RecyclerView recyclerView;


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
        setSupportActionBar(findViewById(R.id.toolbar));
        initView();
    }

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
        data = new CardSourceImpl(getResources()).init();
        initRecyclerView();
        ImageButton buttonCreateNew = findViewById(R.id.buttonCreateNew);

        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.addCardData(new CardData("заметка " + data.size(),
                        "текст заметки " + data.size(),

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
                /*Toast.makeText(getApplicationContext(), String.format("Позиция - %d", position), Toast.LENGTH_SHORT).show();*/
                /*CardData cardData = data.getCardData(position);*/
                Intent runNoteViewActivity = new Intent(MainActivity.this, NoteViewActivity.class);
                runNoteViewActivity.putExtra(TITLE, cardData.getTitle().toString());
                runNoteViewActivity.putExtra(NOTETEXT, cardData.getNoteText().toString());
                runNoteViewActivity.putExtra(ISIMPORTANT, cardData.getImportant());
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
            /*data.updateCardData(position,
                    new CardData("Кадр " + position,
                            data.getCardData(position).getDescription(),
                            data.getCardData(position).getPicture(),
                            false));
            adapter.notifyItemChanged(position);
            return true;*/
        } else if (item.getItemId() == R.id.action_delete) {
            data.deleteCardData(position);
            adapter.notifyItemRemoved(position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

}