package fish.philwants.glwaterloosquash;

import android.app.Activity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fish.philwants.glwaterloosquash.client.tasks.UpdateStandingsTask;
import fish.philwants.glwaterloosquash.provider.SquashContract.StandingsEntry;

public class GroupFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private String LOG_TAG = GroupFragment.class.getSimpleName();
    private static final int STANDINGS_LOADER = 1;

    private OnFragmentInteractionListener mListener;
    private StandingsAdaptor cAdaptor;

    static final int COL_ID = 0;
    static final int COL_GROUP = 1;
    static final int COL_PLAYER = 2;
    static final int COL_GAMES_PLAYED = 3;
    static final int COL_WINS = 4;
    static final int COL_LOSSES = 5;
    static final int COL_POINTS = 6;

    public static final String[] STANDINGS_COLUMNS = {
            StandingsEntry._ID,
            StandingsEntry.COLUMN__GROUP,
            StandingsEntry.COLUMN__PLAYER_NAME,
            StandingsEntry.COLUMN__GAMES__PLAYED,
            StandingsEntry.COLUMN__WINS,
            StandingsEntry.COLUMN__LOSSES,
            StandingsEntry.COLUMN__POINTS };


    public void updateStandings() {
        UpdateStandingsTask task = new UpdateStandingsTask(getActivity());
        task.execute();
        getLoaderManager().restartLoader(STANDINGS_LOADER, null, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //cAdaptor = new StandingsAdaptor(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_standings, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_standings);
        listView.setAdapter(cAdaptor);

        return rootView;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(STANDINGS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri standingsUri = StandingsEntry.CONTENT_URI;
        String sortOrder = StandingsEntry.COLUMN__GROUP +  " ASC, " + "CAST(" + StandingsEntry.COLUMN__POINTS + " as integer) DESC";

        PreferenceUtility prefs = new PreferenceUtility(getContext());
        return new CursorLoader(getActivity(),
                standingsUri,
                STANDINGS_COLUMNS,
                StandingsEntry.COLUMN__GROUP + "= ?",
                new String[] { prefs.group() },
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cAdaptor.swapCursor(data);
        cAdaptor.updateHeaderIndexs();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cAdaptor.swapCursor(null);
    }
}
