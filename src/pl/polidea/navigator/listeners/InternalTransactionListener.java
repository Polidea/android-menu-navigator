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
    private static final String TEL_PREFIX = "tel:";

    @Override
    public boolean handleTransaction(final String transaction) {
        if (transaction != null) {
            if (transaction.startsWith(INTERNAL_PREFIX)) {
                final String method = transaction.substring(INTERNAL_PREFIX.length());
                Log.d("Internal transaction: ", method);
                return functionsMaker(method);
            }
            // if this transaction i tel transaction it may cause internal and
            // call actions
            if (transaction.startsWith(TEL_PREFIX)) {
                final String method = transaction.substring(TEL_PREFIX.length());
                Log.d("Call transaction through internal listener: ", method);
                functionsMaker(method);
                return false;
            }
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
