package com.robwilliamson.healthyesther.fragment.home;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.fragment.AddEventFragment;
import com.robwilliamson.healthyesther.fragment.DbFragment;

public class AddFragment extends DbFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_add;
    }

    private AddEventFragment getAddEventFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.add_event_fragment, AddEventFragment.class);
    }
}
