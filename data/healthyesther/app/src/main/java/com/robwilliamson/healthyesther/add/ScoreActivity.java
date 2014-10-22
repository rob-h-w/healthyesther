package com.robwilliamson.healthyesther.add;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;
import android.widget.Toast;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.db.use.QueryUser;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.fragment.edit.EditFragment;
import com.robwilliamson.healthyesther.fragment.edit.EditScoreFragment;

import java.util.ArrayList;

public class ScoreActivity extends AbstractAddActivity implements QueryUser {
    private ArrayList<Pair<EditFragment, String>> mFragments;

    @Override
    protected ArrayList<Pair<EditFragment, String>> getEditFragments(boolean create) {
        return mFragments;
    }

    @Override
    protected void onModifySelected(SQLiteDatabase db) {

    }

    @Override
    protected int getModifyFailedStringId() {
        return 0;
    }

    @Override
    protected QueryUser[] getOnResumeQueryUsers() {
        return new QueryUser[0];
    }

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new GetHealthScoresQuery() {

                    private ArrayList<Pair<EditFragment, String>> mFragments;

                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mFragments = new ArrayList<Pair<EditFragment, String>>(cursor.getCount());
                        final int rowIdIndex = cursor.getColumnIndex(HealthScore._ID);
                        final int nameIndex = cursor.getColumnIndex(HealthScore.NAME);
                        final int bestValueIndex = cursor.getColumnIndex(HealthScore.BEST_VALUE);
                        final int minLabelIndex = cursor.getColumnIndex(HealthScore.MIN_LABEL);
                        final int maxLabelIndex = cursor.getColumnIndex(HealthScore.MAX_LABEL);

                        if (cursor.moveToFirst()) {
                            do {
                                mFragments.add(new Pair<EditFragment, String>(
                                        EditScoreFragment.newInstance(
                                                cursor.getLong(rowIdIndex),
                                                cursor.getString(nameIndex),
                                                cursor.getInt(bestValueIndex),
                                                cursor.getString(minLabelIndex),
                                                cursor.getString(maxLabelIndex)),
                                        cursor.getString(nameIndex)));
                            } while(cursor.moveToNext());
                        }
                    }

                    @Override
                    public void onQueryComplete(Cursor cursor) {
                        ScoreActivity.this.mFragments = mFragments;
                        ScoreActivity.this.resetFragments(getEditFragments(true));
                    }

                    @Override
                    public void onQueryFailed(Throwable error) {
                        Toast.makeText(ScoreActivity.this, getText(R.string.could_not_get_score_type_list), Toast.LENGTH_SHORT).show();
                    }
                }
        };
    }
}
