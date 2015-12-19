package fish.philwants.glwaterloosquash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import fish.philwants.glwaterloosquash.client.tasks.UpdateStandingsTask;
import fish.philwants.glwaterloosquash.client.SquashServer;
import fish.philwants.glwaterloosquash.client.tasks.UpdateUserInfoTask;


public class LoginFragment extends Fragment {
    private static final String LOG_TAG = LoginFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        Button button = (Button) rootView.findViewById(R.id.login_button);
        button.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                Button b = (Button) view.findViewById(R.id.login_button);
                b.setEnabled(false);
                String username = getUsername((View) view.getParent());
                String password = getPassword((View) view.getParent());

                SquashServer server = new SquashServer();
                Boolean loginSuccessful = server.checkLogin(username, password);

                if(loginSuccessful) {
                    // Store the user's credentials
                    storeUsernameAndPassword(getContext(), username, password);

                    // Get and store the user's profile information
                    UpdateUserInfoTask task = new UpdateUserInfoTask(getContext());
                    task.execute();

                    Intent intent = new Intent(getContext(), StandingsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), R.string.failed_login, Toast.LENGTH_LONG).show();
                }
                b.setEnabled(true);
            }
        });

        return rootView;
    }

    private String getUsername(View view) {
        TextView usernameView = (TextView) view.findViewById(R.id.username);
        return usernameView.getText().toString();
    }

    private String getPassword(View view) {
        TextView passwordView = (TextView) view.findViewById(R.id.password);
        return passwordView.getText().toString();
    }

    private void storeUsernameAndPassword(Context context, String username, String password) {
        PreferenceUtility pref = new PreferenceUtility(context);
        pref.storeUsername(username);
        pref.storePassword(password);
        pref.storeLoginFlag(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.displaystandingsfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            UpdateStandingsTask task = new UpdateStandingsTask(getActivity());
            task.execute();
        }

        return super.onOptionsItemSelected(item);
    }
}
