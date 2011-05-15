package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu firing a transaction.
 */
public class TransactionMenu extends AbstractNavigationMenu {

    private static final long serialVersionUID = 1L;

    public TransactionMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.TRANSACTION, parent);
        transaction = jsonMenu.getString("transaction");
    }

    public String transaction;

    @Override
    public boolean isDisabled() {
        return transaction == null;
    }

    @Override
    public String toString() {
        return "TransactionMenu [transaction=" + transaction + ", " + super.toString() + "]";
    }

}
