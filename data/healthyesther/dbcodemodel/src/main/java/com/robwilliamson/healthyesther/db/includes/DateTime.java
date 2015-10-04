package com.robwilliamson.healthyesther.db.includes;


import java.util.HashMap;

public class DateTime
        implements Comparable<DateTime> {

    private final static HashMap<Class, Converter> sConverterRegistry = new HashMap<>();
    private final String mString;

    public DateTime(String string) {
        mString = string;
    }

    public static <T> void register(Class<T> type, DateTime.Converter converter) {
        sConverterRegistry.put(type, converter);
    }

    private static <T> DateTime.Converter retrieve(Class<T> type) {
        return sConverterRegistry.get(type);
    }

    public String getString() {
        return mString;
    }

    @Override
    public int compareTo(DateTime other) {
        if (mString == null) {
            if (other.mString == null) {
                return 0;
            }
            return -1;
        }
        return mString.compareTo(other.mString);
    }

    public interface Converter<T> {


        public DateTime convert(T fromType);

        public T convert(DateTime dateTime);

    }

}