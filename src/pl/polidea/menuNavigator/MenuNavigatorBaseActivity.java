package pl.polidea.menuNavigator;

import pl.polidea.menuNavigator.menuTypes.AbstractNavigationMenu;
import pl.polidea.menuNavigator.menuTypes.MenuType;
import pl.polidea.menuNavigator.menuTypes.TransactionMenu;
import pl.polidea.menuNavigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.menuNavigator.ui.BreadcrumbFragment;
import pl.polidea.menuNavigator.ui.OnMenuDownListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

public class MenuNavigatorBaseActivity extends FragmentActivity {

    private final OnMenuDownListener menuDownListener = new OnMenuDownListener() {
        @Override
        public void onMenuDown(final AbstractNavigationMenu navigationMenu) {
            if (navigationMenu.getMenuType() == MenuType.TRANSACTION) {
                transactionListener.executeTransaction(((TransactionMenu) navigationMenu).transaction);
            }
            final AbstractMenuNavigatorFragment contentFragment = fragmentsFactory.createFragment(navigationMenu, this,
                    transactionListener);
            if (contentFragment != null) {
                breadcrumbFragment.setNavigationMenu(navigationMenu);
                breadcrumbFragment.updateMenu();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                try {
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.replace(R.id.content_id, contentFragment);
                    fragmentTransaction.addToBackStack(navigationMenu.name);
                } finally {
                    fragmentTransaction.commit();
                }
            }
        }
    };

    private final OnTransactionListener transactionListener = new CallTransactionListener(this);

    private final OnBackStackChangedListener backStackChangedListener = new OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            final AbstractMenuNavigatorFragment currentContentFragment = (AbstractMenuNavigatorFragment) fragmentManager
                    .findFragmentById(R.id.content_id);
            navigationMenu = currentContentFragment.getNavigationMenu();
            breadcrumbFragment.setNavigationMenu(navigationMenu);
            breadcrumbFragment.updateMenu();
        }
    };

    private FragmentManager fragmentManager;
    private BaseFragmentFactory fragmentsFactory;
    private AbstractNavigationMenu navigationMenu;
    private BreadcrumbFragment breadcrumbFragment;
    private AbstractMenuNavigatorFragment contentFragment;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentsFactory = new BaseFragmentFactory();
        setContentView(R.layout.main_activity_layout);
        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState == null) {
            breadcrumbFragment = new BreadcrumbFragment();
            final MenuNavigatorBaseApplication application = (MenuNavigatorBaseApplication) getApplication();
            navigationMenu = application.getNavigationMenu();
            contentFragment = fragmentsFactory.createFragment(navigationMenu, menuDownListener, transactionListener);
            breadcrumbFragment.setNavigationMenu(navigationMenu);
            final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            try {
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.replace(R.id.content_id, contentFragment);
                fragmentTransaction.replace(R.id.breadcrumb_id, breadcrumbFragment);
            } finally {
                fragmentTransaction.commit();
            }
        } else {
            contentFragment = (AbstractMenuNavigatorFragment) fragmentManager
                    .getFragment(savedInstanceState, "content");
            contentFragment.setMenuDownListener(menuDownListener);
            breadcrumbFragment = (BreadcrumbFragment) fragmentManager.getFragment(savedInstanceState, "breadcrumb");
            navigationMenu = (AbstractNavigationMenu) savedInstanceState.get("menu");
        }
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("menu", navigationMenu);
        fragmentManager.putFragment(outState, "breadcrumb", breadcrumbFragment);
        fragmentManager.putFragment(outState, "content", contentFragment);
    }

}
