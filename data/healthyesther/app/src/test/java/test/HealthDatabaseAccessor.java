/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package test;

import com.robwilliamson.healthyesther.db.generated.EventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreEventTable;
import com.robwilliamson.healthyesther.db.generated.HealthScoreTable;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;
import com.robwilliamson.healthyesther.db.includes.Table;
import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import static org.junit.Assert.fail;

public enum HealthDatabaseAccessor {
    INSTANCE;

    private final Map<Field, Table> mOriginals = new HashMap<>();

    public final Field EVENT_TABLE;
    public final Field HEALTH_SCORE_EVENT_TABLE;
    public final Field HEALTH_SCORE_TABLE;
    public final Field MEAL_EVENT_TABLE;
    public final Field MEAL_TABLE;

    HealthDatabaseAccessor() {
        try {
            EVENT_TABLE = DatabaseAccessor.class.getField("EVENT_TABLE");
            HEALTH_SCORE_EVENT_TABLE = DatabaseAccessor.class.getField("HEALTH_SCORE_EVENT_TABLE");
            HEALTH_SCORE_TABLE = DatabaseAccessor.class.getField("HEALTH_SCORE_TABLE");
            MEAL_EVENT_TABLE = DatabaseAccessor.class.getField("MEAL_EVENT_TABLE");
            MEAL_TABLE = DatabaseAccessor.class.getField("MEAL_TABLE");

            Field[] fields = new Field[]{
                    EVENT_TABLE,
                    HEALTH_SCORE_EVENT_TABLE,
                    HEALTH_SCORE_TABLE,
                    MEAL_EVENT_TABLE,
                    MEAL_TABLE
            };

            for (Field field : fields) {
                field.setAccessible(true);

                // remove final modifier from field
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setEventTable(EventTable table) {
        setTable(EVENT_TABLE, table);
    }

    public void setHealthScoreEventTable(HealthScoreEventTable table) {
        setTable(HEALTH_SCORE_EVENT_TABLE, table);
    }

    public void setHealthScoreTable(HealthScoreTable table) {
        setTable(HEALTH_SCORE_TABLE, table);
    }

    public void setMealEventTable(MealEventTable mealEventTable) {
        setTable(MEAL_EVENT_TABLE, mealEventTable);
    }

    public void setMealTable(MealTable mealTable) {
        setTable(MEAL_TABLE, mealTable);
    }

    public void reset(Field field) {
        if (!mOriginals.containsKey(field)) {
            return;
        }

        try {
            field.set(null, mOriginals.get(field));
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    public void resetAll() {
        for (Field field : mOriginals.keySet()) {
            reset(field);
        }
    }

    private <T extends Table> void setTable(@Nonnull Field field, @Nonnull T table) {
        try {
            storeOriginal(field);
            field.set(null, table);
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    private void storeOriginal(@Nonnull Field field) throws IllegalAccessException {
        if (mOriginals.containsKey(field)) {
            return;
        }

        Table table = (Table) field.get(null);
        mOriginals.put(field, table);
    }
}
