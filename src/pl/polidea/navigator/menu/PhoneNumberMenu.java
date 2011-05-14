package pl.polidea.navigator.menu;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Menu for entering phone neumber.
 */
public class PhoneNumberMenu extends AbstractNumberMenu {

    private static final long serialVersionUID = 1L;

    public PhoneNumberMenu(final JSONObject jsonMenu, final File directory, final AbstractNavigationMenu parent)
            throws JSONException {
        super(jsonMenu, directory, MenuType.PHONE_NUMBER, parent);
    }

    @Override
    public String toString() {
        return "PhoneNumberMenu [" + super.toString() + "]";
    }

}
