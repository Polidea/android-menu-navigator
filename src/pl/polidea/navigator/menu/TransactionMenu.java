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
        ;
    }

    @Override
    public boolean isDisabled() {
        return super.isDisabled() || transaction == null;
    }

    public boolean superIsDisabled() {
        return super.isDisabled();
    }

    @Override
    public String toString() {
        return "TransactionMenu [transaction=" + transaction + ", " + super.toString() + "]";
    }

}
