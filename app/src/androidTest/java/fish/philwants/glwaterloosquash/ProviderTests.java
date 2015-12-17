package fish.philwants.glwaterloosquash;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.test.AndroidTestCase;

import fish.philwants.glwaterloosquash.provider.SquashContract;
import fish.philwants.glwaterloosquash.provider.SquashDbHelper;

public class ProviderTests extends AndroidTestCase {
    public static final String LOG_TAG = ProviderTests.class.getSimpleName();


    void deleteAllRecordsFromDb() {
        SQLiteDatabase db = new SquashDbHelper(mContext).getWritableDatabase();
        db.delete(SquashContract.StandingsEntry.TABLE_NAME, null, null);
        db.close();
    }

    void deleteAllRecords() {
        mContext.getContentResolver().delete(
                SquashContract.StandingsEntry.CONTENT_URI,
                null,
                null);

        Cursor c = mContext.getContentResolver().query(SquashContract.StandingsEntry.CONTENT_URI, null, null, null, null);
        assertEquals("Error: you didnt delet all records", 0, c.getCount());
        c.close();
    }



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecordsFromDb();
    }

    public void testInsertReadProvider() {
        ContentValues cv = TestUtilities.createTestData();

        mContext.getContentResolver().insert(SquashContract.StandingsEntry.CONTENT_URI, cv);
        Cursor c = mContext.getContentResolver().query(
                SquashContract.StandingsEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: did not insert into database using provider", c.moveToFirst());
    }

    public void testDeleteRecords() {
        testInsertReadProvider();
        deleteAllRecords();
        Cursor c = mContext.getContentResolver().query(
                SquashContract.StandingsEntry.CONTENT_URI,
                null,
                null,
                null,
                null,
                null);
        assertEquals(0, c.getCount());
    }

    public void testUpdateRecord() {
        // Insert row into db and gets its row id
        ContentValues cv = TestUtilities.createTestData();
        Uri uri = mContext.getContentResolver().insert(SquashContract.StandingsEntry.CONTENT_URI, cv);
        long locationRowId = ContentUris.parseId(uri);
        assertTrue(locationRowId != -1);

        // Create updated value
        ContentValues newcv = new ContentValues(cv);
        newcv.put(SquashContract.StandingsEntry._ID, locationRowId);
        newcv.put(SquashContract.StandingsEntry.COLUMN__WINS, "2");

        // Perform the update, confirm one record modified
        int count = mContext.getContentResolver().update(
                SquashContract.StandingsEntry.CONTENT_URI,
                newcv,
                SquashContract.StandingsEntry._ID + "= ?", new String[]{Long.toString(locationRowId)});
        assertEquals(count, 1);

        // Check the record was modified correctly
        Cursor c = mContext.getContentResolver().query(SquashContract.StandingsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
        assertTrue("Error: this shouldnt ever happen", c.moveToFirst());
        int columnIndex = c.getColumnIndex(SquashContract.StandingsEntry.COLUMN__WINS);
        String wins = c.getString(columnIndex);
        assertEquals("2", wins);
    }
}
