package fish.philwants.glwaterloosquash.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import fish.philwants.glwaterloosquash.provider.SquashContract.StandingsEntry;

public class SquashProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sSquashQueryBuilder;
    private SquashDbHelper mOpenHelper;

    public static final int ENTRY = 100;
    public static final int FULL_STANDINGS = 101;
    public static final int GROUPS = 102;

    static {
        sSquashQueryBuilder = new SQLiteQueryBuilder();
        sSquashQueryBuilder.setTables(StandingsEntry.TABLE_NAME);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new SquashDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case FULL_STANDINGS:
            {
                retCursor = null;
                break;
            }
            case ENTRY:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        StandingsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GROUPS:
            {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        true,
                        StandingsEntry.TABLE_NAME,
                        new String[]{StandingsEntry.COLUMN__GROUP},
                        null,
                        null,
                        null,
                        null,
                        StandingsEntry.COLUMN__GROUP + " ASC",
                        null
                );
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknownn uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case ENTRY:
                return StandingsEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match  = sUriMatcher.match(uri);
        Uri resultUri;

        switch (match) {
            case ENTRY:
            {
                long _id = db.insert(StandingsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    resultUri = StandingsEntry.buildUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Failed to insert new row into " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        //db.close();
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        if (selection == null) selection = "1";

        switch (match) {
            case ENTRY: {
                rowsDeleted = db.delete(StandingsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Cannot delete for uri " + uri);
        }

        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //db.close();
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case ENTRY:
            {
                rowsUpdated = db.update(StandingsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Cannot update for uri: " + uri);
        }


        //db.close();
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case ENTRY:
            {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(StandingsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(SquashContract.CONTENT_AUTHORITY, SquashContract.PATH_STANDINGS, ENTRY);
        uriMatcher.addURI(SquashContract.CONTENT_AUTHORITY, SquashContract.PATH_GROUPS, GROUPS);

        return uriMatcher;
    }
}
