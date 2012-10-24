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
    public String transaction;

    public TransactionMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent,
            final Context context) throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.TRANSACTION, parent, context);
        transaction = jsonMenu.getString("transaction");
    }

    public TransactionMenu(final String name, final String transaction, final Context context) {
        super(name, BasicMenuTypes.TRANSACTION, context);
        this.transaction = transaction;
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
