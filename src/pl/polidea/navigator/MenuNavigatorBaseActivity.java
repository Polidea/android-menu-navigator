package pl.polidea.navigator;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import pl.polidea.navigator.factories.FragmentFactoryInterface;
import pl.polidea.navigator.listeners.OnTransactionListener;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.TransactionMenu;
import pl.polidea.navigator.ui.AbstractMenuNavigatorFragment;
import pl.polidea.navigator.ui.BreadcrumbFragment;
import pl.polidea.navigator.ui.OnLevelChangeListener;
import pl.polidea.navigator.ui.OnMenuDownListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;

/**
 * Activity that should be used as base for all activities using menu navigator.
 * 
 */
public class MenuNavigatorBaseActivity extends FragmentActivity implements OnTransactionListener, OnLevelChangeListener {

    private final Set<OnTransactionListener> transactionListeners = new LinkedHashSet<OnTransactionListener>();

    private FragmentManager fragmentManager;
    private FragmentFactoryInterface fragmentsFactory;
    private AbstractNavigationMenu navigationMenu;
    private BreadcrumbFragment breadcrumbFragment;
    private AbstractMenuNavigatorFragment contentFragment;
    private TextView infoTextView;

    @Override
    public boolean handleTransaction(String transaction) {
        final Map<String, String> map = navigationMenu.menuContext.variables;
        for (final Entry<String, String> entry : map.entrySet()) {
            transaction = transaction.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        final HashSet<OnTransactionListener> listenersCopy = new LinkedHashSet<OnTransactionListener>();
        synchronized (this) {
            for (final OnTransactionListener listener : transactionListeners) {
                listenersCopy.add(listener);
            }
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
        if (navigationMenu != null) {
            if (navigationMenu.description == null) {
                infoTextView.setVisibility(View.GONE);
            } else {
                infoTextView.setText(navigationMenu.description);
                infoTextView.setVisibility(View.VISIBLE);
            }
            FlurryAgent.onEvent("Show Menu", Collections.singletonMap("menu", navigationMenu.name));
        }
        fragmentsFactory.updateFragment(contentFragment, navigationMenu);
    }

    private final OnMenuDownListener menuDownListener = new OnMenuDownListener() {
        @Override
        public void onMenuDown(final AbstractNavigationMenu navigationMenu) {
            if (BasicMenuTypes.TRANSACTION.equals(navigationMenu.menuType)) {
                final String t = ((TransactionMenu) navigationMenu).transaction;
                FlurryAgent.onEvent("Transaction", Collections.singletonMap("transaction", t));
                handleTransaction(t);
                return;
            }
            final AbstractMenuNavigatorFragment newContentFragment = fragmentsFactory.createFragment(navigationMenu);
            if (newContentFragment != null) {
                contentFragment = newContentFragment;
                addFragmentToBackStack(navigationMenu);
                updateActivityWithCurrentFragment();
                FlurryAgent.onEvent("Select submenu", Collections.singletonMap("menu", navigationMenu.name));
            }
        }

    };

    private final OnBackStackChangedListener backStackChangedListener = new OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            contentFragment = (AbstractMenuNavigatorFragment) fragmentManager.findFragmentById(R.id.content_id);
            updateActivityWithCurrentFragment();
            FlurryAgent.onEvent("Back stack changed", Collections.singletonMap("menu", navigationMenu.name));
        }

    };

    private String flurryKey;

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
        outState.putSerializable("menu", navigationMenu);
        fragmentManager.putFragment(outState, "breadcrumb", breadcrumbFragment);
        fragmentManager.putFragment(outState, "content", contentFragment);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final MenuNavigatorBaseApplication application = (MenuNavigatorBaseApplication) getApplication();
        flurryKey = application.getFlurryKey();
        if (savedInstanceState == null) {
            navigationMenu = application.getNavigationMenu();
        } else {
            navigationMenu = (AbstractNavigationMenu) savedInstanceState.get("menu");
        }
        setContentView(R.layout.main_activity_layout);
        infoTextView = (TextView) findViewById(R.id.infoTextView);
        fragmentManager = getSupportFragmentManager();
        fragmentsFactory = application.getFragmentFactory();
        if (savedInstanceState == null) {
            breadcrumbFragment = application.createBreadcrumbFragment();
            contentFragment = fragmentsFactory.createFragment(navigationMenu);
            insertNewFragmentToActivity();
        } else {
            breadcrumbFragment = (BreadcrumbFragment) fragmentManager.getFragment(savedInstanceState, "breadcrumb");
            contentFragment = (AbstractMenuNavigatorFragment) fragmentManager
                    .getFragment(savedInstanceState, "content");
        }
        updateActivityWithCurrentFragment();
        fragmentManager.addOnBackStackChangedListener(backStackChangedListener);
    }

    @Override
    protected void onDestroy() {
        fragmentManager.removeOnBackStackChangedListener(backStackChangedListener);
        super.onDestroy();
    }

    protected synchronized void registerTransactionListener(final OnTransactionListener listener) {
        transactionListeners.add(listener);
    }

    protected synchronized void unregisterTransactionListener(final OnTransactionListener listener) {
        transactionListeners.remove(listener);
    }

    @Override
    public void changeLevel(final int toLevel) {
        FlurryAgent.onEvent("Change breadcrumb level", Collections.singletonMap("level", Integer.toString(toLevel)));
        final int startingLevel = fragmentManager.getBackStackEntryCount();
        for (int currentLevel = startingLevel; currentLevel >= toLevel + 1; currentLevel--) {
            fragmentManager.popBackStack();
        }
    }

    protected void cleanTransactionListeners() {
        transactionListeners.clear();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (flurryKey != null) {
            FlurryAgent.onStartSession(this, flurryKey);
        }
    }

    @Override
    protected void onStop() {
        if (flurryKey != null) {
            FlurryAgent.onEndSession(this);
        }
        super.onStop();
    }
}
