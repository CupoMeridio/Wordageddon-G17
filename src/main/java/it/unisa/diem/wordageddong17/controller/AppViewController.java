package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.database.DatabaseRegistrazioneLogin;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

    // ========== ATTRIBUTI PRIVATI ==========
    /**
     * @brief Istanza singleton del database per autenticazione
     */
    private final DatabaseRegistrazioneLogin db = DatabaseRegistrazioneLogin.getInstance();
    private final DatabaseClassifica sbc = DatabaseClassifica.getInstance();
    private Utente utente;
    private byte[] fotoProfiloBytes = null;   // -> vediamo se c'è una soluzione migliore

    private ObservableList<Classifica> classificaFacile;
    private ObservableList<Classifica> classificaMedia;
    private ObservableList<Classifica> classificaDifficile;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ritagliaQuadrato(imageView, 250);  // per la schermata di registrazione
        ritagliaCerchio(fotoProfilo, 40);
        classificaFacile = FXCollections.observableArrayList();
        classificaMedia = FXCollections.observableArrayList();
        classificaDifficile = FXCollections.observableArrayList();

        initializeClassifiche();

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

        if (email.isEmpty() || password.isEmpty()) {
            mostraAlert("Errore\n", "I campi non possono essere vuoti\n", Alert.AlertType.WARNING);
            return;
        }

        if (!isValidEmail(email)) {
            mostraAlert("Errore", "Inserisci un indirizzo email valido.", Alert.AlertType.WARNING);
            return;
        }

        boolean pwCorretta = db.verificaPassword(email, password);

        if (pwCorretta) {
            Utente u = db.prendiUtente(email);
            benvenutoLabel.setText("Benvenuto " + u.getUsername());
            configuraPulsantiAdmin();
            emailTextField.clear();
            passwordTextField.clear();
            chiudiTutto();
            schermataHome.setVisible(true);
        } else {
            mostraAlert("Errore", "Email o password errati", Alert.AlertType.WARNING);
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

    private Task<Void> currentLoadingTask;

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
        // Nasconde tutte le schermate e mostra solo il login
        schermataDiLogin.setVisible(true);
        schermataHome.setVisible(false);
        schermataClassifiche.setVisible(false);
        schermataSelezioneDifficoltà.setVisible(false);
        dashboardMenu.setVisible(false);

        // Reset dei campi di login
        emailTextField.clear();
        passwordTextField.clear();
        
        //Nasconde i pulsanti admin
        newDocument.setVisible(false);
        stopwordList.setVisible(false);
    } // TODO: Implementare il logout 

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
        if (this.isValidEmail(email.getText())) {
            if (password.getText().equals(repeatPassword.getText())) {
                db.inserisciUtente(username.getText(), email.getText(), password.getText(), this.fotoProfiloBytes);
                configuraPulsantiAdmin();
                chiudiTutto();
                schermataHome.setVisible(true);
                benvenutoLabel.setText("Benvenuto" + username.getText());
                if (this.fotoProfiloBytes == null) {
                    utente = new Utente(username.getText(), email.getText(), 0, TipoUtente.giocatore);
                    fotoProfilo.setImage(getPlaceholderImage());
                } else {
                    utente = new Utente(username.getText(), email.getText(), 0, this.fotoProfiloBytes, TipoUtente.giocatore);
                    fotoProfilo.setImage(getImageFromByte(this.fotoProfiloBytes));
                }
                pulisciTutto();
            } else {
                mostraAlert("Password non corrispondenti", "Le due password inserite non corrispondono", Alert.AlertType.ERROR);
            }
        } else {
            mostraAlert("Email non valida", "L'email inserita non è valida.", Alert.AlertType.ERROR);
        }

    }

    @FXML
    private void caricaIMG() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Caricamento in background
            Task<Image> loadImageTask = new Task<>() {
                @Override
                protected Image call() throws Exception {
                    return new Image(file.toURI().toString());
                }
            };

            loadImageTask.setOnSucceeded(event -> {
                Image image = loadImageTask.getValue();
                if (image != null) {
                    imageView.setImage(image);
                    mostraAlert("Successo", "Immagine selezionata correttamente", Alert.AlertType.INFORMATION);
                } else {
                    mostraAlert("Errore", "Errore nel caricamento dell'immagine", Alert.AlertType.ERROR);
                }
            });

            loadImageTask.setOnFailed(event -> {
                mostraAlert("Errore", "Errore nel caricamento dell'immagine", Alert.AlertType.ERROR);
            });

            new Thread(loadImageTask).start();

        } else {
            // Nessun file selezionato: messaggio corretto
            mostraAlert("Info", "Nessuna immagine selezionata", Alert.AlertType.INFORMATION);
        }
    }

    private Image getImageFromByte(byte[] img) {
        ByteArrayInputStream bis = new ByteArrayInputStream(img);
        return (new Image(bis));
    }

    private void chiudiTutto() {
        schermataDiRegistrazione.setVisible(false);
        schermataDiLogin.setVisible(false);
        schermataHome.setVisible(false);
        schermataClassifiche.setVisible(false);
        schermataSelezioneDifficoltà.setVisible(false);
        dashboardMenu.setVisible(false);
    }

    private void pulisciTutto() {
        emailTextField.textProperty().set("");
        passwordTextField.textProperty().set("");
        username.textProperty().set("");
        email.textProperty().set("");
        password.textProperty().set("");
        repeatPassword.textProperty().set("");
    }

    private Image getPlaceholderImage() {
        return new Image(getClass().getResource("/imgs/person.png").toExternalForm());
    }

    @FXML
    private void passaALogin(ActionEvent event) {
        chiudiTutto();
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
            stopwordList.setVisible(true);
        } else {
            newDocument.setVisible(false);
            stopwordList.setVisible(false);
        }
    }

}
