package fish.philwants.glwaterloosquash;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

import fish.philwants.glwaterloosquash.provider.SquashContract;
import fish.philwants.glwaterloosquash.provider.SquashProvider;


public class TestUrimatcher extends AndroidTestCase {
    public static final String LOG_TAG = TestUrimatcher.class.getSimpleName();

    public void testUriMatcher() {
        UriMatcher testMatcher = SquashProvider.buildUriMatcher();

        Uri TEST_SQUASH_ENTRY_DIR = SquashContract.StandingsEntry.CONTENT_URI;
        assertEquals("Error: The Squash Entry was matched incorrectly", testMatcher.match(TEST_SQUASH_ENTRY_DIR), SquashProvider.ENTRY);
    }

    public void testUriMatcher2() {
        UriMatcher testMatcher = SquashProvider.buildUriMatcher();

        Uri TEST_SQUASH_ENTRY_DIR = SquashContract.StandingsEntry.CONTENT_GROUPS;
        assertEquals("Error: The Squash Entry was matched incorrectly", testMatcher.match(TEST_SQUASH_ENTRY_DIR), SquashProvider.GROUPS);
    }

}
