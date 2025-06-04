package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.database.DatabaseRegistrazioneLogin;
import it.unisa.diem.wordageddong17.model.Amministratore;
import it.unisa.diem.wordageddong17.model.Classifica;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
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

    // ========== COMPONENTI FXML ==========
    /**
     * @brief Container principale per tutte le schermate
     */
    @FXML
    private StackPane root;

    // Schermata Login
    /**
     * @brief Container per la schermata di login
     */
    @FXML
    private VBox schermataDiLogin;

    /**
     * @brief Campo di input per l'email nel login
     */
    @FXML
    private TextField emailTextField;

    /**
     * @brief Campo di input per la password nel login
     */
    @FXML
    private TextField passwordTextField;

    /**
     * @brief Pulsante per confermare il login
     */
    @FXML
    private Button accediButton;

    /**
     * @brief Pulsante per passare alla registrazione
     */
    @FXML
    private Button passaARegistratiButton;

    // Schermata Home
    /**
     * @brief Container per la schermata home
     */
    @FXML
    private AnchorPane schermataHome;

    /**
     * @brief Container per il contenuto principale della home
     */
    @FXML
    private VBox contenutoHome;

    /**
     * @brief Pulsante per avviare il gioco
     */
    @FXML
    private Button startButton;

    /**
     * @brief Pulsante per visualizzare le classifiche
     */
    @FXML
    private Button classificheButton;

    /**
     * @brief Container per le informazioni utente in alto a destra
     */
    @FXML
    private HBox quickInfoUtente;

    /**
     * @brief Label di benvenuto con nome utente
     */
    @FXML
    private Label benvenutoLabel;

    /**
     * @brief Immagine del profilo utente (cliccabile)
     */
    @FXML
    private ImageView fotoProfilo;

    /**
     * @brief Menu dropdown del dashboard utente
     */
    @FXML
    private VBox dashboardMenu;

    @FXML
    private Button newDocument;

    @FXML
    private Button stopwordList;

    // Schermata Classifiche
    /**
     * @brief Container per la schermata delle classifiche
     */
    @FXML
    private VBox schermataClassifiche;

    /**
     * @brief Tabella classifiche livello facile
     */
    @FXML
    private TableView<Classifica> facileTable;

    /**
     * @brief Tabella classifiche livello medio
     */
    @FXML
    private TableView<Classifica> mediaTable;

    /**
     * @brief Tabella classifiche livello difficile
     */
    @FXML
    private TableView<Classifica> difficileTable;

    /**
     * @brief Pulsante per chiudere la schermata classifiche
     */
    @FXML
    private Button chiudiClassificheButton;

    // Schermata Selezione Difficoltà
    /**
     * @brief Container per la selezione della difficoltà
     */
    @FXML
    private VBox schermataSelezioneDifficoltà;

    /**
     * @brief Pulsante per selezionare difficoltà facile
     */
    @FXML
    private Button selezionaFacileButton;

    /**
     * @brief Pulsante per selezionare difficoltà media
     */
    @FXML
    private Button selezionaMedioButton;

    /**
     * @brief Pulsante per selezionare difficoltà difficile
     */
    @FXML
    private Button selezionaDifficileButton;

    // Schermata Registrazione
    /**
     * @brief Label per il campo username
     */
    @FXML
    private Label usernameLabel;

    /**
     * @brief Campo di input per l'username nella registrazione
     */
    @FXML
    private TextField username;

    /**
     * @brief Label per il campo email
     */
    @FXML
    private Label emailLabel;

    /**
     * @brief Campo di input per l'email nella registrazione
     */
    @FXML
    private TextField email;

    /**
     * @brief Label per il campo password
     */
    @FXML
    private Label passwordLabel;

    /**
     * @brief Campo di input per la password nella registrazione
     */
    @FXML
    private TextField password;

    /**
     * @brief Label per il campo conferma password
     */
    @FXML
    private Label repeatLabel;

    /**
     * @brief Campo di input per confermare la password
     */
    @FXML
    private TextField repeatPassword;

    /**
     * @brief Pulsante per confermare la registrazione
     */
    @FXML
    private Button registerButton;

    /**
     * @brief Pulsante per tornare al login dalla registrazione
     */
    @FXML
    private Button registerPageLogin;

    /**
     * @brief Immagine del profilo nella registrazione
     */
    @FXML
    private ImageView imageView;

    /**
     * @brief Container per la schermata di registrazione
     */
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
    
    // ========== ATTRIBUTI PRIVATI ==========
    
    /** @brief Istanza singleton del database per autenticazione */
    private final DatabaseRegistrazioneLogin db = DatabaseRegistrazioneLogin.getInstance();
    private final DatabaseClassifica sbc = DatabaseClassifica.getInstance();
    private Utente utente; 
    
    private ObservableList<Classifica> classificaFacile;
    private ObservableList<Classifica> classificaMedia;
    private ObservableList<Classifica> classificaDifficile;
    private ObservableList<Classifica> listaCronologiaPartite;
    private Task<Void> currentLoadingTask;
    
    private Amministratore admin;
    private File fileSelezionato;
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
    private Button backHomeButton;
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
   
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        chiudiTutto();
        schermataDiLogin.setVisible(true);
        ritagliaQuadrato(imageView, 250);  // per la schermata di registrazione
        ritagliaQuadrato(immagineInfoUtente,250);
        ritagliaCerchio(fotoProfilo, 40);
        classificaFacile = FXCollections.observableArrayList();
        classificaMedia = FXCollections.observableArrayList();
        classificaDifficile = FXCollections.observableArrayList();
        listaCronologiaPartite = FXCollections.observableArrayList();
        initializeClassifiche();
        initializeInfoUtente();
     

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
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if  (email.isEmpty() || password.isEmpty()) {
            mostraAlert("Errore\n", "I campi non possono essere vuoti\n", Alert.AlertType.WARNING);
            return;
        }

        if (!isValidEmail(email)) {
            mostraAlert("Errore", "Inserisci un indirizzo email valido.", Alert.AlertType.WARNING);
            return;
        }
  
        try {
            boolean pwCorretta = db.verificaPassword(email, password);
      
            if(pwCorretta){
                utente = db.prendiUtente(email);
                if (utente != null) {
                    aggiornaFotoProfilo(utente.getFotoProfilo());
                    configuraPulsantiAdmin();
                    benvenutoLabel.setText("Benvenuto "+ utente.getUsername());
                    emailTextField.clear();
                    passwordTextField.clear();
                    chiudiTutto();
                    schermataHome.setVisible(true);
                    initializeCronologiaPartite();
                } else {
                    mostraAlert("Errore", "Si è verificato un problema durante il recupero dei dati utente", Alert.AlertType.ERROR);
                }
            } else {
                mostraAlert("Errore", "Email o password errati", Alert.AlertType.WARNING);
            }
        } catch (Exception e) {
            mostraAlert("Errore", "Si è verificato un errore durante il login: " + e.getMessage(), Alert.AlertType.ERROR);
            Logger.getLogger(AppViewController.class.getName()).log(Level.SEVERE, "Errore durante il login", e);
        }
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

        currentLoadingTask.setOnFailed(e -> {
            Throwable exception = currentLoadingTask.getException();
            Platform.runLater(()
                    -> mostraAlert("Errore", "Caricamento fallito: " + exception.getMessage(), Alert.AlertType.ERROR));
            exception.printStackTrace(); // Log to console
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
        utente=null;
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
        if (!isValidEmail(email.getText())) {
            mostraAlert("Email non valida", "L'email inserita non è valida.", Alert.AlertType.ERROR);
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
                // Prendiamo il file selezionato
                String url = immagine.getUrl().replace("file:", "");
                immagineBytes = Files.readAllBytes(new File(url).toPath());
            } catch (IOException e) {
                mostraAlert("Errore", "Errore nella lettura dell'immagine", Alert.AlertType.ERROR);
            }
        }

        db.inserisciUtente(username.getText(), email.getText(), password.getText(), immagineBytes);
        utente = new Utente(username.getText(), email.getText(), immagineBytes, TipoUtente.giocatore);
        aggiornaFotoProfilo(immagineBytes);
        chiudiTutto();
        configuraPulsantiAdmin();
        schermataHome.setVisible(true);
        benvenutoLabel.setText("Benvenuto " + username.getText());

        if (immagineBytes == null) {
            utente = new Utente(username.getText(), email.getText(), TipoUtente.giocatore);
            fotoProfilo.setImage(getPlaceholderImage());
        } else {
            utente = new Utente(username.getText(), email.getText(), immagineBytes, TipoUtente.giocatore);
            fotoProfilo.setImage(new Image(new ByteArrayInputStream(immagineBytes)));
        }

        pulisciTutto();
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
        if (utente != null && utente.getTipo() == TipoUtente.amministratore) {
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
        schermataDocumentiAdmin.setVisible(true);
    }
    private void mostraGestioneStopwords() {
        chiudiTutto();
        schermataStopwords.setVisible(true);
    }
     @FXML
    private void tornaAllaHome() {
        chiudiTutto();
        schermataHome.setVisible(true);
    }

    public String getDifficoltaSelezionataAdmin() {
        if (adminRadioFacile.isSelected()) return "Facile";
        if (adminRadioMedio.isSelected()) return "Medio";
        if (adminRadioDifficile.isSelected()) return "Difficile";
        
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
        }
    }
    
    @FXML
    private void caricaTesto(ActionEvent event) {
        if (fileSelezionato == null || getDifficoltaSelezionataAdmin() == null) {
            mostraAlert("Errore", "Completa tutti i campi prima di salvare.", Alert.AlertType.WARNING);
            return;
        }

        String nomeFile = fileSelezionato.getName();
        String path = fileSelezionato.getAbsolutePath();
        String difficolta = getDifficoltaSelezionataAdmin();

        admin.caricaTesto(nomeFile, difficolta, path);

        mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
        pathTextField.clear();
        fileSelezionato = null;
        
    }
    
    @FXML
    private void caricaStopword(ActionEvent event) {
        if (fileSelezionato == null || getDifficoltaSelezionataAdmin() == null) {
            mostraAlert("Errore", "Completa tutti i campi prima di salvare.", Alert.AlertType.WARNING);
            return;
        }

        String nomeFile = fileSelezionato.getName();
        String path = fileSelezionato.getAbsolutePath();
        

        admin.CaricareStopwords(nomeFile, path);

        mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
        pathTextField.clear();
        fileSelezionato = null;
        
    }
    
    @FXML
    private void passaAInfoProfilo(ActionEvent event) {
        if (currentLoadingTask != null && currentLoadingTask.isRunning()) {
            currentLoadingTask.cancel();
        }
    
        chiudiTutto();
        schermataInfoUtente.setVisible(true);
    
        // Aggiorna subito le info utente (non dipendono dal DB)
        usernameInfoUtente.setText("Username: "+utente.getUsername());
        emailInfoUtente.setText("E-mail: "+utente.getEmail());
    
        currentLoadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (isCancelled()) return null;
            
                // Carica dati in background
                List<Classifica> cronologiaPartiteList = sbc.recuperaCronologiaPartite(utente.getEmail());
                int facileCount = sbc.recuperaNumeroPartite(utente.getEmail(), LivelloPartita.FACILE.getDbValue());
                int medioCount = sbc.recuperaNumeroPartite(utente.getEmail(), LivelloPartita.MEDIO.getDbValue());
                int difficileCount = sbc.recuperaNumeroPartite(utente.getEmail(), LivelloPartita.DIFFICILE.getDbValue());
                float facilePunteggio = sbc.recuperaMigliorPunteggio(utente.getEmail(), LivelloPartita.FACILE.getDbValue());
                float medioPunteggio = sbc.recuperaMigliorPunteggio(utente.getEmail(), LivelloPartita.MEDIO.getDbValue());
                float difficilePunteggio = sbc.recuperaMigliorPunteggio(utente.getEmail(), LivelloPartita.DIFFICILE.getDbValue());

                Platform.runLater(() -> {
                    listaCronologiaPartite.setAll(cronologiaPartiteList);
                    n_facile.setText("N° partite difficoltà facile:"+String.valueOf(facileCount));
                    n_medio.setText("N° partite difficoltà media: "+String.valueOf(medioCount));
                    n_difficile.setText("N° partite difficoltà difficile: "+String.valueOf(difficileCount));
                    migliore_facile.setText(String.format("Miglior punteggio in difficoltà facile: %.1f", facilePunteggio));
                    migliore_medio.setText(String.format("Miglior punteggio in difficoltà media: %.1f", medioPunteggio));
                    migliore_difficile.setText(String.format("Miglior punteggio in difficoltà difficile: %.1f", difficilePunteggio));
                    immagineInfoUtente.setImage(getImageFromByte(utente.getFotoProfilo()));
                });
            
                return null;
            }
        };
    
        currentLoadingTask.setOnFailed(e -> {
            Throwable ex = currentLoadingTask.getException();
            Logger.getLogger(AppViewController.class.getName()).log(Level.SEVERE, "Errore nel caricamento info profilo", ex);
            Platform.runLater(() -> 
                mostraAlert("Errore", "Impossibile caricare i dati del profilo", Alert.AlertType.ERROR));
        });
    
    new Thread(currentLoadingTask).start(); 
}
    

    private void initializeCronologiaPartite() {
        dataCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("data"));
        difficoltàCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        punteggioCronologiaPartite.setCellValueFactory(new PropertyValueFactory("punti"));
        initializeCronologiaFiltro();
        usernameInfoUtente.setText("Username: "+utente.getUsername());
        emailInfoUtente.setText("E-mail: "+utente.getEmail());
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
        aggiornaFotoProfilo(nuovaImmagine);
    }

    private byte[] scegliImmagine(ImageView imgv) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
    
        try {
            File file = fileChooser.showOpenDialog(null);
            if (file == null) return null;

            if (!validaImmagine(file)) return null;

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

        // Listener combinato per filtri
        Runnable filtro = () -> {
            filteredList.setPredicate(classifica -> {
                // Filtro checkbox difficoltà
                String livelloDb = classifica.getDifficolta() != null ? classifica.getDifficolta().getDbValue() : "";

                boolean matchDifficolta =
                    (checkFacile.isSelected() && "facile".equalsIgnoreCase(livelloDb)) ||
                    (checkMedio.isSelected() && "medio".equalsIgnoreCase(livelloDb)) ||
                    (checkDifficile.isSelected() && "difficile".equalsIgnoreCase(livelloDb));

                if (!checkFacile.isSelected() && !checkMedio.isSelected() && !checkDifficile.isSelected()) {
                    matchDifficolta = true; // Se nessuna selezionata, mostra tutto
                }

                // Filtro data
                LocalDate data = classifica.getData().toLocalDateTime().toLocalDate();
                LocalDate dataMin = dataInizio.getValue();
                LocalDate dataMax = dataFine.getValue();

                boolean matchData = (dataMin == null || !data.isBefore(dataMin)) &&
                                    (dataMax == null || !data.isAfter(dataMax));

                // Filtro punteggio
                Float punteggio = classifica.getPunti();
                float min = parseSafeFloat(punteggioMinimo.getText(), Float.MIN_VALUE);
                float max = parseSafeFloat(punteggioMassimo.getText(), Float.MAX_VALUE);
                boolean matchPunteggio = punteggio >= min && punteggio <= max;

                return matchDifficolta && matchData && matchPunteggio;
            });
        };

        // Registra listener su ogni campo
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
        try {
            // Salva prima nel database
            boolean successo = utente.setFotoProfilo(immagineBytes);
        
            if (successo) {
            // Se il DB è ok, aggiorna lo stato locale
                utente.setFotoProfilo(immagineBytes);
                Image image = immagineBytes != null ? new Image(new ByteArrayInputStream(immagineBytes)) : getPlaceholderImage();
            
                Platform.runLater(() -> {
                    fotoProfilo.setImage(image);
                    immagineInfoUtente.setImage(image);
                });
            } else {
                throw new RuntimeException("Salvataggio nel database fallito");
            }
        } catch (Exception e) {
            Platform.runLater(() -> 
                mostraAlert("Errore", "Impossibile aggiornare l'immagine: " + e.getMessage(), Alert.AlertType.ERROR));
        
            // Ripristina l'immagine precedente
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
            // Verifica dimensione massima (es. 5MB)
            long maxSize = 5 * 1024 * 1024; 
            if (file.length() > maxSize) {
                mostraAlert("Errore", "L'immagine è troppo grande (max 5MB)", Alert.AlertType.ERROR);
                return false;
            }
        
            // Verifica che sia un'immagine leggibile
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
    private void mostraGestioneStopwords(ActionEvent event) {
    }
    
}
