package com.robwilliamson.healthyesther.fragment.edit;


import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.data.MealData;
import com.robwilliamson.healthyesther.db.data.MealEventData;
import com.robwilliamson.healthyesther.db.definition.Meal;
import com.robwilliamson.healthyesther.db.definition.MealEvent;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Transaction;
import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;
import com.robwilliamson.healthyesther.db.includes.Where;
import com.robwilliamson.healthyesther.db.use.GetOneWithColumnIdQuery;
import com.robwilliamson.healthyesther.db.use.GetOneWithEventIdQuery;
import com.robwilliamson.healthyesther.db.use.InitializationQuerier;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueuedQueryExecutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;

public class EditMealFragment extends SuggestionEditFragment<EditMealFragment.Watcher> implements InitializationQuerier<MealTable.Row> {

    private static final String NAME_TO_ROW_MAP = "name to row map";

    private EventData mEventData;
    private MealEventData mMealEventData;
    private MealData mMealData;
    private Map<String, MealTable.Row> mNameToRowMap = new HashMap<>();
    private MealTable.Row mRow;

    public EditMealFragment() {
        super(EditMealFragment.Watcher.class);
    }

    public void setEventData(EventData eventData) {
        this.mEventData = eventData;

        if (mEventData.get_id() != null && mEventData.get_id() != 0L) {
            callWatcher(new WatcherCaller<Watcher>() {
                @Override
                public void call(Watcher watcher) {
                    ArrayList<Query> queries = new ArrayList<>();
                    queries.addAll(Arrays.asList(getQueries()));
                    queries.add(new GetOneWithEventIdQuery(mEventData.get_id()) {
                        List<MealEventData> mMealEvents;

                        @Override
                        public String getTableName() {
                            return MealEvent.TABLE_NAME;
                        }

                        @Override
                        protected void postQueryProcessOne(Cursor cursor) {
                            mMealEvents = MealEventData.listFrom(cursor, MealEventData.class);
                        }

                        @Override
                        public void onQueryComplete(Cursor cursor) {
                            if (mMealEvents.isEmpty()) {
                                mMealEventData = null;
                            } else if (mMealEvents.size() == 1) {
                                mMealEventData = mMealEvents.get(0);
                            }

                            requestMeal();
                        }

                        @Override
                        public void onQueryFailed(Throwable error) {
                            reportQueryFailed(error);
                        }
                    });
                    watcher.enqueueQueries(queries);
                }
            });
        }
    }

    private void requestMeal() {
        callWatcher(new WatcherCaller<Watcher>() {
            @Override
            public void call(Watcher watcher) {
                watcher.enqueueQueries(Arrays.asList(new Query[]{
                        new GetOneWithColumnIdQuery(mMealEventData.getMealId()) {
                            private MealData mMealData;

                            @Override
                            protected void postQueryProcessOne(Cursor cursor) {
                                List<MealData> meals = MealData.listFrom(cursor, MealData.class);
                                mMealData = meals.get(0);
                            }

                            @Override
                            public String getIdColumnName() {
                                return Meal._ID;
                            }

                            @Override
                            public String getTableName() {
                                return Meal.TABLE_NAME;
                            }

                            @Override
                            public void onQueryComplete(Cursor cursor) {
                                EditMealFragment.this.mMealData = mMealData;

                                if (getNameView() != null) {
                                    getNameView().getText().clear();

                                    if (mMealData.getName() != null) {
                                        getNameView().getText().append(mMealData.getName());
                                    }
                                }
                            }

                            @Override
                            public void onQueryFailed(Throwable error) {
                                reportQueryFailed(error);
                            }
                        }
                }));
            }
        });
    }

    private void reportQueryFailed(final Throwable error) {
        callWatcher(new WatcherCaller<Watcher>() {
            @Override
            public void call(Watcher watcher) {
                watcher.onQueryFailed(EditMealFragment.this, error);
            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_meal;
    }

    @Override
    public Query[] getQueries() {
        return null;
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
            mNameToRowMap = (Map<String, MealTable.Row>) savedInstanceState.getSerializable(NAME_TO_ROW_MAP);
        }
    }

    /**
     * Called to ask the fragment to save its current dynamic state, so it
     * can later be reconstructed in a new instance of its process is
     * restarted.  If a new instance of the fragment later needs to be
     * created, the data you place in the Bundle here will be available
     * in the Bundle given to {@link #onCreate(Bundle)},
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
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

    @Override
    public void onResume() {
        super.onResume();

        getNameView().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateWatcher();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getNameView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateWatcher();
            }
        });
    }

    @Override
    public Modification getModification() {
        String name = getName();

        Long id = getSuggestionId(name);
        if (id != null) {
            return new Meal.Modification(id, name);
        }

        return new Meal.Modification(name);
    }

    @Override
    public boolean validate() {
        return MealData.validateName(getName());
    }

    @Override
    protected void updateWatcher(Watcher watcher) {
        watcher.onFragmentUpdate(this);
    }

    public String getName() {
        return getNameView().getText().toString();
    }

    @Override
    protected AutoCompleteTextView getNameView() {
        return getTypeSafeView(R.id.meal_name, AutoCompleteTextView.class);
    }

    @Nonnull
    @Override
    public TransactionExecutor.QueryOperation<MealTable.Row> getInitializationQuery() {
        return new TransactionExecutor.QueryOperation<MealTable.Row>() {

            @Override
            public void doTransactionally(@Nonnull Database database, @Nonnull Transaction transaction) {
                setResults(HealthDatabase.MEAL_TABLE.select(database, new Where() {
                    @Override
                    public String getWhere() {
                        return null;
                    }
                }));
            }
        };
    }

    public MealTable.Row getRow() {
        String name = getName();
        if (name == null) {
            return null;
        }

        if (mNameToRowMap.containsKey(name)) {
            return mNameToRowMap.get(name);
        }

        return new MealTable.Row(name);
    }

    public void setRow(final MealTable.Row row) {
        this.mRow = row;
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getNameView().setText(row.getName());
            }
        });
    }

    @Override
    public void onInitializationQueryResponse(@Nonnull MealTable.Row[] rows) {
        Map<String, Long> suggestionIds = new HashMap<>();
        for (MealTable.Row row : rows) {
            final String name = row.getName();
            suggestionIds.put(name, row.getConcretePrimaryKey().getId());
            mNameToRowMap.put(name, row);
        }

        setSuggestionIds(suggestionIds);
    }

    public interface Watcher extends QueuedQueryExecutor {
        void onFragmentUpdate(EditMealFragment fragment);

        void onQueryFailed(EditMealFragment fragment, Throwable error);
    }
}
