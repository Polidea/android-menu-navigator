package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractDataEntryMenu;
import pl.polidea.navigator.transformers.TransformationException;
import pl.polidea.navigator.transformers.TransformerInterface;
import android.content.res.Resources;
import android.widget.Toast;

/**
 * Abstract navigable number fragment - works for all numeric types.
 */
public abstract class AbstractDataEntryFragment extends
        AbstractMenuNavigatorFragment {

    protected TransformerInterface transformer;

    public AbstractDataEntryFragment() {
        super();
    }

    @Override
    public AbstractDataEntryMenu getNavigationMenu() {
        return (AbstractDataEntryMenu) super.getNavigationMenu();
    }

    public boolean goNext(final String transaction) {
        final String transformedText = transformText(transaction);
        if (transformedText == null) {
            return false;
        }
        if (getNavigationMenu().minLength != null
                && getNavigationMenu().minLength > transformedText.length()) {
            final Resources resources = getActivity().getResources();
            final String toastText = String.format(
                    resources.getString(R.string.error_too_short),
                    getNavigationMenu().minLength);
            Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
            return false;
        }
        final AbstractDataEntryMenu menu = getNavigationMenu();
        if (menu.variable != null) {
            menu.menuContext.variables.put(menu.variable, transformedText);
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

    private String transformText(final String transaction) {
        String transformedText = null;
        if (transformer == null) {
            transformedText = transaction;
        } else {
            try {
                transformedText = transformer.transformEnteredText(transaction);
            } catch (final TransformationException e) {
                Toast.makeText(getActivity(), e.userMessage, Toast.LENGTH_LONG)
                        .show();
                return null;
            }
        }
        return transformedText;
    }

    public void setTransformer(final TransformerInterface transformer) {
        this.transformer = transformer;
    }
}