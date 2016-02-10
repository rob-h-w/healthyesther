
package com.robwilliamson.healthyesther.db.generated;

import com.robwilliamson.healthyesther.db.includes.Database;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.includes.Transaction;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class HealthDatabase {

    public final static String FILE_NAME = "health.db3";
    public final static UnitsTable UNITS_TABLE = new UnitsTable();
    public final static AndroidMetadataTable ANDROID_METADATA_TABLE = new AndroidMetadataTable();
    public final static EventTypeTable EVENT_TYPE_TABLE = new EventTypeTable();
    public final static HealthScoreTable HEALTH_SCORE_TABLE = new HealthScoreTable();
    public final static EventTable EVENT_TABLE = new EventTable();
    public final static MedicationTable MEDICATION_TABLE = new MedicationTable();
    public final static NoteTable NOTE_TABLE = new NoteTable();
    public final static MealTable MEAL_TABLE = new MealTable();
    public final static NoteEventTable NOTE_EVENT_TABLE = new NoteEventTable();
    public final static HealthScoreJudgmentRangeTable HEALTH_SCORE_JUDGMENT_RANGE_TABLE = new HealthScoreJudgmentRangeTable();
    public final static MedicationEventTable MEDICATION_EVENT_TABLE = new MedicationEventTable();
    public final static HealthScoreEventTable HEALTH_SCORE_EVENT_TABLE = new HealthScoreEventTable();
    public final static MedicationNameTable MEDICATION_NAME_TABLE = new MedicationNameTable();
    public final static MealEventTable MEAL_EVENT_TABLE = new MealEventTable();
    public final static Table[] TABLES = new Table[] {UNITS_TABLE, ANDROID_METADATA_TABLE, EVENT_TYPE_TABLE, HEALTH_SCORE_TABLE, EVENT_TABLE, MEDICATION_TABLE, NOTE_TABLE, MEAL_TABLE, NOTE_EVENT_TABLE, HEALTH_SCORE_JUDGMENT_RANGE_TABLE, MEDICATION_EVENT_TABLE, HEALTH_SCORE_EVENT_TABLE, MEDICATION_NAME_TABLE, MEAL_EVENT_TABLE };

    protected HealthDatabase() {
    }

    public static void create(Transaction transaction) {
        Database.create(transaction, TABLES);
    }

    public static void drop(Transaction transaction) {
        Database.drop(transaction, TABLES);
    }

    public static void upgrade(Transaction transaction, int from, int to) {
        Database.upgrade(transaction, from, to, TABLES);
    }

}
