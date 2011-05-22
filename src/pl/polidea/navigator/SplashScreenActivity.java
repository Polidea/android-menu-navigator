package pl.polidea.navigator;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;

import com.apphance.android.Apphance;

/**
 * Activity displaying splash screen.
 */
public class SplashScreenActivity extends Activity {

    protected void getSplashScreen() {
        setContentView(R.layout.splashscreen);
    }

    protected int getApphanceResourceId() {
        return R.string.default_apphance_key;
    }

    @Override
    protected void onCreate(final android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Apphance.start(this, getApphanceResourceId());
        final MenuNavigatorBaseApplication application = (MenuNavigatorBaseApplication) getApplication();
        if (savedInstanceState == null) {
            getSplashScreen();
            final ImageView imageView = (ImageView) findViewById(pl.polidea.navigator.R.id.splashscreen);
            imageView.setImageResource(R.drawable.navigator);
            final AssetMenuRetrieverAsyncTask task = new AssetMenuRetrieverAsyncTask(application, this);
            task.execute((Void[]) null);
        }
    };

    public void signalMenuReady() {
        startActivity(getNextActivityIntent());
        finish();
    }

    protected Intent getNextActivityIntent() {
        return new Intent(this, MenuNavigatorBaseActivity.class);
    }
}
