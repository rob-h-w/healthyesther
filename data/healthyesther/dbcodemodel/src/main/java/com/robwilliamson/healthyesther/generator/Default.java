/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JDefinedClass;

public class Default {
    public static void configuration(JDefinedClass clazz) {
        clazz.javadoc().append(
                "This class is generated, and should not be edited. Edits will be overwritten");
    }
}
