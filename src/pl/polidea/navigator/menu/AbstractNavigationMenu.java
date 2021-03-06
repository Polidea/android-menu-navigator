package pl.polidea.navigator.menu;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.navigator.JsonMenuReader;
import pl.polidea.navigator.Persistence;
import android.content.Context;

/**
 * Base class for all menu types.
 * 
 */
public abstract class AbstractNavigationMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public String description;
    public final String help;
    public final String iconFile;
    public final String breadCrumbIconFile;
    public final String breadCrumbRightIconFile;
    public final File directory;
    public final String menuType;
    public final Map<String, String> parameters;
    public transient MenuContext menuContext;
    public transient AbstractNavigationMenu parent;

    private transient Persistence persistence;

    public AbstractNavigationMenu(final String name, final String description, final String help,
            final String iconFile, final String breadCrumbIconFile, final String breadCrumbRightIconFile,
            final String menuType, final Context context) {
        this.name = name;
        this.description = description;
        this.help = help;
        this.iconFile = iconFile;
        this.breadCrumbIconFile = breadCrumbIconFile;
        this.breadCrumbRightIconFile = breadCrumbRightIconFile;
        this.menuType = menuType;
        this.persistence = new Persistence(context);

        this.parameters = null;
        this.directory = null;

    }

    public AbstractNavigationMenu(final JsonMenuReader reader, final JSONObject jsonMenu, final String menuType,
            final AbstractNavigationMenu parent, final Context context) throws JSONException {
        this.directory = reader.directory;
        this.menuType = menuType;
        this.menuContext = reader.menuContext;
        this.persistence = new Persistence(context);
        // Note. Tree Map here is because hashmap has a weird bug/feature which
        // results in lots of "loadFactor"
        // related logs to be printed to log file when serializing/deserializing
        // TreeMap has no such problem
        this.parameters = new TreeMap<String, String>();
        name = jsonMenu.getString("name");
        description = JsonMenuReader.getStringOrNull(jsonMenu, "description");
        iconFile = JsonMenuReader.getStringOrNull(jsonMenu, "icon");
        breadCrumbIconFile = JsonMenuReader.getStringOrNull(jsonMenu, "breadcrumb_icon");
        breadCrumbRightIconFile = JsonMenuReader.getStringOrNull(jsonMenu, "breadcrumb_right_icon");
        help = JsonMenuReader.getStringOrNull(jsonMenu, "help");
        JsonMenuReader.readParameters(this.parameters, jsonMenu, "parameters");
        this.parent = parent;
    }

    public boolean isDisabled() {
        if (persistence != null) {
            return !persistence.getMenuVisibility(name);
        }
        return true;
    }

    public void setDescription(final String decription) {
        this.description = decription;
    }

    @Override
    public String toString() {
        return "AbstractNavigationMenu [name=" + name + ", description=" + description + ", iconFile=" + iconFile
                + ", breadCrumbIconFile=" + breadCrumbIconFile + ", directory=" + directory + ", menuType=" + menuType
                + ", parent=" + (parent == null ? "null" : parent.getClass()) + ", " + ", parameters=" + parameters
                + super.toString() + "]";
    }

    public void updateTransientAttributes(final MenuContext menuContext, final AbstractNavigationMenu parent,
            final Context context) {
        this.parent = parent;
        this.menuContext = menuContext;
        persistence = new Persistence(context);
    }
}
