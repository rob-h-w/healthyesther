/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.fragment.edit;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.generated.MedicationTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.WhereContains;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;
import com.robwilliamson.healthyesther.db.use.InitializationQuerier;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EditMedicationFragment extends SuggestionEditFragment<MedicationTable.Row>
        implements InitializationQuerier {

    private static final String NAME_TO_ROW_MAP = "name_to_row_map";
    private Map<String, MedicationTable.Row> mNameToRowMap = new HashMap<>();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_medication;
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
            mNameToRowMap = (Map<String, MedicationTable.Row>) savedInstanceState.getSerializable(NAME_TO_ROW_MAP);
        }
    }

    @Nonnull
    @Override
    public MedicationTable.Row getRow() {
        final String name = getName();
        return getRow(mNameToRowMap, getName(), new NameComparator<MedicationTable.Row>() {
            @Override
            public boolean equals(@Nonnull MedicationTable.Row row, @Nonnull String name) {
                return name.equals(row.getName());
            }
        });
    }

    @Override
    public void setRow(@Nonnull MedicationTable.Row row) {
        super.setRow(row);
        Editable editable = getNameView().getEditableText();
        editable.clear();
        editable.append(row.getName());
    }

    @Override
    protected void onNameChanged() {
        super.onNameChanged();

        getRow().setName(getName());
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link android.support.v4.app.Fragment#onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>This corresponds to {@link Activity#onSaveInstanceState(Bundle)
     * Activity.onSaveInstanceState(Bundle)} and most of the discussion there
     * applies here as well.  Note however: <em>this method may be called
     * at any time before {@link #onDestroy()}</em>.  There are many situations
     * where a fragment may be mostly torn down (such as when placed on the
     * back stack with no UI showing), but its state will not be saved until
     * its owning activity actually needs to save its state.
     *
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(NAME_TO_ROW_MAP, (Serializable) mNameToRowMap);
    }

    @Nullable
    @Override
    protected MedicationTable.Row createRow() {
        String name = getName();
        MedicationTable.Row row = new MedicationTable.Row(name);
        mNameToRowMap.put(name, row);
        return row;
    }

    @Nonnull
    public String getName() {
        if (getNameView() != null) {
            return getNameView().getText().toString();
        }

        return "";
    }

    @Override
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.medication_name, AutoCompleteTextView.class);
    }

    @Nonnull
    @Override
    public TransactionExecutor.Operation getInitializationQuery() {
        return new TransactionExecutor.Operation() {
            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                MedicationTable.Row[] rows = DatabaseAccessor.MEDICATION_TABLE.select(database, WhereContains.any());

                Map<String, Long> suggestionIds = new HashMap<>();
                for (MedicationTable.Row row : rows) {
                    String name = row.getName();
                    suggestionIds.put(name, row.getConcretePrimaryKey().getId());
                    mNameToRowMap.put(name, row);
                }

                setSuggestionIds(suggestionIds);
            }
        };
    }
}
