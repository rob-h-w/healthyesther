/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.use;

import com.robwilliamson.healthyesther.db.includes.TransactionExecutor;

import javax.annotation.Nonnull;

public interface InitializationQuerier {
    @Nonnull
    TransactionExecutor.Operation getInitializationQuery();
}
