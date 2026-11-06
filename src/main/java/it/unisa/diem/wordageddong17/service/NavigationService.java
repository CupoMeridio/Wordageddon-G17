package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.App;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;

/**
 * Servizio di navigazione centralizzato per la gestione del passaggio tra le viste
 * dell'applicazione e il controllo dell'overlay di caricamento.
 */
public class NavigationService {

    private static final Logger LOGGER = Logger.getLogger(NavigationService.class.getName());

    /**
     * Enumerazione delle schermate dell'applicazione con il relativo percorso FXML.
     */
    public enum View {
        /** Schermata di autenticazione utente. */
        LOGIN("/fxml/LoginView.fxml"),
        /** Schermata di registrazione nuovo profilo. */
        REGISTRATION("/fxml/RegistrationView.fxml"),
        /** Schermata principale (dashboard giocatore / admin). */
        HOME("/fxml/HomeView.fxml"),
        /** Schermata della classifica globale e filtri. */
        LEADERBOARD("/fxml/LeaderboardView.fxml"),
        /** Schermata di configurazione nuova partita. */
        GAME_SETUP("/fxml/GameSetupView.fxml"),
        /** Schermata del profilo utente e statistiche. */
        PROFILE("/fxml/ProfileView.fxml"),
        /** Schermata di gestione dei documenti di testo (admin). */
        DOCUMENT_MANAGEMENT("/fxml/DocumentManagementView.fxml"),
        /** Schermata di caricamento nuovi documenti (admin). */
        DOCUMENT_UPLOAD("/fxml/DocumentUploadView.fxml"),
        /** Schermata di gestione e modifica stopwords (admin). */
        STOPWORDS("/fxml/StopwordsView.fxml");

        private final String fxmlPath;

        View(String fxmlPath) {
            this.fxmlPath = fxmlPath;
        }

        /**
         * Restituisce il percorso della risorsa FXML associata alla vista.
         *
         * @return percorso relativo della risorsa FXML nel classpath
         */
        public String getFxmlPath() {
            return fxmlPath;
        }
    }

    private static final NavigationService INSTANCE = new NavigationService();

    private StackPane viewContainer;
    private Node loadingOverlay;
    private View pendingView;

    private NavigationService() {}

    /**
     * Restituisce l'istanza singleton di {@link NavigationService}.
     *
     * @return istanza condivisa
     */
    public static NavigationService getInstance() {
        return INSTANCE;
    }

    /**
     * Inizializza il contenitore delle schermate e l'overlay di caricamento.
     * Se c'era una vista in sospeso, viene caricata immediatamente.
     *
     * @param viewContainer Pannello contenitore delle schermate dinamiche.
     * @param loadingOverlay Componente overlay di caricamento.
     */
    public void init(StackPane viewContainer, Node loadingOverlay) {
        this.viewContainer = viewContainer;
        this.loadingOverlay = loadingOverlay;
        if (this.pendingView != null) {
            View target = this.pendingView;
            this.pendingView = null;
            navigateTo(target);
        }
    }

    /**
     * Mostra o nasconde l'overlay globale di caricamento.
     *
     * @param loading {@code true} per mostrare il loader, {@code false} per nasconderlo.
     */
    public void setLoading(boolean loading) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> setLoading(loading));
            return;
        }
        if (loadingOverlay != null) {
            loadingOverlay.setVisible(loading);
        }
    }

    /**
     * Carica e visualizza la vista specificata nel contenitore principale.
     *
     * @param view Schermata da visualizzare.
     */
    public void navigateTo(View view) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> navigateTo(view));
            return;
        }
        if (viewContainer == null) {
            pendingView = view;
            try {
                App.setRoot("AppView");
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Errore nel caricamento del guscio AppView", e);
            }
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getFxmlPath()));
            Node node = loader.load();
            viewContainer.getChildren().setAll(node);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Errore durante il caricamento della vista: " + view, ex);
            AlertUtils.mostraAlert("Errore interfaccia", "Impossibile caricare la vista: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /**
     * Passa alla schermata di gioco SessionView modificando la radice della scena.
     */
    public void navigateToSession() {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(this::navigateToSession);
            return;
        }
        try {
            App.setRoot("SessionView");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Errore durante il passaggio alla SessionView", ex);
            AlertUtils.mostraAlert("Errore", "Impossibile avviare la sessione di gioco.", Alert.AlertType.ERROR);
        }
    }

    /**
     * Torna al guscio principale AppView impostando la schermata iniziale desiderata.
     *
     * @param initialView Vista da visualizzare all'apertura di AppView.
     */
    public void navigateToApp(View initialView) {
        this.pendingView = initialView;
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> {
                try {
                    App.setRoot("AppView");
                } catch (IOException ex) {
                    LOGGER.log(Level.SEVERE, "Errore durante il ritorno ad AppView", ex);
                }
            });
            return;
        }
        try {
            App.setRoot("AppView");
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Errore durante il ritorno ad AppView", ex);
        }
    }
}
