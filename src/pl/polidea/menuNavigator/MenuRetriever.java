package pl.polidea.menuNavigator;

import java.io.File;
import java.io.IOException;

public interface MenuRetriever {

    public abstract void copyMenu() throws IOException;

    public abstract String getMenuSignature() throws IOException;

    public abstract File getBaseDirectory();

}
