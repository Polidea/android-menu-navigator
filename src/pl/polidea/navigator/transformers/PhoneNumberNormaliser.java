package pl.polidea.navigator.transformers;

import pl.polidea.navigator.R;
import android.content.Context;

/**
 * Normalises phone numbers.
 */
public class PhoneNumberNormaliser implements TransformerInterface {

    private final String countryPrefix;
    private final int minLength;
    private final int maxLength;
    private final Context ctx;

    public PhoneNumberNormaliser(final Context ctx, final int minLength,
            final int maxLength, final String countryPrefix) {
        this.ctx = ctx;
        this.minLength = minLength;
        this.maxLength = maxLength;
        this.countryPrefix = countryPrefix;
    }

    /**
     * Returns normalised phone number (ecluding country prefix).
     * 
     * @param phoneNumber
     *            normalized number or null if number cannot be normalised.
     * @return
     */
    @Override
    public String transformEnteredText(final String phoneNumber)
            throws TransformationException {
        final StringBuilder sb = new StringBuilder();
        for (final char c : phoneNumber.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        if (sb.length() > maxLength && countryPrefix != null
                && sb.toString().startsWith(countryPrefix)) {
            sb.delete(0, countryPrefix.length());
        }
        if (sb.length() < minLength || sb.length() > maxLength) {
            throw new TransformationException(
                    ctx.getString(R.string.error_phone_number_not_valid), null);
        }
        return sb.toString();
    }
}
