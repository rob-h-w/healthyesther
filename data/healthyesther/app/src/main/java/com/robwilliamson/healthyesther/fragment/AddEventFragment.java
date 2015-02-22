package com.robwilliamson.healthyesther.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.add.MealActivity;
import com.robwilliamson.healthyesther.add.MedicationActivity;
import com.robwilliamson.healthyesther.add.NoteActivity;
import com.robwilliamson.healthyesther.add.ScoreActivity;

public class AddEventFragment extends BaseFragment {

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_add_event;
    }

    @Override
    public void onResume() {
        super.onResume();

        getCreateMealEventButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MealActivity.class);
                getActivity().startActivity(intent);
            }
        });

        getCreateMedicationEventButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MedicationActivity.class);
                getActivity().startActivity(intent);
            }
        });

        getCreateHealthScoreEventButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ScoreActivity.class);
                getActivity().startActivity(intent);
            }
        });

        getCreateNoteEventButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                getActivity().startActivity(intent);
            }
        });
    }

    private Button getCreateMealEventButton() {
        return getTypeSafeView(R.id.create_meal_event_button);
    }

    private Button getCreateMedicationEventButton() {
        return getTypeSafeView(R.id.create_medication_event_button);
    }

    private Button getCreateHealthScoreEventButton() {
        return getTypeSafeView(R.id.create_health_score_event_button);
    }

    private Button getCreateNoteEventButton() {
        return getTypeSafeView(R.id.create_note_event_button);
    }

    private <T extends View> T getTypeSafeView(int id) {
        return Utils.View.getTypeSafeView(getView(), id);
    }
}
