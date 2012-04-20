package pl.polidea.navigator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import pl.polidea.navigator.factories.FragmentFactoryBase;
import pl.polidea.navigator.factories.FragmentFactoryInterface;
import pl.polidea.navigator.factories.NavigationMenuFactoryBase;
import pl.polidea.navigator.factories.NavigationMenuFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.retrievers.AssetMenuRetriever;
import pl.polidea.navigator.retrievers.MenuRetrieverInterface;
import pl.polidea.navigator.ui.BreadcrumbFragment;
import android.app.Application;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Application that should be extended by any menu navigation application.
 */
public class MenuNavigatorBaseApplication extends Application {
    private static final String TAG = MenuNavigatorBaseApplication.class.getSimpleName();
    private MenuRetrieverInterface firstTimeMenuRetriever;
    private AbstractNavigationMenu navigationMenu;
    private NavigationMenuFactoryInterface navigationMenuFactory;
    private FragmentFactoryInterface fragmentFactory;
    private MenuRetrieverInterface timedRunMenuRetriever;
    private ScheduledExecutorService executor;
    private Properties localConfig;
    private String flurryKey;
    private MenuRetrieverInterface selectedMenuRetriever;

    @Override
    public void onCreate() {
        readLocalConfig();
        setupFlurryKey();
        createBaseFactories();
        super.onCreate();
    }

    private void setupFlurryKey() {
        if (localConfig != null) {
            flurryKey = localConfig.getProperty("flurry_key");
        }
    }

    public Properties getLocalConfig() {
        return localConfig;
    }

    protected void readLocalConfig() {
        final int configResId = getResources().getIdentifier("local_config", "raw", getPackageName());
        if (configResId > 0) {
            Log.d(TAG, "Reading mPay configuration from raw resources using res id = " + configResId);
            try {
                localConfig = new Properties();
                final InputStream is = getResources().openRawResource(configResId);
                try {
                    localConfig.load(is);
                } finally {
                    is.close();
                }
                Log.d(TAG, "Properties read: " + localConfig);
            } catch (final IOException e) {
                Log.d(TAG, "Exception while reading configuration." + e, e);
            }
        } else {
            Log.d(TAG, "Skipping loading configuration - the local_config file is missing");
        }
    }

    protected void createBaseFactories() {
        firstTimeMenuRetriever = createFirstTimeMenuRetriever();
        timedRunMenuRetriever = createTimedRunMenuRetriever();
        navigationMenuFactory = createNavigationMenuFactory();
        fragmentFactory = createFragmentFactory();
    }

    public final AbstractNavigationMenu getNavigationMenu() {
        return navigationMenu;
    }

    public final FragmentFactoryInterface getFragmentFactory() {
        return fragmentFactory;
    }

    public final NavigationMenuFactoryInterface getJsonReaderFactory() {
        return navigationMenuFactory;
    }

    public final MenuRetrieverInterface getFirstTimeMenuRetriever() {
        return firstTimeMenuRetriever;
    }

    public final MenuRetrieverInterface getSelectedMenuRetriever() {
        return selectedMenuRetriever;
    }

    public MenuRetrieverInterface getTimedMenuRetriever() {
        return timedRunMenuRetriever;
    }

    public final NavigationMenuFactoryInterface getNavigationMenuFactory() {
        return navigationMenuFactory;
    }

    protected MenuRetrieverInterface createFirstTimeMenuRetriever() {
        return new AssetMenuRetriever(this, "testmenu", "menu");
    }

    protected MenuRetrieverInterface createTimedRunMenuRetriever() {
        return null;
    }

    protected FragmentFactoryBase createFragmentFactory() {
        return new FragmentFactoryBase();
    }

    protected NavigationMenuFactoryInterface createNavigationMenuFactory() {
        return new NavigationMenuFactoryBase();
    }

    public BreadcrumbFragment createBreadcrumbFragment() {
        return new BreadcrumbFragment();
    }

    public BitmapReader getBitmapReader(final DisplayMetrics displayMetrics) {
        return new BitmapReader(this, selectedMenuRetriever, displayMetrics, R.drawable.warning);
    }

    public final void setNavigationMenu(final AbstractNavigationMenu navigationMenu) {
        this.navigationMenu = navigationMenu;
    }

    protected void startTimerRetrieverThread(final long initialDelay, final long delay, final TimeUnit timeUnit) {
        final MenuRetrieverInterface timedRetriever = getTimedMenuRetriever();
        if (timedRetriever == null) {
            Log.d(TAG, "Skipping timer startup because there is no timed retriever.");
            return;
        }
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(TAG, "Scheduled run started for menu retrieval.");
                    try {
                        timedRetriever.copyMenu();
                    } catch (final IOException e) {
                        Log.w(TAG, "Error while retrieving menu from remote: ", e);
                    }
                    Log.d(TAG, "Scheduled run finished for menu retrieval.");
                } catch (final Throwable t) { // NOPMD - it is needed here to
                                              // show any errors that might
                                              // occur.
                    Log.w(TAG, "Error when retrieving new menu: ", t);
                }
            }

        }, initialDelay, delay, TimeUnit.SECONDS);
        Log.d(TAG, "Scheduled menu retrieval to run after few seconds, every hour,");
    }

    public final String getFlurryKey() {
        return flurryKey;
    }

    public void setSelectedMenuRetriever(final MenuRetrieverInterface menuRetriever) {
        this.selectedMenuRetriever = menuRetriever;
    }
}
