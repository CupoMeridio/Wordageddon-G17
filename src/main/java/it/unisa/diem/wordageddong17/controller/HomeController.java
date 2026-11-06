package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import it.unisa.diem.wordageddong17.util.ImageUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Controller per la schermata principale (Home).
 */
public class HomeController implements Initializable {

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public HomeController() {}

    @FXML
    private AnchorPane schermataHome;

    @FXML
    private VBox contenutoHome;

    @FXML
    private Button startButton;

    @FXML
    private Button classificheButton;

    @FXML
    private HBox quickInfoUtente;

    @FXML
    private Label benvenutoLabel;

    @FXML
    private ImageView fotoProfilo;

    @FXML
    private VBox dashboardMenu;

    @FXML
    private Button infoProfiloButton;

    @FXML
    private Button newDocument;

    @FXML
    private Button stopwordList;

    private final AppState appState = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageUtils.ritagliaCerchio(fotoProfilo, 40);
        dashboardMenu.setVisible(false);

        Utente utente = appState.getUtente();
        if (utente != null) {
            benvenutoLabel.setText("Benvenuto " + utente.getUsername());
            fotoProfilo.setImage(ImageUtils.getImageFromByte(utente.getFotoProfilo()));

            boolean isAdmin = utente.getTipo() == TipoUtente.amministratore;
            newDocument.setVisible(isAdmin);
            newDocument.setManaged(isAdmin);
            stopwordList.setVisible(isAdmin);
            stopwordList.setManaged(isAdmin);
        } else {
            benvenutoLabel.setText("Benvenuto Giocatore");
            fotoProfilo.setImage(ImageUtils.getPlaceholderImage());
            newDocument.setVisible(false);
            newDocument.setManaged(false);
            stopwordList.setVisible(false);
            stopwordList.setManaged(false);
        }
    }

    @FXML
    private void startOnAction(ActionEvent event) {
        Utente utente = appState.getUtente();
        if (utente != null && SessioneDiGioco.esisteSalvataggio(utente.getEmail())) {
            AlertUtils.mostraAlertConferma(
                "Hai un salvataggio passato",
                "Vuoi continuare a giocare col salvataggio?",
                confermato -> {
                    if (confermato) {
                        appState.setSessioneSalvata(true);
                        nav.navigateToSession();
                    } else {
                        SessioneDiGioco.eliminaSalvataggio(utente.getEmail());
                        nav.navigateTo(View.GAME_SETUP);
                    }
                }
            );
        } else {
            nav.navigateTo(View.GAME_SETUP);
        }
    }

    @FXML
    private void classificheOnAction(ActionEvent event) {
        nav.navigateTo(View.LEADERBOARD);
    }

    @FXML
    private void toggleDashboard(MouseEvent event) {
        dashboardMenu.setVisible(!dashboardMenu.isVisible());
    }

    @FXML
    private void passaAInfoProfilo(ActionEvent event) {
        dashboardMenu.setVisible(false);
        nav.navigateTo(View.PROFILE);
    }

    @FXML
    private void mostraGestioneDocumenti(ActionEvent event) {
        dashboardMenu.setVisible(false);
        nav.navigateTo(View.DOCUMENT_MANAGEMENT);
    }

    @FXML
    private void mostraGestioneStopwords(ActionEvent event) {
        dashboardMenu.setVisible(false);
        nav.navigateTo(View.STOPWORDS);
    }

    @FXML
    private void logout(ActionEvent event) {
        appState.reset();
        nav.navigateTo(View.LOGIN);
    }
}
