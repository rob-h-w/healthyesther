package com.robwilliamson.healthyesther.fragment.home;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.fragment.DbFragment;
import com.robwilliamson.healthyesther.fragment.edit.EventHistoryListFragment;

public class EditFragment extends DbFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_edit;
    }

    private EventHistoryListFragment getEventHistoryFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.event_list_fragment, EventHistoryListFragment.class);
    }
}
