package pl.polidea.navigator;

import java.util.ArrayList;
import java.util.List;

import pl.polidea.navigator.menu.AbstractDataEntryMenu;
import pl.polidea.navigator.menu.AbstractTransactionMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.FloatNumberMenu;
import pl.polidea.navigator.menu.NumberMenu;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import pl.polidea.navigator.menu.StringMenu;
import pl.polidea.navigator.menu.TransactionMenu;
import android.content.Context;
import android.content.SharedPreferences;

public class Persistence {

    private static final String MENU_BUTTON = "Menu_button:";
    private static final String LATEST_LIST = "Latest_list";

    protected final SharedPreferences sharedPreferences;
    private final Context context;

    public Persistence(final Context context) {
        this.context = context;
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

    public List<AbstractTransactionMenu> getLatestList(final String menuName) {
        final List<AbstractTransactionMenu> list = new ArrayList<AbstractTransactionMenu>();
        for (int i = 0; i < LATEST_LIST_MAX_LENGTH; i++) {
            AbstractTransactionMenu menu = null;

            final String name = sharedPreferences.getString(LATEST_LIST + menuName + "name" + i, null);
            final String description = sharedPreferences.getString(LATEST_LIST + menuName + "description" + i, null);
            final String help = sharedPreferences.getString(LATEST_LIST + menuName + "help" + i, null);
            final String iconFile = sharedPreferences.getString(LATEST_LIST + menuName + "iconFile" + i, null);
            final String breadCrumbIconFile = sharedPreferences.getString(LATEST_LIST + menuName + "breadCrumbIconFile"
                    + i, null);
            final String rightIconFile = sharedPreferences
                    .getString(LATEST_LIST + menuName + "rightIconFile" + i, null);
            final String menuType = sharedPreferences.getString(LATEST_LIST + menuName + "menuType" + i, null);

            final String transaction = sharedPreferences.getString(LATEST_LIST + menuName + "transaction" + i, null);
            final String shortcut = sharedPreferences.getString(LATEST_LIST + menuName + "shortcut" + i, null);

            // All classes that extends AbstractDataEntryMenu
            if (BasicMenuTypes.extendsAbstractDataEntryMenu(menuType)) {

                final String variable = sharedPreferences.getString(LATEST_LIST + menuName + "variable" + i, null);
                Integer minLength = null;
                minLength = Integer.valueOf(sharedPreferences.getInt(LATEST_LIST + menuName + "minLength" + i,
                        Integer.MAX_VALUE));
                if (minLength != null && minLength == Integer.MAX_VALUE) {
                    minLength = null;
                }
                Integer maxLength = null;
                maxLength = Integer.valueOf(sharedPreferences.getInt(LATEST_LIST + menuName + "maxLength" + i,
                        Integer.MIN_VALUE));
                if (maxLength != null && maxLength == Integer.MAX_VALUE) {
                    maxLength = null;
                }
                final String hint = sharedPreferences.getString(LATEST_LIST + menuName + "hint" + i, null);

                if (BasicMenuTypes.NUMBER.equals(menuType)) {
                    menu = new NumberMenu(name, description, help, iconFile, breadCrumbIconFile, rightIconFile,
                            menuType, transaction, shortcut, variable, minLength, maxLength, hint, context);
                    list.add(menu);
                } else if (BasicMenuTypes.STRING.equals(menuType)) {
                    menu = new StringMenu(name, description, help, iconFile, breadCrumbIconFile, rightIconFile,
                            menuType, transaction, shortcut, variable, minLength, maxLength, hint, context);
                    list.add(menu);
                } else if (BasicMenuTypes.PHONE_NUMBER.equals(menuType)) {
                    menu = new PhoneNumberMenu(name, description, help, iconFile, breadCrumbIconFile, rightIconFile,
                            menuType, transaction, shortcut, variable, minLength, maxLength, hint, context);
                    list.add(menu);
                } else if (BasicMenuTypes.FLOAT_NUMBER.equals(menuType)) {
                    Integer minVal = null;
                    minVal = Integer.valueOf(sharedPreferences.getInt(LATEST_LIST + menuName + "minVal" + i,
                            Integer.MAX_VALUE));
                    if (minVal != null && minVal == Integer.MAX_VALUE) {
                        minVal = null;
                    }
                    Integer maxVal = null;
                    maxVal = Integer.valueOf(sharedPreferences.getInt(LATEST_LIST + menuName + "maxVal" + i,
                            Integer.MIN_VALUE));
                    if (maxVal != null && maxVal == Integer.MAX_VALUE) {
                        maxVal = null;
                    }
                    menu = new FloatNumberMenu(name, description, help, iconFile, breadCrumbIconFile, rightIconFile,
                            menuType, transaction, shortcut, variable, minLength, maxLength, hint, minVal, maxVal,
                            context);
                    list.add(menu);
                }
            } else if (BasicMenuTypes.TRANSACTION.equals(menuType)) {
                menu = new TransactionMenu(name, description, help, iconFile, breadCrumbIconFile, rightIconFile,
                        menuType, transaction, shortcut, context);
                list.add(menu);
            }
        }
        return list;
    }

    public void setLatestListElement(final String menuName, final AbstractTransactionMenu menu) {
        if (menu.transaction == null) {
            return;
        }
        final List<AbstractTransactionMenu> savedList = getLatestList(menuName);
        for (final AbstractTransactionMenu savedMenu : savedList) {
            if (savedMenu.transaction == menu.transaction) {
                return;
            }
        }
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        setLatestListElementAtPosition(menuName, menu, editor, 0);

        for (int i = 0; i < savedList.size() && i < LATEST_LIST_MAX_LENGTH; i++) {
            setLatestListElementAtPosition(menuName, savedList.get(i), editor, i + 1);
        }
        editor.commit();
    }

    private void setLatestListElementAtPosition(final String menuName, final AbstractTransactionMenu menu,
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
        editor.putString(LATEST_LIST + menuName + "rightIconFile" + position, menu.breadCrumbRightIconFile);
        editor.putString(LATEST_LIST + menuName + "menuType" + position, menu.menuType);

        // AbstractTransactionMenu
        editor.putString(LATEST_LIST + menuName + "transaction" + position, menu.transaction);
        editor.putString(LATEST_LIST + menuName + "shortcut" + position, menu.shortcut);

        // AbstractDataEntryMenu
        if (menu instanceof AbstractDataEntryMenu) {
            editor.putString(LATEST_LIST + menuName + "variable" + position, ((AbstractDataEntryMenu) menu).variable);
            editor.putInt(LATEST_LIST + menuName + "minLength" + position, ((AbstractDataEntryMenu) menu).minLength);
            editor.putInt(LATEST_LIST + menuName + "maxLength" + position, ((AbstractDataEntryMenu) menu).maxLength);
            editor.putString(LATEST_LIST + menuName + "hint" + position, ((AbstractDataEntryMenu) menu).hint);

            if (menu instanceof FloatNumberMenu) {
                editor.putInt(LATEST_LIST + menuName + "minVal" + position, ((FloatNumberMenu) menu).minVal);
                editor.putInt(LATEST_LIST + menuName + "maxVal" + position, ((FloatNumberMenu) menu).maxVal);
            }
        }
    }
}
