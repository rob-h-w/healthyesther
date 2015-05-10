package com.robwilliamson.healthyesther.db.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.robwilliamson.healthyesther.Utils;
import com.robwilliamson.healthyesther.db.definition.MealEvent;

public class MealEventData extends DataAbstraction {
    private long mEventId;
    private long mMealId;
    private Double mAmount;
    private Long mUnitsId;

    public MealEventData(EventData eventData, MealData mealData, double amount, long unitsId) {
        mEventId = Utils.checkNotNull(eventData)._id;
        mMealId = Utils.checkNotNull(mealData)._id;
        mAmount = amount;
        mUnitsId = unitsId;
    }

    @Override
    public Bundle asBundle() {
        Bundle bundle = new Bundle();
        bundle.putLong(MealEvent.EVENT_ID, mEventId);
        bundle.putLong(MealEvent.MEAL_ID, mMealId);

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
        contentValues.put(MealEvent.EVENT_ID, mEventId);
        contentValues.put(MealEvent.MEAL_ID, mMealId);

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
        final int eventIdIndex = cursor.getColumnIndex(MealEvent.EVENT_ID);
        final int mealIdIndex = cursor.getColumnIndex(MealEvent.MEAL_ID);
        final int amountIndex = cursor.getColumnIndex(MealEvent.AMOUNT);
        final int unitsIdIndex = cursor.getColumnIndex(MealEvent.UNITS_ID);

        mEventId = cursor.getLong(eventIdIndex);
        mMealId = cursor.getLong(mealIdIndex);
        mAmount = cursor.getDouble(amountIndex);
        mUnitsId = cursor.getLong(unitsIdIndex);
    }

    @Override
    protected void populateFrom(Bundle bundle) {
        mEventId = bundle.getLong(MealEvent.EVENT_ID);
        mMealId = bundle.getLong(MealEvent.MEAL_ID);
        mAmount = bundle.containsKey(MealEvent.AMOUNT) ? bundle.getDouble(MealEvent.AMOUNT) : null;
        mUnitsId = bundle.containsKey(MealEvent.UNITS_ID) ? bundle.getLong(MealEvent.UNITS_ID) : null;
    }

    public long getEventId() {
        return mEventId;
    }

    public long getMealId() {
        return mMealId;
    }
}
