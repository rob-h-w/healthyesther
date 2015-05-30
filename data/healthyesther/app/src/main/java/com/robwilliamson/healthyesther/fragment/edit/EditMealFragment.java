package com.robwilliamson.healthyesther.fragment.edit;


import android.database.Cursor;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.robwilliamson.healthyesther.R;
import com.robwilliamson.healthyesther.db.Utils;
import com.robwilliamson.healthyesther.db.data.EventData;
import com.robwilliamson.healthyesther.db.data.MealData;
import com.robwilliamson.healthyesther.db.data.MealEventData;
import com.robwilliamson.healthyesther.db.definition.Meal;
import com.robwilliamson.healthyesther.db.definition.MealEvent;
import com.robwilliamson.healthyesther.db.definition.Modification;
import com.robwilliamson.healthyesther.db.use.GetAllMealsQuery;
import com.robwilliamson.healthyesther.db.use.GetOneWithColumnIdQuery;
import com.robwilliamson.healthyesther.db.use.GetOneWithEventIdQuery;
import com.robwilliamson.healthyesther.db.use.Query;
import com.robwilliamson.healthyesther.db.use.QueuedQueryExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class EditMealFragment extends SuggestionEditFragment<EditMealFragment.Watcher> {

    public interface Watcher extends QueuedQueryExecutor {
        void onFragmentUpdate(EditMealFragment fragment);
        void onQueryFailed(EditMealFragment fragment, Throwable error);
    }

    private EventData mEventData;
    private MealEventData mMealEventData;
    private MealData mMealData;

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
                            } else if(mMealEvents.size() == 1) {
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
                watcher.enqueueQueries(Arrays.asList(new Query[] {
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

                                    if (mMealData.name != null) {
                                        getNameView().getText().append(mMealData.name);
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

    public EditMealFragment() {
        super(EditMealFragment.Watcher.class);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_edit_meal;
    }

    @Override
    public Query[] getQueries() {
        return new Query[] {
                new GetAllMealsQuery() {
                    HashMap<String, Long> mSuggestionIds;

                    @Override
                    public void postQueryProcessing(Cursor cursor) {
                        mSuggestionIds = Utils.Db.cursorToSuggestionList(cursor,
                                com.robwilliamson.healthyesther.db.definition.Meal.NAME,
                                com.robwilliamson.healthyesther.db.definition.Meal._ID);
                    }

                    @Override
                    public void onQueryComplete(final Cursor cursor) {
                        EditMealFragment.this.setSuggestionIds(mSuggestionIds);
                    }

                    @Override
                    public void onQueryFailed(Throwable error) {
                        reportQueryFailed(error);
                    }
                }
        };
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
            return new Meal.Modification(id);
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
}
