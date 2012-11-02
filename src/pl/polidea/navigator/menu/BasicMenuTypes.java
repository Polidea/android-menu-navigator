package pl.polidea.navigator.menu;

/**
 * Basic supported menu types.
 */
public final class BasicMenuTypes {
    private BasicMenuTypes() {
        // no instantiation
    }

    public static final String ICONS = "ICONS";
    public static final String LIST = "LIST";
    public static final String MENU_IMPORT = "MENU_IMPORT";
    public static final String NUMBER = "NUMBER";
    public static final String STRING = "STRING";
    public static final String FLOAT_NUMBER = "FLOAT_NUMBER";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String TRANSACTION = "TRANSACTION";

    public static boolean extendsAbstractDataEntryMenu(final String menuType) {
        if (BasicMenuTypes.NUMBER.equals(menuType) || BasicMenuTypes.STRING.equals(menuType)
                || BasicMenuTypes.PHONE_NUMBER.equals(menuType) || BasicMenuTypes.FLOAT_NUMBER.equals(menuType)) {
            return true;
        }
        return false;
    }
}
