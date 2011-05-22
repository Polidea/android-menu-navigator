package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.StringMenu;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

/**
 * Fragment for entering a number.
 */
public class StringFragment extends AbstractDataEntryFragment {

    @Override
    public StringMenu getNavigationMenu() {
        return (StringMenu) super.getNavigationMenu();
    }

    @Override
    protected ViewGroup inflateViewGroup(final LayoutInflater inflater, final ViewGroup container) {
        return (ViewGroup) inflater.inflate(R.layout.string_layout_fragment, container, false);
    }

    @Override
    protected void setEditTextOptions() {
        text.setImeOptions(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.IME_ACTION_NEXT);
    }

}
