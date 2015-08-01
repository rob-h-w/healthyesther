package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JTypeVar;
import com.sun.codemodel.JVar;

@ClassGeneratorFeatures(name = "DateTime", parameterName = "dateTime")
public class DateTimeGenerator extends BaseClassGenerator {
    private static DateTimeGenerator sInstance;

    public static DateTimeGenerator create(JPackage rootPackage) throws JClassAlreadyExistsException {
        if (sInstance != null) {
            throw new JClassAlreadyExistsException(sInstance.getJClass());
        }

        sInstance = new DateTimeGenerator(rootPackage);
        return sInstance;
    }

    public static DateTimeGenerator getInstance() {
        return sInstance;
    }

    private JFieldVar mString;
    private JDefinedClass mConverterFrom;
    private JDefinedClass mConverterTo;

    public DateTimeGenerator(JPackage jPackage) throws JClassAlreadyExistsException {
        setJClass(jPackage._class(JMod.PUBLIC, getName()));
        JDefinedClass clazz = getJClass();
        JClass comparable = model().ref(Comparable.class).narrow(clazz);
        clazz._implements(comparable);
        mString = clazz.field(JMod.PRIVATE | JMod.FINAL, String.class, "mString");

        makeConverters();
        makeConstructor();
        makeGetter();
        makeComparable();
    }

    public JDefinedClass getConverterFrom() {
        return mConverterFrom;
    }

    public JDefinedClass getConverterTo() {
        return mConverterTo;
    }

    private void makeConverters() throws JClassAlreadyExistsException {
        JDefinedClass clazz = getJClass();

        mConverterFrom = clazz._interface(JMod.PUBLIC, "ConvertFrom");
        JTypeVar generic = mConverterFrom.generify("T");
        JMethod convert = mConverterFrom.method(
                JMod.PUBLIC,
                clazz,
                "convert");
        convert.param(generic, "fromType");

        mConverterTo = clazz._interface(JMod.PUBLIC, "ConverterTo");
        generic = mConverterTo.generify("T");
        convert = mConverterTo.method(
                JMod.PUBLIC,
                generic,
                "convert");
        convert.param(clazz, "dateTime");
    }

    private void makeConstructor() {
        JDefinedClass clazz = getJClass();
        JMethod setter = clazz.constructor(JMod.PUBLIC);
        JVar string = setter.param(String.class, "string");
        setter.body().assign(mString, string);
    }

    private void makeGetter() {
        JDefinedClass clazz = getJClass();

        JMethod getter = clazz.method(JMod.PUBLIC, String.class, "getString");
        getter.body()._return(mString);
    }

    private void makeComparable() {
        JDefinedClass clazz = getJClass();
        JMethod compareTo = clazz.method(JMod.PUBLIC, model().INT, "compareTo");
        compareTo.annotate(Override.class);
        JVar other = compareTo.param(clazz, "other");

        JBlock body = compareTo.body();

        JBlock nullBody = body._if(mString.eq(JExpr._null()))._then();
        nullBody._if(other.ref(mString).eq(JExpr._null()))._then()._return(JExpr.lit(0));
        nullBody._return(JExpr.lit(-1));

        body._return(mString.invoke("compareTo").arg(other.ref(mString)));
    }
}
