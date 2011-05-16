package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.StringMenu;
import android.os.Bundle;
import android.text.InputFilter;
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

/**
 * Fragment for entering a number.
 */
public class StringFragment extends AbstractDataEntryFragment {

    @Override
    public StringMenu getNavigationMenu() {
        return (StringMenu) super.getNavigationMenu();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (getNavigationMenu() == null) {
            return null;
        }
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.string_layout_fragment, container, false);
        final EditText text = (EditText) layout.findViewById(R.id.provide_string_text);
        final Button nextButton = (Button) layout.findViewById(R.id.provide_string_button);
        final StringMenu menu = getNavigationMenu();
        text.setImeOptions(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.IME_ACTION_NEXT);
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
        if (menu.maxLength != null) {
            final InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(menu.maxLength);
            text.setFilters(filterArray);
        }
        // add min length
        return layout;
    }
}
