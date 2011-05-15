package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu importing from another file.
 */
public class MenuImport extends AbstractNavigationMenu {
    private static final long serialVersionUID = 1L;

    public MenuImport(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, MenuType.MENU_IMPORT, parent);
        link = reader.readLink(jsonMenu, directory, parent);
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
