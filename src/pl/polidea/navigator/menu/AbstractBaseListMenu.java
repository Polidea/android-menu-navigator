package pl.polidea.navigator.menu;

import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Base menu class for all kinds of lists.
 * 
 */
public abstract class AbstractBaseListMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1L;

    public final AbstractNavigationMenu[] items;

    public AbstractBaseListMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        super(reader, jsonMenu, menuType, parent, context);
        items = reader.readItems(jsonMenu, directory, this, menuContext);
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || items == null;
    }

    @Override
    public String toString() {
        return "AbstractBaseListMenu [items=" + Arrays.toString(items) + ", " + super.toString() + "]";
    }

    @Override
    public void updateTransientAttributes(final MenuContext menuContext, final AbstractNavigationMenu parent,
            final Context context) {
        super.updateTransientAttributes(menuContext, parent, context);
        for (final AbstractNavigationMenu item : items) {
            item.updateTransientAttributes(menuContext, this, context);
        }
    }

}
