package pl.polidea.navigator.retrievers;

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
import android.util.Log;

/**
 * Abstract class that provides basic building blocks for any retriever.
 */
public abstract class AbstractMenuRetrieverBase implements MenuRetrieverInterface {

    protected static final String TAG = AssetMenuRetriever.class.getSimpleName();
    private static final int BUFFER_SIZE = 8192;
    protected final File internalDirectory;
    protected final File internalTmpDirectory;
    protected final File internalOldDirectory;
    protected Context ctx;

    public AbstractMenuRetrieverBase(final Context ctx, final String toInternalLocation) {
        internalDirectory = ctx.getDir(toInternalLocation, Context.MODE_PRIVATE);
        internalTmpDirectory = ctx.getDir(toInternalLocation + ".tmp", Context.MODE_PRIVATE);
        internalOldDirectory = ctx.getDir(toInternalLocation + ".old", Context.MODE_PRIVATE);
        this.ctx = ctx;
    }

    protected abstract void copyMenuInternally() throws IOException;

    protected String getOldSignature() throws IOException {
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

    protected void copyFile(final InputStream inStream, final File destinationFile) {
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

    protected int countFileLength(final InputStream inStream) {
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
    protected void cleanUpDirectory(final File directory) {
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

    protected void saveSignatureToFile(final String newSignature) throws IOException {
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
        copyMenuInternally();
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
    public File getBaseDirectory() {
        return internalDirectory;
    }

    @Override
    public int getMenuVersion() {
        int version = -1;
        final File f = new File(internalDirectory, "version.txt");
        final Properties p = new Properties();
        String versionString = null;
        try {
            p.load(new FileInputStream(f));
            versionString = p.getProperty("menu_version");
            if (versionString != null) {
                version = Integer.parseInt(versionString);
            }
        } catch (final NumberFormatException e) {
            Log.e(TAG, "Could not read version.txt (" + versionString + "): ", e);
        } catch (final FileNotFoundException e) {
            Log.e(TAG, "Could not read version.txt : ", e);
        } catch (final IOException e) {
            Log.e(TAG, "Could not read version.txt : ", e);
        }
        Log.d(TAG, "Returning version (for " + internalDirectory + ":" + version);
        return version;
    }

    @Override
    public String toString() {
        return "AbstractMenuRetrieverBase [internalDirectory=" + internalDirectory + ", internalTmpDirectory="
                + internalTmpDirectory + ", internalOldDirectory=" + internalOldDirectory + "]";
    }

}
