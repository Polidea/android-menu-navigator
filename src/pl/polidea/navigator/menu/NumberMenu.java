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

    public NumberMenu(final String name, final String description, final String help, final String iconFile,
            final String breadCrumbIconFile, final String rightIconFile, final String menuType,
            final String transaction, final String shortcut, final String variable, final Integer minLength,
            final Integer maxLength, final String hint, final Context context) {
        super(name, description, help, iconFile, breadCrumbIconFile, rightIconFile, menuType, transaction, shortcut,
                variable, minLength, maxLength, hint, context);
    }

    @Override
    public String toString() {
        return "NumberMenu [" + super.toString() + "]";
    }
}
