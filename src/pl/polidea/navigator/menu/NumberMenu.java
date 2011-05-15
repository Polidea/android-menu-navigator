package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu asking for a number.
 */
public class NumberMenu extends AbstractNumberMenu {

    private static final long serialVersionUID = 1L;

    public NumberMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, MenuType.NUMBER, parent);
    }

    @Override
    public String toString() {
        return "NumberMenu [" + super.toString() + "]";
    }

}
