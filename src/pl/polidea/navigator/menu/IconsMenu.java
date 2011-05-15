package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu displaying icons.
 */
public class IconsMenu extends AbstractBaseListMenu {

    private static final long serialVersionUID = -3924139469870130480L;

    public IconsMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, MenuType.ICONS, parent);
    }

    @Override
    public String toString() {
        return "IconsMenu [" + super.toString() + "]";
    }

}
