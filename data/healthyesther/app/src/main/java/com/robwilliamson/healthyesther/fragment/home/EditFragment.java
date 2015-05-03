package com.robwilliamson.healthyesther.fragment.home;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.ActivityGraphFragment;

public class EditFragment extends AbstractHomeFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_edit;
    }

    @Override
    public Query[] getQueries() {
        return getActivityGraphFragment().getQueries();
    }

    @Override
    public QueryUser[] getQueryUsers() {
        return queryUserArray(getActivityGraphFragment());
    }

    private ActivityGraphFragment getActivityGraphFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.activity_graph_fragment, ActivityGraphFragment.class);
    }
}
