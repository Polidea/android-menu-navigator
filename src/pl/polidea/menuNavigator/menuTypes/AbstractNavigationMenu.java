package pl.polidea.menuNavigator.menuTypes;

import java.io.File;
import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import pl.polidea.menuNavigator.JsonMenuReader;

public abstract class AbstractNavigationMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    public final String name;
    public final String description;
    public final String iconFile;
    public final String breadCrumbIconFile;
    protected final File directory;
    private final MenuType menuType;
    public final AbstractNavigationMenu parent;

    public AbstractNavigationMenu(final JSONObject jsonMenu, final File directory, final MenuType menuType,
            final AbstractNavigationMenu parent) throws JSONException {
        this.directory = directory;
        this.menuType = menuType;
        name = jsonMenu.getString("name");
        description = JsonMenuReader.getStringOrNull(jsonMenu, "description");
        iconFile = JsonMenuReader.getStringOrNull(jsonMenu, "icon");
        breadCrumbIconFile = JsonMenuReader.getStringOrNull(jsonMenu, "breadCrumbIcon");
        this.parent = parent;
    }

    public abstract boolean isDisabled();

    public File getDirectory() {
        return directory;
    }

    public MenuType getMenuType() {
        return menuType;
    }

    @Override
    public String toString() {
        return "AbstractNavigationMenu [name=" + name + ", description=" + description + ", iconFile=" + iconFile
                + ", breadCrumbIconFile=" + breadCrumbIconFile + ", directory=" + directory + ", menuType=" + menuType
                + ", parent=" + (parent == null ? "null" : parent.getClass()) + ", " + super.toString() + "]";
    }

}
