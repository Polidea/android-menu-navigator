package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 *
 */

/**
 * @author Marek Multarzynski
 * 
 */
public abstract class AbstractTransactionMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1154798706565678846L;
    public final String transaction;

    // public final String shortcut;

    public AbstractTransactionMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        super(reader, jsonMenu, menuType, parent, context);
        transaction = JsonMenuReader.getStringOrNull(jsonMenu, "transaction");
        // shortcut = JsonMenuReader.getStringOrNull(jsonMenu, "shortcut");
    }

}
