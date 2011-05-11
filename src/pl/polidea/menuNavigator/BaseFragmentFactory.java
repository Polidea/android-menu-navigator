package pl.polidea.menuNavigator;

import pl.polidea.menuNavigator.menuTypes.AbstractNavigationMenu;
import pl.polidea.menuNavigator.menuTypes.MenuImport;
import pl.polidea.menuNavigator.menuTypes.MenuType;
import pl.polidea.menuNavigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.menuNavigator.ui.IconsFragment;
import pl.polidea.menuNavigator.ui.ListMenuFragment;
import pl.polidea.menuNavigator.ui.NumberFragment;
import pl.polidea.menuNavigator.ui.OnMenuDownListener;
import pl.polidea.menuNavigator.ui.PhoneNumberFragment;

/**
 * Creates appropriate fragment depending on the menu item passed.
 * 
 */
public class BaseFragmentFactory implements FragmentFactoryInterface {

    @Override
    public AbstractMenuNavigatorFragment createFragment(final AbstractNavigationMenu navigationMenu,
            final OnMenuDownListener menuDownListener, final OnTransactionListener transactionListener) {
        AbstractMenuNavigatorFragment fragment = null;
        final MenuType menuType = navigationMenu.getMenuType();
        switch (menuType) {
        case ICONS:
            fragment = new IconsFragment();
            break;
        case LIST:
            fragment = new ListMenuFragment();
            break;
        case NUMBER:
            fragment = new NumberFragment();
            break;
        case PHONE_NUMBER:
            fragment = new PhoneNumberFragment();
            break;
        case MENU_IMPORT:
            fragment = createFragment(((MenuImport) navigationMenu).link, menuDownListener, transactionListener);
            break;
        // TODO: add more
        }
        if (fragment != null && menuType != MenuType.MENU_IMPORT) {
            fragment.setNavigationMenu(navigationMenu);
            fragment.setOnTransactionListener(transactionListener);
            fragment.setMenuDownListener(menuDownListener);
        }
        return fragment;
    }
}
