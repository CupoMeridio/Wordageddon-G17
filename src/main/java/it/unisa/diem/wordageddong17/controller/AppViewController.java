package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Controller per il contenitore principale dell'applicazione (AppView).
 * Gestisce l'inizializzazione del {@link NavigationService} e lo smistamento
 * della schermata iniziale in base allo stato corrente dell'applicazione.
 */
public class AppViewController implements Initializable {

    @FXML
    private StackPane viewContainer;

    @FXML
    private AnchorPane loadingOverlay;

    private final AppState appState = AppState.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NavigationService nav = NavigationService.getInstance();
        boolean vistaPendente = nav.init(viewContainer, loadingOverlay);

        // Se nessuna vista è stata richiesta esplicitamente, si sceglie la schermata
        // predefinita in base allo stato di autenticazione.
        if (!vistaPendente) {
            nav.navigateTo(appState.getUtente() == null ? View.LOGIN : View.HOME);
        }
    }

    /**
     * Costruttore di default.
     */
    public AppViewController() {
        super();
    }
}
