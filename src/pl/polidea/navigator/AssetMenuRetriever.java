package pl.polidea.navigator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.util.Log;

/**
 * Unpacks menu from assets to internal folders.
 * 
 */
public class AssetMenuRetriever implements MenuRetrieverInterface {
    private static final String TAG = AssetMenuRetriever.class.getSimpleName();
    private static final int BUFFER_SIZE = 8192;
    private final String fromAssetLocation;
    private final AssetManager assetManager;
    private final File internalDirectory;
    private final File internalTmpDirectory;
    private final File internalOldDirectory;

    public AssetMenuRetriever(final Context ctx, final String fromAssetLocation, final String toInternalLocation) {
        this.fromAssetLocation = fromAssetLocation;
        assetManager = ctx.getAssets();
        internalDirectory = ctx.getDir(toInternalLocation, Context.MODE_PRIVATE);
        internalTmpDirectory = ctx.getDir(toInternalLocation + ".tmp", Context.MODE_PRIVATE);
        internalOldDirectory = ctx.getDir(toInternalLocation + ".old", Context.MODE_PRIVATE);
    }

    private void copyFile(final InputStream inStream, final File destinationFile) {
        try {
            try {
                final BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(destinationFile),
                        BUFFER_SIZE);
                try {
                    final BufferedInputStream bufferedInputStream = new BufferedInputStream(inStream, BUFFER_SIZE);
                    int readByte;
                    while ((readByte = bufferedInputStream.read()) != -1) {
                        outStream.write(readByte);
                    }
                } finally {
                    outStream.close();
                }
            } finally {
                inStream.close();
            }
        } catch (final IOException e) {
            Log.w(TAG, "An error while copying " + inStream + " to " + destinationFile + ":" + e, e);
        }
    }

    private int countFileLength(final InputStream inStream) {
        int count = 0;
        try {
            try {
                final BufferedInputStream bufferedInputStream = new BufferedInputStream(inStream, BUFFER_SIZE);
                while (bufferedInputStream.read() != -1) {
                    count++;
                }
            } finally {
                inStream.close();
            }
            return count;
        } catch (final IOException e) {
            Log.w(TAG, "An error while ccounting length of  " + inStream, e);
            return 0;
        }
    }

    /**
     * Recursive internal directory cleanup routine. It can report progress to
     * any callback registered.
     * 
     * @param directory
     *            directory to clean
     */
    private void cleanUpDirectory(final File directory) {
        if (!directory.mkdirs()) {
            Log.w(TAG, "Could not create directory " + directory);
        }
        for (final File f : directory.listFiles()) {
            if (f.isDirectory()) {
                cleanUpDirectory(f);
            }
            if (!f.delete()) {
                Log.d(TAG, "Could not delete " + f);
                return;
            }
        }
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

    private void saveSignatureToFile(final String newSignature) throws IOException {
        final File signatureFile = new File(internalTmpDirectory, "signature.properties");
        Log.d(TAG, "Saving signature " + newSignature + " to " + signatureFile);
        final Properties p = new Properties();
        p.setProperty("signature", newSignature);
        final FileOutputStream out = new FileOutputStream(signatureFile);
        try {
            p.save(out, "Automatically generated at copying from assets");
        } finally {
            out.close();
        }
    }

    private String getOldSignature() throws IOException {
        final File signatureFile = new File(internalDirectory, "signature.properties");
        final Properties p = new Properties();
        String oldSignature = null;
        if (signatureFile.exists()) {
            Log.d(TAG, "Signature file " + signatureFile + " exists.");
            final FileInputStream signatureFileStream = new FileInputStream(signatureFile);
            try {
                p.load(signatureFileStream);
                oldSignature = p.getProperty("signature");
            } finally {
                signatureFileStream.close();
            }
        }
        return oldSignature;
    }

    @Override
    public File getBaseDirectory() {
        return internalDirectory;
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
}
