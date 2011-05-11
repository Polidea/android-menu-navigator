package pl.polidea.menuNavigator;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Describes error with particular menu.
 * 
 * @author potiuk
 * 
 */
public class MenuErrorDescription {

    public MenuErrorDescription(final String fileName, final String description, final int lineNumber,
            final int columnNumber, final Throwable t) {
        super();
        this.fileName = fileName;
        this.description = description;
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
        this.linkedThrowable = t;
    }

    public final String fileName;
    public final String description;
    public final int lineNumber;
    public final int columnNumber;
    public final Throwable linkedThrowable;

    @Override
    public String toString() {
        String exceptionDescription = "";
        if (linkedThrowable != null) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            linkedThrowable.printStackTrace(pw);
            exceptionDescription = "\n" + sw.toString();
        }
        return "Menu Error: " + description + " in file \"" + fileName + "\", lineNumber=" + lineNumber
                + ", columnNumber=" + columnNumber + "" + exceptionDescription;
    }

}
