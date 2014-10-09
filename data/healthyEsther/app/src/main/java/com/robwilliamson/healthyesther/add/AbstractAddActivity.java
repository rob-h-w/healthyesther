package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;

import java.util.ArrayList;

public abstract class AbstractAddActivity extends DbActivity {
    public AbstractAddActivity(boolean runToCompletion) {
        super(runToCompletion);
    }

    protected abstract ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create);
    protected abstract void onModifySelected(SQLiteDatabase db);
    protected abstract int getModifyFailedStringId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        ArrayList<Pair<EditFragment, String>> fragments = getEditFragments(true);
        for (Pair<EditFragment, String> pair : fragments) {
            transaction.add(R.id.base_activity_content_layout, pair.first, pair.second);
        }
        transaction.commit();
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
                @Override
                public Cursor query(SQLiteDatabase db) {
                    db.beginTransaction();
                    try {
                        onModifySelected(db);
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }
                    return null;
                }

                @Override
                public void postQueryProcessing(Cursor cursor) {

                }

                @Override
                public void onQueryComplete(Cursor cursor) {
                    finish();
                }

                @Override
                public void onQueryFailed(Throwable error) {
                    Toast.makeText(AbstractAddActivity.this, getText(getModifyFailedStringId()), Toast.LENGTH_SHORT).show();
                }
            });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ArrayList<Pair<EditFragment, String>> fragments = getEditFragments(false);
        for (Pair<EditFragment, String> fragmentStringPair : fragments) {
            if (!fragmentStringPair.first.validate()) {
                return false;
            }
        }

        return true;
    }
}
