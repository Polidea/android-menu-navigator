package pl.polidea.navigator.transformers;

/**
 * Normalises phone numbers.
 */
public class PhoneNumberNormaliser {

    private final String countryPrefix;
    private final int minLength;
    private final int maxLength;

    public PhoneNumberNormaliser(final int minLength, final int maxLength, final String countryPrefix) {
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
    public String normalisePhoneNumber(final String phoneNumber) {
        final StringBuilder sb = new StringBuilder();
        for (final char c : phoneNumber.toCharArray()) {
            if (Character.isDigit(c)) {
                sb.append(c);
            }
        }
        if (sb.length() > maxLength && countryPrefix != null && sb.toString().startsWith(countryPrefix)) {
            sb.delete(0, countryPrefix.length());
        }
        if (sb.length() < minLength || sb.length() > maxLength) {
            return null;
        }
        return sb.toString();
    }
}
