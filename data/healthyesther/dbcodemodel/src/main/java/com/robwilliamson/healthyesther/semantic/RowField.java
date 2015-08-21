package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.generator.RowGenerator;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;

public class RowField extends BaseField {
    public final RowGenerator rowGenerator;

    public RowField(JDefinedClass owningClass, RowGenerator rowGenerator, String name) {
        super(owningClass, name, rowGenerator.getJClass());
        this.rowGenerator = rowGenerator;
    }

    public RowField(JFieldVar fieldVar, RowGenerator rowGenerator) {
        super(fieldVar, rowGenerator.getName());
        this.rowGenerator = rowGenerator;
    }

    public RowField(RowField other) {
        super(other);
        this.rowGenerator = other.rowGenerator;
    }

    public RowField(JDefinedClass owningClass, RowGenerator rowGenerator) {
        super(owningClass, rowGenerator.getName(), rowGenerator.getJClass());
        this.rowGenerator = rowGenerator;
    }
}
