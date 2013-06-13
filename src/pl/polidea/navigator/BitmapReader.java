package pl.polidea.navigator;

import java.io.File;

import pl.polidea.navigator.retrievers.MenuRetrieverInterface;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.util.Log;

/**
 * Reads bitmap from a directory.
 */
public class BitmapReader {
    private final String iconPrefix;
    private final Resources resources;
    private final MenuRetrieverInterface menuRetriever;
    private final int warningResource;
    private final DisplayMetrics displayMetrics;

    private static final String TAG = BitmapReader.class.getSimpleName();

    public BitmapReader(final Context context, final MenuRetrieverInterface menuRetriever,
            final DisplayMetrics displayMetrics, final int warningResource) {
        this.warningResource = warningResource;
        this.menuRetriever = menuRetriever;
        this.displayMetrics = displayMetrics;
        this.resources = context.getResources();

        if (displayMetrics.densityDpi < 140) {
            iconPrefix = "drawable-ldpi";
        } else if (displayMetrics.densityDpi < 200) {
            iconPrefix = "drawable-mdpi";
        } else if (displayMetrics.densityDpi < 300) {
            iconPrefix = "drawable-hdpi";
        } else {
            iconPrefix = "drawable-xhdpi";
        }
    }

    public Bitmap getBitmap(final String fileName) {
        Log.d(TAG, "Retrieving bitmap " + fileName + " from " + iconPrefix);
        final Options options = new BitmapFactory.Options();
        options.inDensity = displayMetrics.densityDpi;
        Bitmap bitmap = null;
        if (menuRetriever != null) {
            final File file = new File(new File(menuRetriever.getBaseDirectory(), iconPrefix), fileName);
            Log.d(TAG, "File to read from " + file);
            bitmap = BitmapFactory.decodeFile(file.getPath(), options);
            Log.d(TAG, "Retrieving bitmap " + fileName);
        }
        if (bitmap == null) {
            Log.d(TAG, "Retrieving bitmap " + fileName);
            return BitmapFactory.decodeResource(resources, warningResource);
        }
        return bitmap;
    }

    public Bitmap getGrayBitmap(final String fileName) {
        return toGrayscale(getBitmap(fileName));
    }

    public Bitmap toGrayscale(final Bitmap bmpOriginal) {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        final Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas c = new Canvas(bmpGrayscale);
        final Paint paint = new Paint();
        final ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        final ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
}
