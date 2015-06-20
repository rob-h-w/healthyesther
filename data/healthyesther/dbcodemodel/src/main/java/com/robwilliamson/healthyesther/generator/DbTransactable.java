package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

@ClassGeneratorFeatures(name = "DbTransactable", parameterName = "transaction")
public class DbTransactable extends BaseClassGenerator {

    public DbTransactable(JPackage jPackage) throws JClassAlreadyExistsException {
        setJClass(jPackage._interface(JMod.PUBLIC, getName()));

        JMethod execSQL = getJClass().method(0, model().ref(String.class), "execSQL");
        execSQL.param(model().ref(String.class), "sql");
    }
}
