package fish.philwants.glwaterloosquash.client.tasks;

import android.os.AsyncTask;

import fish.philwants.glwaterloosquash.client.SquashHttpClient;

public class CheckLoginTask extends AsyncTask<Void, Void, Boolean> {
    private final String LOG_TAG = CheckLoginTask.class.getSimpleName();
    private String username;
    private String password;

    public CheckLoginTask(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        SquashHttpClient client = new SquashHttpClient();
        return client.login(username, password);
    }

    @Override
    protected void onPostExecute(Boolean loginSuccessful) {
    }
}
