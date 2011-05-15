package pl.polidea.navigator.menu;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Base menu class for all kinds of lists.
 * 
 */
public abstract class AbstractBaseListMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1L;

    public final AbstractNavigationMenu[] items;

    public AbstractBaseListMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final MenuType menuType,
            final AbstractNavigationMenu parent) throws JSONException {
        super(reader, jsonMenu, menuType, parent);
        items = reader.readItems(jsonMenu, directory, this, menuContext);
    }

    @Override
    public boolean isDisabled() {
        return items == null;
    }

    @Override
    public String toString() {
        return "AbstractBaseListMenu [items=" + Arrays.toString(items) + ", " + super.toString() + "]";
    }

}
