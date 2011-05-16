package pl.polidea.navigator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Handles transactions that have not been handled otherwise.
 */
public class SignalUnhandledTransactionListener implements
        OnTransactionListener {

    private static final String TAG = SignalUnhandledTransactionListener.class
            .getSimpleName();
    private final Context ctx;

    public SignalUnhandledTransactionListener(final Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean handleTransaction(final String transaction) {
        Log.w(TAG, "Unhandled transaction: " + transaction);
        Toast.makeText(ctx, "UNHANDLED:" + transaction, Toast.LENGTH_SHORT)
                .show();
        return true;
    }

}
