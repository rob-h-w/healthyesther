/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.edit;

import android.os.Bundle;
import android.text.Editable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.robwilliamson.healthyesther.DbActivity;
import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.HealthScoreJudgmentRangeTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EditScoreFragment extends SuggestionEditFragment<HealthScoreTable.Row> {
    public static final int MAX = 5;
    private static final String SCORES = "scores";
    private static final String INITIAL_SCORE = "initial_score";
    private HealthScoreTable.Row mInitialScore = null;
    private HashMap<String, HealthScoreTable.Row> mScores = null;

    public EditScoreFragment() {
        super();
    }

    @Override
    public void onResume() {
        super.onResume();

        Utils.checkNotNull(getTypeSafeView(R.id.name_title, TextView.class)).setText(R.string.score_name);
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
                    HealthScoreTable.Row[] rows = DatabaseAccessor.HEALTH_SCORE_TABLE.select(database, WhereContains.any());
                    mScores = new HashMap<>(rows.length);
                    for (HealthScoreTable.Row row : rows) {
                        mScores.put(row.getName(), row);
                    }
                }
            });
        }
    }

    @Nullable
    @Override
    protected HealthScoreTable.Row createRow() {
        if (!isResumed()) {
            return null;
        }
        return new HealthScoreTable.Row(getName(), getRandomQuery(), getMaxLabel(), getMinLabel());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_health_score_values;
    }

    @Override
    @Nullable
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.autocomplete_name, AutoCompleteTextView.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(SCORES, mScores);
        outState.putSerializable(INITIAL_SCORE, mInitialScore);
    }

    @Nonnull
    public List<HealthScoreJudgmentRangeTable.Row> getJudgments() {
        List<HealthScoreJudgmentRangeTable.Row> rows = new ArrayList<>();
        HealthScoreTable.Row scoreRow = getRow();

        if (scoreRow == null) {
            return rows;
        }

        SeekBar bestScoreBar = Utils.checkNotNull(getBestScoreBar());

        rows.add(new HealthScoreJudgmentRangeTable.Row(
                scoreRow,
                (long) bestScoreBar.getProgress(),
                null,
                null));

        return rows;
    }

    @Nullable
    @Override
    public HealthScoreTable.Row getRow() {
        if (mScores == null) {
            return null;
        }

        HealthScoreTable.Row row = getRow(mScores, getName(), new NameComparator<HealthScoreTable.Row>() {
            @Override
            public boolean equals(@Nonnull HealthScoreTable.Row row, @Nonnull String name) {
                return row.getName().equals(name);
            }
        });

        if (row != null) {
            row.setMaxLabel(getMaxLabel());
            row.setMinLabel(getMinLabel());
            row.setRandomQuery(getRandomQuery());
        }

        return row;
    }

    @Override
    public void setRow(@Nonnull HealthScoreTable.Row row) {
        super.setRow(row);

        setName(row.getName());
        setRandomQuery(row.getRandomQuery());
        setMaxLabel(row.getMaxLabel());
        setMinLabel(row.getMinLabel());
    }

    @Override
    protected void updateAttachedActivity() {
        HealthScoreTable.Row row = getRow();

        if (row != null) {
            populateFromUi(row);
        }

        super.updateAttachedActivity();
    }

    private void populateFromUi(@Nonnull HealthScoreTable.Row row) {
        row.setName(getName());
        row.setRandomQuery(getRandomQuery());
        row.setMaxLabel(getMaxLabel());
        row.setMinLabel(getMinLabel());
    }

    @Nonnull
    private String getName() {
        AutoCompleteTextView autoCompleteTextView = getNameView();

        if (autoCompleteTextView == null) {
            return "";
        }

        Editable editable = autoCompleteTextView.getText();

        if (editable == null) {
            return "";
        }

        return getNameView().getText().toString().trim();
    }

    private void setName(@Nonnull String name) {
        HealthScoreTable.Row row = getRow();
        if (row != null) {
            row.setName(name);
        }

        AutoCompleteTextView autoCompleteTextView = getNameView();
        if (autoCompleteTextView != null) {
            autoCompleteTextView.getEditableText().clear();
            autoCompleteTextView.getEditableText().append(name);
        }
    }

    private boolean getRandomQuery() {
        return getRandomQueryWidget().isChecked();
    }

    private void setRandomQuery(boolean value) {
        getRandomQueryWidget().setChecked(value);
    }

    @Nonnull
    private Switch getRandomQueryWidget() {
        return Utils.checkNotNull(Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.random_query, Switch.class));
    }

    private String getMinLabel() {
        return getMinLabelWidget().getText().toString().trim();
    }

    private void setMinLabel(String value) {
        getMinLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMinLabelWidget() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.min_label, EditText.class);
    }

    private String getMaxLabel() {
        return getMaxLabelWidget().getText().toString().trim();
    }

    private void setMaxLabel(String value) {
        getMaxLabelWidget().setText(value == null ? "" : value);
    }

    private EditText getMaxLabelWidget() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.max_label, EditText.class);
    }

    @Nullable
    private SeekBar getBestScoreBar() {
        return Utils.View.getTypeSafeView(Utils.checkNotNull(getView()), R.id.best_value, SeekBar.class);
    }

    @Override
    public boolean isValid() {
        HealthScoreTable.Row healthScore = getRow();

        return healthScore != null && super.isValid() && getRow().isValid();
    }

    public interface Watcher {
        @SuppressWarnings("unused")
        void onScoreModified(@Nonnull HealthScoreTable.Row score);
    }
}
