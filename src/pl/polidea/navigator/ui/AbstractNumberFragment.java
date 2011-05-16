package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractNumberMenu;
import android.content.res.Resources;
import android.widget.Toast;

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
        if (getNavigationMenu().minLength != null && getNavigationMenu().minLength > text.length()) {
            final Resources resources = getActivity().getResources();
            final String toastText = String.format(resources.getString(R.string.error_number_too_short),
                    getNavigationMenu().minLength);
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
            return false;
        }
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