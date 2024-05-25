package com.example.ocenus;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final ArrayList<String> daysOfMonth;
    private final OnItemListener onItemListener;

    private final int startDayOffset; // Zadeklaruj pole startDayOffset
    private int currentYear;
    private int currentMonth;

    // Dodaj startDayOffset jako argument konstruktora
    public CalendarAdapter(ArrayList<String> daysOfMonth, OnItemListener onItemListener, int startDayOffset) {
        this.daysOfMonth = daysOfMonth;
        this.onItemListener = onItemListener;
        this.startDayOffset = startDayOffset; // Przypisz wartość startDayOffset

        LocalDate currentDate = LocalDate.now();
        currentYear = currentDate.getYear();
        currentMonth = currentDate.getMonthValue();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        return new CalendarViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.dayOfMonth.setText(daysOfMonth.get(position));

        if (isToday(position)) {
            //holder.dayOfMonth.setBackgroundResource(R.drawable.circle_gray);
        } else {
            holder.dayOfMonth.setBackgroundResource(0);
        }
    }

    @Override
    public int getItemCount() {
        return daysOfMonth.size();
    }

    public interface OnItemListener {
        void onItemClick(int position, String dayText);
    }

    private boolean isToday(int position) {
        LocalDate today = LocalDate.now();
        int year = currentYear;
        int month = currentMonth;

        // Sprawdź, czy dzień w adapterze odpowiada dzisiejszemu dniu w bieżącym miesiącu
        if (year == today.getYear() && month == today.getMonthValue()) {
            String dayOfMonthString = daysOfMonth.get(position);

            // Sprawdź, czy dzień miesiąca nie jest pusty
            if (!dayOfMonthString.isEmpty()) {
                int dayOfMonth = Integer.parseInt(dayOfMonthString);
                return dayOfMonth == today.getDayOfMonth();
            }
        }

        return false;
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView dayOfMonth;
        private final OnItemListener onItemListener;

        public CalendarViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            dayOfMonth = itemView.findViewById(R.id.cellDayText);
            this.onItemListener = onItemListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
        }
    }
}