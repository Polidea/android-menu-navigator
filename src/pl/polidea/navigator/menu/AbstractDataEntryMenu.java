package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Base class for all number menu types.
 * 
 */
public abstract class AbstractDataEntryMenu extends AbstractTransactionMenu {

    private static final long serialVersionUID = 1L;
    public final String variable;
    public final AbstractNavigationMenu link;
    public final Integer minLength;
    public final Integer maxLength;
    public final String hint;

    public AbstractDataEntryMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        super(reader, jsonMenu, menuType, parent, context);
        minLength = JsonMenuReader.getIntOrNull(jsonMenu, "minLength");
        maxLength = JsonMenuReader.getIntOrNull(jsonMenu, "maxLength");
        variable = jsonMenu.getString("variable");
        hint = JsonMenuReader.getStringOrNull(jsonMenu, "hint");
        link = reader.readLink(jsonMenu, directory, this);
        if (link != null && transaction != null) {
            throw new JSONException("Exactly one of \"link\" and \"transaction\" can be defined in " + jsonMenu);
        }
    }

    public AbstractDataEntryMenu(final String name, final String description, final String help, final String iconFile,
            final String breadCrumbIconFile, final String rightIconFile, final String menuType,
            final String transaction, final String shortcut, final String variable, final Integer minLength,
            final Integer maxLength, final String hint, final Context context) {
        super(name, description, help, iconFile, breadCrumbIconFile, rightIconFile, menuType, transaction, shortcut,
                context);
        this.variable = variable;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.hint = hint;
        this.link = null;
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || (link == null && transaction == null);
    }

    @Override
    public String toString() {
        return "AbstractNumberMenu [variable=" + variable + ", link=" + link + ", minLength=" + minLength
                + ", maxLength=" + maxLength + ", transaction=" + transaction + ", hint=" + hint + ", "
                + super.toString() + "]";
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