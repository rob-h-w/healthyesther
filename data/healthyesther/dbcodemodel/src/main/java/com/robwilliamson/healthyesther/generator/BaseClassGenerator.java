/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JExpressionImpl;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;

public abstract class BaseClassGenerator implements ClassOwner {
    private JDefinedClass mClass;

    public BaseClassGenerator() {
        try {
            getFeatures(getClass());
        } catch (NullPointerException e) {
            throw new MissingAnnotationException(e);
        }
    }

    public static String getName(Class<?> clazz) {
        return getFeatures(clazz).name();
    }

    public static String getPreferredParameterName(Class<?> clazz) {
        return getFeatures(clazz).parameterName();
    }

    protected static ClassGeneratorFeatures getFeatures(Class<?> clazz) {
        ClassGeneratorFeatures features = clazz.getAnnotation(ClassGeneratorFeatures.class);

        if (features == null) {
            throw new MissingAnnotationException();
        }

        return features;
    }

    @Override
    public JDefinedClass getJClass() {
        return mClass;
    }

    protected void setJClass(JDefinedClass clazz) {
        mClass = clazz;

        if (mClass != null) {
            Default.configuration(mClass);
        }
    }

    public String getName() {
        return getName(getClass());
    }

    public String getPreferredParameterName() {
        return getPreferredParameterName(getClass());
    }

    protected JCodeModel model() {
        return mClass.owner();
    }

    protected JMethod makeBasicEquals() {
        JDefinedClass theClass = getJClass();
        JCodeModel model = model();
        JMethod eqMethod = theClass.method(JMod.PUBLIC, model.BOOLEAN, "equals");
        eqMethod.annotate(Override.class);
        JBlock equals = eqMethod.body();
        JVar other = eqMethod.param(model.ref(Object.class), "other");

        // If other == null
        JBlock ifBlock = equals._if(other.eq(JExpr._null()))._then();
        ifBlock._return(JExpr.lit(false));

        // If other == this
        ifBlock = equals._if(other.eq(JExpr._this()))._then();
        ifBlock._return(JExpr.lit(true));

        // Check type
        ifBlock = equals._if(other._instanceof(theClass).not())._then();
        ifBlock._return(JExpr.lit(false));

        return eqMethod;
    }

    protected JExpression makeEquals(JVar left, JExpressionImpl right) {
        if (left.type().isPrimitive()) {
            return left.eq(right);
        }

        return left.eq(JExpr._null()).cand(right.eq(JExpr._null())).cor(left.ne(JExpr._null()).cand(left.invoke("equals").arg(right)));
    }

    public static class MissingAnnotationException extends RuntimeException {
        private static String message = "Class generators must have ClassGeneratorFeatures annotations!";

        public MissingAnnotationException() {
            super(message);
        }

        public MissingAnnotationException(Throwable e) {
            super(message, e);
        }
    }
}
