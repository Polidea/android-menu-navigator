package pl.polidea.navigator.menu;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Menu displaying icons.
 */
public class IconsMenu extends AbstractBaseListMenu {

    private static final long serialVersionUID = -3924139469870130480L;

    public IconsMenu(final JSONObject jsonMenu, final File directory, final AbstractNavigationMenu parent)
            throws JSONException {
        super(jsonMenu, directory, MenuType.ICONS, parent);
    }

    @Override
    public String toString() {
        return "IconsMenu [" + super.toString() + "]";
    }

}
