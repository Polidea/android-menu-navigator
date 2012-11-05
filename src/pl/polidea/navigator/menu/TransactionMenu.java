package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import android.content.Context;

/**
 * Menu firing a transaction.
 */
public class TransactionMenu extends AbstractTransactionMenu {

    private static final long serialVersionUID = 1L;

    public TransactionMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.TRANSACTION, parent, context);
    }

    public TransactionMenu(final String name, final String description, final String help, final String iconFile,
            final String rightIconFile, final String breadCrumbIconFile, final String menuType,
            final String transaction, final String shortcut, final Context context) {
        super(name, description, help, iconFile, rightIconFile, breadCrumbIconFile, menuType, transaction, shortcut,
                context);
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
