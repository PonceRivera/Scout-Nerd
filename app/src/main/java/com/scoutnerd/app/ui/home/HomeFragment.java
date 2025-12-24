package com.scoutnerd.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.scoutnerd.app.R;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private TextView mEventStatusText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEventStatusText = view.findViewById(R.id.text_current_event_name);

        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        mViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            if (events != null && !events.isEmpty()) {
                mEventStatusText.setText("Events Loaded: " + events.size());
            } else {
                mEventStatusText.setText("No Event Selected");
            }
        });

        // Set up interactions using helper method
        setupNavigation(view, R.id.card_browse_teams, R.id.action_homeFragment_to_teamListFragment);
        setupNavigation(view, R.id.card_match_scout, R.id.action_homeFragment_to_matchScoutFragment);
        setupNavigation(view, R.id.card_pit_scout, R.id.action_homeFragment_to_pitScoutFragment);
        setupNavigation(view, R.id.card_analytics, R.id.action_homeFragment_to_analyticsFragment);
        setupNavigation(view, R.id.card_settings, R.id.action_homeFragment_to_settingsFragment);
        setupNavigation(view, R.id.card_select_event, R.id.action_homeFragment_to_eventSelectionFragment);
    }

    private void setupNavigation(View root, int viewId, int actionId) {
        View v = root.findViewById(viewId);
        if (v != null) {
            v.setOnClickListener(clickedView -> {
                try {
                    Navigation.findNavController(clickedView).navigate(actionId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
