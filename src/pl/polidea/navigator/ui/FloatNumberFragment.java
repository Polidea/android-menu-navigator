package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.FloatNumberMenu;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Fragment for entering a number.
 */
public class FloatNumberFragment extends AbstractDataEntryFragment {

    @Override
    public FloatNumberMenu getNavigationMenu() {
        return (FloatNumberMenu) super.getNavigationMenu();
    }

    @Override
    protected ViewGroup inflateViewGroup(final LayoutInflater inflater, final ViewGroup container) {
        return (ViewGroup) inflater.inflate(R.layout.number_layout_fragment, container, false);
    }

    @Override
    protected void setEditTextOptions(final EditText text) {
        text.setImeOptions(EditorInfo.TYPE_CLASS_NUMBER | EditorInfo.TYPE_NUMBER_FLAG_DECIMAL
                | EditorInfo.IME_ACTION_NEXT);
    }
}
