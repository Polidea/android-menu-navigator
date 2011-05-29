package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.FloatNumberMenu;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

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
    protected void setEditTextOptions() {
        text.setImeOptions(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL
                | EditorInfo.IME_ACTION_NEXT);
        text.setKeyListener(new DigitsKeyListener(false, true));
    }
}
