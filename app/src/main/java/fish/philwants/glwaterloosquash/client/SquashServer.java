package fish.philwants.glwaterloosquash.client;

import android.util.Log;

import java.util.concurrent.ExecutionException;

import fish.philwants.glwaterloosquash.client.tasks.CheckLoginTask;

public class SquashServer {
    private static String LOG_TAG = SquashServer.class.getSimpleName();

    public Boolean checkLogin(String username, String password) {
        Boolean loginSuccessful = false;
        Log.v(LOG_TAG, "Phil: loggin in with creds " + username + ":" + password);
        CheckLoginTask task = new CheckLoginTask(username, password);
        try {
            loginSuccessful = task.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return loginSuccessful;
    }
}
