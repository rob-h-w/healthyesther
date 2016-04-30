package test;

import com.robwilliamson.healthyesther.db.generated.HealthDatabase;
import com.robwilliamson.healthyesther.db.generated.MealEventTable;
import com.robwilliamson.healthyesther.db.generated.MealTable;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.junit.Assert.fail;

public enum HealthDatabaseAccessor {
    INSTANCE;

    public final Field MEAL_EVENT_TABLE;
    public final Field MEAL_TABLE;

    private HealthDatabaseAccessor() {
        try {
            MEAL_EVENT_TABLE = HealthDatabase.class.getDeclaredField("MEAL_EVENT_TABLE");
            MEAL_TABLE = HealthDatabase.class.getDeclaredField("MEAL_TABLE");

            Field[] fields = new Field[]{
                    MEAL_EVENT_TABLE,
                    MEAL_TABLE
            };

            for (Field field : fields) {
                field.setAccessible(true);

                // remove final modifier from field
                Field modifiersField = null;
                modifiersField = Field.class.getDeclaredField("modifiers");
                modifiersField.setAccessible(true);
                modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void setMealEventTable(MealEventTable mealEventTable) {
        try {
            MEAL_EVENT_TABLE.set(null, mealEventTable);
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }

    public void setMealTable(MealTable mealTable) {
        try {
            MEAL_TABLE.set(null, mealTable);
        } catch (IllegalAccessException e) {
            fail(e.getMessage());
        }
    }
}
