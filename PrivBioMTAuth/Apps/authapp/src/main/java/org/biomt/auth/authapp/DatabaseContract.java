package org.biomt.auth.authapp;

/**
 * Created by hasini on 8/24/16.
 */

import android.provider.BaseColumns;

/**
 * This defines the constants for the SQLite database.
 */
public final class DatabaseContract {
    /*Empty constructor for avoiding someone from creating an instance from this.*/
    public DatabaseContract(){}

    public static abstract class IdentityTokenTable implements BaseColumns{
        public static final String IDT_TABLE_NAME = "identity_token";
        public static final String COLUMN_NAME_FROM = "fromIdentity";
        public static final String COLUMN_NAME_TO = "toIdentity";
        public static final String COLUMN_NAME_IDT = "idt";
        public static final String COLUMN_NAME_EXPIRATION_TIME_STAMP = "expiration_ts";
    }

}
