package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.IconsMenu;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Icon - displaying fragment.
 */
public class IconsFragment extends AbstractMenuNavigatorFragment {

    private TableLayout tableLayout;
    private int iconWidth;
    private int noOfColumns;

    public IconsFragment() {
        super();
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (displayMetrics.widthPixels > displayMetrics.heightPixels) {
            noOfColumns = 4;
        } else {
            noOfColumns = 3;
        }
        this.iconWidth = displayMetrics.widthPixels / noOfColumns;
    }

    @Override
    public IconsMenu getNavigationMenu() {
        return (IconsMenu) super.getNavigationMenu();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        if (getNavigationMenu() == null) {
            return null;
        }
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.icons_fragment_layout, container, false);
        tableLayout = (TableLayout) layout.findViewById(R.id.tableIconsLayout);
        tableLayout.removeAllViews();
        TableRow tableRow = null;
        for (int i = 0; i < getNavigationMenu().items.length; i++) {
            if (i % noOfColumns == 0) {
                tableRow = new TableRow(this.getActivity());
                tableLayout.addView(tableRow);
            }
            final View iconLayout = inflater.inflate(R.layout.single_icon_layout, null);
            final LinearLayout iconWrapperLayout = (LinearLayout) iconLayout
                    .findViewById(R.id.single_icon_wrapper_layout);
            final ImageView imageView = (ImageView) iconWrapperLayout.findViewById(R.id.icon_layout_image);
            Bitmap bitmap = null;
            final AbstractNavigationMenu item = getNavigationMenu().items[i];
            if (item.isDisabled()) {
                iconLayout.setClickable(false);
                bitmap = bitmapReader.getGrayBitmap(item.iconFile);
            } else {
                iconLayout.setClickable(true);
                bitmap = bitmapReader.getBitmap(item.iconFile);
                iconLayout.setOnClickListener(new MenuNavigatorOnClickListener(item));
            }
            imageView.setImageBitmap(bitmap);
            final TextView textView = (TextView) iconLayout.findViewById(R.id.icon_layout_text);
            final String showTitle = item.parameters.get("show_title");
            if ("false".equals(showTitle)) {
                textView.setVisibility(View.GONE);
            } else {
                textView.setText(item.name);
            }
            iconLayout.setLayoutParams(new TableRow.LayoutParams(iconWidth, LayoutParams.WRAP_CONTENT));
            tableRow.addView(iconLayout);
        }
        return layout;
    }
}
