package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public class SimplePrimaryKeyGenerator extends BasePrimaryKeyGenerator {
    public SimplePrimaryKeyGenerator(TableGenerator tableGenerator) throws JClassAlreadyExistsException {
        super(tableGenerator);
    }

    @Override
    protected void makeEquals() {
        JDefinedClass theClass = getJClass();
        JCodeModel model = theClass.owner();
        JMethod eqMethod = theClass.method(JMod.PUBLIC, model.BOOLEAN, "equals");
        JBlock equals = eqMethod.body();
        JVar other = eqMethod.param(theClass, getPreferredParameterName());

        // If other == null
        JBlock ifBlock = equals._if(other.eq(JExpr._null()))._then();
        ifBlock._return(JExpr.lit(false));

        // If other == this
        ifBlock = equals._if(other.eq(JExpr._this()))._then();
        ifBlock._return(JExpr.lit(true));

        // Finally return true
        equals._return(JExpr.lit(true));
    }
}
