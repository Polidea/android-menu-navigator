package pl.polidea.navigator;

import java.io.File;

import pl.polidea.navigator.factories.NavigationMenuFactoryBase;
import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.BasicMenuTypes;
import pl.polidea.navigator.menu.IconsMenu;
import pl.polidea.navigator.menu.ListMenu;
import pl.polidea.navigator.menu.MenuContext;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import pl.polidea.navigator.retrievers.AssetMenuRetriever;
import android.test.ApplicationTestCase;
import com.apphance.android.Log;

/**
 * Test for menu reader.
 */
public class MenuReaderTest extends ApplicationTestCase<MenuNavigatorBaseApplication> {

    public MenuReaderTest() {
        super(MenuNavigatorBaseApplication.class);
    }

    private AssetMenuRetriever assetRetriever;
    private AbstractNavigationMenu navigationMenu;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        createApplication();
        assetRetriever = new AssetMenuRetriever(getApplication(), "testmenu", "menu");
        assetRetriever.copyMenu();
        final JsonMenuReader reader = new JsonMenuReader(new File(assetRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null, new NavigationMenuFactoryBase(), true);
        reader.createMenu(new MenuContext());
        navigationMenu = reader.getMyMenu();
        Log.d("MenuReaderTest", navigationMenu.toString());
    }

    public void testMainMenuReadCorrectly() {
        assertEquals("Main menu", navigationMenu.name);
        assertEquals("Choose menu", navigationMenu.description);
        assertEquals(BasicMenuTypes.ICONS, navigationMenu.menuType);
        assertTrue(navigationMenu instanceof IconsMenu);
        assertNull(navigationMenu.iconFile);
    }

    public void testMainMenuHas3Items() {
        final IconsMenu iconsNavigationMenu = (IconsMenu) navigationMenu;
        assertEquals(7, iconsNavigationMenu.items.length);
        for (int i = 0; i < 7; i++) {
            assertNotNull(iconsNavigationMenu.items[i]);
        }
    }

    public void testMainMenuFirstItemHasLinkWith3Subitems() {
        final IconsMenu iconsNavigationMenu = (IconsMenu) navigationMenu;
        final ListMenu linkedMenu = (ListMenu) ((MenuImport) iconsNavigationMenu.items[0]).link;
        assertEquals(3, linkedMenu.items.length);
    }

    public void testMainMenuFirstItemsSecondSubItemHasLinkWithChooseOneOf6Elements() {
        final IconsMenu iconsNavigationMenu = (IconsMenu) navigationMenu;
        final ListMenu linkedMenu = (ListMenu) ((MenuImport) iconsNavigationMenu.items[0]).link;
        assertNotNull(((MenuImport) linkedMenu.items[1]).link);
        final PhoneNumberMenu phoneNumber = (PhoneNumberMenu) ((MenuImport) linkedMenu.items[1]).link;
        assertEquals("Choose one of the elements", phoneNumber.link.name);
        assertEquals(6, ((ListMenu) phoneNumber.link).items.length);
    }
    // TODO: Add assert errors
    // TODO: Add other types of screen

}
