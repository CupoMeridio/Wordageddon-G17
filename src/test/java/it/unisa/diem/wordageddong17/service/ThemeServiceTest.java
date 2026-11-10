package it.unisa.diem.wordageddong17.service;

import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test unitari per {@link ThemeService}.
 */
public class ThemeServiceTest {

    private ThemeService service;

    @BeforeEach
    public void setUp() {
        service = ThemeService.getInstance();
        service.setTheme(ThemeService.Theme.DARK);
    }

    @AfterEach
    public void tearDown() {
        service.setTheme(ThemeService.Theme.DARK);
    }

    @Test
    public void testSingletonInstance() {
        assertNotNull(service, "L'istanza singleton non deve essere null");
        assertEquals(service, ThemeService.getInstance(), "Le chiamate a getInstance devono restituire la stessa istanza");
    }

    @Test
    public void testToggleTheme() {
        assertEquals(ThemeService.Theme.DARK, service.getCurrentTheme());
        assertTrue(service.isDark());

        service.toggleTheme();
        assertEquals(ThemeService.Theme.LIGHT, service.getCurrentTheme());
        assertFalse(service.isDark());

        service.toggleTheme();
        assertEquals(ThemeService.Theme.DARK, service.getCurrentTheme());
        assertTrue(service.isDark());
    }

    @Test
    public void testSetTheme() {
        service.setTheme(ThemeService.Theme.LIGHT);
        assertEquals(ThemeService.Theme.LIGHT, service.getCurrentTheme());

        service.setTheme(null);
        assertEquals(ThemeService.Theme.LIGHT, service.getCurrentTheme());

        service.setTheme(ThemeService.Theme.DARK);
        assertEquals(ThemeService.Theme.DARK, service.getCurrentTheme());
    }

    @Test
    public void testThemeChangeListener() {
        AtomicReference<ThemeService.Theme> notifiedTheme = new AtomicReference<>();
        java.util.function.Consumer<ThemeService.Theme> listener = notifiedTheme::set;

        service.addThemeChangeListener(listener);
        service.setTheme(ThemeService.Theme.LIGHT);

        assertEquals(ThemeService.Theme.LIGHT, notifiedTheme.get());

        service.removeThemeChangeListener(listener);
        service.setTheme(ThemeService.Theme.DARK);

        // Non deve essere aggiornato poiché rimosso
        assertEquals(ThemeService.Theme.LIGHT, notifiedTheme.get());
    }
}
