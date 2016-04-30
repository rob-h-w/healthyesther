package com.robwilliamson.healthyesther.edit;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;

import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.use.InitializationQuerier;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class AbstractEditEventActivity extends DbActivity {

    protected abstract List<Pair<EditFragment, String>> getEditFragments(boolean create);

    protected abstract TransactionExecutor.Operation onModifySelected();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        resetFragments(getEditFragments(true));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_modify) {
            TransactionExecutor.Operation operation = onModifySelected();
            if (operation == null) {
                finish();
            } else {
                getExecutor().perform(operation);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isBusy()) {
            return false;
        }

        List<Pair<EditFragment, String>> fragments = getEditFragments(false);
        for (Pair<EditFragment, String> fragmentStringPair : fragments) {
            if (!fragmentStringPair.first.isValid()) {
                return false;
            }
        }

        return true;
    }

    protected final void resetFragments(@Nonnull List<Pair<EditFragment, String>> editFragments) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        //noinspection StatementWithEmptyBody
        while (getSupportFragmentManager().popBackStackImmediate()) ;

        if (fragments != null) {
            for (Fragment fragment : fragments) {
                transaction.remove(fragment);
            }
        }

        final List<TransactionExecutor.Operation> operations = new ArrayList<>();

        for (Pair<EditFragment, String> pair : editFragments) {
            transaction.add(getActivityContentLayoutResourceId(), pair.first, pair.second);
            if (pair.first instanceof InitializationQuerier) {
                InitializationQuerier querier = (InitializationQuerier) pair.first;
                TransactionExecutor.Operation operation = querier.getInitializationQuery();
                operations.add(operation);
            }
        }

        getExecutor().perform(new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                for (TransactionExecutor.Operation operation : operations) {
                    operation.doTransactionally(database, transaction);
                }
            }
        });

        transaction.commit();
    }
}
