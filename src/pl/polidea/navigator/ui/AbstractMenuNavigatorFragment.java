package pl.polidea.navigator.ui;

import pl.polidea.navigator.BitmapReader;
import pl.polidea.navigator.MenuNavigatorBaseApplication;
import pl.polidea.navigator.OnTransactionListener;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Base fragment for all menu types.
 */
public abstract class AbstractMenuNavigatorFragment extends Fragment {
    protected class MenuNavigatorOnClickListener implements OnClickListener {
        private final AbstractNavigationMenu navigationMenu;

        protected MenuNavigatorOnClickListener(final AbstractNavigationMenu navigationMenu) {
            this.navigationMenu = navigationMenu;
        }

        @Override
        public void onClick(final View v) {
            menuDownListener.onMenuDown(navigationMenu);
        }
    }

    protected class NavigateBackOnClickListener implements OnClickListener {
        private final String name;

        protected NavigateBackOnClickListener(final String name) {
            this.name = name;
        }

        @Override
        public void onClick(final View v) {
            menuUpListener.onMenuUp(name);
        }
    }

    protected DisplayMetrics displayMetrics;
    protected BitmapReader bitmapReader;
    protected MenuNavigatorBaseApplication application;
    private AbstractNavigationMenu navigationMenu;
    protected OnMenuDownListener menuDownListener;
    protected OnMenuUpListener menuUpListener;
    protected OnTransactionListener onTransactionListener;

    public void setNavigationMenu(final AbstractNavigationMenu navigationMenu) {
        this.navigationMenu = navigationMenu;
    }

    public AbstractNavigationMenu getNavigationMenu() {
        return navigationMenu;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            navigationMenu = (AbstractNavigationMenu) savedInstanceState.get("menu");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("menu", navigationMenu);
    }

    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        application = (MenuNavigatorBaseApplication) activity.getApplication();
        bitmapReader = application.getBitmapReader(displayMetrics);
    }

    public void setMenuDownListener(final OnMenuDownListener menuChangedListener) {
        this.menuDownListener = menuChangedListener;
    }

    public void setMenuBackListener(final OnMenuUpListener menuUpListener) {
        this.menuUpListener = menuUpListener;
    }

    public void setOnTransactionListener(final OnTransactionListener onTransactionListener) {
        this.onTransactionListener = onTransactionListener;
    }

}
