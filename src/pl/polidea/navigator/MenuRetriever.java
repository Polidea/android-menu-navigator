package pl.polidea.navigator;

import java.io.File;
import java.io.IOException;

/**
 * Interface implemented by a mechanism retrieving menu from a source
 * (assets/zip file etc.).
 */
public interface MenuRetriever {

    /**
     * Copy menu from source to destination. This is expensive (in terms of
     * performance).
     * 
     * @throws IOException
     */
    void copyMenu() throws IOException;

    /**
     * Retrieves signature of the menu without actual menu retrieval (this is
     * cheap!).
     * 
     * @return the signature - unchanged signature means that there is no need
     *         to perform the costly operation of copying a menu.
     * @throws IOException
     */
    String getMenuSignature() throws IOException;

    /**
     * Retrieves base directory where menu is stored.
     * 
     * @return
     */
    File getBaseDirectory();

}
