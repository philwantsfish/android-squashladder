package fish.philwants.glwaterloosquash;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import fish.philwants.glwaterloosquash.client.SquashResponseParser;
import fish.philwants.glwaterloosquash.data.PlayerProfileInfo;


public class SquashResponseParserTests extends AndroidTestCase {
    public void testResponseParser() {
        Document doc = Jsoup.parse(SquashResponseParserData.responseStandingsHtml());
        SquashResponseParser srp = new SquashResponseParser();
        ArrayList<ContentValues> cv= srp.standings(doc);
        assertEquals(64, cv.size());
    }

    public void testProfile() {
        Document doc = Jsoup.parse(SquashResponseParserData.responseMyProfileHtml());
        SquashResponseParser srp = new SquashResponseParser();
        PlayerProfileInfo player = srp.profile(doc);
        assertEquals("Philip", player.getFirstName());
        assertEquals("OKeefe", player.getLastName());
        assertEquals("okeefephil@gmail.com", player.getEmail());
        assertEquals("2267513717", player.getPhoneNumber());
        assertEquals("I", player.getGroup());
        assertEquals("26038", player.getPlayerId());

    }
}
