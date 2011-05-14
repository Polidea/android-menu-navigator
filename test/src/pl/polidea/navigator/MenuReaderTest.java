package pl.polidea.navigator;

import java.io.File;

import pl.polidea.navigator.menu.AbstractNavigationMenu;
import pl.polidea.navigator.menu.IconsMenu;
import pl.polidea.navigator.menu.ListMenu;
import pl.polidea.navigator.menu.MenuImport;
import pl.polidea.navigator.menu.MenuType;
import pl.polidea.navigator.menu.PhoneNumberMenu;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

/**
 * Test for menu reader.
 */
public class MenuReaderTest extends ActivityInstrumentationTestCase2<MenuNavigatorBaseActivity> {

    private MenuNavigatorBaseActivity mActivity;
    private AssetMenuRetriever assetRetriever;
    private AbstractNavigationMenu navigationMenu;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = this.getActivity();
        assetRetriever = new AssetMenuRetriever(mActivity, "testmenu", "menu");
        assetRetriever.copyMenu();
        final JsonMenuReader reader = new JsonMenuReader(new File(assetRetriever.getBaseDirectory(), "menu"),
                "main_menu.json", null);
        reader.createMenu();
        navigationMenu = reader.getMyMenu();
        Log.d("MenuReaderTest", navigationMenu.toString());
    }

    public MenuReaderTest() {
        super("pl.polidea.menuNavigator", MenuNavigatorBaseActivity.class);
    }

    public void testPrecondition() {
        assertNotNull(mActivity);
    }

    public void testMainMenuReadCorrectly() {
        assertEquals("Main menu", navigationMenu.name);
        assertEquals("Choose menu", navigationMenu.description);
        assertEquals(MenuType.ICONS, navigationMenu.getMenuType());
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
