package com.scoutnerd.app.ui.event;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.scoutnerd.app.R;

public class EventSelectionFragment extends Fragment {
    private androidx.recyclerview.widget.RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private com.scoutnerd.app.ui.scout.MatchScoutViewModel mViewModel; // Reusing VM to access repo

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_events);
        mRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(getContext()));

        mAdapter = new EventAdapter(event -> {
            // Save selection
            android.content.SharedPreferences prefs = android.preference.PreferenceManager
                    .getDefaultSharedPreferences(getContext());
            prefs.edit().putString("selected_event_key", event.key).apply();

            android.widget.Toast.makeText(getContext(), "Selected: " + event.name, android.widget.Toast.LENGTH_SHORT)
                    .show();
            androidx.navigation.Navigation.findNavController(view).navigateUp();
        });
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new androidx.lifecycle.ViewModelProvider(this)
                .get(com.scoutnerd.app.ui.scout.MatchScoutViewModel.class);
        mViewModel.getEvents(2025).observe(getViewLifecycleOwner(), events -> {
            mAdapter.setEvents(events);
        });

        // Search functionality
        com.google.android.material.textfield.TextInputEditText searchInput = view.findViewById(R.id.search_events);
        if (searchInput != null) {
            searchInput.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mAdapter.filter(s.toString());
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                }
            });
        }
    }
}
