package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;

public class SimpleRowIdGenerator extends BaseRowIdGenerator {
    public SimpleRowIdGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super(tableGenerator);
    }
}
