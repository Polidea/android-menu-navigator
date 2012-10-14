package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu displaying lists.
 */
public class ListMenu extends AbstractBaseListMenu {

    private static final long serialVersionUID = 1L;

    public ListMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.LIST, parent, context);
    }

    @Override
    public String toString() {
        return "ListMenu [" + super.toString() + "]";
    }

}
