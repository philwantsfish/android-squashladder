package fish.philwants.glwaterloosquash;


import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

import fish.philwants.glwaterloosquash.provider.SquashContract;

public class TestUtilities extends AndroidTestCase {
    static ContentValues createTestData() {
        ContentValues tv = new ContentValues();
        tv.put(SquashContract.StandingsEntry.COLUMN__GAMES__PLAYED, "1");
        tv.put(SquashContract.StandingsEntry.COLUMN__GROUP, "A");
        tv.put(SquashContract.StandingsEntry.COLUMN__LOSSES, "0");
        tv.put(SquashContract.StandingsEntry.COLUMN__PLAYER_NAME, "Nikola Tesla");
        tv.put(SquashContract.StandingsEntry.COLUMN__POINTS, "6");
        tv.put(SquashContract.StandingsEntry.COLUMN__WINS, "1");
        return tv;
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }
}
