package fish.philwants.glwaterloosquash;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import fish.philwants.glwaterloosquash.provider.SquashContract;
import fish.philwants.glwaterloosquash.provider.SquashDbHelper;
import fish.philwants.glwaterloosquash.provider.SquashProvider;


public class TestDb extends AndroidTestCase {
    void deleteTheDatabase() { mContext.deleteDatabase(SquashDbHelper.DATABASE_NAME);}

    public void setUp() { deleteTheDatabase(); }


    public void testCreateDb() throws Throwable {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(SquashContract.StandingsEntry.TABLE_NAME);

        mContext.deleteDatabase(SquashDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new SquashDbHelper(this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: Database was not created successfully", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Database was created with the proper table", tableNameHashSet.isEmpty());

        c = db.rawQuery("PRAGMA table_info(" + SquashContract.StandingsEntry.TABLE_NAME + ")", null);
        assertTrue("Error: Unable to query db for table info", c.moveToFirst());

        final HashSet<String> locationColumnHashSet = new HashSet<String>();
        locationColumnHashSet.add(SquashContract.StandingsEntry._ID);
        locationColumnHashSet.add(SquashContract.StandingsEntry.COLUMN__GAMES__PLAYED);
        locationColumnHashSet.add(SquashContract.StandingsEntry.COLUMN__GROUP);
        locationColumnHashSet.add(SquashContract.StandingsEntry.COLUMN__LOSSES);
        locationColumnHashSet.add(SquashContract.StandingsEntry.COLUMN__PLAYER_NAME);
        locationColumnHashSet.add(SquashContract.StandingsEntry.COLUMN__POINTS);
        locationColumnHashSet.add(SquashContract.StandingsEntry.COLUMN__WINS);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (c.moveToNext());
        assertTrue("Error: Tabke doesnt have all of the columns", locationColumnHashSet.isEmpty());
        db.close();
    }

    public long insertTestData() {
        SQLiteDatabase db = new SquashDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        ContentValues cv = TestUtilities.createTestData();

        long rowid = db.insert(SquashContract.StandingsEntry.TABLE_NAME, null, cv);
        assertTrue(rowid != -1);

        Cursor c = db.query(SquashContract.StandingsEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Moving cursor to row one", c.moveToFirst());
        TestUtilities.validateCurrentRecord("Error location query validation failed", c, cv);

        assertFalse("error: more than one record returned from location query", c.moveToNext());
        c.close();
        db.close();

        return rowid;
    }

    public void testLocationTable() {
        insertTestData();
    }

}
