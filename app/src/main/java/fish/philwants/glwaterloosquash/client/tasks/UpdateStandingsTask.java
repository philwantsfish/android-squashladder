package fish.philwants.glwaterloosquash.client.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import java.util.ArrayList;

import fish.philwants.glwaterloosquash.client.SquashHttpClient;
import fish.philwants.glwaterloosquash.provider.SquashContract;

/**
 * This task will update the SquashProvider with the latest standings data
 */
public class UpdateStandingsTask extends AsyncTask<Void, Void, ArrayList<ContentValues>> {
    private final String LOG_TAG = UpdateStandingsTask.class.getSimpleName();

    private Context mContext;
    public UpdateStandingsTask(Context context) {
        mContext = context;
    }

    @Override
    protected ArrayList<ContentValues> doInBackground(Void... params) {
        SquashHttpClient client = new SquashHttpClient();

        //if(!client.login()) {
        //    Log.e(LOG_TAG, "Phil: fail");
        //}

        ArrayList<ContentValues> standings = client.standings();
        ContentValues[] cv = standings.toArray(new ContentValues[standings.size()]);
        mContext.getContentResolver().delete(SquashContract.StandingsEntry.CONTENT_URI, null, null);
        mContext.getContentResolver().bulkInsert(SquashContract.StandingsEntry.CONTENT_URI, (ContentValues[]) cv);
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ContentValues> standings) {
        if (standings != null) {

            /*StandingsFragment.standingsAdapter.clear();
            for (String key : standings.keySet()) {
                Log.i(LOG_TAG, "Adding group " + key);
                StandingsFragment.standingsAdapter.add("Group " + key);
                for (GroupRow player : standings.get(key)) {
                    Log.i(LOG_TAG, "Phil: adding player " + player.toString());
                    StandingsFragment.standingsAdapter.add(player.toString());
                }
            }*/
        }
    }
}
