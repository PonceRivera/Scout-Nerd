package com.scoutnerd.app.ui.team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.scoutnerd.app.R;

public class TeamListFragment extends Fragment {

    private TeamListViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private TeamAdapter mAdapter;

    // TODO: Pass this via arguments (SafeArgs)
    private static final String DEFAULT_EVENT_KEY = "2025txho"; // Example: Houston

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_team_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.recycler_teams);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TeamAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mViewModel = new ViewModelProvider(this).get(TeamListViewModel.class);

        // Simulating receiving an event key. In real app, use Bundle or SafeArgs.
        // For testing "Team List" feature specifically, we can hardcode or rely on what
        // Home passes.
        // Let's assume we want to view a specific event for now or just the first one
        // found.
        mViewModel.setEventKey(DEFAULT_EVENT_KEY);

        android.widget.EditText searchInput = view.findViewById(R.id.search_teams);
        if (searchInput != null) {
            searchInput.addTextChangedListener(new android.text.TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    mViewModel.setSearchQuery(s.toString());
                }

                @Override
                public void afterTextChanged(android.text.Editable s) {
                }
            });
        }

        mViewModel.getTeams().observe(getViewLifecycleOwner(), teams -> {
            mAdapter.setTeams(teams);
        });
    }
}
