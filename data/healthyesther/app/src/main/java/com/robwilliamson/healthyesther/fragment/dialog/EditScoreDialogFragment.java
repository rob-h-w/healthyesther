package com.robwilliamson.healthyesther.fragment.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;

import java.util.HashMap;

import javax.annotation.Nonnull;

public class EditScoreDialogFragment extends AbstractAddNamedDialogFragment {
    public static final int MAX = 5;
    private static final String SCORES = "scores";
    private static final String INITIAL_SCORE = "initial_score";
    private HealthScoreTable.Row mInitialScore = null;
    private HashMap<String, HealthScoreTable.Row> mScores = null;

    public EditScoreDialogFragment() {
        super();
    }

    public static EditScoreDialogFragment createDialog(@Nonnull HealthScoreTable.Row score) {
        EditScoreDialogFragment dialog = new EditScoreDialogFragment();
        dialog.mInitialScore = score;
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null && mScores == null) {
            //noinspection unchecked
            mScores = (HashMap<String, HealthScoreTable.Row>) savedInstanceState.getSerializable(SCORES);

            mInitialScore = (HealthScoreTable.Row) savedInstanceState.getSerializable(INITIAL_SCORE);
        }

        if (mScores == null) {
            ((DbActivity) getActivity()).getExecutor().perform(new TransactionExecutor.Operation() {
                @Override
                public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                    HealthScoreTable.Row[] rows = DatabaseAccessor.HEALTH_SCORE_TABLE.select(database, WhereContains.all());
                    mScores = new HashMap<String, HealthScoreTable.Row>(rows.length);
                    for (HealthScoreTable.Row row : rows) {
                        mScores.put(row.getName(), row);
                    }
                }
            });
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
        bestValue.setMax(MAX - 1); // Seek bars are 0-based
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

        outState.putSerializable(SCORES, mScores);
        outState.putSerializable(INITIAL_SCORE, mInitialScore);
    }

    @Override
    protected void newNameEntered(String name) {
        updateValid();
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
        HealthScoreTable.Row score = scoreFromUi();

        if (scoreModified(score)) {
            getWatcher().onScoreModified(score);
        }
    }

    private boolean scoreModified(@Nonnull HealthScoreTable.Row score) {
        HealthScoreTable.Row listMatch = mScores.get(score.getName());

        return !score.equals(listMatch);
    }

    private void updateUiToMatch(@Nonnull HealthScoreTable.Row score) {
        setName(score.getName());
        setBestValue(score.getBestValue());
        setRandomQuery(score.getRandomQuery());
        setMinLabel(score.getMinLabel());
        setMaxLabel(score.getMaxLabel());
    }

    private HealthScoreTable.Row scoreFromUi() {
        final String name = getName();
        HealthScoreTable.Row score;

        if (mScores.containsKey(name)) {
            score = mScores.get(name);
        } else {
            score = new HealthScoreTable.Row(
                    getBestValue(),
                    getName(),
                    getRandomQuery(),
                    getMaxLabel(),
                    getMinLabel()
            );
        }

        if (mInitialScore != null) {
            score = mInitialScore;
        }

        score.setBestValue(getBestValue());
        score.setName(name);
        score.setRandomQuery(getRandomQuery());
        score.setMaxLabel(getMaxLabel());
        score.setMinLabel(getMinLabel());

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

    private void setBestValue(long value) {
        getBestValueWidget().setProgress((int) value - 1);
    }

    private TextView getBestValueCurrentValue() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.best_value_current_value, TextView.class);
    }

    private SeekBar getBestValueWidget() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.best_value, SeekBar.class);
    }

    private boolean getRandomQuery() {
        return getRandomQueryWidget().isChecked();
    }

    private void setRandomQuery(boolean value) {
        getRandomQueryWidget().setChecked(value);
    }

    private Switch getRandomQueryWidget() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.random_query, Switch.class);
    }

    private String getMinLabel() {
        return getMinLabelWidget().getText().toString();
    }

    private void setMinLabel(String value) {
        getMinLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMinLabelWidget() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.min_label, EditText.class);
    }

    private String getMaxLabel() {
        return getMaxLabelWidget().getText().toString();
    }

    private void setMaxLabel(String value) {
        getMaxLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMaxLabelWidget() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.max_label, EditText.class);
    }

    private Watcher getWatcher() {
        return (Watcher) getActivity();
    }

    public interface Watcher {
        void onScoreModified(@Nonnull HealthScoreTable.Row score);
    }
}
