package com.robwilliamson.healthyesther.fragment.home;

import com.robwilliamson.healthyesther.db.use.QueryUser;
import com.robwilliamson.healthyesther.db.use.QueryUserProvider;
import com.robwilliamson.healthyesther.fragment.AbstractQueryFragment;

import java.util.ArrayList;

public abstract class AbstractHomeFragment extends AbstractQueryFragment implements QueryUserProvider {

    protected static QueryUser[] queryUserArray(QueryUser... users) {
        ArrayList<QueryUser> queryUsers = new ArrayList<>(users.length);

        for (QueryUser user : users) {
            if (user != null) {
                queryUsers.add(user);
            }
        }

        QueryUser[] queryUsersArray = new QueryUser[queryUsers.size()];

        return queryUsers.toArray(queryUsersArray);
    }
}
