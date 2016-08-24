package org.biomt.auth.authapp;

import android.provider.ContactsContract;

/**
 * Created by hasini on 8/24/16.
 */
public class DatabaseManager {

    /*Define the constants*/
    private static final String TEXT_NAME = " TEXT";
    private static final String COMMA_SEPARATOR = ",";
    private static final String SQL_CREATE_IDT_TABLE =
            "CREATE TABLE " + DatabaseContract.IdentityTokenTable.IDT_TABLE_NAME + " (" +
                    DatabaseContract.IdentityTokenTable._ID + " INTEGER PRIMARY KEY," +
                    DatabaseContract.IdentityTokenTable.COLUMN_NAME_FROM + TEXT_NAME + COMMA_SEPARATOR +
                    DatabaseContract.IdentityTokenTable.COLUMN_NAME_TO + TEXT_NAME + COMMA_SEPARATOR +
                    DatabaseContract.IdentityTokenTable.COLUMN_NAME_IDT + TEXT_NAME + COMMA_SEPARATOR +
                    DatabaseContract.IdentityTokenTable.COLUMN_NAME_EXPIRATION_TIME_STAMP + TEXT_NAME + COMMA_SEPARATOR +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.IdentityTokenTable.IDT_TABLE_NAME;

}
