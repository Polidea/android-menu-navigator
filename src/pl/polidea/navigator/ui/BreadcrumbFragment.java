package pl.polidea.navigator.ui;

import pl.polidea.navigator.R;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import android.os.Bundle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Breadcrumb - displays the menu path.
 */
public class BreadcrumbFragment extends AbstractMenuNavigatorFragment implements OnBackStackChangedListener {
    private LinearLayout breadcrumbLayout;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        this.inflater = inflater;
        final View view = inflater.inflate(R.layout.breadcrumb_fragment_layout, container, false);
        breadcrumbLayout = (LinearLayout) view.findViewById(R.id.breadCrumbLayout);
        updateMenu();
        return view;
    }

    public void updateMenu() {
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
        final TextView tv = (TextView) inflater.inflate(R.layout.breadcrumb_textview, null);
        tv.setText(navigationMenu.name);
        tv.setClickable(true);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO: implement breadcrumb behaviours
            }
        });
        breadcrumbLayout.addView(tv);
        if (!last) {
            final TextView separator = (TextView) inflater.inflate(R.layout.breadcrumb_textview, null);
            separator.setText(getSeparator());
            breadcrumbLayout.addView(separator);
        }
    }

    protected String getSeparator() {
        return ">";
    }

    @Override
    public void onBackStackChanged() {
        setNavigationMenu(application.getNavigationMenu());
        updateMenu();
    }

}
