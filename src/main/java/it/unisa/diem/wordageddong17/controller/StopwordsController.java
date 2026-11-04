package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.service.CaricaStopWordsService;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.service.PrendiStopWordsService;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

/**
 * Controller per la gestione e configurazione dell'elenco delle Stopwords (Admin).
 */
public class StopwordsController implements Initializable {

    @FXML
    private TextArea stopwordTextArea;

    @FXML
    private Button tornaHomeButton;

    @FXML
    private Button salvaStopwordsButton;

    private final AppState appState = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        caricaStopwords();
    }

    private void caricaStopwords() {
        nav.setLoading(true);
        PrendiStopWordsService ps = new PrendiStopWordsService("ListaStopwords");

        ps.setOnSucceeded(e -> {
            nav.setLoading(false);
            byte[] bytes = ps.getValue();
            if (bytes != null) {
                stopwordTextArea.setText(new String(bytes, StandardCharsets.UTF_8));
            } else {
                stopwordTextArea.setText("");
            }
        });

        ps.setOnFailed(e -> {
            nav.setLoading(false);
            AlertUtils.mostraAlert("Warning", "Non risultano stopwords caricate.", Alert.AlertType.INFORMATION);
        });

        ps.start();
    }

    @FXML
    private void salvaStopwords(ActionEvent event) {
        String testo = stopwordTextArea.getText();
        byte[] bytes = testo != null ? testo.getBytes(StandardCharsets.UTF_8) : new byte[0];
        String email = appState.getUtente() != null ? appState.getUtente().getEmail() : "";

        nav.setLoading(true);
        CaricaStopWordsService cs = new CaricaStopWordsService("ListaStopwords", email, bytes);

        cs.setOnSucceeded(e -> {
            nav.setLoading(false);
            AlertUtils.mostraAlert("Successo", "Stopwords salvate con successo.", Alert.AlertType.INFORMATION);
        });

        cs.setOnFailed(e -> {
            nav.setLoading(false);
            Throwable ex = cs.getException();
            String msg = (ex != null && ex.getMessage() != null)
                    ? ex.getMessage()
                    : "Stopwords non caricate correttamente.";
            AlertUtils.mostraAlert("Errore Stopwords", msg, Alert.AlertType.ERROR);
        });

        cs.start();
    }

    @FXML
    private void tornaAllaHome(ActionEvent event) {
        nav.navigateTo(View.HOME);
    }
}
