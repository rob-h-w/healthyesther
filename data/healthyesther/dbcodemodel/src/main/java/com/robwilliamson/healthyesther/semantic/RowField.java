package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.generator.RowGenerator;
import com.robwilliamson.healthyesther.type.Column;
import com.sun.codemodel.JDefinedClass;

public class RowField extends BaseField {
    public final RowGenerator rowGenerator;
    public final Column column;

    public RowField(JDefinedClass owningClass, Column column, RowGenerator rowGenerator, String name) {
        super(owningClass, name, rowGenerator.getJClass());
        this.rowGenerator = rowGenerator;
        this.column = column;
    }
}
