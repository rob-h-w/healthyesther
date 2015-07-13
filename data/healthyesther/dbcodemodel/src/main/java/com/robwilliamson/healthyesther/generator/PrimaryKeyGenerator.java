package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.robwilliamson.healthyesther.semantic.Table;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ClassGeneratorFeatures(name = "PrimaryKey", parameterName = "PrimaryKey")
public class PrimaryKeyGenerator extends BaseClassGenerator {
    private final TableGenerator mTableGenerator;
    private final Map<String, JFieldVar> mPrimaryKeyFields;

    public PrimaryKeyGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super();

        mTableGenerator = tableGenerator;
        mPrimaryKeyFields = new HashMap<>();

        setJClass(tableGenerator.getJClass()._class(JMod.PUBLIC | JMod.STATIC | JMod.FINAL, getName()));
        populatePrimaryKeyValues();
        makeEquals();
    }

    public TableGenerator getTableGenerator() {
        return mTableGenerator;
    }

    @Override
    public String getName() {
        return getTableGenerator().getName() + super.getName();
    }

    @Override
    public String getPreferredParameterName() {
        return getTableGenerator().getPreferredParameterName() + super.getPreferredParameterName();
    }

    private void populatePrimaryKeyValues() {
        List <Column> primaryKeyColumns = getPrimaryKeyColumns();
        for (Column column : primaryKeyColumns) {
            JFieldVar primaryKeyField = getJClass().field(JMod.PRIVATE, column.getJtype(model()), "m" + Strings.capitalize(Strings.underscoresToCamel(column.getName())));
            mPrimaryKeyFields.put(column.getName(), primaryKeyField);
        }
    }

    private List<Column> getPrimaryKeyColumns() {
        List<Column> primaryKeyColumns = new ArrayList<>();

        Table table = getTableGenerator().getTable();

        for (Column column : table.getColumns()) {
            if (table.isPrimaryKey(column) || column.isPrimaryKey()) {
                primaryKeyColumns.add(column);
            }
        }

        return primaryKeyColumns;
    }

    private void makeEquals() {
        JDefinedClass theClass = getJClass();
        JCodeModel model = theClass.owner();
        JMethod eqMethod = theClass.method(JMod.PUBLIC, model.BOOLEAN, "equals");
        JBlock equals = eqMethod.body();
        JVar other = eqMethod.param(model.ref(Object.class), "other");

        // If other == null
        JBlock ifBlock = equals._if(other.eq(JExpr._null()))._then();
        ifBlock._return(JExpr.lit(false));

        // If other == this
        ifBlock = equals._if(other.eq(JExpr._this()))._then();
        ifBlock._return(JExpr.lit(true));

        // Check type
        ifBlock = equals._if(other._instanceof(theClass).not())._then();
        ifBlock._return(JExpr.lit(false));

        JFieldVar[] fields = new JFieldVar[mPrimaryKeyFields.size()];
        mPrimaryKeyFields.values().toArray(fields);
        Arrays.sort(fields, new Comparator<JFieldVar>() {
            @Override
            public int compare(JFieldVar o1, JFieldVar o2) {
                return o1.name().compareTo(o2.name());
            }
        });

        if (fields.length > 0) {
            // Cast
            JVar otherType = equals.decl(theClass, "the" + theClass.name(), JExpr.cast(theClass, other));

            // Check each primary key column.
            for (JFieldVar primaryKeyField : fields) {
                ifBlock = equals._if(otherType.ref(primaryKeyField).ne(primaryKeyField))._then();
                ifBlock._return(JExpr.lit(false));
            }
        }

        // Finally return true
        equals._return(JExpr.lit(true));
    }
}
