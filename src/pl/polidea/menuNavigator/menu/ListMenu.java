package pl.polidea.menuNavigator.menu;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

public class ListMenu extends AbstractBaseListMenu {

    private static final long serialVersionUID = 1L;

    public ListMenu(final JSONObject jsonMenu, final File directory, final AbstractNavigationMenu parent)
            throws JSONException {
        super(jsonMenu, directory, MenuType.LIST, parent);
    }

    @Override
    public String toString() {
        return "ListMenu [" + super.toString() + "]";
    }

}
