package pl.polidea.navigator.transformers;

/**
 * Perform transformation of data entered by the user into computer-readable,
 * higly structured form.
 */
public interface TransformerInterface {

    /**
     * Transforms text entered by the user.
     * 
     * @param text
     *            text to transform
     * @return result of transformation
     * @throws TransformationException
     *             in case the entered data was incorrect
     */
    String transformEnteredText(final String text) throws TransformationException;

}
