package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu asking for a number.
 */
public class StringMenu extends AbstractDataEntryMenu {

    private static final long serialVersionUID = 1L;

    public StringMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.STRING, parent);
    }

    @Override
    public String toString() {
        return "StringMenu [" + super.toString() + "]";
    }

}
