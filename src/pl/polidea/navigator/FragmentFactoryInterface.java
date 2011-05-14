package pl.polidea.navigator;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.OnMenuDownListener;

public interface FragmentFactoryInterface {
    AbstractMenuNavigatorFragment createFragment(final AbstractNavigationMenu navigationMenu,
            final OnMenuDownListener menuDownListener, final OnTransactionListener transactionListener);
}
