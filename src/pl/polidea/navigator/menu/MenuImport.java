package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu importing from another file.
 */
public class MenuImport extends AbstractNavigationMenu {
    private static final long serialVersionUID = 1L;

    public MenuImport(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.MENU_IMPORT, parent, context);
        link = reader.readLink(jsonMenu, directory, parent);
    }

    public AbstractNavigationMenu link;

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || link == null;
    }

    @Override
    public String toString() {
        return "MenuImport [link=" + link + ", " + super.toString() + "]";
    }

    @Override
    public void updateTransientAttributes(final MenuContext menuContext, final AbstractNavigationMenu parent,
            final Context context) {
        super.updateTransientAttributes(menuContext, parent, context);
        if (link != null) {
            link.updateTransientAttributes(menuContext, this, context);
        }
    }

}
