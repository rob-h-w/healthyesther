/**
  * © Robert Williamson 2014-2016.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nullable;

public interface Where {
    @Nullable
    String getWhere();
}
