package developer.ishank.forlalit.sir.lsacademy.account;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.HashMap;

public class SessionManager {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    public Context context;
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String EMAIL = "EMAIL";

    public SessionManager(Context context) {
        int PRIVATE_MODE = 0;
        this.context = context;
        mSharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();
    }

    void createSession(String name, String email) {
        mEditor.putBoolean(LOGIN, true);
        mEditor.putString(NAME, name);
        mEditor.putString(EMAIL, email);
        mEditor.apply();
    }

    public boolean isLogging() {
        return mSharedPreferences.getBoolean(LOGIN, false);
    }


    public HashMap<String, String> getUserDetail() {
        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, mSharedPreferences.getString(NAME, null));
        user.put(EMAIL, mSharedPreferences.getString(EMAIL, null));
        return user;
    }

    public void logout() {
        mEditor.clear();
        mEditor.commit();
    }
}
