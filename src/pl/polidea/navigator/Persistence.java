package pl.polidea.navigator;

import android.content.Context;
import android.content.SharedPreferences;

public class Persistence {

    private static final String MENU_BUTTON = "Menu_button:";

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
}
