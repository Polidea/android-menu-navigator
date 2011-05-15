package pl.polidea.navigator.menu;

import java.io.File;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;

/**
 * Base class for all menu types.
 * 
 */
public abstract class AbstractNavigationMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String name;
    public final String description;
    public final String iconFile;
    public final String breadCrumbIconFile;
    public final File directory;
    public final String menuType;
    public final MenuContext menuContext;
    public final AbstractNavigationMenu parent;

    public AbstractNavigationMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent) throws JSONException {
        this.directory = reader.directory;
        this.menuType = menuType;
        this.menuContext = reader.menuContext;
        name = jsonMenu.getString("name");
        description = JsonMenuReader.getStringOrNull(jsonMenu, "description");
        iconFile = JsonMenuReader.getStringOrNull(jsonMenu, "icon");
        breadCrumbIconFile = JsonMenuReader.getStringOrNull(jsonMenu, "breadCrumbIcon");
        this.parent = parent;
    }

    public abstract boolean isDisabled();

    @Override
    public String toString() {
        return "AbstractNavigationMenu [name=" + name + ", description=" + description + ", iconFile=" + iconFile
                + ", breadCrumbIconFile=" + breadCrumbIconFile + ", directory=" + directory + ", menuType=" + menuType
                + ", parent=" + (parent == null ? "null" : parent.getClass()) + ", " + super.toString() + "]";
    }

}
