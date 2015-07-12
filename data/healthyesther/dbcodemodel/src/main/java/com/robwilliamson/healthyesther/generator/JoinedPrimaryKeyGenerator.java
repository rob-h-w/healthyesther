package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;

public class JoinedPrimaryKeyGenerator extends BasePrimaryKeyGenerator {
    public JoinedPrimaryKeyGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super(tableGenerator);
    }

    @Override
    protected void makeEquals() {
        //
    }
}
