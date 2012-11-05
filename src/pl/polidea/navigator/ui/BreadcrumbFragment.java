package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import android.os.Bundle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Breadcrumb - displays the menu path.
 */
public class BreadcrumbFragment extends AbstractMenuNavigatorFragment implements OnBackStackChangedListener {
    private LinearLayout breadcrumbLayout;
    private ImageView rightImageView;
    private LayoutInflater inflater;
    private int currentLevel;
    private OnLevelChangeListener levelChangeListener;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.breadcrumb_fragment_layout, container, false);
        breadcrumbLayout = (LinearLayout) view.findViewById(R.id.breadCrumbLayout);
        rightImageView = (ImageView) view.findViewById(R.id.rightIcon);
        updateMenu();
        return view;
    }

    public void updateMenu() {
        currentLevel = 0;
        if (breadcrumbLayout != null) {
            breadcrumbLayout.removeAllViews();
            if (getNavigationMenu() != null) {
                addItemToBreadcrumb(getNavigationMenu(), true);
            }
        }
    }

    private void addItemToBreadcrumb(final AbstractNavigationMenu navigationMenu, final boolean last) {
        if (navigationMenu.parent != null) {
            addItemToBreadcrumb(navigationMenu.parent, false);
        }
        if (!BasicMenuTypes.MENU_IMPORT.equals(navigationMenu.menuType)) {
            View breadCrumbView = null;
            if (navigationMenu.breadCrumbIconFile == null) {
                final TextView tv = (TextView) inflater.inflate(R.layout.breadcrumb_textview, null);
                tv.setText(navigationMenu.name);
                breadCrumbView = tv;
            } else {
                final ImageView iv = (ImageView) inflater.inflate(R.layout.breadcrumb_iconview, null);
                iv.setImageBitmap(bitmapReader.getBitmap(navigationMenu.breadCrumbIconFile));
                breadCrumbView = iv;
            }
            breadCrumbView.setClickable(true);
            final int level = currentLevel;
            breadCrumbView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View v) {
                    levelChangeListener.changeLevel(level);
                }
            });
            breadcrumbLayout.addView(breadCrumbView);
            if (!last) {
                final View separator = createSeparator();
                breadcrumbLayout.addView(separator);
            }
            if (navigationMenu.breadCrumbRightIconFile != null) {
                rightImageView.setImageBitmap(bitmapReader.getBitmap(navigationMenu.breadCrumbRightIconFile));
            }
            currentLevel++;
        }
    }

    protected TextView createSeparator() {
        final TextView separator = (TextView) inflater.inflate(R.layout.breadcrumb_textview, null);
        separator.setText(">");
        return separator;
    }

    @Override
    public void onBackStackChanged() {
        setNavigationMenu(application.getNavigationMenu());
        updateMenu();
    }

    public void setLevelChangeListener(final OnLevelChangeListener onLevelChangeListener) {
        this.levelChangeListener = onLevelChangeListener;
    }
}
