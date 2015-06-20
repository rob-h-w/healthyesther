
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public final class HealthDatabase {

    public final static String FILE_NAME = "health.db3";
    public final static EventTable EVENT_TABLE = new EventTable();
    public final static HealthScoreTable HEALTH_SCORE_TABLE = new HealthScoreTable();
    public final static AndroidMetadataTable ANDROID_METADATA_TABLE = new AndroidMetadataTable();
    public final static MedicationNameTable MEDICATION_NAME_TABLE = new MedicationNameTable();
    public final static MedicationEventTable MEDICATION_EVENT_TABLE = new MedicationEventTable();
    public final static EventTypeTable EVENT_TYPE_TABLE = new EventTypeTable();
    public final static MedicationTable MEDICATION_TABLE = new MedicationTable();
    public final static HealthScoreEventTable HEALTH_SCORE_EVENT_TABLE = new HealthScoreEventTable();
    public final static NoteTable NOTE_TABLE = new NoteTable();
    public final static UnitsTable UNITS_TABLE = new UnitsTable();
    public final static MealTable MEAL_TABLE = new MealTable();
    public final static NoteEventTable NOTE_EVENT_TABLE = new NoteEventTable();
    public final static MealEventTable MEAL_EVENT_TABLE = new MealEventTable();

    public HealthDatabase() {
    }

    public final void create(DbTransactable transaction) {
    }

}
