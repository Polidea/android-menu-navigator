package pl.polidea.navigator.transformers;

/**
 * Exception thrown (with user-displayable message!) when there is a problem
 * with transformation. This can be used to communicate some constraints on
 * entered data to the user.
 */
public class TransformationException extends Exception {
    private static final long serialVersionUID = 1L;
    public final String userMessage;

    public TransformationException(final String userMessage) {
        super();
        this.userMessage = userMessage;
    }
}
