package org.biomt.auth.authapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lib.zkp4.identity.commit.IdentityToken;
import lib.zkp4.identity.util.IdentityTokenEncoderDecoder;
import lib.zkp4.identity.util.JSONIdentityTokenEncoderDecoder;

/**
 * Created by hasini on 8/25/16.
 */

/**
 * This performs data base access operations : Read/Write with locking.
 */
public class DatabaseAccessManager {

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock rl = rwl.readLock();
    private final Lock wl = rwl.writeLock();

    public String readFromIdentityTokenTable(Context context, String fromIdentity, String toIdentity) {
        DatabaseHelper dbHelper = null;
        SQLiteDatabase db = null;
        rl.lock();
        try {
            //get the db in read mode
            dbHelper = new DatabaseHelper(context);
            db = dbHelper.getReadableDatabase();

            //format of the query result:
            String[] projection = {DatabaseContract.IdentityTokenTable.COLUMN_NAME_IDT};
            String selection = DatabaseContract.IdentityTokenTable.COLUMN_NAME_FROM + "=?" + " and " +
                    DatabaseContract.IdentityTokenTable.COLUMN_NAME_TO + "=?";
            String[] selectionArgs = {fromIdentity, toIdentity};
            Cursor results = db.query(DatabaseContract.IdentityTokenTable.IDT_TABLE_NAME, projection,
                    selection, selectionArgs, null, null, null);
            //we assume there is only one identity token for a given to and from value combination.
            boolean resultExists = results.moveToFirst();
            if (resultExists){
                return results.getString(results.getColumnIndex(DatabaseContract.IdentityTokenTable.COLUMN_NAME_IDT));
            } else {
                return null;
            }

        } finally {
            db.close();
            dbHelper.close();
            rl.unlock();
        }
    }

    /*Although IdentityToken could be built from the String, we perform minimal operations in this
    * method as this is executed inside a lock and we need to release the lock soon.*/
    public String writeToIdentityTokenTable(Context context, String identityTokenString,
                                            IdentityToken identityToken) throws Exception {
        DatabaseHelper dbHelper = null;
        SQLiteDatabase db = null;
        wl.lock();
        try {
            //get the database in the write mode
            dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();

            //create a map of values with the data extracted from the identity token to be put into the db
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.IdentityTokenTable.COLUMN_NAME_FROM, identityToken.getFromIdentity());
            values.put(DatabaseContract.IdentityTokenTable.COLUMN_NAME_TO, identityToken.getToIdentity());
            values.put(DatabaseContract.IdentityTokenTable.COLUMN_NAME_IDT, identityTokenString);
            //TODO: calculate the expiration timestamp
            values.put(DatabaseContract.IdentityTokenTable.COLUMN_NAME_EXPIRATION_TIME_STAMP,
                    identityToken.getCreationTimestamp().toString());

            //insert the new row.
            long newRowId = db.insert(DatabaseContract.IdentityTokenTable.IDT_TABLE_NAME, null, values);
            //return the row id.
            return String.valueOf(newRowId);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new Exception("Error in decoding the Identity Token.");
        } finally {
            db.close();
            dbHelper.close();
            wl.unlock();
        }
    }
}
