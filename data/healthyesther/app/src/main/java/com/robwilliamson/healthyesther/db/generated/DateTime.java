
package com.robwilliamson.healthyesther.db.generated;



/**
 * This class is generated, and should not be edited. Edits will be overwritten
 * 
 */
public class DateTime
    implements Comparable<DateTime>
{

    private String mString;

    public DateTime(String string) {
        mString = string;
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

}
