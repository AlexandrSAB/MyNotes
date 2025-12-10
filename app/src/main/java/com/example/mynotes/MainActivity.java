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


public class MainActivity extends AppCompatActivity implements Constants{
    private static final int MY_DEFAULT_DURATION = 1000;
    private CardSource data;
    private MyAdapter adapter;
    private RecyclerView recyclerView;

    private SharedPreferences sharedPref = null;

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 42;

    private String notificationTitle;
    private String notificationText;




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
                showNotification("Уведомление", "Заметка создана");
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
            showNotification("Уведомление", "Заметка удалена");
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



    void showNotification(String notificationTitle, String notificationText) {
        // Создаем NotificationChannel, но это делается только для API 26+
        // Потому что NotificationChannel -- это новый класс и его нет в support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        // Все цветные иконки отображаются только в оттенках серого
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_ID); // константа вашего выбора
            }
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Пользователь разрешил показывать уведомления
            showNotification(notificationTitle, notificationText); // Можно показать уведомление повторно
        } else {
            // Пользователь отказался давать разрешение
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

        // Регистрируем канал в системе
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

}