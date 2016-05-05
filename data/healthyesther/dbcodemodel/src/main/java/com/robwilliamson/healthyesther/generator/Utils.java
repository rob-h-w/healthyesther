/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JMethod;
import com.sun.codemodel.JVar;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Utils {

    public static void annotateNonull(JMethod method, boolean nonnull) {
        if (method.type().isPrimitive()) {
            return;
        }

        if (nonnull) {
            method.annotate(Nonnull.class);
        } else {
            method.annotate(Nullable.class);
        }
    }

    public static void annotateNonull(JVar jVar, boolean nonnull) {
        if (jVar.type().isPrimitive()) {
            return;
        }

        if (nonnull) {
            jVar.annotate(Nonnull.class);
        } else {
            jVar.annotate(Nullable.class);
        }
    }
}
