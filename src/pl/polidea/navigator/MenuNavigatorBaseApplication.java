package pl.polidea.navigator;

import java.io.File;
import java.io.IOException;

import pl.polidea.navigator.factories.FragmentFactoryBase;
import pl.polidea.navigator.factories.FragmentFactoryInterface;
import pl.polidea.navigator.factories.NavigationMenuFactoryBase;
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
    protected MenuRetrieverInterface menuRetriever;
    private AbstractNavigationMenu navigationMenu;
    private NavigationMenuFactoryBase jsonReaderFactory;
    private FragmentFactoryInterface fragmentFactory;
    private BreadcrumbFragment breadcrumbFragment;

    @Override
    public void onCreate() {
        super.onCreate();
        createBaseFactories();
        try {
            menuRetriever.copyMenu();
        } catch (final IOException e) {
            Log.w(TAG, "Error when copying standard menu");
        }
        final JsonMenuReader reader = new JsonMenuReader(new File(menuRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null, jsonReaderFactory);
        reader.createMenu(new MenuContext());
        navigationMenu = reader.getMyMenu();
        if (navigationMenu == null) {
            throw new IllegalStateException("Menu was not created!");
        }
    }

    public AbstractNavigationMenu getNavigationMenu() {
        return navigationMenu;
    }

    protected void createBaseFactories() {
        menuRetriever = new AssetMenuRetriever(this, "testmenu", "menu");
        jsonReaderFactory = new NavigationMenuFactoryBase();
        fragmentFactory = new FragmentFactoryBase();
        breadcrumbFragment = new BreadcrumbFragment();
    }

    public FragmentFactoryInterface getFragmentFactory() {
        return fragmentFactory;
    }

    public BreadcrumbFragment getBreadcrumbFragment() {
        return breadcrumbFragment;
    }

    public NavigationMenuFactoryBase getJsonReaderFactory() {
        return jsonReaderFactory;
    }

    public MenuRetrieverInterface getMenuRetriever() {
        return menuRetriever;
    }
}
