package com.robwilliamson.healthyesther.dialog;

import android.content.Context;
import android.database.Cursor;
import android.widget.SeekBar;
import android.widget.TextView;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.HashMap;
import java.util.List;

public abstract class AddScoreDialog extends AbstractAddNamedDialog {
    HashMap<String, GetHealthScoresQuery.Score> mScores = null;

    protected AddScoreDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initialize();
    }

    @Override
    protected void onStart() {
        super.onStart();

        SeekBar bestValue = getBestValueWidget();
        bestValue.setMax(HealthScore.MAX - 1); // Seek bars are 0-based
        bestValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateBestValueCurrentValue();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        updateBestValueCurrentValue();
    }

    @Override
    protected Query getQuery() {
        return new GetHealthScoresQuery() {
            HashMap<String, Long> mSuggestions;
            @Override
            public void postQueryProcessing(final Cursor cursor) {
                List<Score> scoresList = scoresFrom(cursor);
                mSuggestions = new HashMap<String, Long>(scoresList.size());
                mScores = new HashMap<String, Score>(scoresList.size());

                for (Score score: scoresList) {
                    mSuggestions.put(score.name, score._id);
                    mScores.put(score.name, score);
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

    @Override
    protected int valueNameId() {
        return R.string.health_score;
    }

    @Override
    protected int valueCompletionHintId() {
        return R.string.descriptive_name_for_the_score;
    }

    @Override
    protected Integer contentLayoutId() {
        return R.layout.fragment_health_score_values;
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
        setTitle(R.string.track_another_score);
    }

    private void updateBestValueCurrentValue() {
        getBestValueCurrentValue().setText(String.valueOf(getBestValueWidget().getProgress() + 1));
    }

    private TextView getBestValueCurrentValue() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.best_value_current_value);
    }

    private SeekBar getBestValueWidget() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.best_value);
    }
}
