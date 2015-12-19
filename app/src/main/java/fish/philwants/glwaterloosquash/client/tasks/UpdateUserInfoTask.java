package fish.philwants.glwaterloosquash.client.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import fish.philwants.glwaterloosquash.PreferenceUtility;
import fish.philwants.glwaterloosquash.client.SquashHttpClient;
import fish.philwants.glwaterloosquash.data.PlayerProfileInfo;
import fish.philwants.glwaterloosquash.provider.SquashContract;

/**
 * This task will update the shared preferences with the users latest info, such as:
 * player identifier, first name, last name, email, phone number, current standings group
 */
public class UpdateUserInfoTask extends AsyncTask<Void, Void, PlayerProfileInfo> {
    private final String LOG_TAG = UpdateStandingsTask.class.getSimpleName();

    private Context mContext;
    public UpdateUserInfoTask(Context context) {
        mContext = context;
    }



    @Override
    protected PlayerProfileInfo doInBackground(Void... params) {
        SquashHttpClient client = new SquashHttpClient();
        PreferenceUtility prefs = new PreferenceUtility(mContext);

        PlayerProfileInfo info = null;
        if(!client.login(prefs.username(), prefs.password())) {
            // if we ever fail to log in I think we should boot the user back to the login screen
            Log.e(LOG_TAG, "Phil: failed to login during player info update");
        } else {
            Log.i(LOG_TAG, "Phil: continue updating player info");
            info = client.profile();
            prefs.storePlayerProfileInfo(info);
        }
        return null;
    }

    @Override
    protected void onPostExecute(PlayerProfileInfo info) {
//        PreferenceUtility prefs = new PreferenceUtility(mContext);
//        if(info != null) {
//            prefs.storePlayerProfileInfo(info);
//        }
    }
}
