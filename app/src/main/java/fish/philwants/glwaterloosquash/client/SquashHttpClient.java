package fish.philwants.glwaterloosquash.client;


import android.content.ContentValues;
import android.util.Log;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fish.philwants.glwaterloosquash.data.PlayerProfileInfo;

public class SquashHttpClient {
    static String LOG_TAG = SquashHttpClient.class.getSimpleName();
    private Map<String, String> cookies;

    public SquashHttpClient() {
        this.cookies = new HashMap<String, String>();
    }

    public static String sendGetRequest(URI uri) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = null;
        try {
            URL url = new URL(uri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int responseCode = urlConnection.getResponseCode();
            if(responseCode != 200 && responseCode != 302){
                Log.i(LOG_TAG, "Phil: Response code is: " + responseCode);
            }


            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                Log.e(LOG_TAG, "Error connecting to Squash!");
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                Log.e(LOG_TAG, "Empty response from Squash!");
                return null;
            }
            response = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        return response;
    }

    public boolean login(String username, String password) {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        Map<String, String> loginParams = new HashMap<String, String>();
        Boolean successfulLogin = false;

        try {
            // Send a request to obtain the asp viewstate data
            Document doc = Jsoup.connect("http://glwaterloo.squashladder.ca/sportleagues/squashladder/glwaterloo/Login.aspx").get();

            // The data is contained in the aspnetForm element
            Element form = doc.getElementById("aspnetForm");
            if (form == null) {
                Log.e(LOG_TAG, "Phil: No form with id aspnetForm");
                return false;
            }
            // Iterate through each one parsing the required data
            Elements inputs = form.getElementsByTag("input");
            for (Element input : inputs) {
                String name = input.attr("id");
                String value = input.attr("value");
                // The elements that start ctl00 change _ to $ for some reason
                // Lets handle these afterwards since they contain credential info anyways
                if(name.startsWith("ctl00")) {
                    continue;
                } else {
                    //Log.i(LOG_TAG, "Phil: Updating parameter: " + name + ":" + value);
                    loginParams.put(name, value);
                }
            }

            // Update login credentials
            loginParams.put("ctl00$MainContentArea$Login1$UserName", username);
            loginParams.put("ctl00$MainContentArea$Login1$Password", password);
            loginParams.put("ctl00$MainContentArea$Login1$LoginButton", "Log In");
            loginParams.put("__EVENTTARGET", "");
            loginParams.put("__EVENTARGUMENT", "");
            loginParams.put("ctl00_uxMainMenu_ClientState", "");


            if (loginParams.size() != 9) {
                Log.e(LOG_TAG, "Phil: Wrong number of arguments");
            }

            // Log in!
            Connection connection = Jsoup.connect("http://glwaterloo.squashladder.ca/sportleagues/squashladder/glwaterloo/Login.aspx?ReturnUrl=%2f")
                    .method(Connection.Method.POST)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .data(loginParams)
                    .followRedirects(true)
                    .ignoreHttpErrors(true);
            Connection.Response response = connection.execute();

            // Confirm a the login was succesful
            Document d = response.parse();
            String responseText = d.toString();
            if(responseText.contains("Your login attempt was not successful")) {
                //Log.e(LOG_TAG, "Phil: Failed login, No logout tag");
                return false;
            } else if(responseText.contains("Logout")) {
                Log.i(LOG_TAG, "Phil: Successful login");
            } else {
                Log.i(LOG_TAG, "Phil: Unknown login status");
                return false;
            }

            // Save cookies for subsequent requests
            cookies.clear();
            cookies.putAll(response.cookies());

        } catch (IOException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            Log.e(LOG_TAG, "Phil: jsoup error");
            return false;
        }

        return true;
    }

    public ArrayList<ContentValues> standings() {
        ArrayList<ContentValues> standings = new ArrayList<ContentValues>();

        try {
            Connection connection = Jsoup.connect("http://glwaterloo.squashladder.ca/standings")
                    .cookies(cookies);
            Connection.Response response = connection.execute();

            SquashResponseParser p = new SquashResponseParser();
            standings = p.standings(response.parse());


        } catch (IOException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            Log.e(LOG_TAG, "Phil: jsoup error2");
        }

        return standings;
    }

    public PlayerProfileInfo profile() {
        PlayerProfileInfo profile = null;
        try {
            Connection connection = Jsoup.connect("http://glwaterloo.squashladder.ca/MyProfile").cookies(cookies);
            Connection.Response response = connection.execute();

            SquashResponseParser p = new SquashResponseParser();
            profile = p.profile(response.parse());
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getLocalizedMessage());
            Log.e(LOG_TAG, "Phil: jsoup error3");
        }
        return profile;
    }
}
