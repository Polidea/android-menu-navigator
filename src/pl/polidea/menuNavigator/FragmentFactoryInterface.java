package pl.polidea.menuNavigator;

import pl.polidea.menuNavigator.menu.AbstractNavigationMenu;
import pl.polidea.menuNavigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.menuNavigator.ui.OnMenuDownListener;

public interface FragmentFactoryInterface {
    AbstractMenuNavigatorFragment createFragment(final AbstractNavigationMenu navigationMenu,
            final OnMenuDownListener menuDownListener, final OnTransactionListener transactionListener);
}
