package pl.polidea.navigator;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.menu.MenuType;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.IconsFragment;
import pl.polidea.navigator.ui.ListMenuFragment;
import pl.polidea.navigator.ui.NumberFragment;
import pl.polidea.navigator.ui.OnMenuDownListener;
import pl.polidea.navigator.ui.PhoneNumberFragment;

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
        default:
            // do nothing
        }
        if (fragment != null && menuType != MenuType.MENU_IMPORT) {
            fragment.setNavigationMenu(navigationMenu);
            fragment.setOnTransactionListener(transactionListener);
            fragment.setMenuDownListener(menuDownListener);
        }
        return fragment;
    }
}
