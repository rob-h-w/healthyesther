
package com.robwilliamson.healthyesther.db.generated;

import java.util.HashMap;


/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class DateTime
    implements Comparable<DateTime>
{

    private final String mString;
    private final static HashMap<Class, DateTime.Converter> sConverterRegistry = new HashMap<Class, DateTime.Converter>();

    public DateTime(String string) {
        mString = string;
    }

    public static<T >void register(Class<T> type, DateTime.Converter converter) {
        sConverterRegistry.put(type, converter);
    }

    private static<T >DateTime.Converter retrieve(Class<T> type) {
        return sConverterRegistry.get(type);
    }

    public String getString() {
        return mString;
    }

    @Override
    public int compareTo(DateTime other) {
        if (mString == null) {
            if (other.mString == null) {
                return  0;
            }
            return -1;
        }
        return mString.compareTo(other.mString);
    }

    public interface Converter<T >{


        public DateTime convert(T fromType);

        public T convert(DateTime dateTime);

    }

}
