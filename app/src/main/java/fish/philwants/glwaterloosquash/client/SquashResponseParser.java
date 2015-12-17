package fish.philwants.glwaterloosquash.client;

import android.content.ContentValues;
import android.util.Log;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import fish.philwants.glwaterloosquash.provider.SquashContract;

public class SquashResponseParser {
    private String LOG_TAG = SquashResponseParser.class.getSimpleName();
    private static final String headerRow = "rgGroupHeader";
    private static final String playerRow = "rgRow";
    private static final String playerRowAlt = "rgAltRow";

    public ArrayList<ContentValues> standings(Document document) {
        ArrayList<ContentValues> standings = new ArrayList<ContentValues>();

        Element table = document.getElementById("ctl00_MainContentArea_RadGrid1_ctl00");
        Elements tableRows = table.getElementsByTag("tr");

        // The table has 3 types of row
        // Group header rows, class="rgGroupHeader"
        // Player row, class="rgRow"
        // Alternate color player row, class="rgAltRow"
        // Iterate through the rows and group each row by its header type
        String currentGroup = "";
        for (Element row : tableRows) {
            String rowtype = row.attr("class");
            //Log.i(LOG_TAG, "Phil: found row type :" + rowtype);
            switch (rowtype) {
                case headerRow:
                    currentGroup = parseHeaderRow(row);
                    // initialize the group
                    //standings.put(currentGroup, new ArrayList<GroupRow>());
                    break;
                case playerRow:
                case playerRowAlt:
                    // Append the row to the grouping
                    //ArrayList<GroupRow> partialGroup = standings.get(currentGroup);
                    //partialGroup.add(parsePlayerRow(row));
                    //standings.put(currentGroup, partialGroup);
                    Elements r = row.getElementsByTag("td");

                    ContentValues cv = new ContentValues();
                    cv.put(SquashContract.StandingsEntry.COLUMN__GROUP, currentGroup);
                    cv.put(SquashContract.StandingsEntry.COLUMN__PLAYER_NAME, r.get(2).getElementsByTag("a").first().text());
                    cv.put(SquashContract.StandingsEntry.COLUMN__WINS, r.get(4).text());
                    cv.put(SquashContract.StandingsEntry.COLUMN__LOSSES, r.get(5).text());
                    cv.put(SquashContract.StandingsEntry.COLUMN__GAMES__PLAYED, r.get(3).text());
                    cv.put(SquashContract.StandingsEntry.COLUMN__POINTS, r.get(9).text());

                    standings.add(cv);
                    /*
                    String playerName = r.get(2).getElementsByTag("a").first().text();
                    String gp = r.get(3).text();
                    String w = r.get(4).text();
                    String l = r.get(5).text();
                    String pts = r.get(9).text();
                    */

                    break;
                default:
                    break;
            }

        }

        /*Log.i(LOG_TAG, "Phil: Parsed " + standings.keySet().size() + " groups");
        for (String key : standings.keySet()) {
            Log.i(LOG_TAG, "Phil: Group " + key + " has " + standings.get(key).size() + " players");
        }*/
        return standings;
    }

    protected String parseHeaderRow(Element row) {
        //Log.i(LOG_TAG, "Phil: inside parseHeaderRow");
        Elements text = row.getElementsByTag("p");
        if (text.size() > 1) {
            Log.e(LOG_TAG, "Phil: Found more than one p tag");
        }
        String groupText =  text.first().text();
        //Log.i(LOG_TAG, "Phil: Group : " + groupText);
        String group = groupText.substring(groupText.length() - 1);
        //Log.i(LOG_TAG, "Phil: Group: " + group);

        return group;
    }
}
