package pl.polidea.navigator;

import pl.polidea.navigator.factories.FragmentFactoryBase;
import pl.polidea.navigator.factories.FragmentFactoryInterface;
import pl.polidea.navigator.factories.NavigationMenuFactoryBase;
import pl.polidea.navigator.factories.NavigationMenuFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.ui.BreadcrumbFragment;
import android.app.Application;
import android.util.DisplayMetrics;

/**
 * Application that should be extended by any menu navigation application.
 */
public class MenuNavigatorBaseApplication extends Application {
    private MenuRetrieverInterface menuRetriever;
    private AbstractNavigationMenu navigationMenu;
    private NavigationMenuFactoryInterface navigationMenuFactory;
    private FragmentFactoryInterface fragmentFactory;

    @Override
    public void onCreate() {
        createBaseFactories();
        super.onCreate();
    }

    protected void createBaseFactories() {
        menuRetriever = createMenuRetriever();
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

    public final MenuRetrieverInterface getMenuRetriever() {
        return menuRetriever;
    }

    public final NavigationMenuFactoryInterface getNavigationMenuFactory() {
        return navigationMenuFactory;
    }

    protected MenuRetrieverInterface createMenuRetriever() {
        return new AssetMenuRetriever(this, "testmenu", "menu");
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
        return new BitmapReader(this, getMenuRetriever(), displayMetrics, R.drawable.warning);
    }

    public final void setNavigationMenu(final AbstractNavigationMenu navigationMenu) {
        this.navigationMenu = navigationMenu;
    }
}
