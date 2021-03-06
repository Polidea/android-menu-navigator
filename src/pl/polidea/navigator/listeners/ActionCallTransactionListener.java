package pl.polidea.navigator.listeners;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Handles transactions resulting in call made.
 * 
 * It handles transactions of the form tel:<Number>, where number can be any
 * form of a valid number including USSD string ending with #.
 * 
 * Note - unlike call action in Android, the number should not be URL encoded.
 * Call listener strips down the tel: prefix and encodes the remaining part, so
 * valid transaction is tel:112*1212312312# (in android ACTION_CALL this has to
 * be encoded as tel:112*1212312312%23 . The main reason for that is to keep
 * transaction description as human-readable as possible and since we are
 * constrained to pretty small character set, it is worth to do the encoding as
 * late as possible automatically.
 */
public class ActionCallTransactionListener implements OnTransactionListener {

    private static final String TEL_PREFIX = "tel:";
    private static final String TAG = ActionCallTransactionListener.class.getSimpleName();
    private final Activity ctx;

    public ActionCallTransactionListener(final Activity ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean handleTransaction(final String transaction) {
        if (transaction != null && transaction.startsWith(TEL_PREFIX)) {
            Log.d(TAG, "Sending " + transaction + " to call.");
            final String numberToCall = transaction.substring(TEL_PREFIX.length());
            Toast.makeText(ctx, numberToCall, Toast.LENGTH_LONG).show();
            final String newTransaction = TEL_PREFIX + Uri.encode(numberToCall);
            try {
                final Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse(newTransaction));
                ctx.startActivityForResult(callIntent, 0);
                return true;
            } catch (final ActivityNotFoundException e) {
                Log.w(TAG, "Could not make a call - intent is not handled by any activity.");
            }
        }
        return false;
    }
}
