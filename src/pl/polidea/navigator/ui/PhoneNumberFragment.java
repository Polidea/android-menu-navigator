package pl.polidea.navigator.ui;

import pl.polidea.menuNavigator.R;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.InputFilter;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Fragment for entering phone number.
 */
public class PhoneNumberFragment extends AbstractMenuNavigatorFragment {
    private static final String TAG = PhoneNumberFragment.class.getSimpleName();

    public boolean goNext() {
        final PhoneNumberMenu menu = getNavigationMenu();
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

    protected static final int CONTACT_PICKER_RESULT = 1234;
    private EditText editText;

    @Override
    public PhoneNumberMenu getNavigationMenu() {
        return (PhoneNumberMenu) super.getNavigationMenu();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (getNavigationMenu() == null) {
            return null;
        }
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.phone_number_layout_fragment, container, false);
        editText = (EditText) layout.findViewById(R.id.provide_phone_number_text);
        final TextView textView = (TextView) layout.findViewById(R.id.provide_phone_number_label);
        final ImageButton contactImage = (ImageButton) layout.findViewById(R.id.select_from_contacts);
        final Button nextButton = (Button) layout.findViewById(R.id.provide_phone_number_button);
        final PhoneNumberMenu menu = getNavigationMenu();
        textView.setText(menu.description);
        editText.setImeOptions(EditorInfo.TYPE_CLASS_PHONE | EditorInfo.IME_ACTION_NEXT);
        editText.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                return goNext();
            }

        });
        editText.setKeyListener(new DialerKeyListener());
        if (menu.maxLength != null) {
            final InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(menu.maxLength);
            editText.setFilters(filterArray);
        }
        contactImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                goNext();
            }
        });
        // add min length
        return layout;
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
            case CONTACT_PICKER_RESULT:
                handleContactResult(data);
                break;
            default:
                // do nothing
            }
        } else {
            // gracefully handle failure
            Log.w(TAG, "Warning: activity result not ok");
        }
    }

    private void handleContactResult(final Intent data) {
        final String[] proj = { Phone.NUMBER, Phone.TYPE };
        final Cursor cursor = getActivity().managedQuery(data.getData(), proj, null, null, null);
        if (cursor.moveToFirst()) {
            editText.setText(cursor.getString(0));
        }
    };
}
