package com.robwilliamson.healthyesther.db;

import com.robwilliamson.healthyesther.db.integration.DatabaseAccessor;

/**
 * Contains database version and file name.
 */
public final class Contract {
    public static final String NAME = DatabaseAccessor.FILE_NAME;
    public static final int VERSION = 4;
}
