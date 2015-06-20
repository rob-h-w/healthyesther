package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;

@ClassGeneratorFeatures(name = "BaseTable", parameterName = "table")
public class BaseTable extends BaseClassGenerator {
    public BaseTable(JPackage jPackage) throws JClassAlreadyExistsException {
        setJClass(jPackage._class(JMod.PUBLIC | JMod.ABSTRACT, getName()));
    }
}
