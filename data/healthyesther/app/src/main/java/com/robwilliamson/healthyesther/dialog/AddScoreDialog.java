package com.robwilliamson.healthyesther.dialog;

import android.content.Context;
import android.database.Cursor;

import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;

import java.util.HashMap;
import java.util.List;

public abstract class AddScoreDialog extends AbstractAddNamedDialog {

    protected AddScoreDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize();
    }

    @Override
    protected Query getQuery() {
        return new GetHealthScoresQuery() {
            HashMap<String, Long> mSuggestions;
            @Override
            public void postQueryProcessing(final Cursor cursor) {
                List<Score> scoresList = scoresFrom(cursor);
                mSuggestions = new HashMap<String, Long>(scoresList.size());

                for (Score score: scoresList) {
                    mSuggestions.put(score.name, score._id);
                }
            }

            @Override
            public void onQueryComplete(final Cursor cursor) {
                setSuggestions(mSuggestions);
                AddScoreDialog.this.queryComplete(cursor);
            }

            @Override
            public void onQueryFailed(final Throwable error) {
                AddScoreDialog.this.queryFailed(error);
            }
        };
    }

    @Override
    protected void newNameEntered(String name) {

    }

    @Override
    protected void queryComplete(Cursor result) {

    }

    @Override
    protected void suggestionSelected(String name, long id) {

    }

    public AddScoreDialog(Context context, int theme) {
        super(context, theme);
        initialize();
    }

    public AddScoreDialog(Context context) {
        super(context);
        initialize();
    }

    private void initialize() {
        setContentView(R.layout.dialog_add_score);
    }
}
