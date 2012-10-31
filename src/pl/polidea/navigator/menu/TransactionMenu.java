package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu firing a transaction.
 */
public class TransactionMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1L;
    public final String transaction;
    public final String shortcut;

    public TransactionMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        super(reader, jsonMenu, menuType != null ? menuType : BasicMenuTypes.TRANSACTION, parent, context);
        transaction = jsonMenu.getString("transaction");
        shortcut = JsonMenuReader.getStringOrNull(jsonMenu, "shortcut");
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || transaction == null;
    }

    @Override
    public String toString() {
        return "TransactionMenu [transaction=" + transaction + ", " + super.toString() + "]";
    }

}
