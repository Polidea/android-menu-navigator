package pl.polidea.navigator;

import java.io.File;
import java.io.IOException;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import android.app.Application;
import android.util.Log;

/**
 * Application that should be extended by any menu navigation application.
 */
public class MenuNavigatorBaseApplication extends Application {
    private static final String TAG = MenuNavigatorBaseApplication.class.getSimpleName();
    private MenuRetriever menuRetriever;
    private AbstractNavigationMenu navigationMenu;

    @Override
    public void onCreate() {
        super.onCreate();
        menuRetriever = createNewMenuRetriever();
        try {
            menuRetriever.copyMenu();
        } catch (final IOException e) {
            Log.w(TAG, "Error when copying standard menu");
        }
        final JsonMenuReader reader = new JsonMenuReader(new File(menuRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null);
        reader.createMenu();
        navigationMenu = reader.getMyMenu();
        if (navigationMenu == null) {
            throw new RuntimeException("Menu was not created!");
        }
    }

    public AbstractNavigationMenu getNavigationMenu() {
        return navigationMenu;
    }

    public File getBaseDirectory() {
        return menuRetriever.getBaseDirectory();
    }

    public MenuRetriever createNewMenuRetriever() {
        return new AssetMenuRetriever(this, "testmenu", "menu");
    }
}
