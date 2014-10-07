package com.robwilliamson.healthyesther.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.add.Meal;

public class AddEventFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(
                R.layout.fragment_add_event, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        getCreateMealEventButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Meal.class);
                getActivity().startActivity(intent);
            }
        });

        getCreateMedicationEventButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "TODO: implement add medication.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Button getCreateMealEventButton() {
        return getTypeSafeView(R.id.create_meal_event_button);
    }

    private Button getCreateMedicationEventButton() {
        return getTypeSafeView(R.id.create_medication_event_button);
    }

    private <T extends View> T getTypeSafeView(int id) {
        return Utils.View.getTypeSafeView(getView(), id);
    }
}
