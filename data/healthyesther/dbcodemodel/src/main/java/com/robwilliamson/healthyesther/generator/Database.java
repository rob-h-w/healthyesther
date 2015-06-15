package com.robwilliamson.healthyesther.generator;

import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JPackage;

public class Database implements ClassGenerator {
    private final com.robwilliamson.healthyesther.type.Database mDb;
    private final JDefinedClass mClass;

    public Database(
            com.robwilliamson.healthyesther.type.Database database,
            JPackage jPackage) throws JClassAlreadyExistsException {
        mDb = database;
        mClass = jPackage._class(0, getName());
        Default.configuration(mClass);
    }

    @Override
    public JDefinedClass getJClass() {
        return mClass;
    }

    private String getName() {
        return Strings.capitalize(mDb.getName()) + "Database";
    }
}
