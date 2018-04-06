/*
  © Robert Williamson 2014-2016.
  This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.analysis;

import com.robwilliamson.healthyesther.db.generated.EventTable;

import javax.annotation.Nonnull;

public interface EventAnalyzer {
    void consider(@Nonnull EventTable.Row row);
}
