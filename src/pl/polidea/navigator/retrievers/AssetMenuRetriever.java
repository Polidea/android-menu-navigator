package pl.polidea.navigator.retrievers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * Unpacks menu from assets to internal folders.
 * 
 */
public class AssetMenuRetriever extends AbstractMenuRetrieverBase implements MenuRetrieverInterface {
    private final String fromAssetLocation;
    private final AssetManager assetManager;

    public AssetMenuRetriever(final Context ctx, final String fromAssetLocation, final String toInternalLocation) {
        super(ctx, toInternalLocation);
        this.fromAssetLocation = fromAssetLocation;
        assetManager = ctx.getAssets();
    }

    @Override
    protected void copyMenuInternally() throws IOException {
        copyRecursivelyFromAsset(null, null);
    }

    private void copyRecursivelyFromAsset(final String path, String[] elements) throws IOException {
        if (elements == null) {
            elements = assetManager.list(fromAssetLocation + (path == null ? "" : "/" + path));
        }
        for (final String element : elements) {
            final String newPath = (path == null ? "" : path + "/") + element;
            final String newPathIncludingAsset = fromAssetLocation + "/" + newPath;
            final String[] subdirs = assetManager.list(newPathIncludingAsset);
            final File destFile = new File(internalTmpDirectory, newPath);
            if (subdirs == null || subdirs.length == 0) {
                Log.d(TAG, "Copying file from " + newPathIncludingAsset + " to " + destFile);
                copyFile(assetManager.open(newPathIncludingAsset, AssetManager.ACCESS_STREAMING), destFile);
            } else {
                if (!destFile.mkdirs()) {
                    Log.w(TAG, "Could not create the " + destFile + " directory.");
                }
                Log.d(TAG, "Creating dir " + destFile);
                copyRecursivelyFromAsset(newPath, subdirs);
            }
        }
    }

    private void addSignatureRecursivelyFromAsset(final StringBuilder sb, final String path, String[] elements)
            throws IOException {
        if (elements == null) {
            elements = assetManager.list(fromAssetLocation + (path == null ? "" : "/" + path));
        }
        for (final String element : elements) {
            final String newPath = (path == null ? "" : path + "/") + element;
            final String newPathIncludingAsset = fromAssetLocation + "/" + newPath;
            final String[] subdirs = assetManager.list(newPathIncludingAsset);
            if (subdirs == null || subdirs.length == 0) {
                try {
                    final AssetFileDescriptor fd = assetManager.openFd(newPathIncludingAsset);
                    final long length = fd.getDeclaredLength();
                    appendFileInfo(sb, newPathIncludingAsset, length);
                } catch (final FileNotFoundException e) {
                    final int length = countFileLength(assetManager.open(newPathIncludingAsset));
                    appendFileInfo(sb, newPathIncludingAsset, length);
                }
            } else {
                addSignatureRecursivelyFromAsset(sb, newPath, subdirs);
            }
        }
    }

    private void appendFileInfo(final StringBuilder sb, final String newPathIncludingAsset, final long length) {
        sb.append(newPathIncludingAsset);
        sb.append(":");
        sb.append(length);
        sb.append(",");
    }

    /**
     * Builds signature for the menu (whenever any file changes, the signature
     * should change as well).
     * 
     * @throws IOException
     */
    @Override
    public String getMenuSignature() throws IOException {
        final StringBuilder sb = new StringBuilder("asset_");
        addSignatureRecursivelyFromAsset(sb, null, null);
        return sb.toString();
    }

    @Override
    public boolean copyMenu() throws IOException {
        final String oldSignature = getOldSignature();
        final String newSignature = getMenuSignature();
        Log.d(TAG, "Comparing " + oldSignature + " with " + newSignature);
        if (newSignature.equals(oldSignature)) {
            Log.d(TAG, "Already initialized with same signature" + oldSignature + " : skipping copying");
            return false;
        }
        Log.d(TAG, "Cleaning up " + internalTmpDirectory);
        cleanUpDirectory(internalTmpDirectory);
        saveSignatureToFile(newSignature);
        copyRecursivelyFromAsset(null, null);
        Log.d(TAG, "Renaming " + internalDirectory + " to " + internalOldDirectory);
        if (!internalDirectory.renameTo(internalOldDirectory)) {
            Log.w(TAG, "Could not rename " + internalDirectory + " to " + internalOldDirectory);
        }
        if (!internalTmpDirectory.renameTo(internalDirectory)) {
            Log.w(TAG, "Could not rename " + internalTmpDirectory + " to " + internalDirectory);
        }
        Log.d(TAG, "Cleaning up " + internalOldDirectory);
        cleanUpDirectory(internalOldDirectory);
        if (!internalOldDirectory.delete()) {
            Log.w(TAG, "Could not delete " + internalOldDirectory);
        }
        Log.d(TAG, "Copied menu to " + internalTmpDirectory);
        return true;
    }

    @Override
    public String toString() {
        return "AssetMenuRetriever [fromAssetLocation=" + fromAssetLocation + ", toString()=" + super.toString() + "]";
    }

}
