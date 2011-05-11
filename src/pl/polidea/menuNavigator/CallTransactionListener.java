package pl.polidea.menuNavigator;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class CallTransactionListener implements OnTransactionListener {

    private static final String TAG = CallTransactionListener.class.getSimpleName();
    private final Context ctx;

    public CallTransactionListener(final Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void executeTransaction(final String transaction) {
        try {
            final Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + Uri.encode(transaction)));
            ctx.startActivity(callIntent);
        } catch (final ActivityNotFoundException e) {
            Log.w(TAG, "Could not make a call - intent is not handled by any activity.");
        }
    }
}
