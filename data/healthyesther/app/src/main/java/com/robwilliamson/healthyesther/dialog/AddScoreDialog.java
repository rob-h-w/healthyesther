package com.robwilliamson.healthyesther.dialog;

import android.content.Context;
import android.database.Cursor;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.robwilliamson.db.definition.HealthScore;
import com.robwilliamson.db.use.GetHealthScoresQuery;
import com.robwilliamson.db.use.Query;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;

import java.util.HashMap;
import java.util.List;

public abstract class AddScoreDialog extends AbstractAddNamedDialog {
    private GetHealthScoresQuery.Score mInitialScore = null;
    private HashMap<String, GetHealthScoresQuery.Score> mScores = null;

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

        if (mInitialScore != null) {
            updateUiToMatch(mInitialScore);
            mInitialScore = null;
        }
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
        updateValid();
    }

    @Override
    protected void queryComplete(Cursor result) {
    }

    @Override
    protected void suggestionSelected(String name, long id) {
        updateUiToMatch(mScores.get(name));
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

    public AddScoreDialog(Context context) {
        super(context);
        initialize();
    }

    public AddScoreDialog(Context context, GetHealthScoresQuery.Score score) {
        super(context);
        mInitialScore = score;
    }

    private void initialize() {
        setTitle(R.string.track_another_score);
    }

    private void updateUiToMatch(GetHealthScoresQuery.Score score) {
        setName(score.name);
        setBestValue(score.bestValue);
        setRandomQuery(score.randomQuery);
        setMinLabel(score.minLabel);
        setMaxLabel(score.maxLabel);
    }

    private GetHealthScoresQuery.Score scoreFromUi() {
        GetHealthScoresQuery.Score score = new GetHealthScoresQuery.Score();
        score.name = getName();

        if (mScores.containsKey(score.name)) {
            score._id = mScores.get(score.name)._id;
        }

        score.bestValue = getBestValue();
        score.randomQuery = getRandomQuery();
        score.minLabel = getMinLabel();
        score.maxLabel = getMaxLabel();
        return score;
    }

    private void updateBestValueCurrentValue() {
        getBestValueCurrentValue().setText(String.valueOf(getBestValue()));
    }

    private void updateValid() {
        setValid(!getName().isEmpty());
    }

    private int getBestValue() {
        return getBestValueWidget().getProgress() + 1;
    }

    private void setBestValue(int value) {
        getBestValueWidget().setProgress(value - 1);
    }

    private TextView getBestValueCurrentValue() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.best_value_current_value);
    }

    private SeekBar getBestValueWidget() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.best_value);
    }

    private boolean getRandomQuery() {
        return getRandomQueryWidget().isChecked();
    }

    private void setRandomQuery(boolean value) {
        getRandomQueryWidget().setChecked(value);
    }

    private Switch getRandomQueryWidget() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.random_query);
    }

    private String getMinLabel() {
        return getMinLabelWidget().getText().toString();
    }

    private void setMinLabel(String value) {
        getMinLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMinLabelWidget() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.min_label);
    }

    private String getMaxLabel() {
        return getMaxLabelWidget().getText().toString();
    }

    private void setMaxLabel(String value) {
        getMaxLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMaxLabelWidget() {
        return Utils.View.getTypeSafeView(getWindow().getDecorView(), R.id.max_label);
    }
}
