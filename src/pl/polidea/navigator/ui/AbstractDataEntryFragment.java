package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractDataEntryMenu;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Abstract navigable number fragment - works for all numeric types.
 */
public abstract class AbstractDataEntryFragment extends AbstractMenuNavigatorFragment {

    public AbstractDataEntryFragment() {
        super();
    }

    @Override
    public AbstractDataEntryMenu getNavigationMenu() {
        return (AbstractDataEntryMenu) super.getNavigationMenu();
    }

    public boolean goNext(final String text) {
        if (getNavigationMenu().minLength != null && getNavigationMenu().minLength > text.length()) {
            final Resources resources = getActivity().getResources();
            final String toastText = String.format(resources.getString(R.string.error_too_short),
                    getNavigationMenu().minLength);
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG);
            return false;
        }
        final AbstractDataEntryMenu menu = getNavigationMenu();
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