package fish.philwants.glwaterloosquash;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fish.philwants.glwaterloosquash.provider.SquashContract.StandingsEntry;

public class GroupAdaptor extends CursorAdapter {
    private ArrayList<Integer> headerIndexs = new ArrayList<Integer>();
    private Context mContext;
    private static final String LOG_TAG = StandingsAdaptor.class.getSimpleName();

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ROW = 1;

    public GroupAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
        //updateHeaderIndexs();
    }
//
//    public void updateHeaderIndexs() {
//        headerIndexs.clear();
//        headerIndexs.add(0);
//        Cursor c = mContext.getContentResolver().query(StandingsEntry.CONTENT_URI,
//                StandingsFragment.STANDINGS_COLUMNS,
//                null,
//                null,
//                StandingsEntry.COLUMN__GROUP + " ASC");
//
//        if(c.moveToFirst()) {
//            String currentGroup = c.getString(StandingsFragment.COL_GROUP);
//            while (c.moveToNext()) {
//                String newGroup = c.getString(StandingsFragment.COL_GROUP);
//                if (!currentGroup.equals(newGroup)) {
//                    currentGroup = newGroup;
//                    headerIndexs.add(c.getPosition());
//                }
//            }
//        }
//        c.close();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return headerIndexs.contains(position) ? VIEW_TYPE_HEADER : VIEW_TYPE_ROW;
//    }
//
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        int layoutId = -1;
        int viewType = getItemViewType(cursor.getPosition());
//        if(viewType == VIEW_TYPE_HEADER){
//            layoutId = R.layout.list_item_standings_header_row;
//        } else if (viewType == VIEW_TYPE_ROW) {
//            layoutId = R.layout.list_item_standings_row;
//        }
        layoutId = R.layout.list_item_standings_row;
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
            groupView.setText("Group " + cursor.getString(StandingsFragment.COL_GROUP));
        }
        playerView.setText(cursor.getString(StandingsFragment.COL_PLAYER));
        games_playedView.setText(cursor.getString(StandingsFragment.COL_GAMES_PLAYED));
        winsView.setText(cursor.getString(StandingsFragment.COL_WINS));
        lossesView.setText(cursor.getString(StandingsFragment.COL_LOSSES));
        pointsView.setText(String.format("%3s" ,cursor.getString(StandingsFragment.COL_POINTS)));
    }
}
