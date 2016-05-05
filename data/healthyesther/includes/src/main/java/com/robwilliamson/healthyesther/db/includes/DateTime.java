/**
 * © Robert Williamson 2014-2016.
 * This program is distributed under the terms of the GNU General Public License.
 */
package com.robwilliamson.healthyesther.db.includes;


import java.io.Serializable;
import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DateTime
        implements Comparable<DateTime>, Serializable {

    private final static HashMap<Class, Converter> sConverterRegistry = new HashMap<>();
    private final String mString;

    public DateTime(String string) {
        mString = string;
    }

    public <T> DateTime(T otherType) {
        //noinspection unchecked
        mString = getConverter((Class<T>) otherType.getClass()).from(otherType).getString();
    }

    @Nonnull
    private static <T> DateTime.Converter<T> getConverter(@Nonnull Class<T> otherType) {
        @SuppressWarnings("unchecked") DateTime.Converter<T> converter = retrieve(otherType);
        if (converter == null) {
            throw new NullPointerException();
        }

        return converter;
    }

    @Nullable
    public static <T> DateTime from(@Nullable T otherType) {
        if (otherType == null) {
            return null;
        }

        //noinspection unchecked
        return getConverter((Class<T>) otherType.getClass()).from(otherType);
    }

    public static <T> void register(Class<T> type, DateTime.Converter<T> converter) {
        sConverterRegistry.put(type, converter);
    }

    private static <T> DateTime.Converter retrieve(Class<T> type) {
        return sConverterRegistry.get(type);
    }

    public String getString() {
        return mString;
    }

    @Override
    public int compareTo(@Nonnull DateTime other) {
        if (mString == null) {
            if (other.mString == null) {
                return 0;
            }
            return -1;
        }
        return mString.compareTo(other.mString);
    }

    @Nonnull
    public <T> T as(@Nonnull Class<T> dateType) {
        Converter converter = retrieve(dateType);
        //noinspection unchecked
        return dateType.cast(converter.to(dateType, this));
    }

    public interface Converter<T> {
        @Nonnull
        DateTime from(@Nonnull T fromType);

        @Nonnull
        T to(@Nonnull Class<T> type, @Nonnull DateTime dateTime);
    }

}
