/*
   © Robert Williamson 2014-2018.
   This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.integration;

import javax.annotation.Nonnull;

public interface Upgrade {
    void upgradeFrom(@Nonnull Transaction transaction);
}
