package pl.polidea.navigator.factories;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.IconsFragment;
import pl.polidea.navigator.ui.ListMenuFragment;
import pl.polidea.navigator.ui.NumberFragment;
import pl.polidea.navigator.ui.PhoneNumberFragment;

/**
 * Creates appropriate fragment depending on the menu item passed.
 * 
 */
public class FragmentFactoryBase implements FragmentFactoryInterface {

    @Override
    public AbstractMenuNavigatorFragment createFragment(final AbstractNavigationMenu navigationMenu) {
        AbstractMenuNavigatorFragment fragment = null;
        final String menuType = navigationMenu.menuType;
        if (BasicMenuTypes.ICONS.equals(menuType)) {
            fragment = new IconsFragment();
        } else if (BasicMenuTypes.LIST.equals(menuType)) {
            fragment = new ListMenuFragment();
        } else if (BasicMenuTypes.NUMBER.equals(menuType)) {
            fragment = new NumberFragment();
        } else if (BasicMenuTypes.PHONE_NUMBER.equals(menuType)) {
            fragment = new PhoneNumberFragment();
        } else if (BasicMenuTypes.MENU_IMPORT.equals(menuType)) {
            // Note! here we call ourselves recursively so we don't want to set
            // navigation menu (below setNavigationMenu)
            // It's already done inside recursive call. That's why we return
            // here
            return createFragment(((MenuImport) navigationMenu).link);
        }
        fragment.setNavigationMenu(navigationMenu);
        return fragment;
    }
}
