package pl.polidea.navigator.factories;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.FloatNumberMenu;
import pl.polidea.navigator.menu.IconsMenu;
import pl.polidea.navigator.menu.ListMenu;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.menu.NumberMenu;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import pl.polidea.navigator.menu.StringMenu;
import pl.polidea.navigator.menu.TransactionMenu;

/**
 * Basic reader of menu object from Json.
 */
public class NavigationMenuFactoryBase implements NavigationMenuFactoryInterface {

    @Override
    public AbstractNavigationMenu readMenuFromJsonObject(final JsonMenuReader reader, final JSONObject jsonMenu,
            final AbstractNavigationMenu parent) throws JSONException {
        final String type = JsonMenuReader.getStringOrNull(jsonMenu, "type");
        if (type == null || BasicMenuTypes.TRANSACTION.equals(type)) {
            return new TransactionMenu(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.ICONS.equals(type)) {
            return new IconsMenu(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.LIST.equals(type)) {
            return new ListMenu(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.MENU_IMPORT.equals(type)) {
            return new MenuImport(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.STRING.equals(type)) {
            return new StringMenu(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.NUMBER.equals(type)) {
            return new NumberMenu(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.PHONE_NUMBER.equals(type)) {
            return new PhoneNumberMenu(reader, jsonMenu, parent);
        } else if (BasicMenuTypes.FLOAT_NUMBER.equals(type)) {
            return new FloatNumberMenu(reader, jsonMenu, parent);
        } else {
            throw new IllegalArgumentException("Type " + type + " is undefined!");
        }
    }
}
