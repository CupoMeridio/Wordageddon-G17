package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.service.CaricaTestoService;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;

/**
 * Controller per la schermata di caricamento di un nuovo documento di testo nel database (Admin).
 */
public class DocumentUploadController {

    private static final Logger LOGGER = Logger.getLogger(DocumentUploadController.class.getName());

    @FXML
    private TextField pathTextField;

    @FXML
    private RadioButton adminRadioIT;
    @FXML
    private RadioButton adminRadioEN;
    @FXML
    private RadioButton adminRadioES;
    @FXML
    private RadioButton adminRadioDE;
    @FXML
    private RadioButton adminRadioFR;
    @FXML
    private ToggleGroup lingua;

    @FXML
    private RadioButton adminRadioFacile;
    @FXML
    private RadioButton adminRadioMedio;
    @FXML
    private RadioButton adminRadioDifficile;
    @FXML
    private ToggleGroup inserisciDifficoltà;

    @FXML
    private Button browseButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button backToGestDoc;

    private File fileSelezionato;
    private final AppState appState = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    private String getDifficoltaSelezionataAdmin() {
        if (adminRadioFacile.isSelected()) return LivelloPartita.FACILE.getDbValue();
        if (adminRadioMedio.isSelected()) return LivelloPartita.MEDIO.getDbValue();
        if (adminRadioDifficile.isSelected()) return LivelloPartita.DIFFICILE.getDbValue();
        return null;
    }

    private Lingua getLinguaSelezionataAdmin() {
        if (adminRadioIT.isSelected()) return Lingua.ITALIANO;
        if (adminRadioDE.isSelected()) return Lingua.TEDESCO;
        if (adminRadioES.isSelected()) return Lingua.SPAGNOLO;
        if (adminRadioEN.isSelected()) return Lingua.INGLESE;
        if (adminRadioFR.isSelected()) return Lingua.FRANCESE;
        return null;
    }

    @FXML
    private void scegliFileTesto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di testo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("File di testo", "*.txt")
        );

        File file = fileChooser.showOpenDialog(pathTextField.getScene() != null ? pathTextField.getScene().getWindow() : null);
        if (file != null) {
            fileSelezionato = file;
            pathTextField.setText(file.getName());
        } else {
            AlertUtils.mostraAlert("Attenzione", "Nessun file selezionato.", Alert.AlertType.WARNING);
        }
    }

    @FXML
    private void caricaTesto(ActionEvent event) {
        if (fileSelezionato == null || getDifficoltaSelezionataAdmin() == null || getLinguaSelezionataAdmin() == null) {
            AlertUtils.mostraAlert("Errore", "Completa tutti i campi prima di salvare.", Alert.AlertType.WARNING);
            return;
        }

        byte[] contenuto;
        try {
            contenuto = Files.readAllBytes(fileSelezionato.toPath());
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Errore durante la lettura del file da caricare", ex);
            AlertUtils.mostraAlert("Errore", "Impossibile leggere il file selezionato.", Alert.AlertType.ERROR);
            return;
        }

        nav.setLoading(true);
        CaricaTestoService cts = new CaricaTestoService();
        cts.setDifficoltà(getDifficoltaSelezionataAdmin());
        cts.setDocumento(contenuto);
        cts.setEmail(appState.getUtente() != null ? appState.getUtente().getEmail() : "");
        cts.setNomeFile(fileSelezionato.getName());
        cts.setLingua(getLinguaSelezionataAdmin());

        cts.setOnSucceeded(e -> {
            nav.setLoading(false);
            AlertUtils.mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
            pathTextField.clear();
            fileSelezionato = null;
        });

        cts.setOnFailed(e -> {
            nav.setLoading(false);
            Throwable ex = cts.getException();
            String msg = (ex != null && ex.getMessage() != null)
                    ? ex.getMessage()
                    : "Errore durante il caricamento del documento.";
            AlertUtils.mostraAlert("Errore Caricamento", msg, Alert.AlertType.ERROR);
            pathTextField.clear();
            fileSelezionato = null;
        });

        cts.start();
    }

    @FXML
    private void backToGestDoc(ActionEvent event) {
        nav.navigateTo(View.DOCUMENT_MANAGEMENT);
    }
}
