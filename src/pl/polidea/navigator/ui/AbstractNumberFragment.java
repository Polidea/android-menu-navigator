package pl.polidea.navigator.ui;

import pl.polidea.navigator.menu.AbstractNumberMenu;

/**
 * Abstract navigable number fragment - works for all numeric types.
 */
public abstract class AbstractNumberFragment extends AbstractMenuNavigatorFragment {

    public AbstractNumberFragment() {
        super();
    }

    @Override
    public AbstractNumberMenu getNavigationMenu() {
        return (AbstractNumberMenu) super.getNavigationMenu();
    }

    public boolean goNext(final String text) {
        final AbstractNumberMenu menu = getNavigationMenu();
        if (menu.variable != null) {
            menu.menuContext.variables.put(menu.variable, text);
        }
        if (menu.link == null) {
            if (menu.transaction != null) {
                onTransactionListener.handleTransaction(menu.transaction);
                return true;
            }
        } else {
            menuDownListener.onMenuDown(menu.link);
            return true;
        }
        return false;
    }

}