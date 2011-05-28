package pl.polidea.navigator.retrievers;

import java.io.File;
import java.io.IOException;

/**
 * Interface implemented by a mechanism retrieving menu from a source
 * (assets/zip file etc.).
 */
public interface MenuRetrieverInterface {

    /**
     * Copy menu from source to destination. This is expensive (in terms of
     * performance).
     * 
     * @return true if copy occured, false if cached version was used actually
     * 
     * @throws IOException
     */
    boolean copyMenu() throws IOException;

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
