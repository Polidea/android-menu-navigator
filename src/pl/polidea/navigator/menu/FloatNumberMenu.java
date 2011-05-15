package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu asking for a number.
 */
public class FloatNumberMenu extends AbstractNumberMenu {

    private static final long serialVersionUID = 1L;

    public FloatNumberMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.FLOAT_NUMBER, parent);
    }

    @Override
    public String toString() {
        return "FloatNumberMenu [" + super.toString() + "]";
    }

}
