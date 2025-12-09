package com.example.mynotes;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private final String TAG = "MyAdapter";
    private CardSource dataSource;
    private Activity activity;
    private OnItemClickListener itemClickListener;
    private int menuPosition;
    public int getMenuPosition() {
        return menuPosition;
    }

    // Передаём в конструктор источник данных
    // В нашем случае это массив, но может быть и запрос к БД
    public MyAdapter(CardSource dataSource, Activity activity) {
        this.dataSource = dataSource;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Создаём новый элемент пользовательского интерфейса
        // Через Inflater
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card_view, parent, false);
        Log.d(TAG, "onCreateViewHolder");
        // Здесь можно установить всякие параметры
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
// Получить элемент из источника данных (БД, интернет...)
        // Вынести на экран, используя ViewHolder
        holder.setData(dataSource.getCardData(position));

        Log.d(TAG, "onBindViewHolder");
    }

    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    public void SetOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

        public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView preview;
        private CheckBox important;
        private LinearLayout linearLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            important = itemView.findViewById(R.id.important);
            preview = itemView.findViewById(R.id.preview);
            linearLayout = itemView.findViewById(R.id.linearLayout);

            important.setClickable(false);
            important.setFocusable(false);

            registerContextMenu(itemView);

            // Обработчик нажатий на этом ViewHolder
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, getAdapterPosition());
                    }
                }
            });

            // Обработчик нажатий на картинке
            linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    menuPosition = getLayoutPosition();
                    itemView.showContextMenu(10, 10);
                    return true;
                }
            });
        }


        private void registerContextMenu(@NonNull View itemView) {
            if (activity != null) {
                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        menuPosition = getLayoutPosition();
                        return false;
                    }
                });
                activity.registerForContextMenu(itemView);
            }
        }


        public void setData(CardData cardData) {
            title.setText(cardData.getTitle());
            preview.setText(cardData.getNoteText());
            important.setChecked(cardData.getImportant());
        }
    }

    public void setCardSourse(CardSource dataSource) {
        this.dataSource = dataSource;
        notifyDataSetChanged();
    }


}