package it.unisa.diem.wordageddong17.service;

import java.net.URL;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NavigationServiceTest {

    @Test
    public void testTuttiIFxmlDelleVisteEsistono() {
        for (NavigationService.View view : NavigationService.View.values()) {
            String path = view.getFxmlPath();
            assertNotNull(path, "Il percorso FXML per la vista " + view + " non deve essere nullo");
            URL resource = getClass().getResource(path);
            assertNotNull(resource, "La risorsa FXML " + path + " per la vista " + view + " deve esistere nel classpath");
        }
    }
}
