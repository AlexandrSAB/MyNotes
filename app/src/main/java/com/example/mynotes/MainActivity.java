package com.example.mynotes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity implements Constants, CardView{
    private static final int MY_DEFAULT_DURATION = 1000;

    private MyAdapter adapter;
    private RecyclerView recyclerView;

    private SharedPreferences sharedPref = null;

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 42;

    private String notificationTitle;
    private String notificationText;

    private CardPresenter presenter;




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




        sharedPref = getSharedPreferences("MyNotesPreferences", MODE_PRIVATE);
        presenter = new CardPresenter(this, sharedPref);


        setSupportActionBar(findViewById(R.id.toolbar));

        initView();

    }



    private void initView() {
        recyclerView = findViewById(R.id.recycler_view_lines);

        initRecyclerView();
        presenter.loadCards();
        ImageButton buttonCreateNew = findViewById(R.id.buttonCreateNew);

        buttonCreateNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onAddCardClicked();

                recyclerView.smoothScrollToPosition(presenter.getDataSource().size() - 1);
                showNotification("Уведомление", "Заметка создана");
            }
        });
    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);

        // Будем работать со встроенным менеджером
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Установим адаптер
        /*final MyAdapter*/
        adapter = new MyAdapter(presenter.getDataSource(), this);
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
                CardData cardData = presenter.getDataSource().getCardData(position);

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
            presenter.deleteCard(position);
            adapter.notifyItemRemoved(position);
            showNotification("Уведомление", "Заметка удалена");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((CardSourceImpl) presenter.getDataSource()).init();
        presenter.loadCards();
        adapter.setCardSourse(presenter.getDataSource());;
    }



    void showNotification(String notificationTitle, String notificationText) {

        notificationTitle = notificationTitle;
        notificationText = notificationText;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_ID);
            }
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            showNotification(notificationTitle, notificationText); // Можно показать уведомление повторно
        } else {

            Toast.makeText(this, "Разрешение на уведомления не предоставлено.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Name";
        String descriptionText = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(descriptionText);


        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void showCards() {
        adapter.setCardSourse(presenter.getDataSource());
        adapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEmptyState() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }



}