package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.database.DatabaseRegistrazioneLogin;
import it.unisa.diem.wordageddong17.model.Amministratore;
import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Giocatore;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 */
/**
 * @class AppViewController
 * @brief Controller FXML principale per l'applicazione WordAged
 *
 * Questa classe gestisce tutte le interazioni dell'interfaccia utente
 * principale, inclusi login, registrazione, navigazione tra schermate e avvio
 * del gioco. Implementa il pattern MVC per separare la logica di controllo
 * dalla vista.
 *
 * @details Il controller gestisce le seguenti funzionalità principali: -
 * Autenticazione utente (login/registrazione) - Navigazione tra diverse
 * schermate - Gestione del profilo utente - Avvio del gioco con selezione
 * difficoltà - Visualizzazione classifiche
 *
 */
public class AppViewController implements Initializable {
    @FXML
    private StackPane root;
    @FXML
    private VBox schermataDiLogin;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button accediButton;
    @FXML
    private Button passaARegistratiButton;
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
    private Button newDocument;
    @FXML
    private Button stopwordList;
    @FXML
    private VBox schermataClassifiche;
    @FXML
    private TableView<Classifica> facileTable;
    @FXML
    private TableView<Classifica> mediaTable;
    @FXML
    private TableView<Classifica> difficileTable;
    @FXML
    private Button chiudiClassificheButton;
    @FXML
    private VBox schermataSelezioneDifficoltà;
    @FXML
    private Button selezionaFacileButton;
    @FXML
    private Button selezionaMedioButton;
    @FXML
    private Button selezionaDifficileButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private TextField username;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField email;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField password;
    @FXML
    private Label repeatLabel;
    @FXML
    private TextField repeatPassword;
    @FXML
    private Button registerButton;
    @FXML
    private Button registerPageLogin;
    @FXML
    private ImageView imageView;
    @FXML
    private HBox schermataDiRegistrazione;    
    @FXML
    private TableColumn<Classifica, Integer> facilePosizione;
    @FXML
    private TableColumn<Classifica, String> facileNome;
    @FXML
    private TableColumn<Classifica, Float> facilePunteggio;
    @FXML
    private TableColumn<Classifica, Integer> mediaPosizione;
    @FXML
    private TableColumn<Classifica, String> mediaNome;
    @FXML
    private TableColumn<Classifica, Float> mediaPunteggio;
    @FXML
    private TableColumn<Classifica, Integer> difficilePosizione;
    @FXML
    private TableColumn<Classifica, String> difficileNome;
    @FXML
    private TableColumn<Classifica, Float> difficilePunteggio;
    @FXML
    private VBox schermataInfoUtente;
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
    private TableView<Classifica> cronologiaPartite;
    @FXML
    private TableColumn<Classifica, Timestamp> dataCronologiaPartite;
    @FXML
    private TableColumn<Classifica, String> difficoltàCronologiaPartite;
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
    @FXML
    private Button tornaAllaHomepage;
    @FXML
    private Button infoProfiloButton;
    @FXML
    private TextField pathTextField;
    @FXML
    private Button browseButton;
    @FXML
    private Button saveButton;
    @FXML
    private VBox schermataDocumentiAdmin;
    @FXML
    private RadioButton adminRadioFacile;
    @FXML
    private RadioButton adminRadioMedio;
    @FXML
    private RadioButton adminRadioDifficile;
    @FXML
    private VBox schermataStopwords;
    @FXML
    private TextField stopwordTextField;
    @FXML
    private Button stopwordBrowse;
    @FXML
    private Button stopwordSave;
    @FXML
    private Button backHomeButton1;
    @FXML
    private VBox gestioneDocumentiView;
    @FXML
    private TableView<DocumentoDiTesto> gestDocTabella;
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
    private TableColumn<DocumentoDiTesto, LivelloPartita> gestDocColDifficoltà;
    @FXML
    private TableColumn<DocumentoDiTesto, String> gestDocColInserito;
    @FXML
    private TableColumn<DocumentoDiTesto, Lingua> gestDocColLingua;
    @FXML
    private TableColumn<DocumentoDiTesto, String> gestDocColDocumento;
    @FXML
    private TableColumn<DocumentoDiTesto, String> gestDocColNomeFile;
    @FXML
    private ToggleGroup lingua;
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
    private Button gestDocButtonTornaAllaHome;
    @FXML
    private Button backToGestDoc;
    @FXML
    private AnchorPane loadingOverlay;
    
    // ========== ATTRIBUTI PRIVATI ==========
    
    /** @brief Istanza singleton del database per autenticazione */
    private final DatabaseRegistrazioneLogin db = DatabaseRegistrazioneLogin.getInstance();
    private final DatabaseClassifica sbc = DatabaseClassifica.getInstance();
    private final AppState appstate = AppState.getInstance();
    private ObservableList<Classifica> classificaFacile;
    private ObservableList<Classifica> classificaMedia;
    private ObservableList<Classifica> classificaDifficile;
    private ObservableList<Classifica> listaCronologiaPartite;
    private ObservableList<DocumentoDiTesto> listaDocumenti;
    private Task<Void> currentLoadingTask;
    private File fileSelezionato;
    @FXML
    private MenuItem eliminaDocItem;
 
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        if(appstate.isSessionViewHomeButton()){
            tornaAllaHome();
            this.configuraPulsantiAdmin();
            this.benvenutoLabel.setText("Benvenuto "+ appstate.getUtente().getUsername());
            this.fotoProfilo.setImage(this.getImageFromByte(this.appstate.getUtente().getFotoProfilo()));
            this.pulisciTutto();
            this.chiudiTutto();
        }
        
        if(appstate.isSessionViewContinuaButton()){
            startOnAction(new ActionEvent());
        }
        chiudiTutto();
        schermataDiLogin.setVisible(true);
        ritagliaQuadrato(imageView, 250);
        ritagliaQuadrato(immagineInfoUtente,250);
        ritagliaCerchio(fotoProfilo, 40);
        initializeClassifiche();
        initializeInfoUtente();
        initializeGestioneDocumenti();
     

    }

    /**
     * @brief Gestisce il processo di login dell'utente
     *
     * Valida i dati inseriti dall'utente e procede con l'autenticazione tramite
     * il database. In caso di successo, reindirizza alla home.
     *
     * @param event Evento generato dal click sul pulsante di login
     *
     * @pre I campi email e password devono contenere dati validi
     * @post In caso di successo, l'utente viene autenticato e reindirizzato
     * alla home
     *
     * @details Validazioni eseguite: - Verifica che i campi non siano vuoti -
     * Controllo formato email valido - Verifica credenziali tramite database
     *
     * @see mostraAlert()
     * @see isValidEmail()
     * @see DatabaseRegistrazioneLogin#verificaPassword()
     */
    @FXML
    private void accediOnAction(ActionEvent event) {
        
        if (accediButton.isDisabled()) return;      // Blocca eventuali click multipli sul tasto prima che venga disabilitato
        
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            mostraAlert("Errore", "I campi non possono essere vuoti", Alert.AlertType.WARNING);
            return;
        }

        if (!isValidEmail(email)) {
            mostraAlert("Errore", "Inserisci un indirizzo email valido.", Alert.AlertType.WARNING);
            return;
        }

        // UI feedback
        accediButton.setDisable(true);
        passaARegistratiButton.setDisable(true);
        loadingOverlay.setVisible(true);

        // Task che esegue il login in background
        Task<Utente> loginTask = new Task<>() {
            @Override
            protected Utente call() throws Exception {
                if (!db.verificaPassword(email, password)) {
                    return null;
                }
                return db.prendiUtente(email); // anche questa operazione può essere lenta
            }
        };

        loginTask.setOnSucceeded(workerStateEvent -> {
            accediButton.setDisable(false);
            passaARegistratiButton.setDisable(false);
            
            Utente utente = loginTask.getValue();
            if (utente != null) {
                appstate.setUtente(utente);
                configuraPulsantiAdmin();
                benvenutoLabel.setText("Benvenuto " + utente.getUsername());
                fotoProfilo.setImage(getImageFromByte(utente.getFotoProfilo()));
                pulisciTutto();
                chiudiTutto();
                schermataHome.setVisible(true);

            } else {
                mostraAlert("Errore", "Email o password errati", Alert.AlertType.WARNING);
            }
            loadingOverlay.setVisible(false);
        });

        loginTask.setOnFailed(workerStateEvent -> {
            accediButton.setDisable(false);
            passaARegistratiButton.setDisable(false);
            loadingOverlay.setVisible(false);

            Throwable e = loginTask.getException();
            mostraAlert("Errore", "Si è verificato un errore durante il login. Riprova più tardi.", Alert.AlertType.ERROR);
        });

        new Thread(loginTask).start();
    }


    /**
     * @brief Reindirizza l'utente alla schermata di registrazione
     *
     * @param event Evento generato dal click sul pulsante di registrazione
     *
     * @post La schermata di registrazione viene visualizzata
     */
    @FXML
    private void passaARegistratiOnAction(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataDiRegistrazione.setVisible(true);

    }

    /**
     * @brief Avvia il processo di selezione difficoltà per iniziare il gioco
     *
     * @param event Evento generato dal click sul pulsante Start
     *
     * @post Viene visualizzata la schermata di selezione difficoltà
     */
    @FXML
    private void startOnAction(ActionEvent event) {

        chiudiTutto();

        schermataSelezioneDifficoltà.setVisible(true);
    }
    
    

     /**
     * @brief Mostra la schermata delle classifiche 
     * 
     * @param event Evento generato dal click sul pulsante Classifiche
     *
     * @post Viene visualizzata la schermata classifiche con i dati caricati
     */
    @FXML
    private void classificheOnAction(ActionEvent event) {
        if(classificheButton.isDisable()) return;
        classificheButton.setDisable(true);
        // Interrompi eventuale caricamento in corso
        if (currentLoadingTask != null && currentLoadingTask.isRunning()) {
            currentLoadingTask.cancel();
        }

        chiudiTutto();
        schermataClassifiche.setVisible(true);

        currentLoadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (isCancelled()) {
                    return null;
                }

                List<Classifica> facile = sbc.prendiClassifica(LivelloPartita.FACILE);
                if (isCancelled()) {
                    return null;
                }

                List<Classifica> medio = sbc.prendiClassifica(LivelloPartita.MEDIO);
                if (isCancelled()) {
                    return null;
                }

                List<Classifica> difficile = sbc.prendiClassifica(LivelloPartita.DIFFICILE);
                if (isCancelled()) {
                    return null;
                }

                Platform.runLater(() -> {
                    classificaFacile.setAll(facile);
                    classificaMedia.setAll(medio);
                    classificaDifficile.setAll(difficile);
                });

                return null;
            }
        };
        currentLoadingTask.setOnSucceeded(e-> classificheButton.setDisable(false));
        currentLoadingTask.setOnCancelled(e-> classificheButton.setDisable(false));
        
        currentLoadingTask.setOnFailed(e -> {
            Throwable exception = currentLoadingTask.getException();
            Platform.runLater(()
                    -> mostraAlert("Errore", "Caricamento fallito: " + exception.getMessage(), Alert.AlertType.ERROR));
            exception.printStackTrace(); // Log to console
            classificheButton.setDisable(false);
        });

        new Thread(currentLoadingTask).start();
    }

    /**
     * @brief Toggle della visibilità del menu dashboard utente
     *
     * @param event Evento del mouse
     *
     * @post Il menu dashboard cambia stato di visibilità
     */
    @FXML
    private void toggleDashboard(MouseEvent event) {
        dashboardMenu.setVisible(!dashboardMenu.isVisible());
    }

    @FXML
    private void logout(ActionEvent event) {
        appstate.setUtente(null);
        chiudiTutto();
        pulisciTutto();
        fotoProfilo.setImage(getPlaceholderImage());
        schermataDiLogin.setVisible(true);
    }

    @FXML
    private void chiudiClassificheOnAction(ActionEvent event) {
        if (currentLoadingTask != null && currentLoadingTask.isRunning()) {
            currentLoadingTask.cancel();
        }
        chiudiTutto();
        schermataHome.setVisible(true);
    }

    @FXML
    private void selezionaFacileButtonOnAction(ActionEvent event) {
        // Imposta la difficoltà su facile e inizia il gioco
        schermataSelezioneDifficoltà.setVisible(false);
        // TODO: Implementare l'avvio del gioco con difficoltà facile
    }

    @FXML
    private void selezionaMedioButtonOnAction(ActionEvent event) {
        // Imposta la difficoltà su medio e inizia il gioco
        schermataSelezioneDifficoltà.setVisible(false);
        // TODO: Implementare l'avvio del gioco con difficoltà media
    }

    @FXML
    private void selezionaDifficileButtonOnAction(ActionEvent event) {
        // Imposta la difficoltà su difficile e inizia il gioco
        schermataSelezioneDifficoltà.setVisible(false);
        // TODO: Implementare l'avvio del gioco con difficoltà difficile
    }

    /**
     * @brief Valida il formato di un indirizzo email
     *
     * Utilizza una regex per verificare che l'email abbia un formato valido
     * secondo gli standard comuni.
     *
     * @param email Stringa contenente l'email da validare
     * @return true se l'email ha un formato valido, false altrimenti
     *
     * @details Pattern regex utilizzato: - Caratteri alfanumerici, +, _, ., -
     * prima della @ - Dominio con caratteri alfanumerici, . e - - Estensione di
     * almeno 2 caratteri
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * @brief Mostra una finestra di alert all'utente
     *
     * Crea e visualizza una finestra di JavaFX con il messaggio specificato. Il
     * tipo di alert determina l'icona e il colore del dialog.
     *
     * @param titolo Titolo della finestra di alert
     * @param messaggio Testo del messaggio da mostrare
     * @param tipo Tipo di alert (INFORMATION, WARNING, ERROR, CONFIRMATION)
     *
     * @post Viene mostrato un dialog modale che blocca l'interazione fino alla
     * chiusura
     */
    private void mostraAlert(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }


    @FXML
    private void registerButtonOnAction(ActionEvent event) {
        if  (username.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || repeatPassword.getText().isEmpty()) {
            mostraAlert("Errore", "Tutti i campi sono obbligatori.", Alert.AlertType.WARNING);
            return;
        }
        if (!isValidEmail(email.getText())) {
            mostraAlert("Email non valida", "L'email inserita non è valida.", Alert.AlertType.ERROR);
            return;
        }
        if (db.prendiUtente(email.getText()) != null) {
            mostraAlert("Email già utilizzata", "Questa email è già associata a un altro account.", Alert.AlertType.ERROR);
            return;
        }
        if (!password.getText().equals(repeatPassword.getText())) {
            mostraAlert("Password non corrispondenti", "Le due password inserite non corrispondono", Alert.AlertType.ERROR);
            return;
        }

        byte[] immagineBytes = null;
        Image immagine = imageView.getImage();
        if (immagine != null && immagine.getUrl() != null && !immagine.getUrl().contains("person.png")) {
            try {
                String url = immagine.getUrl().replace("file:", "");
                immagineBytes = Files.readAllBytes(new File(url).toPath());
            } catch (IOException e) {
                mostraAlert("Errore", "Errore nella lettura dell'immagine", Alert.AlertType.ERROR);
                return;
            }
        }

        try {
            db.inserisciUtente(username.getText(), email.getText(), password.getText(), immagineBytes);
        } catch (Exception e) {
            mostraAlert("Errore", "Errore durante la registrazione: " + e.getMessage(), Alert.AlertType.ERROR);
            return;
        }

        // Crea l'utente una sola volta, a seconda che ci sia immagine o meno
        Giocatore nuovoUtente;
        if (immagineBytes == null) {
            nuovoUtente = new Giocatore(username.getText(), email.getText(), TipoUtente.giocatore);
            fotoProfilo.setImage(getPlaceholderImage());
        } else {
            nuovoUtente = new Giocatore(username.getText(), email.getText(), immagineBytes, TipoUtente.giocatore);
            fotoProfilo.setImage(new Image(new ByteArrayInputStream(immagineBytes)));
        }
        appstate.setUtente(nuovoUtente);

        aggiornaFotoProfilo(immagineBytes);
        configuraPulsantiAdmin();
        
        chiudiTutto();
        schermataHome.setVisible(true);
        benvenutoLabel.setText("Benvenuto " + username.getText());
        pulisciTutto();
        mostraAlert("Registrazione completata", "Registrazione avvenuta con successo!", Alert.AlertType.INFORMATION);
    }



    @FXML
    private void caricaIMG() {
        scegliImmagine(imageView);
    }

    private Image getImageFromByte(byte[] img) {
        if (img == null) {
            return getPlaceholderImage(); // fallback se l'immagine è nulla
        }
        return new Image(new ByteArrayInputStream(img));
    }

    private void chiudiTutto() {
        schermataDiRegistrazione.setVisible(false);
        schermataDiLogin.setVisible(false);
        schermataHome.setVisible(false);
        schermataClassifiche.setVisible(false);
        schermataSelezioneDifficoltà.setVisible(false);
        dashboardMenu.setVisible(false);
        schermataInfoUtente.setVisible(false);
        schermataDocumentiAdmin.setVisible(false);
        schermataStopwords.setVisible(false);
        schermataDocumentiAdmin.setVisible(false);
        gestioneDocumentiView.setVisible(false);
    }

    private void pulisciTutto() {
        emailTextField.textProperty().set("");
        passwordTextField.textProperty().set("");
        username.textProperty().set("");
        email.textProperty().set("");
        password.textProperty().set("");
        repeatPassword.textProperty().set("");
        usernameInfoUtente.textProperty().set("");
        emailInfoUtente.setText("");
        n_difficile.setText("");
        n_facile.setText("");
        n_medio.setText("");
        dataInizio.getEditor().clear();
        dataFine.getEditor().clear();
        dataFine.setValue(null);
        dataInizio.setValue(null);
        immagineInfoUtente.setImage(new Image(getClass().getResource("/imgs/Profile_avatar_placeholder_large.png").toExternalForm()));
        imageView.setImage(new Image(getClass().getResource("/imgs/Profile_avatar_placeholder_large.png").toExternalForm()));
        gestDocBarraDiRicerca.setText("");
    }

    private Image getPlaceholderImage() {
        return new Image(getClass().getResource("/imgs/person.png").toExternalForm());
    }

    @FXML
    private void passaALogin(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataDiLogin.setVisible(true);
    }

    private void ritagliaQuadrato(ImageView imageView, double dimensione) {
        imageView.setFitWidth(dimensione);
        imageView.setFitHeight(dimensione);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(dimensione, dimensione);
        imageView.setClip(clip);

        // Centrare l'immagine ritagliando l'eccesso con viewport
        imageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                double imgWidth = newImg.getWidth();
                double imgHeight = newImg.getHeight();

                double viewportSize = Math.min(imgWidth, imgHeight); // lato minore per ritaglio quadrato
                double xOffset = (imgWidth - viewportSize) / 2;
                double yOffset = (imgHeight - viewportSize) / 2;

                imageView.setViewport(new javafx.geometry.Rectangle2D(xOffset, yOffset, viewportSize, viewportSize));
            }
        });
    }

    private void ritagliaCerchio(ImageView imageView, double dimensione) {
        imageView.setFitWidth(dimensione);
        imageView.setFitHeight(dimensione);
        imageView.setPreserveRatio(true);

        // Clip circolare centrata
        Circle clip = new Circle(dimensione / 2, dimensione / 2, dimensione / 2);
        imageView.setClip(clip);

        imageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                double imgWidth = newImg.getWidth();
                double imgHeight = newImg.getHeight();

                double viewportSize = Math.min(imgWidth, imgHeight); // ritaglio centrato
                double xOffset = (imgWidth - viewportSize) / 2;
                double yOffset = (imgHeight - viewportSize) / 2;

                imageView.setViewport(new javafx.geometry.Rectangle2D(xOffset, yOffset, viewportSize, viewportSize));
            }
        });
    }

    private void initializeClassifiche() {
        // Inizializza le tabelle
        classificaFacile = FXCollections.observableArrayList();
        classificaMedia = FXCollections.observableArrayList();
        classificaDifficile = FXCollections.observableArrayList();
        listaCronologiaPartite = FXCollections.observableArrayList();
        setupTable(facileTable, facilePosizione, facileNome, facilePunteggio, classificaFacile);
        setupTable(mediaTable, mediaPosizione, mediaNome, mediaPunteggio, classificaMedia);
        setupTable(difficileTable, difficilePosizione, difficileNome, difficilePunteggio, classificaDifficile);
    }

    //Informazioni reperite da: https://stackoverflow.com/questions/33353014/creating-a-row-index-column-in-javafx
    private void setupTable(TableView<Classifica> table, TableColumn<Classifica, Integer> posizioneCol, TableColumn<Classifica, String> nomeCol, TableColumn<Classifica, Float> punteggioCol, ObservableList<Classifica> data) {
        // Configura le colonne
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        punteggioCol.setCellValueFactory(new PropertyValueFactory<>("punti"));

        // Configura colonna posizione con emoji
        posizioneCol.setCellFactory(col -> new TableCell<Classifica, Integer>() {
            @Override
            protected void updateItem(Integer posizione, boolean empty) {
                super.updateItem(posizione, empty);

                if (empty || posizione == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    switch (posizione) {
                        case 1:
                            setText("🥇");
                            break;
                        case 2:
                            setText("🥈");
                            break;
                        case 3:
                            setText("🥉");
                            break;
                        default:
                            setText(posizione.toString());
                    }
                }
            }
        });

        posizioneCol.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyIntegerWrapper(index).asObject();
        });

        // Imposta i dati e abilita ordinamento
        table.setItems(data);
        punteggioCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getSortOrder().add(punteggioCol);
        table.sort();

    }

    /**
     * @brief Configura la visibilità dei pulsanti amministratore
     *
     * Mostra i pulsanti newDocument e stopwordList solo se l'utente corrente è
     * di tipo amministratore.
     *
     * @pre L'oggetto utente deve essere inizializzato
     * @post I pulsanti amministratore sono visibili solo agli admin
     */
    private void configuraPulsantiAdmin() {
        Utente utente = appstate.getUtente();
        if (utente!= null && utente.getTipo() == TipoUtente.amministratore) {
            newDocument.setVisible(true);
            newDocument.setManaged(true);
            stopwordList.setVisible(true);
            stopwordList.setManaged(true);
        } else {
            newDocument.setVisible(false);
            newDocument.setManaged(false);
            stopwordList.setVisible(false);
            stopwordList.setManaged(false);
        }
    }
    
    @FXML
    private void mostraGestioneDocumenti() {
        chiudiTutto();
        gestioneDocumentiView.setVisible(true);

        Object utente = appstate.getUtente();

        try {
            if (utente instanceof Amministratore amministratore) {
                listaDocumenti.clear(); // Buona pratica: evita duplicati
                listaDocumenti.addAll(amministratore.prendiTuttiIDocumenti());

                if (listaDocumenti.isEmpty()) {
                    mostraAlert("Attenzione", "Nessun documento trovato.", Alert.AlertType.INFORMATION);
                }
            } else {
                mostraAlert("Errore", "Accesso negato: solo un amministratore può visualizzare i documenti.", Alert.AlertType.ERROR);
            }
        } catch (Exception e) {
            mostraAlert("Errore", "Impossibile caricare la lista documenti: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    
    @FXML
    private void mostraGestioneStopwords() {
        chiudiTutto();
        schermataStopwords.setVisible(true);
    }
    
     @FXML
    private void tornaAllaHome() {
        chiudiTutto();
        schermataHome.setVisible(true);
    }

    private String getDifficoltaSelezionataAdmin() {
        if (adminRadioFacile.isSelected()) return LivelloPartita.FACILE.getDbValue();
        if (adminRadioMedio.isSelected()) return LivelloPartita.MEDIO.getDbValue();
        if (adminRadioDifficile.isSelected()) return LivelloPartita.DIFFICILE.getDbValue();
        
        
        return null;
    }
    
    private Lingua getLinguaSelezionataAdmin(){
        if(adminRadioIT.isSelected()) return Lingua.ITALIANO;
        if(adminRadioDE.isSelected()) return Lingua.TEDESCO;
        if(adminRadioES.isSelected()) return Lingua.SPAGNOLO;
        if(adminRadioEN.isSelected()) return Lingua.INGLESE;
        if(adminRadioFR.isSelected()) return Lingua.FRANCESE;
        return null;
    }
    
    @FXML
    private void scegliFileTesto(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di testo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("File di testo", "*.txt")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            fileSelezionato = file;
            pathTextField.setText(file.getName());
        } else {
            mostraAlert("Attenzione", "Nessun file selezionato.", Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void scegliFileStopwords(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("File di testo", "*.txt")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            fileSelezionato = file;
            stopwordTextField.setText(file.getName());
        } else {
            mostraAlert("Attenzione", "Nessun file selezionato.", Alert.AlertType.WARNING);
        }
    }
    
    @FXML
    private void caricaTesto(ActionEvent event) {
        if (fileSelezionato == null || getDifficoltaSelezionataAdmin() == null || getLinguaSelezionataAdmin() == null) {
            mostraAlert("Errore", "Completa tutti i campi prima di salvare.", Alert.AlertType.WARNING);
            return;
        }
        Utente utente = appstate.getUtente();
        if (!(utente instanceof Amministratore amministratore)) {
            mostraAlert("Errore", "Solo un amministratore può caricare testi.", Alert.AlertType.ERROR);
            return;
        }
        try {
            amministratore.caricaTesto(
                fileSelezionato.getName(),
                getDifficoltaSelezionataAdmin(),
                fileSelezionato.getAbsolutePath(),
                getLinguaSelezionataAdmin()
            );
            mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
            pathTextField.clear();
            fileSelezionato = null;
        } catch (Exception e) {
            mostraAlert("Errore", "Errore durante il caricamento del documento: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void caricaStopword(ActionEvent event) {
        if (fileSelezionato == null || getDifficoltaSelezionataAdmin() == null) {
            mostraAlert("Errore", "Completa tutti i campi prima di salvare.", Alert.AlertType.WARNING);
            return;
        }
        String nomeFile = fileSelezionato.getName();
        String path = fileSelezionato.getAbsolutePath();
        Object utente = appstate.getUtente();
        if (!(utente instanceof Amministratore amministratore)) {
            mostraAlert("Errore", "Solo un amministratore può caricare stopwords.", Alert.AlertType.ERROR);
            return;
        }
        try {
            amministratore.CaricareStopwords(nomeFile, path);
            mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
            pathTextField.clear();
            fileSelezionato = null;
        } catch (Exception e) {
            mostraAlert("Errore", "Errore durante il caricamento delle stopwords: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    
    @FXML
    private void passaAInfoProfilo(ActionEvent event) {
        Utente utente = appstate.getUtente();
        if (utente == null) {
            mostraAlert("Errore", "Utente non autenticato.", Alert.AlertType.ERROR); 
            return;
        }

        if (currentLoadingTask != null && currentLoadingTask.isRunning()) {
            currentLoadingTask.cancel();
        }

        chiudiTutto();
        schermataInfoUtente.setVisible(true);

        // Dati dell’utente: possono essere mostrati subito
        usernameInfoUtente.setText("Username: " + utente.getUsername());
        emailInfoUtente.setText("E-mail: " + utente.getEmail());

        // Task per dati lenti da DB
        currentLoadingTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    List<Classifica> cronologiaPartiteList = sbc.recuperaCronologiaPartite(utente.getEmail());
                    int facileCount = sbc.recuperaNumeroPartite(utente.getEmail(), LivelloPartita.FACILE.getDbValue());
                    int medioCount = sbc.recuperaNumeroPartite(utente.getEmail(), LivelloPartita.MEDIO.getDbValue());
                    int difficileCount = sbc.recuperaNumeroPartite(utente.getEmail(), LivelloPartita.DIFFICILE.getDbValue());
                    float facilePunteggio = sbc.recuperaMigliorPunteggio(utente.getEmail(), LivelloPartita.FACILE.getDbValue());
                    float medioPunteggio = sbc.recuperaMigliorPunteggio(utente.getEmail(), LivelloPartita.MEDIO.getDbValue());
                    float difficilePunteggio = sbc.recuperaMigliorPunteggio(utente.getEmail(), LivelloPartita.DIFFICILE.getDbValue());

                    Platform.runLater(() -> {
                        listaCronologiaPartite.setAll(cronologiaPartiteList);
                        n_facile.setText("N° partite difficoltà facile: " + facileCount);
                        n_medio.setText("N° partite difficoltà media: " + medioCount);
                        n_difficile.setText("N° partite difficoltà difficile: " + difficileCount);
                        migliore_facile.setText(String.format("Miglior punteggio in difficoltà facile: %.1f", facilePunteggio));
                        migliore_medio.setText(String.format("Miglior punteggio in difficoltà media: %.1f", medioPunteggio));
                        migliore_difficile.setText(String.format("Miglior punteggio in difficoltà difficile: %.1f", difficilePunteggio));
                    });

                } catch (Exception e) {
                    Platform.runLater(() -> mostraAlert("Errore", "Impossibile caricare i dati del profilo: " + e.getMessage(), Alert.AlertType.ERROR)); 
                }
                return null;
            }
        };

        currentLoadingTask.setOnFailed(e -> {
            Logger.getLogger(AppViewController.class.getName()).log(Level.SEVERE, "Errore nel caricamento info profilo", currentLoadingTask.getException());
            Platform.runLater(() ->
                mostraAlert("Errore", "Impossibile caricare i dati del profilo", Alert.AlertType.ERROR)); 
        });

        new Thread(currentLoadingTask).start();

        // Task separato per immagine
        Task<Image> immagineTask = new Task<>() {
            @Override
            protected Image call() throws Exception {
                return getImageFromByte(utente.getFotoProfilo());
            }
        };

        immagineTask.setOnSucceeded(e -> immagineInfoUtente.setImage(immagineTask.getValue()));

        immagineTask.setOnFailed(e -> {
            Logger.getLogger(AppViewController.class.getName()).log(Level.SEVERE, "Errore caricamento immagine profilo", immagineTask.getException());
        });

        new Thread(immagineTask).start();
    }

    

    @SuppressWarnings("unchecked")
    private void initializeCronologiaPartite() {
        Utente utente = appstate.getUtente();
        dataCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("data"));
        difficoltàCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        punteggioCronologiaPartite.setCellValueFactory(new PropertyValueFactory("punti"));
        initializeCronologiaFiltro();
    }

    
    
    @FXML
    private void chiudiInfoUtente(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataHome.setVisible(true);
    }

    @FXML
    private void cambiaIMG(MouseEvent event) {
        byte[] nuovaImmagine = scegliImmagine(immagineInfoUtente);
        if(nuovaImmagine!=null)
        aggiornaFotoProfilo(nuovaImmagine);
    }

    private byte[] scegliImmagine(ImageView imgv) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
    
        try {
            File file = fileChooser.showOpenDialog(null);
            if (file == null) {
                mostraAlert("Attenzione", "Nessun file selezionato.", Alert.AlertType.WARNING); 
                return null;
            }

            if (!validaImmagine(file)) {
                // validaImmagine mostra già alert
                return null;
            }

            byte[] imageBytes = Files.readAllBytes(file.toPath());
            Image image = new Image(file.toURI().toString());
        
            Platform.runLater(() -> {
                imgv.setImage(image);
                mostraAlert("Successo", "Immagine caricata correttamente", Alert.AlertType.INFORMATION); 
            });
        
            return imageBytes;
        } catch (IOException e) {
            Platform.runLater(() ->
                mostraAlert("Errore", "Errore di lettura: " + e.getMessage(), Alert.AlertType.ERROR)); 
            return null;
        } catch (SecurityException e) {
            Platform.runLater(() ->
                mostraAlert("Errore", "Permessi insufficienti per leggere il file", Alert.AlertType.ERROR)); 
            return null;
        } catch (Exception e) {
            Platform.runLater(() ->
                mostraAlert("Errore", "Errore inaspettato: " + e.getMessage(), Alert.AlertType.ERROR)); 
            return null;
        }
    }

    private void initializeInfoUtente() {
        UnaryOperator<TextFormatter.Change> floatFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("-?\\d*(\\.\\d*)?") ? change : null;
        };
        punteggioMinimo.setTextFormatter(new TextFormatter<>(floatFilter));
        punteggioMassimo.setTextFormatter(new TextFormatter<>(floatFilter));
    }

    private void initializeCronologiaFiltro() {
        FilteredList<Classifica> filteredList = new FilteredList<>(listaCronologiaPartite, p -> true);
        cronologiaPartite.setItems(filteredList);

        Runnable filtro = () -> {
            filteredList.setPredicate(classifica -> {
                String livelloDb = classifica.getDifficolta() != null ? classifica.getDifficolta().getDbValue() : "";

                boolean matchDifficolta =
                    (checkFacile.isSelected() && "facile".equalsIgnoreCase(livelloDb)) ||
                    (checkMedio.isSelected() && "medio".equalsIgnoreCase(livelloDb)) ||
                    (checkDifficile.isSelected() && "difficile".equalsIgnoreCase(livelloDb));

                if (!checkFacile.isSelected() && !checkMedio.isSelected() && !checkDifficile.isSelected()) {
                    matchDifficolta = true;
                }

                LocalDate data = classifica.getData().toLocalDateTime().toLocalDate();
                LocalDate dataMin = dataInizio.getValue();
                LocalDate dataMax = dataFine.getValue();

                boolean matchData = (dataMin == null || !data.isBefore(dataMin)) &&
                                    (dataMax == null || !data.isAfter(dataMax));

                Float punteggio = classifica.getPunti();
                float min = parseSafeFloat(punteggioMinimo.getText(), Float.MIN_VALUE);
                float max = parseSafeFloat(punteggioMassimo.getText(), Float.MAX_VALUE);
                boolean matchPunteggio = punteggio >= min && punteggio <= max;

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
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private void aggiornaFotoProfilo(byte[] immagineBytes) {
        Utente utente = appstate.getUtente();
        try {
            boolean successo = utente.setFotoProfilo(immagineBytes);

            if (!successo) {
                throw new RuntimeException("Salvataggio nel database fallito");
            }

            Image image = immagineBytes != null ? new Image(new ByteArrayInputStream(immagineBytes)) : getPlaceholderImage();

            Platform.runLater(() -> {
                fotoProfilo.setImage(image);
                immagineInfoUtente.setImage(image);
            });

        } catch (Exception e) {
            Platform.runLater(() -> mostraAlert("Errore", "Impossibile aggiornare l'immagine: " + e.getMessage(), Alert.AlertType.ERROR));

            // Ripristino immagine vecchia solo se c'è un errore
            byte[] vecchiaImmagine = utente.getFotoProfilo();
            Image oldImage = vecchiaImmagine != null ?
                new Image(new ByteArrayInputStream(vecchiaImmagine)) :
                getPlaceholderImage();

            Platform.runLater(() -> {
                fotoProfilo.setImage(oldImage);
                immagineInfoUtente.setImage(oldImage);
            });
        }
    }


    private boolean validaImmagine(File file) {
        try {
            long maxSize = 5 * 1024 * 1024;
            if (file.length() > maxSize) {
                mostraAlert("Errore", "L'immagine è troppo grande (max 5MB)", Alert.AlertType.ERROR); 
                return false;
            }

            Image test = new Image(file.toURI().toString(), 10, 10, true, true);
            if (test.isError()) {
                mostraAlert("Errore", "File immagine non valido", Alert.AlertType.ERROR); 
                return false;
            }

            return true;
        } catch (Exception e) {
            mostraAlert("Errore", "Impossibile leggere il file: " + e.getMessage(), Alert.AlertType.ERROR); 
            return false;
        }
    }

    @FXML
    private void gestDocNuovoButtonOnAction(ActionEvent event) {
        chiudiTutto();
        schermataDocumentiAdmin.setVisible(true);
    }

    private void inizializzaTabellaDocumenti() {
        listaDocumenti = FXCollections.observableArrayList();

        gestDocColDifficoltà.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        gestDocColInserito.setCellValueFactory(new PropertyValueFactory<>("emailAmministratore"));
        gestDocColNomeFile.setCellValueFactory(new PropertyValueFactory<>("nomeFile"));
        gestDocColLingua.setCellValueFactory(new PropertyValueFactory<>("lingua"));

        gestDocColDocumento.setCellFactory(col -> new TableCell<DocumentoDiTesto, String>() {
            private final Button btnDownload = new Button("🗎");

            {
                btnDownload.setStyle("-fx-background-color: transparent; -fx-cursor: hand; -fx-font-size: 16;");
                btnDownload.setOnAction(event -> {
                    DocumentoDiTesto doc = getTableView().getItems().get(getIndex());
                    if (doc == null) return;

                    Object utente = appstate.getUtente();
                    if (!(utente instanceof Amministratore amministratore)) {
                        mostraAlert("Errore", "Operazione non permessa!", Alert.AlertType.ERROR);
                        return;
                    }

                    try {
                        byte[] contenuto = amministratore.prendiTesto(doc.getNomeFile());
                        if (contenuto == null || contenuto.length == 0) {
                            mostraAlert("Attenzione", "File vuoto o non trovato", Alert.AlertType.WARNING);
                            return;
                        }

                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Salva documento");
                        fileChooser.setInitialFileName(doc.getNomeFile());
                        File fileDest = fileChooser.showSaveDialog(getTableView().getScene().getWindow());

                        if (fileDest != null) {
                            Files.write(fileDest.toPath(), contenuto);
                            mostraAlert("Successo", "File salvato in:\n" + fileDest.getAbsolutePath(), Alert.AlertType.INFORMATION);
                        }
                    } catch (Exception e) {
                        mostraAlert("Errore", "Errore durante il salvataggio: " + e.getMessage(), Alert.AlertType.ERROR);
                    }
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
                }
            }
        });
    }


    private FilteredList<DocumentoDiTesto> creaListaFiltrata() {
        FilteredList<DocumentoDiTesto> filteredList = new FilteredList<>(listaDocumenti, p -> true);
        gestDocTabella.setItems(filteredList);
        return filteredList;
    }

    private Runnable creaFiltro(FilteredList<DocumentoDiTesto> filteredList) {
        return () -> filteredList.setPredicate(doc -> {
            String livelloDb = doc.getDifficolta() != null ? doc.getDifficolta().getDbValue() : "";

            boolean matchDifficolta =
                    (gestDocCheckFacile.isSelected() && "facile".equalsIgnoreCase(livelloDb)) ||
                    (gestDocCheckMedia.isSelected() && "medio".equalsIgnoreCase(livelloDb)) ||
                    (gestDocCheckDifficile.isSelected() && "difficile".equalsIgnoreCase(livelloDb));

            if (!gestDocCheckFacile.isSelected() && !gestDocCheckMedia.isSelected() && !gestDocCheckDifficile.isSelected()) {
                matchDifficolta = true;
            }

            Lingua lingua = doc.lingua();

            boolean matchLingua =
                (gestDocIT.isSelected() && lingua == Lingua.ITALIANO) ||
                (gestDocCheckEN.isSelected() && lingua == Lingua.INGLESE) ||
                (gestDocCheckES.isSelected() && lingua == Lingua.SPAGNOLO) ||
                (gestDocCheckFR.isSelected() && lingua == Lingua.FRANCESE) ||
                (gestDocCheckDE.isSelected() && lingua == Lingua.TEDESCO) ||
                (!gestDocIT.isSelected() && !gestDocCheckEN.isSelected() && !gestDocCheckES.isSelected() &&
                !gestDocCheckFR.isSelected() && !gestDocCheckDE.isSelected());

            String query = gestDocBarraDiRicerca.getText() != null ? gestDocBarraDiRicerca.getText().toLowerCase() : "";
            boolean matchRicerca = query.isBlank()
                || doc.nomeFile().toLowerCase().contains(query)
                || doc.emailAmministratore().toLowerCase().contains(query);

            return matchDifficolta && matchLingua && matchRicerca;
        });
    }

    private void aggiungiListenerFiltri(Runnable filtro) {
        gestDocIT.selectedProperty().addListener((obs, o, n) -> filtro.run());
        gestDocCheckEN.selectedProperty().addListener((obs, o, n) -> filtro.run());
        gestDocCheckES.selectedProperty().addListener((obs, o, n) -> filtro.run());
        gestDocCheckFR.selectedProperty().addListener((obs, o, n) -> filtro.run());
        gestDocCheckDE.selectedProperty().addListener((obs, o, n) -> filtro.run());
        gestDocBarraDiRicerca.textProperty().addListener((obs, oldVal, newVal) -> filtro.run());
        gestDocCheckFacile.selectedProperty().addListener((obs, oldVal, newVal) -> filtro.run());
        gestDocCheckMedia.selectedProperty().addListener((obs, oldVal, newVal) -> filtro.run());
        gestDocCheckDifficile.selectedProperty().addListener((obs, oldVal, newVal) -> filtro.run());
    }

    private void initializeGestioneDocumenti() {
        inizializzaTabellaDocumenti();
        FilteredList<DocumentoDiTesto> filteredList = creaListaFiltrata();
        Runnable filtro = creaFiltro(filteredList);
        aggiungiListenerFiltri(filtro);
        gestDocTabella.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    @FXML
    private void tornaAllaHomeFromGestDoc(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        listaDocumenti.clear();
        schermataHome.setVisible(true);
    }

    @FXML
    private void backToGestDoc(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        Utente u = appstate.getUtente();
        if (u instanceof Amministratore amministratore) {
                listaDocumenti.clear(); // Buona pratica: evita duplicati
                listaDocumenti.addAll(amministratore.prendiTuttiIDocumenti());
        }
        gestioneDocumentiView.setVisible(true);
    }

    @FXML
    private void eliminaDoc(ActionEvent event) {
        List<DocumentoDiTesto> selezionati = new ArrayList<>(gestDocTabella.getSelectionModel().getSelectedItems());

        if (selezionati.isEmpty()) {
            mostraAlert("Attenzione", "Nessun documento selezionato.", Alert.AlertType.INFORMATION);
            return;
        }

        if (!(appstate.getUtente() instanceof Amministratore)) {
            mostraAlert("Errore", "Solo l'amministratore può eliminare documenti.", Alert.AlertType.ERROR);
            return;
        }

        Amministratore a = (Amministratore) appstate.getUtente();

        // Mostra l'overlay prima di iniziare
        loadingOverlay.setVisible(true);

        Task<Void> eliminaTask = new Task<>() {
            @Override
            protected Void call() {
                for (DocumentoDiTesto doc : selezionati) {
                    a.cancellaTesto(doc.getNomeFile()); // operazione di eliminazione lato "server"
                }
                return null;
            }

            @Override
            protected void succeeded() {
                Platform.runLater(() -> {
                    loadingOverlay.setVisible(false);
                    listaDocumenti.removeAll(selezionati);
                    mostraAlert("Successo", "Documenti eliminati con successo.", Alert.AlertType.INFORMATION);
                });
            }

            @Override
            protected void failed() {
                Throwable e = getException();
                Platform.runLater(() -> {
                    loadingOverlay.setVisible(false);
                    mostraAlert("Errore", "Errore durante l'eliminazione: " + e.getMessage(), Alert.AlertType.ERROR);
                });
                e.printStackTrace();
            }

            @Override
            protected void cancelled() {
                Platform.runLater(() -> loadingOverlay.setVisible(false));
            }
        };

        new Thread(eliminaTask).start();
    }

}
