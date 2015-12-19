package fish.philwants.glwaterloosquash;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import fish.philwants.glwaterloosquash.client.SquashServer;

public class StandingsActivity extends AppCompatActivity implements StandingsFragment.OnFragmentInteractionListener {
    private String LOG_TAG = StandingsActivity.class.getSimpleName();
    public String DISPLAY_TAG = "DisplayTag";
    public String DISPLAY_TAG2 = "DisplayTag2";

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean checkCredentials() {
        SquashServer server = new SquashServer();
        PreferenceUtility prefs = new PreferenceUtility(this);
        return server.checkLogin(prefs.username(), prefs.password());
    }

    private boolean hasLoggedInSuccessfully() {
        PreferenceUtility prefs = new PreferenceUtility(this);
        return prefs.hasLoggedIn();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // A user must enter their credentials on first use
        // once they've entered valid credentials, bypass this check.
        boolean successful_login = checkCredentials();

        if(hasLoggedInSuccessfully()) {
            startLoginActivity();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new StandingsFragment(), DISPLAY_TAG)
                    //.add(R.id.container, new GroupFragment(), DISPLAY_TAG2)
                    .commit();
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        StandingsFragment ds = (StandingsFragment) getSupportFragmentManager().findFragmentByTag(DISPLAY_TAG);
        GroupFragment groupFragment = (GroupFragment) getSupportFragmentManager().findFragmentByTag(DISPLAY_TAG2);
        if (ds != null) {
            Log.i(LOG_TAG, "Phil: Updating the database");
            //ds.updateStandings();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
