package pl.polidea.navigator.factories;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.FloatNumberFragment;
import pl.polidea.navigator.ui.IconsFragment;
import pl.polidea.navigator.ui.ListMenuFragment;
import pl.polidea.navigator.ui.NumberFragment;
import pl.polidea.navigator.ui.PhoneNumberFragment;
import pl.polidea.navigator.ui.StringFragment;
import android.util.Log;

/**
 * Creates appropriate fragment depending on the menu item passed.
 * 
 */
public class FragmentFactoryBase implements FragmentFactoryInterface {

    private static final String TAG = FragmentFactoryBase.class.getSimpleName();

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
        } else if (BasicMenuTypes.STRING.equals(menuType)) {
            fragment = new StringFragment();
        } else if (BasicMenuTypes.PHONE_NUMBER.equals(menuType)) {
            fragment = new PhoneNumberFragment();
        } else if (BasicMenuTypes.FLOAT_NUMBER.equals(menuType)) {
            fragment = new FloatNumberFragment();
        } else if (BasicMenuTypes.MENU_IMPORT.equals(menuType)) {
            // Note! here we call ourselves recursively so we don't want to set
            // navigation menu (below setNavigationMenu)
            // It's already done inside recursive call. That's why we return
            // here
            return createFragment(((MenuImport) navigationMenu).link);
        } else {
            throw new IllegalArgumentException("Type " + menuType + " is undefined!");
        }
        updateFragment(fragment, navigationMenu);
        Log.d(TAG, "Returning fragment: " + fragment);
        return fragment;
    }

    @Override
    public void updateFragment(final AbstractMenuNavigatorFragment fragment, final AbstractNavigationMenu navigationMenu) {
        fragment.setNavigationMenu(navigationMenu);
    }
}
