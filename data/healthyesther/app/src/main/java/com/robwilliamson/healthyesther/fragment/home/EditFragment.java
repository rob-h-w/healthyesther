package com.robwilliamson.healthyesther.fragment.home;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.fragment.ActivityGraphFragment;
import com.robwilliamson.healthyesther.fragment.edit.EventHistoryListFragment;

import java.util.Arrays;
import java.util.List;

public class EditFragment extends AbstractHomeFragment {
    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_home_edit;
    }

    @Override
    public Query[] getQueries() {
        List<Query> queries = Arrays.asList(getActivityGraphFragment().getQueries());
        queries.addAll(Arrays.asList(getEventHistoryFragment().getQueries()));
        Query[] queryArray = new Query[queries.size()];
        return queries.toArray(queryArray);
    }

    @Override
    public QueryUser[] getQueryUsers() {
        return queryUserArray(getActivityGraphFragment());
    }

    private ActivityGraphFragment getActivityGraphFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.activity_graph_fragment, ActivityGraphFragment.class);
    }

    private EventHistoryListFragment getEventHistoryFragment() {
        return Utils.View.getTypeSafeFragment(getChildFragmentManager(), R.id.event_list_fragment, EventHistoryListFragment.class);
    }
}
