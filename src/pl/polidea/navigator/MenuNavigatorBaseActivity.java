package pl.polidea.navigator;

import java.util.HashSet;
import java.util.Set;

import pl.polidea.menuNavigator.R;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.MenuType;
import pl.polidea.navigator.menu.TransactionMenu;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.BreadcrumbFragment;
import pl.polidea.navigator.ui.OnMenuDownListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;

/**
 * Activity that should be used as base for all activities using menu navigator.
 * 
 */
public class MenuNavigatorBaseActivity extends FragmentActivity implements OnTransactionListener {

    private final Set<OnTransactionListener> transactionListeners = new HashSet<OnTransactionListener>();

    private FragmentManager fragmentManager;
    private BaseFragmentFactory fragmentsFactory;
    private AbstractNavigationMenu navigationMenu;
    private BreadcrumbFragment breadcrumbFragment;
    private AbstractMenuNavigatorFragment contentFragment;

    @Override
    public boolean handleTransaction(final String transaction) {
        final HashSet<OnTransactionListener> listenersCopy = new HashSet<OnTransactionListener>();
        synchronized (this) {
            listenersCopy.addAll(transactionListeners);
        }
        for (final OnTransactionListener listener : listenersCopy) {
            if (listener.handleTransaction(transaction)) {
                return true;
            }
        }
        return false;
    }

    private final OnMenuDownListener menuDownListener = new OnMenuDownListener() {

        @Override
        public void onMenuDown(final AbstractNavigationMenu navigationMenu) {
            if (navigationMenu.getMenuType() == MenuType.TRANSACTION) {
                handleTransaction(((TransactionMenu) navigationMenu).transaction);
            }
            final AbstractMenuNavigatorFragment newContentFragment = fragmentsFactory.createFragment(navigationMenu,
                    this, MenuNavigatorBaseActivity.this);
            if (newContentFragment != null) {
                breadcrumbFragment.setNavigationMenu(navigationMenu);
                breadcrumbFragment.updateMenu();
                final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                try {
                    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    fragmentTransaction.replace(R.id.content_id, newContentFragment);
                    fragmentTransaction.addToBackStack(navigationMenu.name);
                } finally {
                    fragmentTransaction.commit();
                }
            }
        }
    };

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
            contentFragment = fragmentsFactory.createFragment(navigationMenu, menuDownListener, this);
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

    protected synchronized void registerTransactionListener(final OnTransactionListener listener) {
        transactionListeners.add(listener);
    }

    protected synchronized void unregisterTransactionListener(final OnTransactionListener listener) {
        transactionListeners.remove(listener);
    }

}
