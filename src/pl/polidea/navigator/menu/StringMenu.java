package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu asking for a number.
 */
public class StringMenu extends AbstractDataEntryMenu {

    private static final long serialVersionUID = 1L;

    public StringMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.STRING, parent, context);
    }

    @Override
    public String toString() {
        return "StringMenu [" + super.toString() + "]";
    }

}
