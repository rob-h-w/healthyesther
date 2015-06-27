package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;

public class JoinedRowIdGenerator extends BaseRowIdGenerator {
    public JoinedRowIdGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super(tableGenerator);
    }
}
