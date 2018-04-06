/**
  * © Robert Williamson 2014-2018.
  * This program is distributed under the terms of the GNU General Public License.
  */
package com.robwilliamson.healthyesther.db.includes;

import javax.annotation.Nonnull;

public interface Cursor extends AutoCloseable {
    Boolean getBoolean(@Nonnull String column);

    Double getDouble(@Nonnull String column);

    Long getLong(@Nonnull String column);

    String getString(@Nonnull String column);

    DateTime getDateTime(@Nonnull String column);

    void moveToFirst();

    boolean moveToNext();

    int count();

    void close();
}
