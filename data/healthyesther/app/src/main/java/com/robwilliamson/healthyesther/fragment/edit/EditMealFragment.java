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
    private static final String OLD_ROW = "Old Row";

    @Nonnull
    private Map<String, MealTable.Row> mNameToRowMap = new HashMap<>();

    @Nullable
    private MealTable.Row mOldRow = null;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_meal;
    }

    /**
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.  It is also useful for fragments that use
     * {@link #setRetainInstance(boolean)} to retain their instance,
     * as this callback tells the fragment when it is fully associated with
     * the new activity instance.  This is called after {@link #onCreateView}
     * and before {@link #onViewStateRestored(Bundle)}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            //noinspection unchecked
            Map<String, MealTable.Row> nameToRowMap = (Map<String, MealTable.Row>) savedInstanceState.getSerializable(NAME_TO_ROW_MAP);
            mNameToRowMap = nameToRowMap == null ? new HashMap<String, MealTable.Row>() : nameToRowMap;

            if (savedInstanceState.containsKey(OLD_ROW)) {
                mOldRow = (MealTable.Row) savedInstanceState.getSerializable(OLD_ROW);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(NAME_TO_ROW_MAP, (Serializable) mNameToRowMap);

        if (mOldRow != null) {
            outState.putSerializable(OLD_ROW, mOldRow);
        }
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

    @Nonnull
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
        String name = getName();

        if (mNameToRowMap.containsKey(name)) {
            MealTable.Row row = mNameToRowMap.get(name);

            if (row != null) {
                super.setRow(row);
                return row;
            }
        }

        if (hasRow()) {
            //noinspection ConstantConditions
            return super.getRow();
        }

        return createRow();
    }

    public void setRow(@NonNull final MealTable.Row row) {
        super.setRow(row);
        mOldRow = row;
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.checkNotNull(getNameView()).setText(row.getName());
            }
        });
    }

    @Nullable
    public MealTable.Row getOldRow() {
        return mOldRow;
    }
}
