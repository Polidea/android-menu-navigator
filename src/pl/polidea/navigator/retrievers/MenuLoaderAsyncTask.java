package pl.polidea.navigator.retrievers;

import java.io.File;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.SplashScreenActivity;
import pl.polidea.navigator.factories.NavigationMenuFactoryInterface;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.MenuContext;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Loads menu from the retriever chosen.
 */
public class MenuLoaderAsyncTask extends AsyncTask<Void, Void, AbstractNavigationMenu> {

    private static final String TAG = MenuLoaderAsyncTask.class.getSimpleName();

    private final SplashScreenActivity activity;

    private final MenuRetrieverInterface menuRetriever;

    private final NavigationMenuFactoryInterface navigationMenuFactory;

    public MenuLoaderAsyncTask(final SplashScreenActivity activity,
            final NavigationMenuFactoryInterface naviationMenuFactory, final MenuRetrieverInterface menuRetriever) {
        super();
        this.activity = activity;
        this.menuRetriever = menuRetriever;
        this.navigationMenuFactory = naviationMenuFactory;
    }

    @Override
    protected AbstractNavigationMenu doInBackground(final Void... params) {
        Log.d(TAG, "Loading menu from " + menuRetriever);
        final JsonMenuReader reader = new JsonMenuReader(new File(menuRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null, navigationMenuFactory, true, activity);
        reader.createMenu(new MenuContext());
        final AbstractNavigationMenu menu = reader.getMyMenu();
        Log.d(TAG, " Returning menu: " + menu);
        return menu;
    }

    @Override
    protected void onPostExecute(final AbstractNavigationMenu result) {
        super.onPostExecute(result);
        activity.signalMenuReady(result);
    }

}
