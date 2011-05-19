package pl.polidea.navigator;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pl.polidea.navigator.factories.FragmentFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.TransactionMenu;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.BreadcrumbFragment;
import pl.polidea.navigator.ui.OnLevelChangeListener;
import pl.polidea.navigator.ui.OnMenuDownListener;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

/**
 * Activity that should be used as base for all activities using menu navigator.
 * 
 */
public class MenuNavigatorBaseActivity extends FragmentActivity implements OnTransactionListener, OnLevelChangeListener {

    protected static final int STOPSPLASH = 0;

    private final Set<OnTransactionListener> transactionListeners = new HashSet<OnTransactionListener>();

    private FragmentManager fragmentManager;
    private FragmentFactoryInterface fragmentsFactory;
    private AbstractNavigationMenu navigationMenu;
    private BreadcrumbFragment breadcrumbFragment;
    private AbstractMenuNavigatorFragment contentFragment;
    private TextView infoTextView;

    @Override
    public boolean handleTransaction(String transaction) {
        final Map<String, String> map = navigationMenu.menuContext.variables;
        for (final String key : map.keySet()) {
            transaction = transaction.replace("{" + key + "}", map.get(key));
        }
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

    private void updateActivityWithCurrentFragment() {
        contentFragment.setOnTransactionListener(this);
        contentFragment.setMenuDownListener(menuDownListener);
        breadcrumbFragment.setLevelChangeListener(this);
        navigationMenu = contentFragment.getNavigationMenu();
        breadcrumbFragment.setNavigationMenu(navigationMenu);
        breadcrumbFragment.updateMenu();
        if (navigationMenu.description == null) {
            infoTextView.setVisibility(View.GONE);
        } else {
            infoTextView.setText(navigationMenu.description);
            infoTextView.setVisibility(View.VISIBLE);
        }
    }

    private final OnMenuDownListener menuDownListener = new OnMenuDownListener() {
        @Override
        public void onMenuDown(final AbstractNavigationMenu navigationMenu) {
            if (BasicMenuTypes.TRANSACTION.equals(navigationMenu.menuType)) {
                handleTransaction(((TransactionMenu) navigationMenu).transaction);
                return;
            }
            final AbstractMenuNavigatorFragment newContentFragment = fragmentsFactory.createFragment(navigationMenu);
            if (newContentFragment != null) {
                contentFragment = newContentFragment;
                addFragmentToBackStack(navigationMenu);
                updateActivityWithCurrentFragment();
            }
        }

    };

    private final OnBackStackChangedListener backStackChangedListener = new OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            contentFragment = (AbstractMenuNavigatorFragment) fragmentManager.findFragmentById(R.id.content_id);
            updateActivityWithCurrentFragment();
        }

    };

    public void addFragmentToBackStack(final AbstractNavigationMenu navigationMenu) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.replace(R.id.content_id, contentFragment);
            fragmentTransaction.addToBackStack(navigationMenu.name);
        } finally {
            fragmentTransaction.commit();
        }
    }

    private void insertNewFragmentToActivity() {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            fragmentTransaction.replace(R.id.content_id, contentFragment);
            fragmentTransaction.replace(R.id.breadcrumb_id, breadcrumbFragment);
        } finally {
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        if (navigationMenu != null) {
            outState.putSerializable("menu", navigationMenu);
        }
        fragmentManager.putFragment(outState, "breadcrumb", breadcrumbFragment);
        fragmentManager.putFragment(outState, "content", contentFragment);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSplashScreen();
            final AssetMenuRetrieverAsyncTask task = new AssetMenuRetrieverAsyncTask(
                    (MenuNavigatorBaseApplication) this.getApplication(), this);
            task.execute((Void[]) null);
        } else {
            setContentView(R.layout.main_activity_layout);
            infoTextView = (TextView) findViewById(R.id.infoTextView);
            fragmentManager = getSupportFragmentManager();
            final MenuNavigatorBaseApplication application = (MenuNavigatorBaseApplication) getApplication();
            fragmentsFactory = application.getFragmentFactory();
            navigationMenu = (AbstractNavigationMenu) savedInstanceState.get("menu");
            breadcrumbFragment = (BreadcrumbFragment) fragmentManager.getFragment(savedInstanceState, "breadcrumb");
            contentFragment = (AbstractMenuNavigatorFragment) fragmentManager
                    .getFragment(savedInstanceState, "content");
        }
    }

    protected void getSplashScreen() {
        setContentView(R.layout.splashscreen);
    }

    protected synchronized void registerTransactionListener(final OnTransactionListener listener) {
        transactionListeners.add(listener);
    }

    protected synchronized void unregisterTransactionListener(final OnTransactionListener listener) {
        transactionListeners.remove(listener);
    }

    @Override
    public void changeLevel(final int toLevel) {
        final int startingLevel = fragmentManager.getBackStackEntryCount();
        for (int currentLevel = startingLevel; currentLevel >= toLevel + 1; currentLevel--) {
            fragmentManager.popBackStack();
        }
    }

    public void signalMenuReady() {
        final Message msg = new Message();
        msg.what = MenuNavigatorBaseActivity.STOPSPLASH;
        setContentView(R.layout.main_activity_layout);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
        fragmentManager = getSupportFragmentManager();
        final MenuNavigatorBaseApplication application = (MenuNavigatorBaseApplication) getApplication();
        fragmentsFactory = application.getFragmentFactory();
        navigationMenu = application.getNavigationMenu();
        breadcrumbFragment = application.createBreadcrumbFragment();
        contentFragment = fragmentsFactory.createFragment(navigationMenu);
        insertNewFragmentToActivity();
        updateActivityWithCurrentFragment();
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener);
    }

}
