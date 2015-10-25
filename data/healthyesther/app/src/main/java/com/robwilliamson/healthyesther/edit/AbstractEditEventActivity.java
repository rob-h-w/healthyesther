package com.robwilliamson.healthyesther.edit;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.fragment.edit.EditEventFragment.Watcher;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.reminder.TimingManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public abstract class AbstractEditEventActivity extends DbActivity implements Watcher {
    private EventData mIntentEventData = null;

    protected abstract List<Pair<EditFragment, String>> getEditFragments(boolean create);

    protected abstract TransactionExecutor.Operation onModifySelected();

    protected abstract void onModifySelected(SQLiteDatabase db);

    protected abstract int getModifyFailedStringId();

    @Override
    public EventData getIntentEventData() {
        return mIntentEventData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mIntentEventData = EventData.from(extras, EventData.class);
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
            // Write to the DB and go back.
            query(new Query() {
                private boolean mFinish;

                @Override
                public Cursor query(SQLiteDatabase db) {
                    db.beginTransaction();
                    try {
                        onModifySelected(db);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    TimingManager.INSTANCE.onUserEntry(getApplicationContext());
                    return null;
                }

                @Override
                public void postQueryProcessing(Cursor cursor) {
                    TransactionExecutor.Operation operation = onModifySelected();
                    if (operation == null) {
                        mFinish = true;
                    } else {
                        getExecutor().perform(operation);
                    }
                }

                @Override
                public void onQueryComplete(Cursor cursor) {
                    if (mFinish) {
                        finish();
                    }
                }

                @Override
                public void onQueryFailed(Throwable error) {
                    Toast.makeText(
                            AbstractEditEventActivity.this,
                            getText(getModifyFailedStringId())
                                    + "/n" + Utils.format(error),
                            Toast.LENGTH_LONG).show();
                }
            });
            return true;
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
            if (!fragmentStringPair.first.validate()) {
                return false;
            }
        }

        return true;
    }

    protected final void resetFragments(@Nonnull List<Pair<EditFragment, String>> editFragments) {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (fragments != null) {
            for (Fragment fragment : fragments) {
                transaction.remove(fragment);
            }
        }

        for (Pair<EditFragment, String> pair : editFragments) {
            transaction.add(getActivityContentLayoutResourceId(), pair.first, pair.second);
        }

        transaction.commit();
    }
}
