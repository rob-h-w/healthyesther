package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.db.definition.MealEvent;
import com.robwilliamson.healthyesther.db.definition.RowIdProvider;
import com.robwilliamson.healthyesther.db.definition.SimpleRowIdProvider;

public class MealEventData extends DataAbstraction {
    private RowIdProvider mEventIdProvider;
    private RowIdProvider mMealIdProvider;
    private Double mAmount;
    private Long mUnitsId;

    public MealEventData() {}

    public MealEventData(long mealDataId, long eventDataId, Double amount, Long unitsId) {
        mMealIdProvider = new SimpleRowIdProvider(mealDataId);
        mEventIdProvider = new SimpleRowIdProvider(eventDataId);
        mAmount = amount;
        mUnitsId = unitsId;
    }

    public MealEventData(RowIdProvider meal, RowIdProvider event, Double amount, Long unitsId) {
        mMealIdProvider = meal;
        mEventIdProvider = event;
        mAmount = amount;
        mUnitsId = unitsId;
    }

    @Override
    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(MealEvent.MEAL_ID, getMealId());
        bundle.putLong(MealEvent.EVENT_ID, getEventId());

        if (mAmount != null) {
            bundle.putDouble(MealEvent.AMOUNT, mAmount);
        }

        if (mUnitsId != null) {
            bundle.putLong(MealEvent.UNITS_ID, mUnitsId);
        }

        return bundle;
    }

    @Override
    public ContentValues asContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MealEvent.MEAL_ID, getMealId());
        contentValues.put(MealEvent.EVENT_ID, getEventId());

        if (mAmount != null) {
            contentValues.put(MealEvent.AMOUNT, mAmount);
        }

        if (mUnitsId != null) {
            contentValues.put(MealEvent.UNITS_ID, mUnitsId);
        }

        return contentValues;
    }

    @Override
    protected void populateFrom(Cursor cursor) {
        final int mealIdIndex = cursor.getColumnIndex(MealEvent.MEAL_ID);
        final int eventIdIndex = cursor.getColumnIndex(MealEvent.EVENT_ID);
        final int amountIndex = cursor.getColumnIndex(MealEvent.AMOUNT);
        final int unitsIdIndex = cursor.getColumnIndex(MealEvent.UNITS_ID);

        mMealIdProvider = new SimpleRowIdProvider(cursor.getLong(mealIdIndex));
        mEventIdProvider = new SimpleRowIdProvider(cursor.getLong(eventIdIndex));
        mAmount = cursor.getDouble(amountIndex);
        mUnitsId = cursor.getLong(unitsIdIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        mMealIdProvider = new SimpleRowIdProvider(bundle.getLong(MealEvent.MEAL_ID));
        mEventIdProvider = new SimpleRowIdProvider(bundle.getLong(MealEvent.EVENT_ID));
        mAmount = bundle.containsKey(MealEvent.AMOUNT) ? bundle.getDouble(MealEvent.AMOUNT) : null;
        mUnitsId = bundle.containsKey(MealEvent.UNITS_ID) ? bundle.getLong(MealEvent.UNITS_ID) : null;
    }

    public long getEventId() {
        return mEventIdProvider.getRowId();
    }

    public long getMealId() {
        return mMealIdProvider.getRowId();
    }
}
