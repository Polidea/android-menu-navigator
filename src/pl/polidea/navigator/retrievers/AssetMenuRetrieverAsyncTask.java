package pl.polidea.navigator.retrievers;

import java.io.File;
import java.io.IOException;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.MenuNavigatorBaseApplication;
import pl.polidea.navigator.SplashScreenActivity;
import pl.polidea.navigator.factories.NavigationMenuFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.MenuContext;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Retrieves menu from asset or downloaded data.
 */
public class AssetMenuRetrieverAsyncTask extends AsyncTask<Void, Void, AbstractNavigationMenu> {

    private static final String TAG = AssetMenuRetrieverAsyncTask.class.getSimpleName();

    private final MenuNavigatorBaseApplication application;

    private final MenuRetrieverInterface menuRetriever;

    private final NavigationMenuFactoryInterface navigationMenuFactory;

    private final SplashScreenActivity activity;

    // Set this variable to true in case you are debugging heavily menus
    // (changing/updating)
    // It will check if the embedded (asset) menus have changed and reload them
    // if necessary
    // At the expense of few seconds slower startup time
    private static final boolean MENU_TESTING_MODE = false;

    public AssetMenuRetrieverAsyncTask(final MenuNavigatorBaseApplication application,
            final SplashScreenActivity activity) {
        super();
        this.application = application;
        menuRetriever = application.getMenuRetriever();
        navigationMenuFactory = application.getNavigationMenuFactory();
        this.activity = activity;
    }

    @Override
    protected AbstractNavigationMenu doInBackground(final Void... params) {
        final SharedPreferences sp = application.getSharedPreferences("myPrefs", 0);
        int versionCode = 0;
        try {
            versionCode = application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionCode;
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
        return reader.getMyMenu();
    }

    @Override
    protected void onPostExecute(final AbstractNavigationMenu result) {
        super.onPostExecute(result);
        application.setNavigationMenu(result);
        activity.signalMenuReady();
    }

}