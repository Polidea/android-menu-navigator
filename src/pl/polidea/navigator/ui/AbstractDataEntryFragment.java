package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractDataEntryMenu;
import pl.polidea.navigator.transformers.TransformationException;
import pl.polidea.navigator.transformers.TransformerInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

/**
 * Abstract navigable number fragment - works for all numeric types.
 */
public abstract class AbstractDataEntryFragment extends AbstractMenuNavigatorFragment {

    private int errorTooShortResourceId = R.string.error_too_short;
    protected TransformerInterface transformer;
    protected EditText text;

    public AbstractDataEntryFragment() {
        super();
    }

    protected abstract ViewGroup inflateViewGroup(LayoutInflater inflater, ViewGroup container);

    protected abstract void setEditTextOptions();

    @Override
    public ViewGroup onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        if (getNavigationMenu() == null) {
            return null;
        }
        final ViewGroup layout = inflateViewGroup(inflater, container);
        text = (EditText) layout.findViewById(R.id.provide_text);
        if (getNavigationMenu().hint != null) {
            text.setHint(getNavigationMenu().hint);
        }
        final Button nextButton = (Button) layout.findViewById(R.id.provide_button);
        setEditTextOptions();
        final AbstractDataEntryMenu menu = getNavigationMenu();
        text.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                return goNext(v.getText().toString());
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                goNext(text.getText().toString());
            }
        });
        text.setKeyListener(new DigitsKeyListener());
        if (menu.maxLength != null) {
            final InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(menu.maxLength);
            text.setFilters(filterArray);
        }
        return null;
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
        if (getNavigationMenu().minLength != null && getNavigationMenu().minLength > transformedText.length()) {
            final Resources resources = getActivity().getResources();
            final String toastText = String.format(resources.getString(errorTooShortResourceId),
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
                Toast.makeText(getActivity(), e.userMessage, Toast.LENGTH_LONG).show();
                return null;
            }
        }
        return transformedText;
    }

    public void setTransformer(final TransformerInterface transformer) {
        this.transformer = transformer;
    }

    public void setErrorTooShortResourceId(final int errorTooShortResourceId) {
        this.errorTooShortResourceId = errorTooShortResourceId;
    }
}