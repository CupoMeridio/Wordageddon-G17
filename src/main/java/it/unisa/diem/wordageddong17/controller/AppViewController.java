package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.App;
import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
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
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
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
import it.unisa.diem.wordageddong17.model.InfoGiocatore;
import it.unisa.diem.wordageddong17.service.CaricaStopWordsService;
import it.unisa.diem.wordageddong17.service.CaricaTestoService;
import it.unisa.diem.wordageddong17.service.EliminaTestoService;
import it.unisa.diem.wordageddong17.service.InserisciUtenteService;
import it.unisa.diem.wordageddong17.service.LoginService;
import it.unisa.diem.wordageddong17.service.ModificaFotoProfiloService;
import it.unisa.diem.wordageddong17.service.PrendiClassificheService;
import it.unisa.diem.wordageddong17.service.PrendiInfoUtenteService;
import it.unisa.diem.wordageddong17.service.PrendiStopWordsService;
import it.unisa.diem.wordageddong17.service.PrendiTestoService;
import it.unisa.diem.wordageddong17.service.PrendiTuttiIDocumentiService;
import it.unisa.diem.wordageddong17.service.PrendiUtenteService;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;


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
    private VBox schermataStopwords;
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
    private Button gestDocButtonTornaAllaHome;
    @FXML
    private AnchorPane loadingOverlay;     
    @FXML
    private MenuItem eliminaDocItem;
    @FXML
    private RadioButton radioFacile;
    @FXML
    private ToggleGroup difficoltà;
    @FXML
    private RadioButton radioMedio;
    @FXML
    private RadioButton radioDifficile;
    @FXML
    private CheckBox checkIT;
    @FXML
    private CheckBox checkEN;
    @FXML
    private CheckBox checkFR;
    @FXML
    private CheckBox checkDE;
    @FXML
    private CheckBox checkES;
    @FXML
    private Button iniziaPartita;
    private VBox gestStopwordsTable;

    
    // ========== ATTRIBUTI PRIVATI ==========
    
    /** @brief Istanza singleton del database per autenticazione */


    private final AppState appstate = AppState.getInstance();
    private ObservableList<Classifica> classificaFacile;
    private ObservableList<Classifica> classificaMedia;
    private ObservableList<Classifica> classificaDifficile;
    private ObservableList<Classifica> listaCronologiaPartite;
    private ObservableList<DocumentoDiTesto> listaDocumenti;
    private File fileSelezionato;
    private final DAOListaStopWords dbSW = DatabaseStopWords.getInstance();
    private PrendiClassificheService pcs = new PrendiClassificheService();
    private PrendiInfoUtenteService pius = new PrendiInfoUtenteService();
    private PrendiUtenteService pus = new PrendiUtenteService();
    private InserisciUtenteService ius = new InserisciUtenteService();
    private LoginService ls = new LoginService();
    private EliminaTestoService ets = new EliminaTestoService();
    private PrendiTuttiIDocumentiService ptds = new PrendiTuttiIDocumentiService();
    private CaricaTestoService cts = new CaricaTestoService();
    private PrendiTestoService pts = new PrendiTestoService();
    private ModificaFotoProfiloService mfps = new ModificaFotoProfiloService();
    @FXML
    private TextArea stopwordTextArea;
    @FXML
    private Button tornaHomeButton;
    @FXML
    private Button salvaStopwordsButton;
    @FXML
    private VBox schermataDocumentiAdmin;
    @FXML
    private TextField pathTextField;
    @FXML
    private RadioButton adminRadioIT;
    @FXML
    private ToggleGroup lingua;
    @FXML
    private RadioButton adminRadioEN;
    @FXML
    private RadioButton adminRadioES;
    @FXML
    private RadioButton adminRadioDE;
    @FXML
    private RadioButton adminRadioFR;
    @FXML
    private RadioButton adminRadioFacile;
    @FXML
    private ToggleGroup inserisciDifficoltà;
    @FXML
    private RadioButton adminRadioMedio;
    @FXML
    private RadioButton adminRadioDifficile;
    @FXML
    private Button browseButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button backToGestDoc;
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        ritagliaQuadrato(imageView, 250);
        ritagliaQuadrato(immagineInfoUtente,250);
        ritagliaCerchio(fotoProfilo, 40);
        initializeClassifiche();
        initializeInfoUtente();
        initializeGestioneDocumenti();
        initializeSchermataDifficoltà();
        chiudiTutto();
        
        if(appstate.isSessionViewHomeButton()){
            tornaAllaHome();
            this.configuraPulsantiAdmin();
            this.benvenutoLabel.setText("Benvenuto "+ appstate.getUtente().getUsername());
            this.fotoProfilo.setImage(this.getImageFromByte(this.appstate.getUtente().getFotoProfilo()));
            this.pulisciTutto();
            
        }
        
        else if(appstate.isSessionViewContinuaButton()){
            startOnAction(new ActionEvent());
        }
        else{
            schermataDiLogin.setVisible(true);
        }
     

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

        this.ls.setEmail(email);
        this.ls.setPassword(password);     

        ls.setOnSucceeded(workerStateEvent -> {
            accediButton.setDisable(false);
            passaARegistratiButton.setDisable(false);
            
            Utente utente = this.ls.getValue();
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
            this.resetService(ls);
        });

        ls.setOnFailed(workerStateEvent -> {
            accediButton.setDisable(false);
            passaARegistratiButton.setDisable(false);
            loadingOverlay.setVisible(false);

            mostraAlert("Errore", "Si è verificato un errore durante il login. Riprova più tardi.", Alert.AlertType.ERROR);
            this.resetService(ls);
        });

        this.ls.start();
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
        
        if(verificaEsistenzaSalvataggio())
            this.mostraAlert("Hai un salvataggio passato", 
                    "Vuoi continuare a giocare col salvataggio?", 
                    Alert.AlertType.CONFIRMATION,
                     e->{try {
                            this.appstate.setSessioneSalvata(e); 
                            System.out.println("this.appstate.setSessioneSalvata(e): "+ this.appstate.isSessioneSalvata());
                            App.setRoot("SessionView");
                        } catch (IOException ex) {
                            this.eliminaSalvataggi();
                            System.getLogger(AppViewController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                        }
                    }
                    ,true);
    }
    
    private boolean verificaEsistenzaSalvataggio(){
        return (new File("SalvataggioDi"+this.appstate.getUtente().getEmail()+".ser").exists()) || 
                (new File("SalvataggioFaseGenerazioneDi"+this.appstate.getUtente().getEmail()+".ser").exists());
    }
    
     private boolean eliminaSalvataggi() {
        boolean f1 = new File("SalvataggioDi"+this.appstate.getUtente().getEmail()+".ser").delete();
        System.out.println("Eliminazione file 1: "+ f1);
        boolean f2 =new File(("SalvataggioFaseGenerazioneDi"+this.appstate.getUtente().getEmail()+".ser")).delete();
        System.out.println("Eliminazione file 1: "+ f2);
        return f1 && f2;
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
        if (this.pcs != null && pcs.isRunning()) {
            pcs.cancel();
        }
        chiudiTutto();
        schermataClassifiche.setVisible(true);
        
        this.pcs.setOnSucceeded(e->{
            classificheButton.setDisable(false);
            Map<LivelloPartita,List<Classifica>> classifiche =new HashMap<LivelloPartita, List<Classifica>>();
            classifiche = pcs.getValue();
            classificaFacile.setAll(classifiche.get(LivelloPartita.FACILE));
            classificaMedia.setAll(classifiche.get(LivelloPartita.MEDIO));
            classificaDifficile.setAll(classifiche.get(LivelloPartita.DIFFICILE));
            this.resetService(pcs);
        });
        
        pcs.setOnCancelled(e->{ 
                classificheButton.setDisable(false);
                this.resetService(pcs);
        });
        
        pcs.setOnFailed(e -> {
            Platform.runLater(()
                    -> mostraAlert("Errore", "Caricamento delle classifiche fallito.", Alert.AlertType.ERROR));
            classificheButton.setDisable(false);
            this.resetService(pcs);
        });

        pcs.start();

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
        if (pcs != null && pcs.isRunning()) {
            pcs.cancel();
        }
        chiudiTutto();
        schermataHome.setVisible(true);
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
    
    private <T> void mostraAlert(String titolo, String messaggio, Alert.AlertType tipo, Consumer<T> azione, T valore) {
        
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        
        if(tipo == Alert.AlertType.CONFIRMATION){
        
            alert.getButtonTypes().setAll(new ButtonType("OK"), new ButtonType("ANNULLA"));
            alert.showAndWait().ifPresent(button -> {
            // Se l'utente conferma, passiamo true al consumer, altrimenti false.
            if (button.getText().equals("OK")) {
                azione.accept(valore);
            }else{
                 this.eliminaSalvataggi();
            }
        });
        }else{
            alert.showAndWait();
        }
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
        this.pus.setEmail(email.getText());
        pus.start();
        
        pus.setOnSucceeded(e->{
            if (pus.getValue() != null) {
            mostraAlert("Email già utilizzata", "Questa email è già associata a un altro account.", Alert.AlertType.ERROR);
            this.resetService(pus);
            return;
        }
        });
        
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
        
        this.ius.setEmail(email.getText());
        this.ius.setUsername(username.getText());
        this.ius.setImmagineBytes(immagineBytes);
        this.ius.setPassword(password.getText());
        
        ius.setOnFailed(e->{
             mostraAlert("Errore", "Errore durante la registrazione. ", Alert.AlertType.ERROR);
             this.resetService(ius);
             return;
        });
        ius.start();

        // Crea l'utente una sola volta, a seconda che ci sia immagine o meno
        Utente nuovoUtente;
        if (immagineBytes == null) {
            nuovoUtente = new Utente(username.getText(), email.getText(), TipoUtente.giocatore);
            fotoProfilo.setImage(getPlaceholderImage());
        } else {
            nuovoUtente = new Utente(username.getText(), email.getText(), immagineBytes, TipoUtente.giocatore);
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
        schermataStopwords.setVisible(false);
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
                ptds.setOnSucceeded(e->{
                listaDocumenti.clear(); // Buona pratica: evita duplicati
                listaDocumenti.addAll(this.ptds.getValue());
                if (listaDocumenti.isEmpty()) {
                    mostraAlert("Attenzione", "Nessun documento trovato.", Alert.AlertType.INFORMATION);
                }
                this.resetService(ptds);
                });
               
                this.ptds.start();
                
                

        } catch (Exception e) {
            mostraAlert("Errore", "Impossibile caricare la lista documenti: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    
    @FXML
    private void mostraGestioneStopwords() {
        chiudiTutto();
        this.mettiStopwordInTextArea();
        schermataStopwords.setVisible(true);
    }
    
    @FXML
    private void tornaAllaHome() {
        chiudiTutto();
        schermataHome.setVisible(true);
    }
    
    @FXML
    private void salvaStopwords(ActionEvent event){
       String testo = this.stopwordTextArea.getText();
       byte[] bytes = testo.getBytes();
       CaricaStopWordsService cs = new CaricaStopWordsService("ListaStopwords", this.appstate.getUtente().getEmail(), bytes);
       
       
       cs.setOnSucceeded(e -> {
           mostraAlert("Avviso", "Stopwords caricate con successo.",  Alert.AlertType.INFORMATION);
           
       });
       
       cs.setOnFailed(e -> {
           mostraAlert("Avviso", "Stopwords non caricate correttamente.",  Alert.AlertType.INFORMATION);
       });
       
       cs.start();
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
    private void caricaTesto(ActionEvent event) {
        if (fileSelezionato == null || getDifficoltaSelezionataAdmin() == null || getLinguaSelezionataAdmin() == null) {
            mostraAlert("Errore", "Completa tutti i campi prima di salvare.", Alert.AlertType.WARNING);
            return;
        }

            this.cts.setDifficoltà(getDifficoltaSelezionataAdmin());
        try {
            this.cts.setDocumento(Files.readAllBytes(fileSelezionato.toPath()));
        } catch (IOException ex) {
            System.getLogger(AppViewController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }

            this.cts.setEmail(appstate.getUtente().getEmail());
            this.cts.setNomeFile(fileSelezionato.getName());
            this.cts.setLingua(getLinguaSelezionataAdmin());
            this.cts.setOnSucceeded(e->{
                mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
                pathTextField.clear();
                fileSelezionato = null;
                this.resetService(cts);
            });

            this.cts.setOnFailed(e->{
                mostraAlert("Errore", "Errore durante il caricamento del documento.", Alert.AlertType.INFORMATION);
                pathTextField.clear();
                fileSelezionato = null;
                this.resetService(cts);
            });
            this.cts.start();
    }
    
    private void mettiStopwordInTextArea(){
        PrendiStopWordsService ps = new PrendiStopWordsService("ListaStopwords");
        
        ps.setOnSucceeded(e -> {
            this.stopwordTextArea.setText(new String(ps.getValue()));
            System.out.println(" this.stopwordTextArea.setText "+  this.stopwordTextArea.getText());
        });
        
        ps.setOnFailed(e -> {
            mostraAlert("Warning", "Non risultano stopwords.", Alert.AlertType.INFORMATION);
            
        });
        
        ps.start();
    }

    
    @FXML
    private void passaAInfoProfilo(ActionEvent event) {
        Utente utente = appstate.getUtente();
        pius.setEmail(utente.getEmail());
        
        if (utente == null) {
            mostraAlert("Errore", "Utente non autenticato.", Alert.AlertType.ERROR); 
            return;
        }

        if (this.pius != null && this.pius.isRunning()) {
            pius.cancel();
        }

        chiudiTutto();
        schermataInfoUtente.setVisible(true);

        // Dati dell’utente: possono essere mostrati subito
        usernameInfoUtente.setText("Username: " + utente.getUsername());
        emailInfoUtente.setText("E-mail: " + utente.getEmail());
            
        pius.setOnSucceeded(e->{
            InfoGiocatore ig = pius.getValue();
            listaCronologiaPartite.setAll(ig.getCronologiaPartiteList());
            n_facile.setText("N° partite difficoltà facile: " + ig.getFacileCount());
            n_medio.setText("N° partite difficoltà media: " + ig.getMedioCount());
            n_difficile.setText("N° partite difficoltà difficile: " + ig.difficileCount());
            migliore_facile.setText(String.format("Miglior punteggio in difficoltà facile: %.1f", ig.getFacilePunteggio()));
            migliore_medio.setText(String.format("Miglior punteggio in difficoltà media: %.1f", ig.getMedioPunteggio()));
            migliore_difficile.setText(String.format("Miglior punteggio in difficoltà difficile: %.1f", ig.getDifficilePunteggio()));
            this.resetService(pius);
        });
           

        pius.setOnFailed(e -> {
            Logger.getLogger(AppViewController.class.getName()).log(Level.SEVERE, "Errore nel caricamento info profilo", pius.getException());
            Platform.runLater(() ->
                mostraAlert("Errore", "Impossibile caricare i dati del profilo", Alert.AlertType.ERROR));
            this.resetService(pius);
        });

        pius.start();

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
        listaCronologiaPartite = FXCollections.observableArrayList();
        UnaryOperator<TextFormatter.Change> floatFilter = change -> {
            String newText = change.getControlNewText();
            return newText.matches("-?\\d*(\\.\\d*)?") ? change : null;
        };
        punteggioMinimo.setTextFormatter(new TextFormatter<>(floatFilter));
        punteggioMassimo.setTextFormatter(new TextFormatter<>(floatFilter));
        initializeCronologiaPartite();
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
        mfps.setEmail(utente.getEmail());
        mfps.setImmagine(immagineBytes);
        this.mfps.setOnSucceeded(e->{   
            Image image = immagineBytes != null ? new Image(new ByteArrayInputStream(immagineBytes)) : getPlaceholderImage();
                utente.setFotoProfilo(immagineBytes);
                fotoProfilo.setImage(image);
                immagineInfoUtente.setImage(image);
                resetService(mfps);
                loadingOverlay.setVisible(false);
            });
            
            this.mfps.setOnFailed(e->{
                mostraAlert("Errore", "Impossibile aggiornare l'immagine: ",Alert.AlertType.ERROR);
                // Ripristino immagine vecchia solo se c'è un errore
                byte[] vecchiaImmagine = utente.getFotoProfilo();
                Image oldImage = vecchiaImmagine != null ?
                new Image(new ByteArrayInputStream(vecchiaImmagine)) :
                getPlaceholderImage();
                fotoProfilo.setImage(oldImage);
                immagineInfoUtente.setImage(oldImage);
                resetService(mfps);
                loadingOverlay.setVisible(false);
            });
            
            loadingOverlay.setVisible(true);
            mfps.start();
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
                    
                    pts.setNomeFile(doc.getNomeFile());
                    pts.setOnSucceeded(e->{
                        byte[] contenuto = pts.getValue();
                        if (contenuto == null || contenuto.length == 0) {
                            mostraAlert("Attenzione", "File vuoto o non trovato", Alert.AlertType.WARNING);
                            return;
                        }

                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Salva documento");
                        fileChooser.setInitialFileName(doc.getNomeFile());
                        File fileDest = fileChooser.showSaveDialog(getTableView().getScene().getWindow());

                        if (fileDest != null) {
                            try {
                                Files.write(fileDest.toPath(), contenuto);
                            } catch (IOException ex) {
                                System.getLogger(AppViewController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                            }
                            mostraAlert("Successo", "File salvato in:\n" + fileDest.getAbsolutePath(), Alert.AlertType.INFORMATION);
                        }
                        resetService(pts);
                    });
                    
                    pts.setOnFailed(e->{
                        mostraAlert("Errore", "Errore durante il recupero del file.", Alert.AlertType.ERROR);
                        resetService(pts);
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

                listaDocumenti.clear(); // Buona pratica: evita duplicati
                this.ptds.setOnSucceeded(e->{
                    listaDocumenti.addAll(this.ptds.getValue());
                    this.resetService(ptds);
                });
                
                this.ptds.start();
        gestioneDocumentiView.setVisible(true);
    }

    @FXML
    private void eliminaDoc(ActionEvent event) {
        List<DocumentoDiTesto> selezionati = new ArrayList<>(gestDocTabella.getSelectionModel().getSelectedItems());

        if (selezionati.isEmpty()) {
            mostraAlert("Attenzione", "Nessun documento selezionato.", Alert.AlertType.INFORMATION);
            return;
        }

        
        // Mostra l'overlay prima di iniziare
        loadingOverlay.setVisible(true);
        ets = new EliminaTestoService(selezionati);


            ets.setOnSucceeded(e->{
                Platform.runLater(() -> {
                    loadingOverlay.setVisible(false);
                    listaDocumenti.removeAll(selezionati);
                    mostraAlert("Successo", "Documenti eliminati con successo.", Alert.AlertType.INFORMATION);
                });
                this.resetService(ets);
            }); 

            ets.setOnFailed(e->{
                    loadingOverlay.setVisible(false);
                    mostraAlert("Errore", "Errore durante l'eliminazione dei documenti", Alert.AlertType.ERROR);
                    this.resetService(ets);
            });
                
            ets.setOnCancelled(e->{
                loadingOverlay.setVisible(false);
                this.resetService(ets);
            });
        ets.start();
    }

    @FXML
    private void iniziaPartitaOnAction(ActionEvent event) {
        AppState stato = AppState.getInstance();

        // Controllo selezione difficoltà
        LivelloPartita difficoltà = null;
        if(radioFacile.isSelected()) difficoltà = LivelloPartita.FACILE;
        if(radioMedio.isSelected()) difficoltà = LivelloPartita.MEDIO;
        if(radioDifficile.isSelected()) difficoltà = LivelloPartita.DIFFICILE;

        if(difficoltà == null) {
           mostraAlert("Selezione obbligatoria", "Devi selezionare un livello di difficoltà!",Alert.AlertType.WARNING);
            return;
        }
        stato.setDifficoltà(difficoltà);

        // Controllo selezione lingue
        ArrayList<Lingua> lingue = new ArrayList<>();
        if(checkIT.isSelected()) lingue.add(Lingua.ITALIANO);
        if(checkEN.isSelected()) lingue.add(Lingua.INGLESE);
        if(checkFR.isSelected()) lingue.add(Lingua.FRANCESE);
        if(checkDE.isSelected()) lingue.add(Lingua.TEDESCO);
        if(checkES.isSelected()) lingue.add(Lingua.SPAGNOLO);

        if(lingue.isEmpty()) {
            mostraAlert("Selezione obbligatoria", "Devi selezionare almeno una lingua!",Alert.AlertType.WARNING);
            return;
        }
        stato.setLingue(lingue);

        try {
            // Prosegui solo se tutti i controlli sono superati
            App.setRoot("SessionView");
        } catch (IOException ex) {
            System.getLogger(AppViewController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }

    private void initializeSchermataDifficoltà() {
        BooleanBinding nessunaLinguaSelezionata = Bindings.createBooleanBinding(() -> 
            !checkIT.isSelected() && 
            !checkEN.isSelected() && 
            !checkFR.isSelected() && 
            !checkDE.isSelected() && 
            !checkES.isSelected(),
            checkIT.selectedProperty(), 
            checkEN.selectedProperty(),
            checkFR.selectedProperty(), 
            checkDE.selectedProperty(), 
            checkES.selectedProperty()
        );

        iniziaPartita.disableProperty().bind(
            Bindings.not(
                radioFacile.selectedProperty()
                    .or(radioMedio.selectedProperty())
                    .or(radioDifficile.selectedProperty())
            ).or(nessunaLinguaSelezionata)
        );
    }
    
    private void resetService(Service<?> service) {
        if (service.getState() == Worker.State.SUCCEEDED || 
            service.getState() == Worker.State.FAILED || 
            service.getState() == Worker.State.CANCELLED) {

            service.reset();  // Resetta lo stato a READY
        }
    }
}  
