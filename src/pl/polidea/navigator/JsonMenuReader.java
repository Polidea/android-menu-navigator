package pl.polidea.navigator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.IconsMenu;
import pl.polidea.navigator.menu.ListMenu;
import pl.polidea.navigator.menu.MenuContext;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.menu.MenuType;
import pl.polidea.navigator.menu.NumberMenu;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import pl.polidea.navigator.menu.TransactionMenu;
import android.util.Log;

/**
 * This class reads menu from specified file and directory in JSon form. It
 * builds menu from subdirectories and directories accordingly and verifies if
 * all the menu items are properly configured.
 * 
 * @author potiuk
 * 
 */
public class JsonMenuReader {
    private static final String TAG = JsonMenuReader.class.getSimpleName();
    public final File directory;
    private final String fileName;
    private final List<MenuErrorDescription> errorList = new LinkedList<MenuErrorDescription>();
    private AbstractNavigationMenu myMenu;
    private final AbstractNavigationMenu parent;
    public MenuContext menuContext;

    public JsonMenuReader(final File directory, final String fileName, final AbstractNavigationMenu parent) {
        this.directory = directory;
        this.fileName = fileName;
        this.parent = parent;
    }

    public static String getStringOrNull(final JSONObject obj, final String name) throws JSONException {
        if (obj.has(name)) {
            return obj.getString(name);
        } else {
            return null;
        }
    }

    public static Integer getIntOrNull(final JSONObject jsonMenu, final String name) throws JSONException {
        if (jsonMenu.has(name)) {
            return jsonMenu.getInt(name);
        } else {
            return null;
        }
    }

    public AbstractNavigationMenu getMyMenu() {
        return myMenu;
    }

    public void createMenu(final MenuContext menuContext) {
        this.menuContext = menuContext;
        Log.d(TAG, "Creating menu");
        try {
            String line;
            final StringBuilder builder = new StringBuilder();
            final File fileToRead = new File(directory, fileName);
            Log.d(TAG, "Reading file: " + fileToRead);
            final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileToRead),
                    "UTF-8"));
            try {
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } finally {
                reader.close();
            }
            Log.d(TAG, "Finished reading file: " + fileToRead);
            final JSONObject jsonMenu = new JSONObject(builder.toString());
            Log.d(TAG, "Read json object: " + jsonMenu);
            myMenu = readMenuFromJsonObject(jsonMenu, directory, parent);
            Log.d(TAG, "Created menu" + myMenu);
        } catch (final IOException e) {
            errorList
                    .add(new MenuErrorDescription(fileName, "I/O error when reading menu: " + e.getMessage(), 0, 0, e));
        } catch (final JSONException e) {
            // we do not know line and column unfortunately
            errorList.add(new MenuErrorDescription(fileName, "JSon error when reading menu: " + e, 0, 0, e));
        }
        if (!errorList.isEmpty()) {
            Log.w(TAG, "Error while reading menu " + fileName + ": " + errorList);

        }
    }

    public AbstractNavigationMenu[] readItems(final JSONObject jsonMenu, final File directory,
            final AbstractNavigationMenu parentMenu, final MenuContext menuContext) throws JSONException {
        final JSONArray array = jsonMenu.getJSONArray("items");
        final AbstractNavigationMenu[] items = new AbstractNavigationMenu[array.length()];
        for (int i = 0; i < array.length(); i++) {
            final JSONObject obj = array.optJSONObject(i);
            if (obj == null) {
                throw new JSONException("The element no. " + i + " in array is null. Possibly coma was "
                        + "left empty at the end of array");
            }
            items[i] = readMenuFromJsonObject(obj, directory, parentMenu);
        }
        return items;
    }

    private AbstractNavigationMenu readMenuFromJsonObject(final JSONObject jsonMenu, final File directory,
            final AbstractNavigationMenu parent) throws JSONException {
        final MenuType type = decodeType(JsonMenuReader.getStringOrNull(jsonMenu, "type"));
        switch (type) {
        case ICONS:
            return new IconsMenu(this, jsonMenu, parent);
        case LIST:
            return new ListMenu(this, jsonMenu, parent);
        case MENU_IMPORT:
            return new MenuImport(this, jsonMenu, parent);
        case NUMBER:
            return new NumberMenu(this, jsonMenu, parent);
        case PHONE_NUMBER:
            return new PhoneNumberMenu(this, jsonMenu, parent);
        case TRANSACTION:
            return new TransactionMenu(this, jsonMenu, parent);
        default:
            return null;
        }
    }

    public AbstractNavigationMenu readLink(final JSONObject jsonMenu, final File directory,
            final AbstractNavigationMenu parentMenu) throws JSONException {
        final String linkedFileName = getStringOrNull(jsonMenu, "link");
        if (linkedFileName == null) {
            return null;
        }
        final File linkedFile = new File(directory, linkedFileName);
        final JsonMenuReader reader = new JsonMenuReader(linkedFile.getParentFile(), linkedFile.getName(), parentMenu);
        reader.createMenu(menuContext);
        return reader.getMyMenu();
    }

    public static MenuType decodeType(final String type) {
        if (type == null) {
            return MenuType.TRANSACTION;
        } else {
            return MenuType.valueOf(type);
        }
    }

    /**
     * Verifies if menu is ok. Returns array of problems found or an empty array
     * if no problem found.
     */
    public MenuErrorDescription[] verify() {
        return errorList.toArray(new MenuErrorDescription[errorList.size()]);
    }

}
