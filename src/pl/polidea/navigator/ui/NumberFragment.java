package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.NumberMenu;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Fragment for entering a number.
 */
public class NumberFragment extends AbstractDataEntryFragment {

    @Override
    public NumberMenu getNavigationMenu() {
        return (NumberMenu) super.getNavigationMenu();
    }

    @Override
    protected ViewGroup inflateViewGroup(final LayoutInflater inflater, final ViewGroup container) {
        return (ViewGroup) inflater.inflate(R.layout.number_layout_fragment, container, false);
    };

    @Override
    protected void setEditTextOptions(final EditText text) {
        text.setImeOptions(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.IME_ACTION_NEXT);
    }
}
