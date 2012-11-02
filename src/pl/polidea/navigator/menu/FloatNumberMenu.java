package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu asking for a number.
 */
public class FloatNumberMenu extends AbstractDataEntryMenu {

    private static final long serialVersionUID = 1L;
    public final Integer minVal;
    public final Integer maxVal;

    public FloatNumberMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.FLOAT_NUMBER, parent, context);

        minVal = JsonMenuReader.getIntOrNull(jsonMenu, "minValue");
        maxVal = JsonMenuReader.getIntOrNull(jsonMenu, "maxValue");
    }

    public FloatNumberMenu(final String name, final String description, final String help, final String iconFile,
            final String breadCrumbIconFile, final String menuType, final String transaction, final String shortcut,
            final String variable, final Integer minLength, final Integer maxLength, final String hint,
            final Integer minVal, final Integer maxVal, final Context context) {
        super(name, description, help, iconFile, breadCrumbIconFile, menuType, transaction, shortcut, variable,
                minLength, maxLength, hint, context);
        this.minVal = minVal;
        this.maxVal = maxVal;
    }

    @Override
    public String toString() {
        return "FloatNumberMenu [" + super.toString() + "]";
    }

}
