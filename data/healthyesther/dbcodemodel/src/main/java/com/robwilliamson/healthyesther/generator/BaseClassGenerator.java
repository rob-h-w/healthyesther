package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public abstract class BaseClassGenerator implements ClassOwner {
    public static class MissingAnnotationException extends RuntimeException {
        public MissingAnnotationException(Throwable e) {
            super("Class generators must have ClassGeneratorFeatures annotations!", e);
        }
    }

    public static String getName(Class<?> clazz) {
        return getFeatures(clazz).name();
    }

    public static String getPreferredParameterName(Class<?> clazz) {
        return getFeatures(clazz).parameterName();
    }

    protected static ClassGeneratorFeatures getFeatures(Class<?> clazz) {
        return clazz.getAnnotation(ClassGeneratorFeatures.class);
    }

    private JDefinedClass mClass;

    public BaseClassGenerator() {
        try {
            getFeatures(getClass());
        } catch (NullPointerException e) {
            throw new MissingAnnotationException(e);
        }
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
}
