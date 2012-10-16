package pl.polidea.navigator.listeners;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Handles http transactions.
 * 
 * It handles transactions of the form http:<URL>
 */
public class ActionWebTransactionListener implements OnTransactionListener {

    private static final String WEB_PREFIX = "http:";
    private static final String TAG = ActionWebTransactionListener.class.getSimpleName();
    private final Activity ctx;

    public ActionWebTransactionListener(final Activity ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean handleTransaction(final String transaction) {
        if (transaction != null && transaction.startsWith(WEB_PREFIX)) {
            Log.d(TAG, "Sending " + transaction + " to call.");
            Toast.makeText(ctx, transaction, Toast.LENGTH_LONG).show();
            final Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(transaction));
            ctx.startActivity(browserIntent);
            return true;
        }
        return false;
    }
}
