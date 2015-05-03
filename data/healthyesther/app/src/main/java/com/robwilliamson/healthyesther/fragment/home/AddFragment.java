package com.robwilliamson.healthyesther.fragment.home;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.ActivityGraphFragment;
import com.robwilliamson.healthyesther.fragment.AddEventFragment;

public class AddFragment extends AbstractHomeFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_add;
    }

    @Override
    public Query[] getQueries() {
        return getActivityGraphFragment().getQueries();
    }

    @Override
    public QueryUser[] getQueryUsers() {
        return queryUserArray(getActivityGraphFragment());
    }

    private AddEventFragment getAddEventFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.add_event_fragment, AddEventFragment.class);
    }

    private ActivityGraphFragment getActivityGraphFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.activity_graph_fragment, ActivityGraphFragment.class);
    }
}
