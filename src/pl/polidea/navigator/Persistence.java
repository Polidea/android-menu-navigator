package pl.polidea.navigator;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.navigator.menu.AbstractDataEntryMenu;
import pl.polidea.navigator.menu.TransactionMenu;
import android.content.Context;
import android.content.SharedPreferences;

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

    private static int LATEST_LIST_MAX_LENGTH = 3;

    public List<TransactionMenu> getLatestList(final String menuName) {
        final List<TransactionMenu> list = new ArrayList<TransactionMenu>();
        for (int i = 0; i < LATEST_LIST_MAX_LENGTH; i++) {
            final String name = sharedPreferences.getString(LATEST_LIST + menuName + "name" + i, null);
            final String description = sharedPreferences.getString(LATEST_LIST + menuName + "description" + i, null);
            final String help = sharedPreferences.getString(LATEST_LIST + menuName + "help" + i, null);
            final String iconFile = sharedPreferences.getString(LATEST_LIST + menuName + "iconFile" + i, null);
            final String breadCrumbIconFile = sharedPreferences.getString(LATEST_LIST + menuName + "breadCrumbIconFile"
                    + i, null);
            final String menuType = sharedPreferences.getString(LATEST_LIST + menuName + "menuType" + i, null);

            final String transaction = sharedPreferences.getString(LATEST_LIST + menuName + "transaction" + i, null);
            final String shortcut = sharedPreferences.getString(LATEST_LIST + menuName + "shortcut" + i, null);

            final boolean isAbstractDataEntryMenu = sharedPreferences.getBoolean(LATEST_LIST + menuName
                    + "isAbstractDataEntryMenu" + i, false);

            final String variable = sharedPreferences.getString(LATEST_LIST + menuName + "variable" + i, null);
            final String minLength = sharedPreferences.getString(LATEST_LIST + menuName + "minLength" + i, null);
            final String maxlength = sharedPreferences.getString(LATEST_LIST + menuName + "maxLength" + i, null);
            final String hint = sharedPreferences.getString(LATEST_LIST + menuName + "hint" + i, null);

            // final String transaction =
            // sharedPreferences.getString(LATEST_LIST +
            // menuName + "transaction" + i, null);
            // if (description != null && transaction != null) {
            // list.add(new Pair<String, String>(description, transaction));
            // }
        }
        return list;
    }

    public void setLatestListElement(final String menuName, final TransactionMenu menu) {
        if (menu.transaction == null) {
            return;
        }
        final List<TransactionMenu> savedList = getLatestList(menuName);
        if (savedList.contains(menu)) {
            return;
        }
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        setLatestListElementAtPosition(menuName, menu, editor, 0);

        for (int i = 0; i < savedList.size() && i < LATEST_LIST_MAX_LENGTH; i++) {
            setLatestListElementAtPosition(menuName, savedList.get(i), editor, i + 1);
        }
        editor.commit();
    }

    private void setLatestListElementAtPosition(final String menuName, final TransactionMenu menu,
            final SharedPreferences.Editor editor, final int position) {
        if (menu.transaction == null) {
            return;
        }
        // AbstractNavigationMenu
        editor.putString(LATEST_LIST + menuName + "name" + position, menu.name);
        editor.putString(LATEST_LIST + menuName + "description" + position, menu.description);
        editor.putString(LATEST_LIST + menuName + "help" + position, menu.help);
        editor.putString(LATEST_LIST + menuName + "iconFile" + position, menu.iconFile);
        editor.putString(LATEST_LIST + menuName + "breadCrumbIconFile" + position, menu.breadCrumbIconFile);
        editor.putString(LATEST_LIST + menuName + "menuType" + position, menu.menuType);

        // TransactionMenu
        editor.putString(LATEST_LIST + menuName + "transaction" + position, menu.transaction);
        editor.putString(LATEST_LIST + menuName + "shortcut" + position, menu.shortcut);

        // AbstractDataEntryMenu
        if (menu instanceof AbstractDataEntryMenu) {
            editor.putString(LATEST_LIST + menuName + "variable" + position, ((AbstractDataEntryMenu) menu).variable);
            editor.putInt(LATEST_LIST + menuName + "minLength" + position, ((AbstractDataEntryMenu) menu).minLength);
            editor.putInt(LATEST_LIST + menuName + "maxLength" + position, ((AbstractDataEntryMenu) menu).maxLength);
            editor.putString(LATEST_LIST + menuName + "hint" + position, ((AbstractDataEntryMenu) menu).hint);
        }
    }
}
