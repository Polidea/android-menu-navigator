package pl.polidea.navigator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import android.content.Context;

/**
 * Creates navigation menu objeect from json object.
 */
public interface NavigationMenuFactoryInterface {
    AbstractNavigationMenu readMenuFromJsonObject(final JsonMenuReader reader, final JSONObject jsonMenu,
            final AbstractNavigationMenu parent, Context context) throws JSONException;
}