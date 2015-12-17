package fish.philwants.glwaterloosquash;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import fish.philwants.glwaterloosquash.provider.SquashContract.StandingsEntry;


import fish.philwants.glwaterloosquash.provider.SquashContract;

public class SquashAdapter extends CursorAdapter {
    private ArrayList<Integer> headerIndexs = new ArrayList<Integer>();
    private Context mContext;
    private static final String LOG_TAG = SquashAdapter.class.getSimpleName();

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ROW = 1;

    public SquashAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
        updateHeaderIndexs();
    }

    public void updateHeaderIndexs() {
        headerIndexs.clear();
        headerIndexs.add(0);
        Cursor c = mContext.getContentResolver().query(StandingsEntry.CONTENT_URI,
                DisplayStandingsFragment.STANDINGS_COLUMNS,
                null,
                null,
                StandingsEntry.COLUMN__GROUP + " ASC");

        if(c.moveToFirst()) {
            String currentGroup = c.getString(DisplayStandingsFragment.COL_GROUP);
            while (c.moveToNext()) {
                String newGroup = c.getString(DisplayStandingsFragment.COL_GROUP);
                if (!currentGroup.equals(newGroup)) {
                    currentGroup = newGroup;
                    headerIndexs.add(c.getPosition());
                }
            }
        }
        c.close();
    }

    @Override
    public int getItemViewType(int position) {
        return headerIndexs.contains(position) ? VIEW_TYPE_HEADER : VIEW_TYPE_ROW;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;
        int viewType = getItemViewType(cursor.getPosition());
        if(viewType == VIEW_TYPE_HEADER){
            layoutId = R.layout.list_item_standings_header_row;
        } else if (viewType == VIEW_TYPE_ROW) {
            layoutId = R.layout.list_item_standings_row;
        }
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView groupView = (TextView) view.findViewById(R.id.heading_group);
        TextView playerView = (TextView) view.findViewById(R.id.standings_player);
        TextView games_playedView = (TextView) view.findViewById(R.id.standings_game_played);
        TextView winsView = (TextView) view.findViewById(R.id.standings_wins);
        TextView lossesView = (TextView) view.findViewById(R.id.standings_losses);
        TextView pointsView = (TextView) view.findViewById(R.id.standings_points);

        if(groupView != null) {
            groupView.setText("Group " + cursor.getString(DisplayStandingsFragment.COL_GROUP));
        }
        playerView.setText(cursor.getString(DisplayStandingsFragment.COL_PLAYER));
        games_playedView.setText(cursor.getString(DisplayStandingsFragment.COL_GAMES_PLAYED));
        winsView.setText(cursor.getString(DisplayStandingsFragment.COL_WINS));
        lossesView.setText(cursor.getString(DisplayStandingsFragment.COL_LOSSES));
        pointsView.setText(String.format("%3s" ,cursor.getString(DisplayStandingsFragment.COL_POINTS)));
    }
}
