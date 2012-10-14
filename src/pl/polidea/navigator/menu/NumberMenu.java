package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu asking for a number.
 */
public class NumberMenu extends AbstractDataEntryMenu {

    private static final long serialVersionUID = 1L;

    public NumberMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.NUMBER, parent, context);
    }

    @Override
    public String toString() {
        return "NumberMenu [" + super.toString() + "]";
    }

}
