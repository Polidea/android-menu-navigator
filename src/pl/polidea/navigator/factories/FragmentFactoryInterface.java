package pl.polidea.navigator.factories;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;

/**
 * Interface implemented by factory which creates fragments depending on menu
 * type.
 */
public interface FragmentFactoryInterface {
    AbstractMenuNavigatorFragment createFragment(final AbstractNavigationMenu navigationMenu);
}
