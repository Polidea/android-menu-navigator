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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
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
    // Set this variable to true in case you are debugging heavily menus
    // (changing/updating)
    // It will check if the embedded (asset) menus have changed and reload them
    // if necessary
    // At the expense of few seconds slower startup time
    private static final boolean MENU_TESTING_MODE = false;

    @Override
    public void onCreate() {
        createBaseFactories();
        super.onCreate();
        final SharedPreferences sp = this.getSharedPreferences("myPrefs", 0);
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
        } catch (final NameNotFoundException e) {
            Log.w(TAG, "Could not read version code.", e);
        }
        if (sp.getInt("menu_app_version", 0) != versionCode || MENU_TESTING_MODE) {
            Log.d(TAG, "Checking if copying needed.");
            try {
                menuRetriever.copyMenu();
                final Editor a = sp.edit();
                try {
                    a.putInt("menu_app_version", versionCode);
                } finally {
                    a.commit();
                }
            } catch (final IOException e) {
                Log.w(TAG, "Error when copying standard menu");
            }
        } else {
            Log.d(TAG, "Skipping menu copying altogether.");
        }
        final JsonMenuReader reader = new JsonMenuReader(new File(menuRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null, navigationMenuFactory, true);
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
