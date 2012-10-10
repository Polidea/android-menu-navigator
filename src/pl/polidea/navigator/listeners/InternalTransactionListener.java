package pl.polidea.navigator.listeners;

import android.util.Log;

/**
 * 
 * @author Marek Multarzynski
 * 
 *         Handles transactions resulting in internal application functions It
 *         handles transactions of the form internal:<function_name>
 */
public abstract class InternalTransactionListener implements OnTransactionListener {

    private static final String INTERNAL_PREFIX = "internal:";

    @Override
    public boolean handleTransaction(final String transaction) {
        if (transaction != null && transaction.startsWith(INTERNAL_PREFIX)) {
            final String method = transaction.substring(INTERNAL_PREFIX.length());
            Log.d("Internal method: ", method);
            return functionsMaker(method);
        }
        return false;
    }

    /**
     * 
     * encode and execute function from string
     * 
     * @param method
     * @return is function correctly encoded
     */
    protected abstract boolean functionsMaker(final String method);

}
