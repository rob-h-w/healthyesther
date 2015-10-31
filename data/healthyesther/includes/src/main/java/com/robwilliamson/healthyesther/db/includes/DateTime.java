package com.robwilliamson.healthyesther.db.includes;


import java.util.HashMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DateTime
        implements Comparable<DateTime> {

    private final static HashMap<Class, Converter> sConverterRegistry = new HashMap<>();
    private final String mString;

    public DateTime(String string) {
        mString = string;
    }

    public <T> DateTime(T otherType) {
        mString = retrieve(otherType.getClass()).from(otherType).getString();
    }

    @Nullable
    public static <T> DateTime from(@Nullable T otherType) {
        if (otherType == null) {
            return null;
        }

        return retrieve(otherType.getClass()).from(otherType);
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
        return dateType.cast(converter.to(dateType, this));
    }

    public interface Converter<T> {
        @Nonnull
        DateTime from(@Nonnull T fromType);

        @Nonnull
        T to(@Nonnull Class<T> type, @Nonnull DateTime dateTime);
    }

}
