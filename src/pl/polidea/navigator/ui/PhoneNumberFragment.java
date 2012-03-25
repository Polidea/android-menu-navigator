package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.InputType;
import android.text.method.DialerKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

/**
 * Fragment for entering phone number.
 */
public class PhoneNumberFragment extends AbstractDataEntryFragment {
    private static final String TAG = PhoneNumberFragment.class.getSimpleName();

    protected static final int CONTACT_PICKER_RESULT = 1234;

    private View contactImage;

    @Override
    public PhoneNumberMenu getNavigationMenu() {
        return (PhoneNumberMenu) super.getNavigationMenu();
    }

    @Override
    protected ViewGroup inflateViewGroup(final LayoutInflater inflater, final ViewGroup container) {
        return (ViewGroup) inflater.inflate(R.layout.phone_number_layout_fragment, container, false);
    }

    @Override
    protected void setEditTextOptions() {
        text.setImeOptions(InputType.TYPE_CLASS_PHONE | EditorInfo.IME_ACTION_NEXT);
        text.setKeyListener(new DialerKeyListener());
    }

    @Override
    public ViewGroup onCreateView(final LayoutInflater inflater, final ViewGroup container,
            final Bundle savedInstanceState) {
        final ViewGroup layout = super.onCreateView(inflater, container, savedInstanceState);
        if (layout == null) {
            return null;
        }
        contactImage = layout.findViewById(R.id.select_from_contacts);
        contactImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                v.setEnabled(false);
                final Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);
            }
        });
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
            Log.w(TAG, "Warning: activity result not ok");
        }
        contactImage.setEnabled(true);
    }

    private void handleContactResult(final Intent data) {
        final String[] proj = { Phone.NUMBER, Phone.TYPE };
        final Cursor cursor = getActivity().managedQuery(data.getData(), proj, null, null, null);
        if (cursor.moveToFirst()) {
            text.setText(cursor.getString(0));
        }
    };
}
