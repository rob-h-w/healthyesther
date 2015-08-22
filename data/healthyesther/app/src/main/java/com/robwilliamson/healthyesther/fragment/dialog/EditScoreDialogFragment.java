package com.robwilliamson.healthyesther.fragment.dialog;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.data.DataAbstraction;
import com.robwilliamson.healthyesther.db.definition.HealthScore;
import com.robwilliamson.healthyesther.db.use.GetHealthScoresQuery;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueuedQueryExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditScoreDialogFragment extends AbstractAddNamedDialogFragment {
    private static final String SCORES = "scores";
    private static final String INITIAL_SCORE = "initial_score";
    private HealthScore.Value mInitialScore = null;
    private HashMap<String, HealthScore.Value> mScores = null;
    public EditScoreDialogFragment() {
        super();
    }

    public static EditScoreDialogFragment createDialog(HealthScore.Value score) {
        EditScoreDialogFragment dialog = new EditScoreDialogFragment();
        dialog.mInitialScore = score;
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && mScores == null) {
            mScores = Utils.Bundles.get(savedInstanceState, SCORES, new Utils.Bundles.HashGetter<HealthScore.Value>() {
                @Override
                public HealthScore.Value get(Bundle bundle, String bundleKey) {
                    return DataAbstraction.from(bundle.getBundle(bundleKey), HealthScore.Value.class);
                }
            });

            mInitialScore = DataAbstraction.from(savedInstanceState.getBundle(INITIAL_SCORE), HealthScore.Value.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        getDialog().setTitle(R.string.track_another_score);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

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
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Utils.Bundles.put(outState, SCORES, mScores, new Utils.Bundles.HashPutter() {
            @Override
            public void put(Bundle bundle, String bundleKey, String key) {
                mScores.get(key).bundle(bundle, bundleKey);
            }
        });

        mInitialScore.bundle(outState, INITIAL_SCORE);
    }

    @Override
    protected Query getQuery() {
        return new GetHealthScoresQuery() {
            HashMap<String, Long> mSuggestions;

            @Override
            public void postQueryProcessing(final Cursor cursor) {
                List<HealthScore.Value> scoresList = DataAbstraction.listFrom(cursor, HealthScore.Value.class);
                mSuggestions = new HashMap<>(scoresList.size());
                mScores = new HashMap<>(scoresList.size());

                for (HealthScore.Value score : scoresList) {
                    mSuggestions.put(score.name, score._id);
                    mScores.put(score.name, score);
                }
            }

            @Override
            public void onQueryComplete(final Cursor cursor) {
                setSuggestions(mSuggestions);
                EditScoreDialogFragment.this.queryComplete(cursor);
            }

            @Override
            public void onQueryFailed(final Throwable error) {
                EditScoreDialogFragment.this.queryFailed(error);
            }
        };
    }

    @Override
    protected void doQuery(Query query) {
        ArrayList<Query> queries = new ArrayList<>(1);
        queries.add(query);
        getWatcher().enqueueQueries(queries);
    }

    @Override
    protected void newNameEntered(String name) {
        updateValid();
    }

    @Override
    protected void queryComplete(Cursor result) {
    }

    @Override
    protected void queryFailed(Throwable error) {
        getWatcher().onQueryFailed(this, error);
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

    @Override
    protected void onOk() {
        HealthScore.Value score = scoreFromUi();

        if (scoreModified(score)) {
            getWatcher().onScoreModified(score);
        }
    }

    private boolean scoreModified(HealthScore.Value score) {
        HealthScore.Value listMatch = mScores.get(score.name);

        return !score.equals(listMatch);
    }

    private void updateUiToMatch(HealthScore.Value score) {
        setName(score.name);
        setBestValue(score.bestValue);
        setRandomQuery(score.randomQuery);
        setMinLabel(score.minLabel);
        setMaxLabel(score.maxLabel);
    }

    private HealthScore.Value scoreFromUi() {
        HealthScore.Value score = new HealthScore.Value();
        score.name = getName();

        if (mScores.containsKey(score.name)) {
            score._id = mScores.get(score.name)._id;
        }

        score.bestValue = getBestValue();
        score.randomQuery = getRandomQuery();
        score.minLabel = getMinLabel();
        score.maxLabel = getMaxLabel();

        if (mInitialScore != null && mInitialScore._id != 0) {
            score._id = mInitialScore._id;
        }

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
        return Utils.View.getTypeSafeView(getView(), R.id.best_value_current_value, TextView.class);
    }

    private SeekBar getBestValueWidget() {
        return Utils.View.getTypeSafeView(getView(), R.id.best_value, SeekBar.class);
    }

    private boolean getRandomQuery() {
        return getRandomQueryWidget().isChecked();
    }

    private void setRandomQuery(boolean value) {
        getRandomQueryWidget().setChecked(value);
    }

    private Switch getRandomQueryWidget() {
        return Utils.View.getTypeSafeView(getView(), R.id.random_query, Switch.class);
    }

    private String getMinLabel() {
        return getMinLabelWidget().getText().toString();
    }

    private void setMinLabel(String value) {
        getMinLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMinLabelWidget() {
        return Utils.View.getTypeSafeView(getView(), R.id.min_label, EditText.class);
    }

    private String getMaxLabel() {
        return getMaxLabelWidget().getText().toString();
    }

    private void setMaxLabel(String value) {
        getMaxLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMaxLabelWidget() {
        return Utils.View.getTypeSafeView(getView(), R.id.max_label, EditText.class);
    }

    private Watcher getWatcher() {
        return (Watcher) getActivity();
    }

    public interface Watcher extends QueuedQueryExecutor {
        void onScoreModified(HealthScore.Value score);

        void onQueryFailed(EditScoreDialogFragment fragment, Throwable error);
    }
}
