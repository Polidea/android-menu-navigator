package pl.polidea.navigator;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

public class Persistence {

    private static final String MENU_BUTTON = "Menu_button:";
    private static final String LATEST_LIST = "Latest_list";

    protected final SharedPreferences sharedPreferences;

    public Persistence(final Context context) {
        sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
    }

    public void setMenuVisibility(final String name, final boolean visible) {
        setBooleanPreference(MENU_BUTTON + name, visible);
    }

    public boolean getMenuVisibility(final String name) {
        return sharedPreferences.getBoolean(MENU_BUTTON + name, true);
    }

    protected void setStringPreference(final String key, final String value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected void setFloatPreference(final String key, final float value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    protected void setBooleanPreference(final String key, final boolean value) {
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public List<Pair<String, String>> getLatestList(final String name) {
        final List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
        for (int i = 0; i < 3; i++) {
            final String description = sharedPreferences.getString(LATEST_LIST + name + "description" + i, null);
            final String transaction = sharedPreferences.getString(LATEST_LIST + name + "transaction" + i, null);
            if (description != null && transaction != null) {
                list.add(new Pair<String, String>(description, transaction));
            }
        }
        return list;
    }
}
