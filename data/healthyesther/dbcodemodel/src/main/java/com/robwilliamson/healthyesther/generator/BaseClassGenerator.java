package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;

public abstract class BaseClassGenerator implements ClassOwner {
    public static class MissingAnnotationException extends RuntimeException {
        private static String message = "Class generators must have ClassGeneratorFeatures annotations!";

        public MissingAnnotationException() {
            super(message);
        }

        public MissingAnnotationException(Throwable e) {
            super(message, e);
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
