package pl.polidea.navigator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.factories.NavigationMenuFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.MenuContext;
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
    private final NavigationMenuFactoryInterface jsonMenuFactory;

    public JsonMenuReader(final File directory, final String fileName, final AbstractNavigationMenu parent,
            final NavigationMenuFactoryInterface jsonMenuFactory) {
        this.directory = directory;
        this.fileName = fileName;
        this.parent = parent;
        this.jsonMenuFactory = jsonMenuFactory;
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
            myMenu = jsonMenuFactory.readMenuFromJsonObject(this, jsonMenu, parent);
            if (myMenu == null) {
                Log.w(TAG, "The menu is null!!");
            } else {
                Log.d(TAG, "Created menu " + myMenu);
            }
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
        final List<AbstractNavigationMenu> items = new ArrayList<AbstractNavigationMenu>(array.length());
        for (int i = 0; i < array.length(); i++) {
            final JSONObject obj = array.optJSONObject(i);
            if (obj == null) {
                Log.w(TAG, "The element no. " + i + " in the array " + array + " is null. Possibly coma was "
                        + " left empty");
            } else {
                items.add(jsonMenuFactory.readMenuFromJsonObject(this, obj, parentMenu));
            }

        }
        return items.toArray(new AbstractNavigationMenu[items.size()]);
    }

    public AbstractNavigationMenu readLink(final JSONObject jsonMenu, final File directory,
            final AbstractNavigationMenu parentMenu) throws JSONException {
        final String linkedFileName = getStringOrNull(jsonMenu, "link");
        if (linkedFileName == null) {
            return null;
        }
        final File linkedFile = new File(directory, linkedFileName);
        final JsonMenuReader reader = new JsonMenuReader(linkedFile.getParentFile(), linkedFile.getName(), parentMenu,
                jsonMenuFactory);
        reader.createMenu(menuContext);
        return reader.getMyMenu();
    }

    /**
     * Verifies if menu is ok. Returns array of problems found or an empty array
     * if no problem found.
     */
    public MenuErrorDescription[] verify() {
        return errorList.toArray(new MenuErrorDescription[errorList.size()]);
    }

}
