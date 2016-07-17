/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.test.rule;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public class Retry implements MethodRule {
    private final int mRetryCount;

    public Retry(int retryCount) {
        mRetryCount = retryCount;
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;

                // implement retry logic here
                for (int i = -1; i < mRetryCount; i++) {
                    try {
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        caughtThrowable = t;
                        System.err.println(method.getName() + ": run " + (i+1) + " failed");
                    }
                }

                System.err.println(method.getName() + ": giving up after " + mRetryCount + 1 + " failures");
                throw caughtThrowable;
            }
        };
    }
}
