package fish.philwants.glwaterloosquash.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class SquashContract {
    public static final String CONTENT_AUTHORITY = "fish.philwants.glwaterloosquash";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STANDINGS = "standings";
    public static final String PATH_GROUPS = "groups";

    public static final class StandingsEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_STANDINGS).build();
        public static final Uri CONTENT_GROUPS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GROUPS).build();

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STANDINGS;

        public static final String TABLE_NAME = "standings";
        public static final String COLUMN__GROUP = "ladder_group";
        public static final String COLUMN__PLAYER_NAME = "player";
        public static final String COLUMN__GAMES__PLAYED = "games_played";
        public static final String COLUMN__WINS = "wins";
        public static final String COLUMN__LOSSES = "losses";
        public static final String COLUMN__POINTS = "points";

        public static Uri buildUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
