package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;

public interface ClassOwner {
    JDefinedClass getJClass() throws JClassAlreadyExistsException;
}
