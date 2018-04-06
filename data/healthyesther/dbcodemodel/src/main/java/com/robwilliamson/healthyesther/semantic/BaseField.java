/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.semantic;

import com.robwilliamson.healthyesther.Strings;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JType;

public abstract class BaseField {
    public final String name;
    public final JFieldVar fieldVar;

    public BaseField(JDefinedClass owningClass, String name, JType type) {
        this.name = name(name);
        fieldVar = owningClass.field(JMod.PRIVATE, type, memberName(name));
    }

    public BaseField(JFieldVar fieldVar, String name) {
        this.name = name(name);
        this.fieldVar = fieldVar;
    }

    public static String memberName(String name) {
        return "m" + Strings.capitalize(name(name));
    }

    public static String name(String name) {
        return Strings.lowerCase(Strings.underscoresToCamel(name));
    }
}
