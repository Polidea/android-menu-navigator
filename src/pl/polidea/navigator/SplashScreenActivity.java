package pl.polidea.navigator;

import java.io.IOException;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.retrievers.MenuLoaderAsyncTask;
import pl.polidea.navigator.retrievers.MenuRetrieverInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

/**
 * Activity displaying splash screen.
 */
public class SplashScreenActivity extends Activity {
    private class InternalMenuRetrieverAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(final Void... params) {
            Log.d(TAG, "Copying the embedded menu.");
            try {
                application.getFirstTimeMenuRetriever().copyMenu();
                final SharedPreferences sp = getSharedPreferences("myPrefs", 0);
                final Editor a = sp.edit();
                try {
                    final int versionCode = getApplicationVersion();
                    a.putInt("menu_app_version", versionCode);
                } finally {
                    a.commit();
                }
            } catch (final IOException e) {
                Log.w(TAG, "Error when copying standard menu", e);
            } catch (final Throwable t) { // NOPMD - it is needed here to show
                                          // any errors.
                Log.w(TAG, "Error when copying standard menu", t);
            }
            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {
            readMenu();
        }

    }

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    // Set this variable to true in case you are debugging heavily menus
    // It will check if the embedded (asset) menus have changed and reload them
    // if needed
    // At the expense of few seconds slower startup time
    private static final boolean MENU_TESTING_MODE = false;

    private MenuNavigatorBaseApplication application;

    protected void getSplashScreen() {
        setContentView(R.layout.splashscreen);
        final ImageView imageView = (ImageView) findViewById(pl.polidea.navigator.R.id.splashscreen);
        imageView.setImageResource(R.drawable.navigator);
    }

    @Override
    protected void onCreate(final android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MenuNavigatorBaseApplication) getApplication();
        if (savedInstanceState == null) {
            getSplashScreen();
            if (checkFirstTimeMenuRetrievalNeeded()) {
                final AsyncTask<Void, Void, Void> embeddedMenuRetrieveTask = new InternalMenuRetrieverAsyncTask();
                embeddedMenuRetrieveTask.execute((Void[]) null);
                // note- reading menu will be fired automaticallly when internal
                // menu refreshed
            } else {
                readMenu();
            }
        }
    };

    protected boolean checkFirstTimeMenuRetrievalNeeded() {
        final SharedPreferences sp = getSharedPreferences("myPrefs", 0);
        final int versionCode = getApplicationVersion();
        if (sp.getInt("menu_app_version", 0) != versionCode || MENU_TESTING_MODE) {
            return true;
        } else {
            Log.d(TAG, "Skipping menu copying altogether.");
            return false;
        }
    }

    protected int getApplicationVersion() {
        int versionCode = -1;
        try {
            versionCode = application.getPackageManager().getPackageInfo(application.getPackageName(), 0).versionCode;
        } catch (final NameNotFoundException e) {
            Log.w(TAG, "Could not read version code.", e);
        }
        return versionCode;
    }

    public void signalMenuReady(final AbstractNavigationMenu navigationMenu) {
        application.setNavigationMenu(navigationMenu);
        afterMenuSetAction();
    }

    protected void afterMenuSetAction() {
        startActivity(getNextActivityIntent());
        finish();
    }

    protected Intent getNextActivityIntent() {
        return new Intent(this, MenuNavigatorBaseActivity.class);
    }

    private void readMenu() {
        final MenuRetrieverInterface firstTimeRetriever = application.getFirstTimeMenuRetriever();
        final MenuRetrieverInterface timedMenuRetriever = application.getTimedMenuRetriever();

        MenuRetrieverInterface menuRetriever = null;
        if (timedMenuRetriever == null) {
            menuRetriever = firstTimeRetriever;
        } else {
            menuRetriever = firstTimeRetriever.getMenuVersion() > timedMenuRetriever.getMenuVersion() ? firstTimeRetriever
                    : timedMenuRetriever;
        }
        Log.d(TAG, "Selected menu retriever:" + menuRetriever);
        application.setSelectedMenuRetriever(menuRetriever);
        Log.d(TAG, "Loading menu " + menuRetriever);
        final MenuLoaderAsyncTask task = new MenuLoaderAsyncTask(this, application.getNavigationMenuFactory(),
                menuRetriever);
        task.execute((Void[]) null);
    }
}
