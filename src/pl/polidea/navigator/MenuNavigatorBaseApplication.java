package pl.polidea.navigator;

import java.io.File;
import java.io.IOException;

import pl.polidea.navigator.factories.FragmentFactoryBase;
import pl.polidea.navigator.factories.FragmentFactoryInterface;
import pl.polidea.navigator.factories.NavigationMenuFactoryBase;
import pl.polidea.navigator.factories.NavigationMenuFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.MenuContext;
import pl.polidea.navigator.ui.BreadcrumbFragment;
import android.app.Application;
import android.util.Log;

/**
 * Application that should be extended by any menu navigation application.
 */
public class MenuNavigatorBaseApplication extends Application {
    private static final String TAG = MenuNavigatorBaseApplication.class.getSimpleName();
    private MenuRetrieverInterface menuRetriever;
    private AbstractNavigationMenu navigationMenu;
    private NavigationMenuFactoryInterface navigationMenuFactory;
    private FragmentFactoryInterface fragmentFactory;

    @Override
    public void onCreate() {
        createBaseFactories();
        super.onCreate();
        try {
            menuRetriever.copyMenu();
        } catch (final IOException e) {
            Log.w(TAG, "Error when copying standard menu");
        }
        final JsonMenuReader reader = new JsonMenuReader(new File(menuRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null, navigationMenuFactory);
        reader.createMenu(new MenuContext());
        navigationMenu = reader.getMyMenu();
        if (navigationMenu == null) {
            throw new IllegalStateException("Menu was not created!");
        }
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

}
