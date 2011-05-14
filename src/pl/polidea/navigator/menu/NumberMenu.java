package pl.polidea.navigator.menu;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Menu asking for a number.
 */
public class NumberMenu extends AbstractNumberMenu {

    private static final long serialVersionUID = 1L;

    public NumberMenu(final JSONObject jsonMenu, final File directory, final AbstractNavigationMenu parent)
            throws JSONException {
        super(jsonMenu, directory, MenuType.NUMBER, parent);
    }

    @Override
    public String toString() {
        return "NumberMenu [" + super.toString() + "]";
    }

}
