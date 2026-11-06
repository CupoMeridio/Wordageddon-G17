package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.InfoGiocatore;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.Utente;
import it.unisa.diem.wordageddong17.service.ModificaFotoProfiloService;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.service.PrendiInfoUtenteService;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import it.unisa.diem.wordageddong17.util.ImageUtils;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller per la visualizzazione delle informazioni profilo dell'utente,
 * statistiche di gioco e cronologia delle partite con filtri avanzati.
 */
public class ProfileController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(ProfileController.class.getName());

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public ProfileController() {}

    @FXML
    private Label usernameInfoUtente;
    @FXML
    private Label emailInfoUtente;
    @FXML
    private Label n_facile;
    @FXML
    private Label migliore_facile;
    @FXML
    private Label n_medio;
    @FXML
    private Label migliore_medio;
    @FXML
    private Label n_difficile;
    @FXML
    private Label migliore_difficile;

    @FXML
    private ImageView immagineInfoUtente;
    @FXML
    private Button tornaAllaHomepage;

    @FXML
    private TableView<Classifica> cronologiaPartite;
    @FXML
    private TableColumn<Classifica, Timestamp> dataCronologiaPartite;
    @FXML
    private TableColumn<Classifica, LivelloPartita> difficoltaCronologiaPartite;
    @FXML
    private TableColumn<Classifica, Float> punteggioCronologiaPartite;

    @FXML
    private DatePicker dataInizio;
    @FXML
    private DatePicker dataFine;
    @FXML
    private TextField punteggioMinimo;
    @FXML
    private TextField punteggioMassimo;
    @FXML
    private CheckBox checkFacile;
    @FXML
    private CheckBox checkMedio;
    @FXML
    private CheckBox checkDifficile;

    private ObservableList<Classifica> listaCronologiaPartite;
    private final AppState appState = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageUtils.ritagliaQuadrato(immagineInfoUtente, 208);
        listaCronologiaPartite = FXCollections.observableArrayList();

        UnaryOperator<TextFormatter.Change> floatFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("-?\\d*(\\.\\d*)?") ? change : null;
        };
        punteggioMinimo.setTextFormatter(new TextFormatter<>(floatFilter));
        punteggioMassimo.setTextFormatter(new TextFormatter<>(floatFilter));

        dataCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("data"));
        difficoltaCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        punteggioCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("punti"));

        inizializzaFiltri();
        caricaDatiProfilo();
    }

    private void inizializzaFiltri() {
        FilteredList<Classifica> filteredList = new FilteredList<>(listaCronologiaPartite, p -> true);
        cronologiaPartite.setItems(filteredList);

        Runnable filtro = () -> {
            final LocalDate dataMin = dataInizio.getValue();
            final LocalDate dataMax = dataFine.getValue();
            final float min = parseSafeFloat(punteggioMinimo.getText(), 0.0f);
            final float max = parseSafeFloat(punteggioMassimo.getText(), Float.MAX_VALUE);

            filteredList.setPredicate(classifica -> {
                String livelloDb = classifica.getDifficolta() != null ? classifica.getDifficolta().getDbValue() : "";

                boolean matchDifficolta =
                    (checkFacile.isSelected() && "facile".equalsIgnoreCase(livelloDb)) ||
                    (checkMedio.isSelected() && "medio".equalsIgnoreCase(livelloDb)) ||
                    (checkDifficile.isSelected() && "difficile".equalsIgnoreCase(livelloDb));

                if (!checkFacile.isSelected() && !checkMedio.isSelected() && !checkDifficile.isSelected()) {
                    matchDifficolta = true;
                }

                Timestamp timestamp = classifica.getData();
                LocalDate data = timestamp != null ? timestamp.toLocalDateTime().toLocalDate() : null;
                boolean matchData = data == null ||
                        ((dataMin == null || !data.isBefore(dataMin)) &&
                         (dataMax == null || !data.isAfter(dataMax)));

                Float punteggio = classifica.getPunti();
                boolean matchPunteggio = punteggio != null && punteggio >= min && punteggio <= max;

                return matchDifficolta && matchData && matchPunteggio;
            });
        };

        checkFacile.selectedProperty().addListener((obs, oldV, newV) -> filtro.run());
        checkMedio.selectedProperty().addListener((obs, oldV, newV) -> filtro.run());
        checkDifficile.selectedProperty().addListener((obs, oldV, newV) -> filtro.run());
        dataInizio.valueProperty().addListener((obs, oldV, newV) -> filtro.run());
        dataFine.valueProperty().addListener((obs, oldV, newV) -> filtro.run());
        punteggioMinimo.textProperty().addListener((obs, oldV, newV) -> filtro.run());
        punteggioMassimo.textProperty().addListener((obs, oldV, newV) -> filtro.run());
    }

    private float parseSafeFloat(String text, float fallback) {
        if (text == null || text.isBlank()) {
            return fallback;
        }
        try {
            return Float.parseFloat(text.trim());
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private void caricaDatiProfilo() {
        Utente utente = appState.getUtente();
        if (utente == null) {
            AlertUtils.mostraAlert("Errore", "Utente non autenticato.", Alert.AlertType.ERROR);
            nav.navigateTo(View.LOGIN);
            return;
        }

        usernameInfoUtente.setText("Username: " + utente.getUsername());
        emailInfoUtente.setText("E-mail: " + utente.getEmail());

        nav.setLoading(true);
        PrendiInfoUtenteService pius = new PrendiInfoUtenteService();
        pius.setEmail(utente.getEmail());

        pius.setOnSucceeded(e -> {
            nav.setLoading(false);
            InfoGiocatore ig = pius.getValue();
            if (ig != null) {
                listaCronologiaPartite.setAll(ig.getCronologiaPartiteList());
                n_facile.setText("N° partite difficoltà facile: " + ig.getFacileCount());
                n_medio.setText("N° partite difficoltà media: " + ig.getMedioCount());
                n_difficile.setText("N° partite difficoltà difficile: " + ig.difficileCount());
                migliore_facile.setText(
                        String.format("Miglior punteggio in difficoltà facile: %.1f", ig.getFacilePunteggio()));
                migliore_medio.setText(
                        String.format("Miglior punteggio in difficoltà media: %.1f", ig.getMedioPunteggio()));
                migliore_difficile.setText(
                        String.format("Miglior punteggio in difficoltà difficile: %.1f", ig.getDifficilePunteggio()));
            }
        });

        pius.setOnFailed(e -> {
            nav.setLoading(false);
            LOGGER.log(Level.SEVERE, "Errore nel caricamento info profilo", pius.getException());
            AlertUtils.mostraAlert("Errore", "Impossibile caricare i dati del profilo", Alert.AlertType.ERROR);
        });

        pius.start();

        Task<Image> immagineTask = new Task<>() {
            @Override
            protected Image call() {
                return ImageUtils.getImageFromByte(utente.getFotoProfilo());
            }
        };
        immagineTask.setOnSucceeded(e -> immagineInfoUtente.setImage(immagineTask.getValue()));
        new Thread(immagineTask).start();
    }

    @FXML
    private void cambiaIMG(MouseEvent event) {
        byte[] nuovaImmagine = ImageUtils.scegliImmagine(
            immagineInfoUtente,
            immagineInfoUtente.getScene() != null ? immagineInfoUtente.getScene().getWindow() : null
        );
        if (nuovaImmagine != null) {
            aggiornaFotoProfilo(nuovaImmagine);
        }
    }

    private void aggiornaFotoProfilo(byte[] nuovaImmagine) {
        Utente utente = appState.getUtente();
        if (utente == null) {
            return;
        }

        nav.setLoading(true);
        ModificaFotoProfiloService mfps = new ModificaFotoProfiloService();
        mfps.setEmail(utente.getEmail());
        mfps.setImmagine(nuovaImmagine);

        mfps.setOnSucceeded(e -> {
            nav.setLoading(false);
            utente.setFotoProfilo(nuovaImmagine);
            immagineInfoUtente.setImage(ImageUtils.getImageFromByte(nuovaImmagine));
        });

        mfps.setOnFailed(e -> {
            nav.setLoading(false);
            AlertUtils.mostraAlert("Errore", "Impossibile aggiornare l'immagine del profilo.", Alert.AlertType.ERROR);
            immagineInfoUtente.setImage(ImageUtils.getImageFromByte(utente.getFotoProfilo()));
        });

        mfps.start();
    }

    @FXML
    private void chiudiInfoUtente(ActionEvent event) {
        nav.navigateTo(View.HOME);
    }
}
