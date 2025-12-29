package com.scoutnerd.app.ui.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.scoutnerd.app.R;
import com.scoutnerd.app.data.local.entity.EventEntity;
import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    // List of ALL events
    private List<EventEntity> mAllEvents = new ArrayList<>();
    // List of DISPLAYED events
    private List<EventEntity> mEvents = new ArrayList<>();

    private final OnEventClickListener mListener;

    public interface OnEventClickListener {
        void onEventClick(EventEntity event);
    }

    public EventAdapter(OnEventClickListener listener) {
        mListener = listener;
    }

    public void setEvents(List<EventEntity> events) {
        mAllEvents = new ArrayList<>(events);
        mEvents = new ArrayList<>(events);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        if (query == null || query.isEmpty()) {
            mEvents = new ArrayList<>(mAllEvents);
        } else {
            List<EventEntity> filteredDate = new ArrayList<>();
            String lowerQuery = query.toLowerCase();
            for (EventEntity event : mAllEvents) {
                if (event.name.toLowerCase().contains(lowerQuery) ||
                        (event.city != null && event.city.toLowerCase().contains(lowerQuery))) {
                    filteredDate.add(event);
                }
            }
            mEvents = filteredDate;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        EventEntity event = mEvents.get(position);
        holder.name.setText(event.name);
        holder.date.setText(event.startDate + " • " + event.city);

        // Simple styling for dark mode since we used android.R.layout
        holder.name.setTextColor(android.graphics.Color.WHITE);
        holder.date.setTextColor(android.graphics.Color.LTGRAY);

        holder.itemView.setOnClickListener(v -> mListener.onEventClick(event));
    }

    @Override
    public int getItemCount() {
        return mEvents != null ? mEvents.size() : 0;
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView date;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(android.R.id.text1);
            date = itemView.findViewById(android.R.id.text2);
        }
    }
}
