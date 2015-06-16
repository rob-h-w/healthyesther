package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JMods;
import com.sun.codemodel.JPackage;

public class DbTransactable implements ClassGenerator {
    private final JDefinedClass mClass;

    public DbTransactable(JPackage jPackage) throws JClassAlreadyExistsException {
        mClass = jPackage._interface("DbTransactable");
        Default.configuration(mClass);
    }

    @Override
    public JDefinedClass getJClass() {
        return mClass;
    }
}
