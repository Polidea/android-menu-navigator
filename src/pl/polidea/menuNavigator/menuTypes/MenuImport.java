package pl.polidea.menuNavigator.menuTypes;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.menuNavigator.JsonMenuReader;

public class MenuImport extends AbstractNavigationMenu {
    private static final long serialVersionUID = 1L;

    public MenuImport(final JSONObject jsonMenu, final File directory, final AbstractNavigationMenu parent)
            throws JSONException {
        super(jsonMenu, directory, MenuType.MENU_IMPORT, parent);
        link = JsonMenuReader.readLink(jsonMenu, directory, parent);
    }

    public AbstractNavigationMenu link;

    @Override
    public boolean isDisabled() {
        return link == null;
    }

    @Override
    public String toString() {
        return "MenuImport [link=" + link + ", " + super.toString() + "]";
    }

}
