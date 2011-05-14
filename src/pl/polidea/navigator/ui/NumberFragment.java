package pl.polidea.navigator.ui;

import pl.polidea.menuNavigator.R;
import pl.polidea.navigator.menu.NumberMenu;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class NumberFragment extends AbstractMenuNavigatorFragment {

    public boolean goNext() {
        final NumberMenu menu = getNavigationMenu();
        if (menu.link != null) {
            menuDownListener.onMenuDown(menu.link);
            return true;
        } else if (menu.transaction != null) {
            onTransactionListener.handleTransaction(menu.transaction);
            return true;
        }
        return false;
    }

    @Override
    public NumberMenu getNavigationMenu() {
        return (NumberMenu) super.getNavigationMenu();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (getNavigationMenu() == null) {
            return null;
        }
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.number_layout_fragment, container, false);
        final EditText text = (EditText) layout.findViewById(R.id.provide_number_text);
        final TextView textView = (TextView) layout.findViewById(R.id.provide_number_label);
        final Button nextButton = (Button) layout.findViewById(R.id.provide_number_button);
        final NumberMenu menu = getNavigationMenu();
        textView.setText(menu.description);
        text.setImeOptions(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.IME_ACTION_NEXT);
        text.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                return goNext();
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                goNext();
            }
        });
        text.setKeyListener(new DigitsKeyListener());
        if (menu.maxLength != null) {
            final InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(menu.maxLength);
            text.setFilters(filterArray);
        }
        // add min length
        return layout;
    }
}
