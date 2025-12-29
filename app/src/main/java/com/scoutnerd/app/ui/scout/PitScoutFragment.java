package com.scoutnerd.app.ui.scout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.scoutnerd.app.R;

public class PitScoutFragment extends Fragment {
    private com.scoutnerd.app.ui.scout.MatchScoutViewModel mViewModel;
    private java.util.Map<String, Long> mMetricNameMap = new java.util.HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pit_scout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new androidx.lifecycle.ViewModelProvider(this)
                .get(com.scoutnerd.app.ui.scout.MatchScoutViewModel.class);

        // Load metric map
        mViewModel.getMetrics().observe(getViewLifecycleOwner(), metrics -> {
            if (metrics != null) {
                mMetricNameMap.clear();
                for (com.scoutnerd.app.data.local.entity.MetricEntity m : metrics) {
                    mMetricNameMap.put(m.name, m.id);
                }
            }
        });

        view.findViewById(R.id.btn_save_pit).setOnClickListener(v -> savePitData());
    }

    private void savePitData() {
        android.widget.EditText teamInput = getView().findViewById(R.id.input_pit_team_number);
        android.widget.EditText drivetrainInput = getView().findViewById(R.id.input_drivetrain);
        android.widget.EditText weightInput = getView().findViewById(R.id.input_weight);
        android.widget.EditText langInput = getView().findViewById(R.id.input_language);

        String teamStr = teamInput.getText().toString().trim();
        if (teamStr.isEmpty()) {
            android.widget.Toast.makeText(getContext(), "Please enter Team Number", android.widget.Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        int teamNumber = Integer.parseInt(teamStr);
        String eventKey = "2025txho"; // Demo key
        long timestamp = System.currentTimeMillis();
        java.util.List<com.scoutnerd.app.data.local.entity.MatchResultEntity> results = new java.util.ArrayList<>();

        addResultIfMetricExists(results, "Drivetrain Type", drivetrainInput.getText().toString(), teamNumber, eventKey,
                timestamp);
        addResultIfMetricExists(results, "Robot Weight", weightInput.getText().toString(), teamNumber, eventKey,
                timestamp);
        addResultIfMetricExists(results, "Programming Language", langInput.getText().toString(), teamNumber, eventKey,
                timestamp);

        if (!results.isEmpty()) {
            mViewModel.saveMatch(0, teamNumber, eventKey, results); // Match 0 for Pit
            android.widget.Toast.makeText(getContext(), "Pit Data Saved!", android.widget.Toast.LENGTH_SHORT).show();
            androidx.navigation.Navigation.findNavController(getView()).navigateUp();
        } else {
            android.widget.Toast
                    .makeText(getContext(), "Error: Metrics not loaded yet", android.widget.Toast.LENGTH_SHORT).show();
        }
    }

    private void addResultIfMetricExists(java.util.List<com.scoutnerd.app.data.local.entity.MatchResultEntity> list,
            String metricName, String value, int team, String event, long time) {
        if (mMetricNameMap.containsKey(metricName)) {
            list.add(new com.scoutnerd.app.data.local.entity.MatchResultEntity(
                    0, team, event, mMetricNameMap.get(metricName), value, time));
        }
    }
}
