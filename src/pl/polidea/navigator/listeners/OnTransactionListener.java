package pl.polidea.navigator.listeners;

/**
 * Listens for the transactions to handle.
 * 
 * 
 */
public interface OnTransactionListener {
    /**
     * Potentially handle the transaction.
     * 
     * @param transaction
     *            transaction to handle
     * @return true if transaction was handled by this listener
     */
    boolean handleTransaction(String transaction);
}
