package fish.philwants.glwaterloosquash.provider;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import fish.philwants.glwaterloosquash.provider.SquashContract.StandingsEntry;

public class SquashDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERION = 2;
    public static final String DATABASE_NAME = "squash.db";

    public SquashDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_STANDINGSENTRY_TABLE = "CREATE TABLE " + StandingsEntry.TABLE_NAME + " (" +
                StandingsEntry._ID + " INTEGER PRIMARY KEY," +
                StandingsEntry.COLUMN__GROUP + " TEXT NOT NULL,  " +
                StandingsEntry.COLUMN__PLAYER_NAME + " TEXT UNIQUE NOT NULL," +
                StandingsEntry.COLUMN__GAMES__PLAYED + " TEXT NOT NULL,  " +
                StandingsEntry.COLUMN__WINS + " TEXT NOT NULL,  " +
                StandingsEntry.COLUMN__LOSSES + " TEXT NOT NULL,  " +
                StandingsEntry.COLUMN__POINTS + " INTEGER NOT NULL  " +
                " );";
        db.execSQL(SQL_CREATE_STANDINGSENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + StandingsEntry.TABLE_NAME);
        onCreate(db);
    }
}
