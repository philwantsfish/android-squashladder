package fish.philwants.glwaterloosquash;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import fish.philwants.glwaterloosquash.data.PlayerProfileInfo;

public class PreferenceUtility {
    private Context mContext;
    private SharedPreferences.Editor editor;
    private SharedPreferences preferences;
    private static String LOG_TAG = PreferenceUtility.class.getSimpleName();

    private String USERNAME_KEY = "username";
    private String PASSWORD_KEY = "password";
    private String LOGIN_KEY = "login_flag";
    private String PLAYER_ID_KEY = "playerId";
    private String EMAIL_KEY = "email";
    private String PHONE_KEY = "phone";
    private String GROUP_KEY = "group";
    private String DISPLAY_NAME_KEY = "DISPLAY_NAME_KEY";

    private String PREFERENCES_FILE_NAME = "preferences";

    public PreferenceUtility(Context context) {
        mContext = context;
        preferences = context.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public String username() {
        return preferences.getString(USERNAME_KEY, "");
    }

    public void storeUsername(String username) {
        editor.putString(USERNAME_KEY, username).commit();
    }

    public String password() {
        return preferences.getString(PASSWORD_KEY, "");
    }

    public void storePassword(String password) {
        editor.putString(PASSWORD_KEY, password).commit();
    }

    public boolean hasLoggedIn() {
        return preferences.getBoolean(LOGIN_KEY, false);
    }

    public void storeLoginFlag(boolean status) {
        editor.putBoolean(LOGIN_KEY, status).commit();
    }

    public String playerId() {
        return preferences.getString(PLAYER_ID_KEY, "");
    }

    public void storePlayerId(String playerId) {
        editor.putString(PLAYER_ID_KEY, playerId).commit();
    }

    public String email() {
        return preferences.getString(EMAIL_KEY, "");
    }

    public void storeEmail(String email) {
        editor.putString(EMAIL_KEY, email);
    }

    public String phone() {
        return preferences.getString(PHONE_KEY, "");
    }

    public void storePhone(String phone) {
        editor.putString(PHONE_KEY, phone).commit();
    }

    public String group() {
        return preferences.getString(GROUP_KEY, "");
    }

    public void storeGroup(String group) {
        editor.putString(GROUP_KEY, group).commit();
    }

    public String displayName() {
        return preferences.getString(DISPLAY_NAME_KEY, "");
    }

    public void storeDisplayName(String displayName) {
        editor.putString(DISPLAY_NAME_KEY, displayName).commit();
    }

    public void storePlayerProfileInfo(PlayerProfileInfo info) {
        storeDisplayName(info.getFirstName() + " " + info.getLastName());
        storeEmail(info.getEmail());
        storeGroup(info.getGroup());
        storePhone(info.getPhoneNumber());
        storePlayerId(info.getPlayerId());
    }
}
