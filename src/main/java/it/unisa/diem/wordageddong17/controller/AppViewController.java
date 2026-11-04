package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

/**
 * Controller per il contenitore principale dell'applicazione (AppView).
 * Gestisce l'inizializzazione del {@link NavigationService} e lo smistamento
 * della schermata iniziale in base allo stato corrente dell'applicazione.
 */
public class AppViewController implements Initializable {

    @FXML
    private BorderPane mainContainer;

    @FXML
    private StackPane contentArea;

    @FXML
    private StackPane viewContainer;

    @FXML
    private AnchorPane loadingOverlay;

    private final AppState appState = AppState.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        NavigationService nav = NavigationService.getInstance();
        nav.init(viewContainer, loadingOverlay);

        if (appState.isSessionViewHomeButton()) {
            appState.setSessionViewHomeButton(false);
            nav.navigateTo(View.HOME);
        } else if (appState.isSessionViewContinuaButton()) {
            appState.setSessionViewContinuaButton(false);
            nav.navigateTo(View.GAME_SETUP);
        } else if (appState.getUtente() == null) {
            nav.navigateTo(View.LOGIN);
        } else {
            nav.navigateTo(View.HOME);
        }
    }

    /**
     * Costruttore di default.
     */
    public AppViewController() {
        super();
    }
}
