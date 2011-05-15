package pl.polidea.navigator.menu;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Context that is passed between menus. Contains variables set by the user
 * while navigating.
 */
public class MenuContext implements Serializable {
    private static final long serialVersionUID = 1L;
    public final Map<String, String> variables = new HashMap<String, String>();
}
