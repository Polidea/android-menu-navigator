package pl.polidea.navigator;

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

/**
 * Application that should be extended by any menu navigation application.
 */
public class MenuNavigatorBaseApplication extends Application {
    private MenuRetrieverInterface firstTimeMenuRetriever;
    private AbstractNavigationMenu navigationMenu;
    private NavigationMenuFactoryInterface navigationMenuFactory;
    private FragmentFactoryInterface fragmentFactory;
    private MenuRetrieverInterface timedRunMenuRetriever;

    @Override
    public void onCreate() {
        createBaseFactories();
        super.onCreate();
    }

    protected void createBaseFactories() {
        firstTimeMenuRetriever = createFirstTimeMenuRetriever();
        timedRunMenuRetriever = timedRunMenuRetriever();
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

    protected MenuRetrieverInterface timedRunMenuRetriever() {
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
}
