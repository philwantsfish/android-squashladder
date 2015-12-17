package fish.philwants.glwaterloosquash;

import android.content.ContentValues;
import android.test.AndroidTestCase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

import fish.philwants.glwaterloosquash.client.SquashResponseParser;


public class SquashResponseParserTests extends AndroidTestCase {
    public void testResponseParser() {
        Document doc = Jsoup.parse(SquashResponseParserData.responseHTML());
        SquashResponseParser srp = new SquashResponseParser();
        ArrayList<ContentValues> cv= srp.standings(doc);
        assertEquals(64, cv.size());
    }
}
