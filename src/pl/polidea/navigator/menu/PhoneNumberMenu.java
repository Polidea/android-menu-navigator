package pl.polidea.navigator.menu;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Menu for entering phone neumber.
 */
public class PhoneNumberMenu extends AbstractNumberMenu {

    private static final long serialVersionUID = 1L;

    public PhoneNumberMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final AbstractNavigationMenu parent)
            throws JSONException {
        super(reader, jsonMenu, BasicMenuTypes.PHONE_NUMBER, parent);
    }

    @Override
    public String toString() {
        return "PhoneNumberMenu [" + super.toString() + "]";
    }

}
