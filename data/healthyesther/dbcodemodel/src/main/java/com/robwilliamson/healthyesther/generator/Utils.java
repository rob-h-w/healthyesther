/**
 * © Robert Williamson 2014-2018.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.generator;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JOp;
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

    public enum Type {
        BOOLEAN,
        BYTE,
        CHAR,
        DOUBLE,
        FLOAT,
        INT,
        LONG,
        NULLABLE_REF,
        REF,
        SHORT;

        public static Type fromString(@Nonnull String string) {
            switch (string.toLowerCase()) {
                case "boolean":
                    return BOOLEAN;
                case "byte":
                    return BYTE;
                case "char":
                    return CHAR;
                case "double":
                    return DOUBLE;
                case "float":
                    return FLOAT;
                case "int":
                    return INT;
                case "long":
                    return LONG;
                case "nullable_ref":
                    return NULLABLE_REF;
                case "ref":
                    return REF;
                case "short":
                    return SHORT;
                default:
                    throw new UnsupportedOperationException(string);
            }
        }
    }

    public static class Hasher {
        @Nonnull
        private JCodeModel mModel;

        @Nonnull
        private JBlock mBody;

        @Nonnull
        private JVar mHash;

        public Hasher(@Nonnull JCodeModel model,
                      @Nonnull JBlock body,
                      @Nonnull JVar hash) {
            mModel = model;
            mBody = body;
            mHash = hash;
        }

        @Nonnull
        public static Hasher makeBasicHashCode(
                @Nonnull JDefinedClass theClass,
                @Nonnull JCodeModel model) {
            JMethod method = theClass.method(JMod.PUBLIC, model.INT, "hashCode");
            method.annotate(Override.class);
            JBlock body = method.body();
            JVar hash = body.decl(model.INT, "hash", JExpr.dotclass(theClass).invoke("getCanonicalName").invoke("hashCode"));
            return new Utils.Hasher(model, body, hash);
        }

        @Nonnull
        public JBlock getBody() {
            return mBody;
        }

        public void setBody(@Nonnull JBlock body) {
            mBody = body;
        }

        @Nonnull
        public JVar getHash() {
            return mHash;
        }

        public void hash(@Nonnull JExpression value, @Nullable Type type) {
            if (type == null) {
                mBody.assign(mHash, mHash.xor(value));
                return;
            }

            switch (type) {
                case BOOLEAN:
                    mBody.assignPlus(mHash, JOp.cond(value, JExpr.lit(3), JExpr.lit(31)));
                    break;
                case BYTE:
                case CHAR:
                case INT:
                case SHORT:
                    mBody.assign(mHash, mHash.shl(JExpr.lit(17).plus(value)).xor(mHash.shr(JExpr.lit(19))));
                    break;
                case LONG:
                    mBody.assign(mHash, mHash.xor(mModel.ref(Long.class).staticInvoke("valueOf").arg(value).invoke("hashCode")));
                    break;
                case DOUBLE:
                case FLOAT:
                    mBody.assign(mHash, mHash.xor(mModel.ref(Double.class).staticInvoke("valueOf").arg(value).invoke("hashCode")));
                    break;
                case NULLABLE_REF:
                    mBody.assign(mHash, JOp.cond(value.eq(JExpr._null()), mHash, mHash.xor(value.invoke("hashCode"))));
                    break;
                case REF:
                    mBody.assign(mHash, mHash.xor(value.invoke("hashCode")));
                    break;
            }
        }
    }
}
