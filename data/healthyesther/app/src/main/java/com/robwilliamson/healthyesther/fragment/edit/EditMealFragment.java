package com.robwilliamson.healthyesther.fragment.edit;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.use.InitializationQuerier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.robwilliamson.healthyesther.db.includes.WhereContains.any;

public class EditMealFragment extends SuggestionEditFragment<MealTable.Row>
        implements InitializationQuerier {

    private static final String NAME_TO_ROW_MAP = "name to row map";

    @Nonnull
    private Map<String, MealTable.Row> mNameToRowMap = new HashMap<>();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_meal;
    }

    /**
     * Called when all saved state has been restored into the view hierarchy
     * of the fragment.  This can be used to do initialization based on saved
     * state that you are letting the view hierarchy track itself, such as
     * whether check box widgets are currently checked.  This is called
     * after {@link #onActivityCreated(Bundle)} and before
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            //noinspection unchecked
            Map<String, MealTable.Row> nameToRowMap = (Map<String, MealTable.Row>) savedInstanceState.getSerializable(NAME_TO_ROW_MAP);
            mNameToRowMap = nameToRowMap == null ? new HashMap<String, MealTable.Row>() : nameToRowMap;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(NAME_TO_ROW_MAP, (Serializable) mNameToRowMap);
    }

    @Override
    public void onResume() {
        super.onResume();

        Utils.checkNotNull(getNameView()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateAttachedActivity();
            }

            @Override
            public void afterTextChanged(Editable s) {
                getRow().setName(s.toString());
            }
        });

        getNameView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateAttachedActivity();
            }
        });
    }

    @Nullable
    @Override
    protected MealTable.Row createRow() {
        String name = getName();
        MealTable.Row row = new MealTable.Row(name);
        mNameToRowMap.put(name, row);
        return row;
    }

    @Nonnull
    public String getName() {
        if (getNameView() == null) {
            return "";
        }

        return getNameView().getText().toString();
    }

    @Override
    @Nullable
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.meal_name, AutoCompleteTextView.class);
    }

    @Nonnull
    @Override
    public TransactionExecutor.Operation getInitializationQuery() {
        return new TransactionExecutor.Operation() {

            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                MealTable.Row[] rows = DatabaseAccessor.MEAL_TABLE.select(database, any());

                final Map<String, Long> suggestionIds = new HashMap<>();
                for (MealTable.Row row : rows) {
                    final String name = row.getName();
                    suggestionIds.put(name, row.getConcretePrimaryKey().getId());
                    mNameToRowMap.put(name, row);
                }

                setSuggestionIds(suggestionIds);
            }
        };
    }

    @Nonnull
    public MealTable.Row getRow() {
        return Utils.checkNotNull(getRow(mNameToRowMap, getName(), new NameComparator<MealTable.Row>() {
            @Override
            public boolean equals(@Nonnull MealTable.Row row, @Nonnull String name) {
                return name.equals(row.getName());
            }
        }));
    }

    public void setRow(@NonNull final MealTable.Row row) {
        super.setRow(row);

        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.checkNotNull(getNameView()).setText(row.getName());
            }
        });
    }
}
