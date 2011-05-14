package pl.polidea.navigator;

import java.io.File;

import pl.polidea.menuNavigator.R;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.DisplayMetrics;

/**
 * Reads bitmap from a directory.
 */
public class BitmapReader {
    private final File directory;
    private final String iconPrefix;
    private final Resources resources;

    public BitmapReader(final Activity activity, final File directory, final DisplayMetrics displayMetrics) {
        this.directory = directory;
        this.resources = activity.getResources();
        switch (displayMetrics.densityDpi) {
        case DisplayMetrics.DENSITY_HIGH:
            iconPrefix = "drawable-hdpi";
            break;
        case DisplayMetrics.DENSITY_MEDIUM:
            iconPrefix = "drawable-mdpi";
            break;
        case DisplayMetrics.DENSITY_LOW:
            iconPrefix = "drawable-ldpi";
            break;
        default:
            throw new IllegalArgumentException("Unsupported density: " + displayMetrics.densityDpi);
        }
    }

    public Bitmap getBitmap(final String fileName) {
        final Bitmap bitmap = BitmapFactory.decodeFile(new File(new File(directory, iconPrefix), fileName).getPath());
        if (bitmap == null) {
            return BitmapFactory.decodeResource(resources, R.drawable.warning);
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
