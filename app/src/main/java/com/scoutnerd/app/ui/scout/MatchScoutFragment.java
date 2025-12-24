package com.scoutnerd.app.ui.scout;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.scoutnerd.app.R;
import com.scoutnerd.app.data.local.entity.MatchResultEntity;
import com.scoutnerd.app.data.local.entity.MetricEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchScoutFragment extends Fragment {

    private MatchScoutViewModel mViewModel;
    private LinearLayout mMetricsContainer;
    private TextInputEditText mMatchNumberInput;

    // Store dynamically created views to extract data later
    // Map metricId -> View
    private Map<Long, View> mDynamicViews = new HashMap<>();
    private List<MetricEntity> mLoadedMetrics = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_match_scout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMetricsContainer = view.findViewById(R.id.metrics_container); // Need to add this ID to XML
        View startButton = view.findViewById(R.id.btn_start_scouting); // Need to add ID
        mMatchNumberInput = view.findViewById(R.id.input_match_number); // Need to add ID

        mViewModel = new ViewModelProvider(this).get(MatchScoutViewModel.class);

        mViewModel.getMetrics().observe(getViewLifecycleOwner(), metrics -> {
            if (metrics != null) {
                mLoadedMetrics = metrics;
                buildDynamicForm(metrics);
            }
        });

        if (startButton != null) {
            startButton.setOnClickListener(v -> saveParams());
        }
    }

    private void buildDynamicForm(List<MetricEntity> metrics) {
        if (mMetricsContainer == null)
            return;
        mMetricsContainer.removeAllViews();
        mDynamicViews.clear();

        int currentCategory = -1;

        for (MetricEntity metric : metrics) {
            // Add Section Header if category changes
            if (metric.category != currentCategory) {
                currentCategory = metric.category;
                addCategoryHeader(getCategoryName(currentCategory));
            }

            // Create View based on type
            View metricView = null;
            switch (metric.type) {
                case MetricEntity.TYPE_BOOLEAN:
                    metricView = createBooleanView(metric);
                    break;
                case MetricEntity.TYPE_COUNTER:
                    metricView = createCounterView(metric);
                    break;
                case MetricEntity.TYPE_SLIDER:
                    metricView = createSliderView(metric);
                    break;
                case MetricEntity.TYPE_TEXT:
                    metricView = createTextView(metric);
                    break;
            }

            if (metricView != null) {
                mMetricsContainer.addView(metricView);
                // Add spacing
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) metricView.getLayoutParams();
                params.setMargins(0, 0, 0, 24);
                metricView.setLayoutParams(params);

                // Track view
                mDynamicViews.put(metric.id, metricView);
            }
        }
    }

    private String getCategoryName(int category) {
        switch (category) {
            case MetricEntity.CATEGORY_AUTO:
                return "Autonomous";
            case MetricEntity.CATEGORY_TELEOP:
                return "Teleop";
            case MetricEntity.CATEGORY_ENDGAME:
                return "Endgame";
            default:
                return "Other";
        }
    }

    // --- View Creators ---

    private void addCategoryHeader(String title) {
        TextView header = new TextView(requireContext());
        header.setText(title);
        header.setTextSize(20);
        header.setTypeface(null, android.graphics.Typeface.BOLD);
        header.setTextColor(getResources().getColor(R.color.secondary, null));
        header.setPadding(0, 32, 0, 16);
        mMetricsContainer.addView(header);
    }

    private View createBooleanView(MetricEntity metric) {
        SwitchMaterial switchView = new SwitchMaterial(requireContext());
        switchView.setText(metric.name);
        switchView.setTextSize(16);
        switchView.setTextColor(getResources().getColor(R.color.white, null));
        return switchView;
    }

    private View createCounterView(MetricEntity metric) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        TextView label = new TextView(requireContext());
        label.setText(metric.name + ": ");
        label.setTextColor(getResources().getColor(R.color.white, null));
        label.setTextSize(16);
        label.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        Button minusBtn = new Button(requireContext());
        minusBtn.setText("-");

        TextView valueText = new TextView(requireContext());
        valueText.setText("0");
        valueText.setTextColor(getResources().getColor(R.color.white, null));
        valueText.setPadding(32, 0, 32, 0);
        valueText.setTextSize(18);

        Button plusBtn = new Button(requireContext());
        plusBtn.setText("+");

        minusBtn.setOnClickListener(v -> {
            int val = Integer.parseInt(valueText.getText().toString());
            if (val > 0)
                valueText.setText(String.valueOf(val - 1));
        });

        plusBtn.setOnClickListener(v -> {
            int val = Integer.parseInt(valueText.getText().toString());
            valueText.setText(String.valueOf(val + 1));
        });

        // Store the value TextView as the tracked view to read from later
        layout.setTag(valueText);

        layout.addView(label);
        layout.addView(minusBtn);
        layout.addView(valueText);
        layout.addView(plusBtn);
        return layout;
    }

    private View createSliderView(MetricEntity metric) {
        LinearLayout layout = new LinearLayout(requireContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView label = new TextView(requireContext());
        label.setText(metric.name);
        label.setTextColor(getResources().getColor(R.color.white, null));

        SeekBar seekBar = new SeekBar(requireContext());
        seekBar.setMax(5);

        layout.addView(label);
        layout.addView(seekBar);
        // Store SeekBar to read
        layout.setTag(seekBar);
        return layout;
    }

    private View createTextView(MetricEntity metric) {
        TextInputLayout inputLayout = new TextInputLayout(requireContext());
        // Simple styling, in real app use styles from XML

        EditText editText = new EditText(requireContext());
        editText.setHint(metric.name);
        editText.setTextColor(getResources().getColor(R.color.white, null));
        editText.setHintTextColor(getResources().getColor(R.color.text_secondary, null));

        inputLayout.addView(editText);
        return inputLayout;
    }

    // --- Save Logic ---
    private void saveParams() {
        // Mock save logic for now to verify UI works
        Toast.makeText(requireContext(), "Saved " + mLoadedMetrics.size() + " data points!", Toast.LENGTH_SHORT).show();

        // Return to home
        androidx.navigation.Navigation.findNavController(requireView()).navigateUp();
    }
}
