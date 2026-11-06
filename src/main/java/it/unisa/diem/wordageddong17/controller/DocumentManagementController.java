package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.service.EliminaTestoService;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.service.PrendiTestoService;
import it.unisa.diem.wordageddong17.service.PrendiTuttiIDocumentiService;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.util.Duration;

/**
 * Controller per la gestione e consultazione dei documenti di testo (visualizzazione,
 * filtri per difficoltà e lingua, download e cancellazione).
 */
public class DocumentManagementController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(DocumentManagementController.class.getName());

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public DocumentManagementController() {}

    @FXML
    private TableView<DocumentoDiTesto> gestDocTabella;
    @FXML
    private TableColumn<DocumentoDiTesto, String> gestDocColNomeFile;
    @FXML
    private TableColumn<DocumentoDiTesto, LivelloPartita> gestDocColDifficolta;
    @FXML
    private TableColumn<DocumentoDiTesto, String> gestDocColInserito;
    @FXML
    private TableColumn<DocumentoDiTesto, Lingua> gestDocColLingua;
    @FXML
    private TableColumn<DocumentoDiTesto, String> gestDocColDocumento;

    @FXML
    private MenuItem eliminaDocItem;

    @FXML
    private TextField gestDocBarraDiRicerca;

    @FXML
    private CheckBox gestDocCheckFacile;
    @FXML
    private CheckBox gestDocCheckMedia;
    @FXML
    private CheckBox gestDocCheckDifficile;

    @FXML
    private CheckBox gestDocIT;
    @FXML
    private CheckBox gestDocCheckEN;
    @FXML
    private CheckBox gestDocCheckES;
    @FXML
    private CheckBox gestDocCheckFR;
    @FXML
    private CheckBox gestDocCheckDE;

    @FXML
    private Button gestDocNuovoButton;
    @FXML
    private Button gestDocButtonTornaAllaHome;

    private ObservableList<DocumentoDiTesto> listaDocumenti;
    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        listaDocumenti = FXCollections.observableArrayList();

        gestDocColDifficolta.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        gestDocColInserito.setCellValueFactory(new PropertyValueFactory<>("emailAmministratore"));
        gestDocColNomeFile.setCellValueFactory(new PropertyValueFactory<>("nomeFile"));
        gestDocColLingua.setCellValueFactory(new PropertyValueFactory<>("lingua"));

        configuraColonnaDownload();

        FilteredList<DocumentoDiTesto> filteredList = new FilteredList<>(listaDocumenti, p -> true);
        gestDocTabella.setItems(filteredList);
        gestDocTabella.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Runnable filtro = creaFiltro(filteredList);
        aggiungiListenerFiltri(filtro);

        caricaDocumenti();
    }

    private void configuraColonnaDownload() {
        gestDocColDocumento.setCellFactory(col -> new TableCell<>() {
            private final Button btnDownload = new Button("🗎");

            {
                btnDownload.setStyle("-fx-background-color: transparent; -fx-cursor: hand; "
                        + "-fx-font-size: 14; -fx-padding: 2;");
                btnDownload.setOnAction(event -> {
                    DocumentoDiTesto doc = getTableView().getItems().get(getIndex());
                    if (doc == null) {
                        return;
                    }

                    nav.setLoading(true);
                    PrendiTestoService pts = new PrendiTestoService();
                    pts.setNomeFile(doc.getNomeFile());

                    pts.setOnSucceeded(e -> {
                        nav.setLoading(false);
                        byte[] contenuto = pts.getValue();
                        if (contenuto == null || contenuto.length == 0) {
                            AlertUtils.mostraAlert("Attenzione", "File vuoto o non trovato", Alert.AlertType.WARNING);
                            return;
                        }

                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Salva documento");
                        fileChooser.setInitialFileName(doc.getNomeFile());
                        fileChooser.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("File di testo", "*.txt")
                        );
                        File fileDest = fileChooser.showSaveDialog(getTableView().getScene().getWindow());

                        if (fileDest != null) {
                            try {
                                Files.write(fileDest.toPath(), contenuto);
                                AlertUtils.mostraAlert("Successo", "File salvato in:\n" + fileDest.getAbsolutePath(),
                                        Alert.AlertType.INFORMATION);
                            } catch (IOException ex) {
                                LOGGER.log(Level.SEVERE, "Errore durante il salvataggio su disco del file", ex);
                                AlertUtils.mostraAlert("Errore", "Impossibile salvare il file: " + ex.getMessage(),
                                        Alert.AlertType.ERROR);
                            }
                        }
                    });

                    pts.setOnFailed(e -> {
                        nav.setLoading(false);
                        AlertUtils.mostraAlert("Errore", "Errore durante il recupero del file.", Alert.AlertType.ERROR);
                    });

                    pts.start();
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(null);
                    setGraphic(btnDownload);
                    setAlignment(javafx.geometry.Pos.CENTER);
                }
            }
        });
    }

    private Runnable creaFiltro(FilteredList<DocumentoDiTesto> filteredList) {
        return () -> {
            filteredList.setPredicate(doc -> {
                String livelloDb = doc.getDifficolta() != null ? doc.getDifficolta().getDbValue() : "";

                boolean matchDifficolta =
                        (gestDocCheckFacile.isSelected() && "facile".equalsIgnoreCase(livelloDb)) ||
                        (gestDocCheckMedia.isSelected() && "medio".equalsIgnoreCase(livelloDb)) ||
                        (gestDocCheckDifficile.isSelected() && "difficile".equalsIgnoreCase(livelloDb));

                if (!gestDocCheckFacile.isSelected() && !gestDocCheckMedia.isSelected()
                        && !gestDocCheckDifficile.isSelected()) {
                    matchDifficolta = true;
                }

                Lingua lingua = doc.lingua();
                boolean matchLingua = true;

                if (gestDocIT.isSelected() || gestDocCheckEN.isSelected() || gestDocCheckES.isSelected() ||
                    gestDocCheckFR.isSelected() || gestDocCheckDE.isSelected()) {

                    matchLingua =
                        (gestDocIT.isSelected() && lingua == Lingua.ITALIANO) ||
                        (gestDocCheckEN.isSelected() && lingua == Lingua.INGLESE) ||
                        (gestDocCheckES.isSelected() && lingua == Lingua.SPAGNOLO) ||
                        (gestDocCheckFR.isSelected() && lingua == Lingua.FRANCESE) ||
                        (gestDocCheckDE.isSelected() && lingua == Lingua.TEDESCO);
                }

                String query = gestDocBarraDiRicerca.getText() != null ?
                               gestDocBarraDiRicerca.getText().toLowerCase().trim() : "";
                boolean matchRicerca = query.isBlank() ||
                        doc.nomeFile().toLowerCase().contains(query) ||
                        doc.emailAmministratore().toLowerCase().contains(query) ||
                        livelloDb.contains(query) ||
                        (lingua != null && lingua.toString().toLowerCase().contains(query));

                return matchDifficolta && matchLingua && matchRicerca;
            });
        };
    }

    private void aggiungiListenerFiltri(Runnable filtro) {
        PauseTransition pause = new PauseTransition(Duration.millis(50));
        pause.setOnFinished(e -> filtro.run());

        gestDocIT.selectedProperty().addListener((obs, o, n) -> pause.playFromStart());
        gestDocCheckEN.selectedProperty().addListener((obs, o, n) -> pause.playFromStart());
        gestDocCheckES.selectedProperty().addListener((obs, o, n) -> pause.playFromStart());
        gestDocCheckFR.selectedProperty().addListener((obs, o, n) -> pause.playFromStart());
        gestDocCheckDE.selectedProperty().addListener((obs, o, n) -> pause.playFromStart());

        gestDocBarraDiRicerca.textProperty().addListener((obs, oldVal, newVal) -> pause.playFromStart());

        gestDocCheckFacile.selectedProperty().addListener((obs, oldVal, newVal) -> pause.playFromStart());
        gestDocCheckMedia.selectedProperty().addListener((obs, oldVal, newVal) -> pause.playFromStart());
        gestDocCheckDifficile.selectedProperty().addListener((obs, oldVal, newVal) -> pause.playFromStart());
    }

    private void caricaDocumenti() {
        nav.setLoading(true);
        PrendiTuttiIDocumentiService ptds = new PrendiTuttiIDocumentiService();
        ptds.setOnSucceeded(e -> {
            nav.setLoading(false);
            listaDocumenti.clear();
            if (ptds.getValue() != null) {
                listaDocumenti.addAll(ptds.getValue());
            }
            if (listaDocumenti.isEmpty()) {
                AlertUtils.mostraAlert("Attenzione", "Nessun documento trovato.", Alert.AlertType.INFORMATION);
            }
        });

        ptds.setOnFailed(e -> {
            nav.setLoading(false);
            LOGGER.log(Level.SEVERE, "Errore nel caricamento documenti", ptds.getException());
            AlertUtils.mostraAlert("Errore", "Impossibile caricare la lista documenti.", Alert.AlertType.ERROR);
        });

        ptds.start();
    }

    @FXML
    private void gestDocNuovoButtonOnAction(ActionEvent event) {
        nav.navigateTo(View.DOCUMENT_UPLOAD);
    }

    @FXML
    private void tornaAllaHomeFromGestDoc(ActionEvent event) {
        nav.navigateTo(View.HOME);
    }

    @FXML
    private void eliminaDoc(ActionEvent event) {
        List<DocumentoDiTesto> selezionati = new ArrayList<>(gestDocTabella.getSelectionModel().getSelectedItems());

        if (selezionati.isEmpty()) {
            AlertUtils.mostraAlert("Attenzione", "Nessun documento selezionato.", Alert.AlertType.INFORMATION);
            return;
        }

        nav.setLoading(true);
        EliminaTestoService ets = new EliminaTestoService(selezionati);

        ets.setOnSucceeded(e -> {
            nav.setLoading(false);
            listaDocumenti.removeAll(selezionati);
            AlertUtils.mostraAlert("Successo", "Documenti eliminati con successo.", Alert.AlertType.INFORMATION);
        });

        ets.setOnFailed(e -> {
            nav.setLoading(false);
            Throwable ex = ets.getException();
            String msg = (ex != null && ex.getMessage() != null)
                    ? ex.getMessage()
                    : "Errore durante l'eliminazione dei documenti.";
            AlertUtils.mostraAlert("Errore Eliminazione", msg, Alert.AlertType.ERROR);
        });

        ets.setOnCancelled(e -> nav.setLoading(false));

        ets.start();
    }
}
