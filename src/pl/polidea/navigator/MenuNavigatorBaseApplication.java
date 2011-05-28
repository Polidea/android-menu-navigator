package pl.polidea.navigator;

import java.io.IOException;
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

import com.apphance.android.Log;

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

    @Override
    public void onCreate() {
        createBaseFactories();
        super.onCreate();
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
        return new BitmapReader(this, getFirstTimeMenuRetriever(), displayMetrics, R.drawable.warning);
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
                } catch (final Throwable t) {
                    Log.w(TAG, "Error when retrieving new menu: ", t);
                }
            }

        }, initialDelay, delay, TimeUnit.SECONDS);
        Log.d(TAG, "Scheduled menu retrieval to run after few seconds, every hour,");

    }
}
