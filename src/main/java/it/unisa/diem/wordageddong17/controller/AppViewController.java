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

    
    // ========== ATTRIBUTI PRIVATI ==========

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

    
    
    /**
     * Inizializza la classe controller configurando l'interfaccia utente all'avvio.
     * <p>
     * In questo metodo vengono eseguite le seguenti operazioni:
     * </p>
     * <ul>
     *   <li>Viene ritagliata l'immagine presente in {@code imageView} e nell'oggetto {@code immagineInfoUtente}
     *       applicando un ritaglio quadrato di dimensione 250.</li>
     *   <li>Viene ritagliata in forma circolare l'immagine presente in {@code fotoProfilo} con raggio 40.</li>
     *   <li>Vengono inizializzate le componenti dell'interfaccia relative alle classifiche, alle informazioni
     *       dell'utente, alla gestione dei documenti e alla schermata di selezione della difficoltà, tramite
     *       le chiamate ai metodi {@code initializeClassifiche()}, {@code initializeInfoUtente()},
     *       {@code initializeGestioneDocumenti()} e {@code initializeSchermataDifficoltà()}.</li>
     *   <li>Vengono chiuse eventuali finestre o pannelli aperti mediante {@code chiudiTutto()}.</li>
     * </ul>
     * <p>
     * Successivamente, in base allo stato della sessione (contenuto nell'oggetto {@code appstate}), il metodo
     * configura l'interfaccia:
     * </p>
     * <ul>
     *   <li>Se il flag {@code sessionViewHomeButton} è {@code true}, vengono eseguite le seguenti operazioni:
     *     <ul>
     *       <li>Viene richiamato {@code tornaAllaHome()} per reimpostare la schermata principale.</li>
     *       <li>Vengono configurati eventuali pulsanti per l'amministrazione tramite {@code configuraPulsantiAdmin()}.</li>
     *       <li>Viene aggiornato il testo della label di benvenuto con il nome utente corrente.</li>
     *       <li>Viene aggiornata l'immagine del profilo utilizzando il contenuto in byte ottenuto da
     *           {@code appstate.getUtente().getFotoProfilo()}, convertito in immagine dal metodo {@code getImageFromByte()}.</li>
     *       <li>Viene eseguito il metodo {@code pulisciTutto()} per ripulire o resettare altre eventuali sezioni.</li>
     *     </ul>
     *   </li>
     *   <li>Se il flag {@code sessionViewContinuaButton} è {@code true}, viene simulato l'evento di avvio tramite
     *       {@code startOnAction(new ActionEvent())}.</li>
     *   <li>Se nessuno dei flag sopra è attivo, viene resa visibile la schermata di login impostando
     *       {@code schermataDiLogin.setVisible(true)}.</li>
     * </ul>
     *
     * @param url la posizione relativa per il caricamento delle risorse (non utilizzato direttamente qui)
     * @param rb il ResourceBundle utilizzato per l'internazionalizzazione (non utilizzato direttamente qui)
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ritagliaQuadrato(imageView, 250);
        ritagliaQuadrato(immagineInfoUtente, 250);
        ritagliaCerchio(fotoProfilo, 40);
        initializeClassifiche();
        initializeInfoUtente();
        initializeGestioneDocumenti();
        initializeSchermataDifficoltà();
        chiudiTutto();

        if (appstate.isSessionViewHomeButton()) {
            tornaAllaHome();
            this.configuraPulsantiAdmin();
            this.benvenutoLabel.setText("Benvenuto " + appstate.getUtente().getUsername());
            this.fotoProfilo.setImage(this.getImageFromByte(this.appstate.getUtente().getFotoProfilo()));
            this.pulisciTutto();

        } else if (appstate.isSessionViewContinuaButton()) {
            startOnAction(new ActionEvent());
        } else {
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
    
    /**
     * Verifica se esiste almeno un file di salvataggio associato all'utente corrente.
     * <p>
     * Il metodo controlla l'esistenza di due file:
     * <ul>
     *   <li>"SalvataggioDi&lt;email&gt;.ser": il file di salvataggio principale dell'utente.</li>
     *   <li>"SalvataggioFaseGenerazioneDi&lt;email&gt;.ser": il file relativo alla fase di generazione del salvataggio.</li>
     * </ul>
     * L'email dell'utente viene ottenuta tramite {@code this.appstate.getUtente().getEmail()}.
     * Se almeno uno di questi file esiste, il metodo restituisce {@code true}.
     * </p>
     *
     * @return {@code true} se almeno uno dei file di salvataggio esiste, {@code false} altrimenti.
     */
    private boolean verificaEsistenzaSalvataggio() {
        return (new File("SalvataggioDi" + this.appstate.getUtente().getEmail() + ".ser").exists()) ||
               (new File("SalvataggioFaseGenerazioneDi" + this.appstate.getUtente().getEmail() + ".ser").exists());
    }

    
    /**
     * Elimina i file di salvataggio associati all'utente corrente.
     * <p>
     * Il metodo tenta di cancellare due file:
     * <ul>
     *   <li>"SalvataggioDi&lt;email&gt;.ser": file di salvataggio principale dell'utente.</li>
     *   <li>"SalvataggioFaseGenerazioneDi&lt;email&gt;.ser": file relativo alla fase di generazione del salvataggio.</li>
     * </ul>
     * L'email dell'utente viene ottenuta tramite {@code this.appstate.getUtente().getEmail()}.
     * I risultati dell'operazione di eliminazione per ciascun file vengono stampati sulla console.
     * </p>
     *
     * @return {@code true} se entrambi i file sono stati eliminati con successo, {@code false} altrimenti.
     */
    private boolean eliminaSalvataggi() {
        boolean f1 = new File("SalvataggioDi" + this.appstate.getUtente().getEmail() + ".ser").delete();
        System.out.println("Eliminazione file 1: " + f1);
        boolean f2 = new File("SalvataggioFaseGenerazioneDi" + this.appstate.getUtente().getEmail() + ".ser").delete();
        System.out.println("Eliminazione file 2: " + f2);
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

    /**
     * Gestisce l'evento di logout dell'utente.
     * <p>
     * Quando questo metodo viene invocato, esegue le seguenti operazioni:
     * <ul>
     *   <li>Rimuove l'utente corrente impostando l'oggetto utente in AppState a {@code null}.</li>
     *   <li>Chiude tutte le finestre o pannelli aperti tramite il metodo {@code chiudiTutto()}.</li>
     *   <li>Pulisce lo stato dell'interfaccia chiamando il metodo {@code pulisciTutto()}.</li>
     *   <li>Imposta l'immagine del profilo a un placeholder predefinito utilizzando {@code getPlaceholderImage()}.</li>
     *   <li>Rende visibile la schermata di login impostando {@code schermataDiLogin.setVisible(true)}.</li>
     * </ul>
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad es. il clic sul pulsante di logout)
     */
    @FXML
    private void logout(ActionEvent event) {
       appstate.setUtente(null);
       chiudiTutto();
       pulisciTutto();
       fotoProfilo.setImage(getPlaceholderImage());
       schermataDiLogin.setVisible(true);
    }


   /**
    * Gestisce l'azione di chiusura delle classifiche.
    * <p>
    * Quando questo metodo viene invocato:
    * <ul>
    *   <li>Verifica se il servizio {@code pcs} è stato inizializzato e se è in esecuzione; in tal caso, lo interrompe
    *       invocando {@code pcs.cancel()}.</li>
    *   <li>Richiama il metodo {@code chiudiTutto()} per chiudere eventuali pannelli o finestre attive.</li>
    *   <li>Rende visibile la schermata principale impostando la visibilità di {@code schermataHome} a {@code true}.</li>
    * </ul>
    * </p>
    *
    * @param event l'evento generato dall'interazione dell'utente (ad esempio il clic sul pulsante di chiusura delle classifiche)
    */
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
    
    /**
     * Mostra una finestra di alert con il titolo, il messaggio e il tipo specificati.
     * <p>
     * Se il tipo specificato è {@code Alert.AlertType.CONFIRMATION}, vengono impostati due pulsanti:
     * "OK" e "ANNULLA". Dopo che l'utente ha interagito con l'alert:
     * <ul>
     *   <li>Se l'utente seleziona "OK", viene eseguita l'azione definita dal {@code Consumer<T>}
     *       passando il valore fornito nel parametro {@code valore}.</li>
     *   <li>Se l'utente seleziona "ANNULLA", viene invocato il metodo {@code eliminaSalvataggi()}.</li>
     * </ul>
     * Per gli altri tipi di alert, l'alert viene semplicemente mostrato e atteso fino alla sua chiusura.
     * </p>
     *
     * @param <T> il tipo del valore da passare al Consumer
     * @param titolo il titolo della finestra di alert
     * @param messaggio il messaggio da visualizzare nell'alert
     * @param tipo il tipo di alert (ad esempio, {@code Alert.AlertType.CONFIRMATION}, {@code Alert.AlertType.INFORMATION}, ecc.)
     * @param azione il {@code Consumer<T>} da eseguire se l'utente conferma l'azione (premendo "OK")
     * @param valore il valore da passare al {@code Consumer} nel caso di conferma
     */
    private <T> void mostraAlert(String titolo, String messaggio, Alert.AlertType tipo, Consumer<T> azione, T valore) {

        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);

        if (tipo == Alert.AlertType.CONFIRMATION) {
            alert.getButtonTypes().setAll(new ButtonType("OK"), new ButtonType("ANNULLA"));
            alert.showAndWait().ifPresent(button -> {
                // Se l'utente conferma, passiamo il valore al Consumer, altrimenti eliminiamo i salvataggi.
                if (button.getText().equals("OK")) {
                    azione.accept(valore);
                } else {
                    this.eliminaSalvataggi();
                }
            });
        } else {
            alert.showAndWait();
        }
    }


    /**
     * Gestisce l'azione di registrazione dell'utente al clic del pulsante "Register".
     * <p>
     * Questo metodo esegue una serie di controlli e operazioni nell'ordine seguente:
     * </p>
     * <ol>
     *   <li>
     *     Verifica che tutti i campi obbligatori (username, email, password e repeatPassword) siano compilati.
     *     Se uno di essi è vuoto, viene mostrato un alert di avviso e l'operazione viene interrotta.
     *   </li>
     *   <li>
     *     Controlla la validità dell'email tramite il metodo {@code isValidEmail}.
     *     Se l'email non rispetta il formato corretto, viene visualizzato un alert di errore e l'esecuzione termina.
     *   </li>
     *   <li>
     *     Imposta l'email nel servizio di verifica (ad esempio, {@code pus}) e lo avvia per controllare se l'email
     *     è già in uso. Se il servizio restituisce un valore non nullo, viene mostrato un alert che segnala che 
     *     l'email è già utilizzata e il servizio viene resettato.
     *   </li>
     *   <li>
     *     Verifica che i campi password e repeatPassword coincidano. In caso contrario, viene mostrato un alert
     *     di errore e l'operazione si interrompe.
     *   </li>
     *   <li>
     *     Gestisce il recupero dell'immagine presente nell' {@code imageView}:
     *     <ul>
     *       <li>
     *         Se è presente un'immagine diversa da quella predefinita (indicata da "person.png"), tenta di leggerne i byte dal file.
     *       </li>
     *       <li>
     *         Se si verifica un errore durante la lettura, viene mostrato un alert di errore e l'operazione viene annullata.
     *       </li>
     *     </ul>
     *   </li>
     *   <li>
     *     Configura il servizio di registrazione (ad esempio, {@code ius}) impostando i valori di email, username,
     *     immagine (se presente) e password. Se il servizio fallisce, viene visualizzato un alert di errore e il servizio
     *     viene resettato.
     *   </li>
     *   <li>
     *     Avvia il servizio di registrazione per completare il processo di creazione dell'account.
     *   </li>
     *   <li>
     *     In base alla presenza o meno dell'immagine, viene creato un nuovo oggetto {@code Utente} e l'immagine del
     *     profilo viene aggiornata di conseguenza. L'utente creato viene impostato nello stato globale tramite {@code AppState}.
     *   </li>
     *   <li>
     *     Aggiorna l'immagine del profilo, configura eventuali pulsanti amministrativi, chiude eventuali finestre aperte,
     *     rende visibile la schermata principale e imposta il messaggio di benvenuto con lo username dell'utente.
     *   </li>
     *   <li>
     *     Mostra infine un alert di informazione per comunicare il completamento della registrazione con successo.
     *   </li>
     * </ol>
     *
     * @param event l'evento generato dal clic sul pulsante di registrazione
     */
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

       pus.setOnSucceeded(e -> {
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

       ius.setOnFailed(e -> {
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




    /**
     * Apre una finestra di scelta immagine per caricare una nuova immagine
     * nell'elemento {@code imageView}.
     */
    @FXML
    private void caricaIMG() {
        scegliImmagine(imageView);
    }

    /**
     * Converte un array di byte in un oggetto {@link Image}.
     * <p>
     * Se l'array di byte passato è {@code null}, viene restituita un'immagine di placeholder
     * come fallback. Altrimenti, l'array viene convertito in un'immagine tramite un {@link ByteArrayInputStream}.
     * </p>
     *
     * @param img l'array di byte rappresentante l'immagine
     * @return un oggetto {@link Image} creato dai byte oppure l'immagine di placeholder se {@code img} è {@code null}
     */
    private Image getImageFromByte(byte[] img) {
        if (img == null) {
            return getPlaceholderImage(); // fallback se l'immagine è nulla
        }
        return new Image(new ByteArrayInputStream(img));
    }

    /**
     * Chiude (nasconde) tutte le schermate e pannelli dell'interfaccia utente.
     * <p>
     * Questo metodo imposta la visibilità a {@code false} per ogni pannello:
     * <ul>
     *   <li>{@code schermataDiRegistrazione}</li>
     *   <li>{@code schermataDiLogin}</li>
     *   <li>{@code schermataHome}</li>
     *   <li>{@code schermataClassifiche}</li>
     *   <li>{@code schermataSelezioneDifficoltà}</li>
     *   <li>{@code dashboardMenu}</li>
     *   <li>{@code schermataInfoUtente}</li>
     *   <li>{@code schermataStopwords}</li>
     *   <li>{@code gestioneDocumentiView}</li>
     * </ul>
     * In questo modo, viene garantito che nessuna di queste schermate venga visualizzata.
     * </p>
     */
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

    /**
     * Resetta i campi di input e ripristina lo stato iniziale dell'interfaccia utente.
     * <p>
     * Le operazioni eseguite includono:
     * <ul>
     *   <li>Azzeramento del testo in tutti i {@link TextField} (email, password, username, ecc.).</li>
     *   <li>Pulizia dei campi data negli {@link DatePicker} tramite il metodo {@code clear()} sugli editor e
     *       reimpostazione dei valori a {@code null}.</li>
     *   <li>Impostazione delle immagini di visualizzazione (come {@code immagineInfoUtente} e {@code imageView})
     *       su un'immagine di placeholder di default.</li>
     *   <li>Azzeramento del campo di ricerca relativo alla gestione dei documenti.</li>
     * </ul>
     * Questo metodo assicura che l'interfaccia torni ad uno stato "pulito" dopo operazioni di logout, registrazione,
     * o quando necessario.
     * </p>
     */
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


    /**
     * Restituisce un'immagine segnaposto (placeholder) utilizzata quando non è disponibile un'immagine utente.
     *
     * @return l'immagine caricata dalla risorsa "/imgs/person.png"
     */
    private Image getPlaceholderImage() {
        return new Image(getClass().getResource("/imgs/person.png").toExternalForm());
    }

    /**
     * Passa alla schermata di login.
     * <p>
     * Questo metodo chiude tutte le schermate attualmente visibili, resetta i campi dell'interfaccia utente e
     * rende la schermata di login visibile.
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad es. clic sul pulsante di login)
     */
    @FXML
    private void passaALogin(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataDiLogin.setVisible(true);
    }

    /**
     * Ritaglia l'immagine visualizzata in un {@link ImageView} in forma quadrata, centrando il contenuto.
     * <p>
     * Il metodo imposta le dimensioni dell'ImageView in base alla dimensione specificata, mantenendo il rapporto
     * delle proporzioni. Viene quindi creato un clip rettangolare che definisce l'area di visualizzazione.
     * Un listener sulla proprietà dell'immagine calcola e imposta un viewport centrato, in modo che
     * venga visualizzata la porzione quadrata centrale dell'immagine.
     * </p>
     *
     * @param imageView la vista che contiene l'immagine da ritagliare
     * @param dimensione la dimensione (larghezza e altezza) desiderata per il ritaglio quadrato
     */
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

                // Determina il lato minore per ottenere un ritaglio quadrato centrato
                double viewportSize = Math.min(imgWidth, imgHeight);
                double xOffset = (imgWidth - viewportSize) / 2;
                double yOffset = (imgHeight - viewportSize) / 2;

                imageView.setViewport(new javafx.geometry.Rectangle2D(xOffset, yOffset, viewportSize, viewportSize));
            }
        });
    }


    /**
     * Ritaglia l'immagine visualizzata in un {@link ImageView} in forma circolare.
     * <p>
     * Il metodo imposta le dimensioni dell'ImageView in base al parametro {@code dimensione} e
     * mantiene il rapporto delle proporzioni. Viene creato un clip circolare centrato sull'immagine,
     * in modo da visualizzare una porzione circolare. Inoltre, è stato aggiunto un listener sulla proprietà
     * dell'immagine in modo da impostare un viewport centrato che ritaglia l'immagine in modo uniforme,
     * considerando il lato minore dell'immagine.
     * </p>
     *
     * @param imageView il {@code ImageView} contenente l'immagine da ritagliare
     * @param dimensione la dimensione (larghezza e altezza) da applicare all'ImageView per il ritaglio circolare.
     */
    private void ritagliaCerchio(ImageView imageView, double dimensione) {
        imageView.setFitWidth(dimensione);
        imageView.setFitHeight(dimensione);
        imageView.setPreserveRatio(true);

        // Clip circolare centrato
        Circle clip = new Circle(dimensione / 2, dimensione / 2, dimensione / 2);
        imageView.setClip(clip);

        imageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                double imgWidth = newImg.getWidth();
                double imgHeight = newImg.getHeight();

                // Determina il lato minore per ottenere un ritaglio centrato
                double viewportSize = Math.min(imgWidth, imgHeight);
                double xOffset = (imgWidth - viewportSize) / 2;
                double yOffset = (imgHeight - viewportSize) / 2;

                imageView.setViewport(new javafx.geometry.Rectangle2D(xOffset, yOffset, viewportSize, viewportSize));
            }
        });
    }

    /**
     * Inizializza le tabelle delle classifiche per le diverse difficoltà.
     * <p>
     * Il metodo crea degli ObservableList per ciascuna delle classifiche relative ai livelli:
     * <ul>
     *   <li>Facile</li>
     *   <li>Media</li>
     *   <li>Difficile</li>
     * </ul>
     * Dopodiché, per ogni tabella viene invocato il metodo {@code setupTable(...)} per configurarla,
     * associando le colonne della posizione, del nome e del punteggio con l'ObservableList corrispondente.
     * </p>
     */
    private void initializeClassifiche() {
        // Inizializza le liste per le classifiche
        classificaFacile = FXCollections.observableArrayList();
        classificaMedia = FXCollections.observableArrayList();
        classificaDifficile = FXCollections.observableArrayList();

        // Configura ciascuna tabella con le rispettive colonne e dati
        setupTable(facileTable, facilePosizione, facileNome, facilePunteggio, classificaFacile);
        setupTable(mediaTable, mediaPosizione, mediaNome, mediaPunteggio, classificaMedia);
        setupTable(difficileTable, difficilePosizione, difficileNome, difficilePunteggio, classificaDifficile);
    }


    /**
     * Configura una tabella per visualizzare la classifica degli utenti.
     * <p>
     * Questo metodo imposta le celle delle colonne della tabella in base alle proprietà
     * dell'oggetto {@link Classifica} e configura un indice di posizione che viene rappresentato
     * con emoji per le prime tre posizioni (🥇, 🥈, 🥉) oppure come numero per le posizioni successive.
     * Le colonne "nome" e "punteggio" vengono configurate per mostrare rispettivamente le proprietà
     * "username" e "punti" del modello {@code Classifica}.
     * </p>
     *
     * <p>
     * Le informazioni sulla creazione di una colonna indice (row index column) sono state
     * ispirate da: 
     * <a href="https://stackoverflow.com/questions/33353014/creating-a-row-index-column-in-javafx">StackOverflow</a>.
     * </p>
     *
     * @param table           la {@link TableView} in cui visualizzare la classifica
     * @param posizioneCol    la {@link TableColumn} che mostrerà la posizione (indice) di ogni elemento;
     *                        viene personalizzata per mostrare emoji per le prime tre posizioni
     * @param nomeCol         la {@link TableColumn} per il nome utente, associata alla proprietà "username" di {@code Classifica}
     * @param punteggioCol    la {@link TableColumn} per il punteggio, associata alla proprietà "punti" di {@code Classifica}
     * @param data            l'{@link ObservableList} contenente i dati della classifica
     */
    private void setupTable(TableView<Classifica> table, 
                            TableColumn<Classifica, Integer> posizioneCol, 
                            TableColumn<Classifica, String> nomeCol, 
                            TableColumn<Classifica, Float> punteggioCol, 
                            ObservableList<Classifica> data) {
        // Configura la colonna del nome per mostrare la proprietà "username"
        nomeCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        // Configura la colonna del punteggio per mostrare la proprietà "punti"
        punteggioCol.setCellValueFactory(new PropertyValueFactory<>("punti"));

        // Configura la colonna di posizione per visualizzare emoji per le prime tre posizioni
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

        // Imposta il valore della colonna di posizione basandosi sull'indice dell'elemento nella lista della tabella
        posizioneCol.setCellValueFactory(cellData -> {
            int index = cellData.getTableView().getItems().indexOf(cellData.getValue()) + 1;
            return new ReadOnlyIntegerWrapper(index).asObject();
        });

        // Imposta i dati nella tabella e attiva l'ordinamento per la colonna del punteggio in ordine decrescente
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
    
    /**
     * Mostra la gestione dei documenti.
     * <p>
     * Questo metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Chiude tutte le schermate attualmente visibili invocando {@code chiudiTutto()}.</li>
     *   <li>Rende visibile la vista della gestione documenti impostando {@code gestioneDocumentiView} a {@code true}.</li>
     *   <li>Avvia il servizio {@code ptds} per recuperare la lista dei documenti:
     *     <ul>
     *       <li>Quando il servizio termina con successo, viene pulita la lista dei documenti per evitare duplicati.</li>
     *       <li>Vengono aggiunti tutti i documenti ottenuti dal servizio alla lista {@code listaDocumenti}.</li>
     *       <li>Se la lista risulta vuota, viene mostrato un alert informativo che segnala l'assenza di documenti.</li>
     *       <li>Al termine, il servizio viene resettato tramite {@code resetService(ptds)}.</li>
     *     </ul>
     *   </li>
     *   <li>In caso di eccezione, viene visualizzato un alert di errore contenente il messaggio dell'eccezione.</li>
     * </ul>
     * </p>
     */
    @FXML
    private void mostraGestioneDocumenti() {
        chiudiTutto();
        gestioneDocumentiView.setVisible(true);

        Object utente = appstate.getUtente();

        try {
            ptds.setOnSucceeded(e -> {
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

    /**
     * Mostra la gestione delle stopwords.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Chiude tutte le schermate attualmente visibili invocando {@code chiudiTutto()}.</li>
     *   <li>Popola l'area di testo con le stopwords tramite il metodo {@code mettiStopwordInTextArea()}.</li>
     *   <li>Rende visibile la schermata delle stopwords impostando {@code schermataStopwords} a {@code true}.</li>
     * </ul>
     * </p>
     */
    @FXML
    private void mostraGestioneStopwords() {
        chiudiTutto();
        this.mettiStopwordInTextArea();
        schermataStopwords.setVisible(true);
    }

    /**
     * Torna alla schermata principale (home).
     * <p>
     * Il metodo chiude tutte le schermate attualmente visibili tramite {@code chiudiTutto()}
     * e rende visibile la schermata home impostando {@code schermataHome} a {@code true}.
     * </p>
     */
    @FXML
    private void tornaAllaHome() {
        chiudiTutto();
        schermataHome.setVisible(true);
    }

    
    /**
     * Salva le stopwords presenti nell'area di testo.
     * <p>
     * Quando viene chiamato questo metodo, si eseguono le seguenti operazioni:
     * <ul>
     *   <li>Si recupera il testo corrente dall'area di testo {@code stopwordTextArea}.</li>
     *   <li>Il testo viene convertito in un array di byte utilizzando {@code getBytes()}.</li>
     *   <li>Viene creato un oggetto {@link CaricaStopWordsService} con i seguenti parametri:
     *     <ul>
     *       <li>"ListaStopwords" come nome del file o identificativo della lista di stopwords,</li>
     *       <li>l'email dell'utente corrente ottenuta da {@code appstate.getUtente().getEmail()},</li>
     *       <li>l'array di byte ottenuto dal testo.</li>
     *     </ul>
     *   </li>
     *   <li>Si configurano i gestori per il completamento con successo e il fallimento del servizio:
     *     <ul>
     *       <li>Se il servizio termina con successo, viene mostrato un alert informativo che conferma il salvataggio.</li>
     *       <li>Se il servizio fallisce, viene mostrato un alert che segnala l'errore nel salvataggio.</li>
     *     </ul>
     *   </li>
     *   <li>Infine, si avvia il servizio con {@code cs.start()}.</li>
     * </ul>
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad esempio, il clic sul pulsante di salvataggio stopwords)
     */
    @FXML
    private void salvaStopwords(ActionEvent event) {
        String testo = this.stopwordTextArea.getText();
        byte[] bytes = testo.getBytes();
        CaricaStopWordsService cs = new CaricaStopWordsService("ListaStopwords", this.appstate.getUtente().getEmail(), bytes);

        cs.setOnSucceeded(e -> {
            mostraAlert("Avviso", "Stopwords caricate con successo.", Alert.AlertType.INFORMATION);
        });

        cs.setOnFailed(e -> {
            mostraAlert("Avviso", "Stopwords non caricate correttamente.", Alert.AlertType.INFORMATION);
        });

        cs.start();
    }

    
    

    /**
     * Restituisce il valore del livello di difficoltà selezionato nell'interfaccia admin.
     * <p>
     * Il metodo controlla lo stato dei radio button relativi alle difficoltà (Facile, Medio, Difficile)
     * e restituisce il valore ottenuto tramite {@code getDbValue()} dell'enum {@link LivelloPartita}
     * corrispondente. Se nessun radio button è selezionato, il metodo restituisce {@code null}.
     * </p>
     *
     * @return il valore di difficoltà selezionato come {@code String}, oppure {@code null} se nessuna opzione è selezionata.
     */
    private String getDifficoltaSelezionataAdmin() {
        if (adminRadioFacile.isSelected()) return LivelloPartita.FACILE.getDbValue();
        if (adminRadioMedio.isSelected()) return LivelloPartita.MEDIO.getDbValue();
        if (adminRadioDifficile.isSelected()) return LivelloPartita.DIFFICILE.getDbValue();
        return null;
    }

    /**
     * Restituisce la lingua selezionata nell'interfaccia admin.
     * <p>
     * Il metodo verifica lo stato dei radio button relativi alle lingue (Italiano, Tedesco, Spagnolo, Inglese, Francese)
     * e restituisce l'enum {@link Lingua} corrispondente all'opzione selezionata. Se nessun radio button è selezionato,
     * il metodo restituisce {@code null}.
     * </p>
     *
     * @return la lingua selezionata come oggetto {@code Lingua}, oppure {@code null} se nessuna lingua è selezionata.
     */
    private Lingua getLinguaSelezionataAdmin() {
        if (adminRadioIT.isSelected()) return Lingua.ITALIANO;
        if (adminRadioDE.isSelected()) return Lingua.TEDESCO;
        if (adminRadioES.isSelected()) return Lingua.SPAGNOLO;
        if (adminRadioEN.isSelected()) return Lingua.INGLESE;
        if (adminRadioFR.isSelected()) return Lingua.FRANCESE;
        return null;
    }

    /**
     * Permette all'utente di scegliere un file di testo utilizzando un FileChooser.
     * <p>
     * Il file chooser viene configurato per mostrare solo file con estensione ".txt". Se l'utente seleziona un file,
     * il file viene assegnato alla variabile {@code fileSelezionato} e il nome del file viene visualizzato in {@code pathTextField}.
     * Se nessun file viene selezionato, viene mostrato un alert di avviso.
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad esempio, clic sul pulsante per scegliere il file)
     */
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

    
    
    
    /**
     * Carica il contenuto di un file di testo e lo invia al servizio di caricamento documento.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Verifica che sia stato selezionato un file, che sia stato scelto un livello di difficoltà e che sia stata selezionata una lingua.
     *       Se uno di questi controlli fallisce, viene mostrato un alert di warning e il metodo termina.</li>
     *   <li>Imposta il livello di difficoltà nel servizio di caricamento documento (cts) utilizzando il valore ottenuto da {@link #getDifficoltaSelezionataAdmin()}.</li>
     *   <li>Legge il contenuto del file selezionato in un array di byte e lo imposta nel servizio. In caso di errore durante la lettura,
     *       l'eccezione viene loggata.</li>
     *   <li>Imposta nel servizio anche l'email dell'utente, il nome del file e la lingua selezionata.</li>
     *   <li>Configura i gestori per il completamento:
     *     <ul>
     *       <li>Se il servizio termina con successo, viene mostrato un alert di successo, il campo di testo del percorso
     *           viene pulito, il file selezionato viene resettato e il servizio viene resettato tramite {@code resetService(cts)}.</li>
     *       <li>Se il servizio termina con un fallimento, viene mostrato un alert d'errore, il campo di testo del percorso
     *           viene pulito, il file selezionato viene resettato e il servizio viene resettato.</li>
     *     </ul>
     *   </li>
     *   <li>Avvia il servizio di caricamento documento (cts) per eseguire l'operazione in background.</li>
     * </ul>
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad esempio, il clic sul pulsante per caricare il documento)
     */
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

        this.cts.setOnSucceeded(e -> {
            mostraAlert("Successo", "Documento caricato correttamente.", Alert.AlertType.INFORMATION);
            pathTextField.clear();
            fileSelezionato = null;
            this.resetService(cts);
        });

        this.cts.setOnFailed(e -> {
            mostraAlert("Errore", "Errore durante il caricamento del documento.", Alert.AlertType.INFORMATION);
            pathTextField.clear();
            fileSelezionato = null;
            this.resetService(cts);
        });

        this.cts.start();
    }

    
    /**
     * Carica le stopwords nell'area di testo dedicata.
     * <p>
     * Questo metodo utilizza il servizio {@code PrendiStopWordsService} per recuperare la lista 
     * delle stopwords associate all'identificativo "ListaStopwords". In particolare:
     * <ul>
     *   <li>Se il servizio termina con successo, il contenuto (in byte) viene convertito in una stringa e 
     *       impostato in {@code stopwordTextArea}. Viene inoltre stampato in console il testo caricato.</li>
     *   <li>Se il servizio fallisce, viene mostrato un alert informativo del tipo 
     *       {@code Alert.AlertType.INFORMATION} con il messaggio "Non risultano stopwords."</li>
     * </ul>
     * Infine, il servizio viene avviato.
     * </p>
     */
    private void mettiStopwordInTextArea() {
        PrendiStopWordsService ps = new PrendiStopWordsService("ListaStopwords");

        ps.setOnSucceeded(e -> {
            this.stopwordTextArea.setText(new String(ps.getValue()));
            System.out.println(" this.stopwordTextArea.setText " + this.stopwordTextArea.getText());
        });

        ps.setOnFailed(e -> {
            mostraAlert("Warning", "Non risultano stopwords.", Alert.AlertType.INFORMATION);
        });

        ps.start();
    }

    /**
     * Mostra le informazioni del profilo dell'utente autenticato.
     * <p>
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Recupera l'utente corrente da {@code appstate}. Se l'utente risulta {@code null}, 
     *       viene mostrato un alert di errore ("Utente non autenticato.") e l'esecuzione viene interrotta.</li>
     *   <li>Imposta l'email dell'utente nel servizio {@code pius} e, se necessario, cancella eventuali 
     *       esecuzioni precedenti del servizio.</li>
     *   <li>Chiude tutte le schermate attive (tramite {@code chiudiTutto()}) e rende visibile la schermata 
     *       dedicata alle informazioni utente ({@code schermataInfoUtente}).</li>
     *   <li>Visualizza immediatamente le informazioni base dell'utente (username ed email) nelle relative etichette.</li>
     *   <li>Configura il servizio informativo {@code pius}:
     *     <ul>
     *       <li>Se il servizio termina con successo, vengono popolati:
     *         <ul>
     *           <li>La lista della cronologia delle partite nell'apposita vista.</li>
     *           <li>I contatori delle partite giocate per le difficoltà facile, media e difficile.</li>
     *           <li>I migliori punteggi per ciascuna difficoltà, formattati opportunamente.</li>
     *         </ul>
     *         Infine, il servizio viene resettato tramite {@code resetService(pius)}.</li>
     *       <li>Se il servizio fallisce, l'errore viene loggato e, tramite {@code Platform.runLater()},
     *           viene mostrato un alert di errore ("Impossibile caricare i dati del profilo"). Il servizio
     *           viene quindi resettato.</li>
     *     </ul>
     *   </li>
     *   <li>Avvia il servizio informativo {@code pius} per ottenere i dettagli dell'utente e aggiornare l'interfaccia.</li>
     *   <li>In parallelo, viene eseguito un task separato per caricare l'immagine del profilo:
     *     <ul>
     *       <li>Il task converte i byte dell'immagine del profilo (ottenuti da {@code utente.getFotoProfilo()})
     *           in un oggetto {@link Image} utilizzando il metodo {@code getImageFromByte()}.</li>
     *       <li>Se il task ha successo, l'immagine viene impostata in {@code immagineInfoUtente}; in caso di fallimento,
     *           l'errore viene loggato.</li>
     *       <li>Il task viene avviato su un nuovo thread.</li>
     *     </ul>
     *   </li>
     * </ul>
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente per visualizzare le informazioni del profilo
     */
    @FXML
    private void passaAInfoProfilo(ActionEvent event) {
        Utente utente = appstate.getUtente();

        if (utente == null) {
            mostraAlert("Errore", "Utente non autenticato.", Alert.AlertType.ERROR);
            return;
        }

        pius.setEmail(utente.getEmail());

        if (this.pius != null && this.pius.isRunning()) {
            pius.cancel();
        }

        chiudiTutto();
        schermataInfoUtente.setVisible(true);

        // Visualizzazione immediata dei dati base dell'utente
        usernameInfoUtente.setText("Username: " + utente.getUsername());
        emailInfoUtente.setText("E-mail: " + utente.getEmail());

        pius.setOnSucceeded(e -> {
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

        // Task separato per caricare l'immagine del profilo
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


    

    /**
     * Inizializza la tabella della cronologia delle partite.
     * <p>
     * Il metodo configura le colonne della tabella utilizzando il {@code PropertyValueFactory} per mappare 
     * le proprietà dell'oggetto cronologia (data, difficoltà e punteggio). Inoltre, richiama il metodo 
     * {@code initializeCronologiaFiltro()} per impostare eventuali filtri sulla cronologia.
     * </p>
     */
    @SuppressWarnings("unchecked")
    private void initializeCronologiaPartite() {
        Utente utente = appstate.getUtente();
        dataCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("data"));
        difficoltàCronologiaPartite.setCellValueFactory(new PropertyValueFactory<>("difficolta"));
        punteggioCronologiaPartite.setCellValueFactory(new PropertyValueFactory("punti"));
        initializeCronologiaFiltro();
    }

    /**
     * Chiude la schermata delle informazioni utente e torna alla schermata home.
     * <p>
     * Viene invocato {@code chiudiTutto()} per nascondere tutte le schermate, seguito da {@code pulisciTutto()}
     * per resettare i campi dell'interfaccia. Infine, la schermata home viene resa visibile.
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad esempio, il clic sul pulsante di chiusura)
     */
    @FXML
    private void chiudiInfoUtente(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataHome.setVisible(true);
    }

    /**
     * Gestisce il cambiamento dell'immagine del profilo dall'interfaccia utente.
     * <p>
     * Quando l'utente clicca per cambiare l'immagine (evento di MouseEvent), il metodo apre un FileChooser 
     * tramite {@link #scegliImmagine(ImageView)} per selezionare una nuova immagine. Se l'immagine viene 
     * selezionata correttamente (cioè, se il byte array restituito non è {@code null}), viene invocato 
     * {@link #aggiornaFotoProfilo(byte[])} per aggiornare la foto del profilo.
     * </p>
     *
     * @param event l'evento generato dal clic sul componente per cambiare immagine
     */
    @FXML
    private void cambiaIMG(MouseEvent event) {
        byte[] nuovaImmagine = scegliImmagine(immagineInfoUtente);
        if (nuovaImmagine != null)
            aggiornaFotoProfilo(nuovaImmagine);
    }

   /**
    * Permette all'utente di scegliere un'immagine tramite un FileChooser e restituisce il contenuto del file come array di byte.
    * <p>
    * Vengono applicati dei filtri per mostrare solamente file immagine (estensioni .png, .jpg, .jpeg, .gif). 
    * Se l'utente non seleziona alcun file, viene mostrato un alert di avviso e il metodo restituisce {@code null}.<br>
    * Se il file selezionato non supera la validazione (tramite {@code validaImmagine(file)}), viene restituito 
    * {@code null} (dato che il metodo {@code validaImmagine} si occupa già di mostrare un alert appropriato).<br>
    * Se il file viene letto correttamente, il metodo aggiorna l'immagine visualizzata nell' {@code ImageView} passato 
    * come parametro e mostra un alert di successo.
    * </p>
    * <p>
    * In caso di eccezioni (IOException, SecurityException o altre), il metodo mostra un alert d'errore e restituisce {@code null}.
    * </p>
    *
    * @param imgv l' {@link ImageView} da aggiornare con l'immagine selezionata
    * @return un array di byte rappresentante il contenuto dell'immagine, oppure {@code null} se l'operazione fallisce
    */
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
               // Il metodo validaImmagine mostra già un alert in caso di errore.
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


    /**
     * Inizializza le informazioni dell'utente per la visualizzazione della cronologia delle partite.
     * <p>
     * In particolare:
     * <ul>
     *   <li>Crea un {@code ObservableList} vuoto per la cronologia delle partite.</li>
     *   <li>Imposta dei filtri di validazione per i campi di punteggio (minimo e massimo) utilizzando un {@code TextFormatter}
     *       che accetta soltanto numeri (in formato float, compresi eventuali segni negativi e decimali).</li>
     *   <li>Richiama il metodo {@code initializeCronologiaPartite()} per configurare le colonne della tabella della cronologia.</li>
     * </ul>
     * </p>
     */
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

    /**
     * Inizializza il filtro per la cronologia delle partite.
     * <p>
     * La lista filtrata (FilteredList) viene costruita a partire dalla lista delle partite
     * e il predicato viene definito sulla base di:
     * <ul>
     *   <li>Il filtro per il livello di difficoltà, basato sui radio button (checkFacile, checkMedio, checkDifficile).</li>
     *   <li>Il filtro per la data, confrontando le date della partita con i valori nei {@code DatePicker} (dataInizio, dataFine).</li>
     *   <li>Il filtro per il punteggio, confrontando il punteggio della partita con i valori inseriti nei campi punteggioMinimo e punteggioMassimo.</li>
     * </ul>
     * Il metodo aggiunge inoltre listener a ciascuna proprietà in modo da aggiornare dinamicamente il predicato.
     * </p>
     */
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

                // Se nessun filtro di difficoltà è selezionato, non si filtra per difficoltà.
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

    /**
     * Cerca di convertire una stringa in un valore di tipo float.
     * <p>
     * Se la conversione fallisce, viene restituito un valore di fallback specificato.
     * </p>
     *
     * @param text il testo da convertire
     * @param fallback il valore di fallback da restituire in caso di conversione fallita
     * @return il valore float ottenuto dalla conversione, oppure il fallback in caso di errore
     */
    private float parseSafeFloat(String text, float fallback) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * Aggiorna la foto del profilo dell'utente.
     * <p>
     * Il metodo:
     * <ul>
     *   <li>Recupera l'utente corrente e imposta l'email nel servizio {@code mfps}.</li>
     *   <li>Imposta l'immagine da aggiornare (passata come array di byte) nel servizio.</li>
     *   <li>Configura i gestori per il completamento:
     *     <ul>
     *       <li>In caso di successo, crea un oggetto {@link Image} (o usa un placeholder se {@code immagineBytes} è null), 
     *           aggiorna la foto del profilo nell'utente e nelle relative viste, poi resettando il servizio e nascondendo il loading overlay.</li>
     *       <li>In caso di fallimento, mostra un alert d'errore e ripristina la vecchia immagine.</li>
     *     </ul>
     *   </li>
     *   <li>Mostra il loading overlay e avvia il servizio {@code mfps}.</li>
     * </ul>
     * </p>
     *
     * @param immagineBytes l'array di byte della nuova immagine da impostare come foto profilo
     */
    private void aggiornaFotoProfilo(byte[] immagineBytes) {
        Utente utente = appstate.getUtente();
        mfps.setEmail(utente.getEmail());
        mfps.setImmagine(immagineBytes);

        this.mfps.setOnSucceeded(e -> {   
            Image image = immagineBytes != null ? new Image(new ByteArrayInputStream(immagineBytes)) : getPlaceholderImage();
            utente.setFotoProfilo(immagineBytes);
            fotoProfilo.setImage(image);
            immagineInfoUtente.setImage(image);
            resetService(mfps);
            loadingOverlay.setVisible(false);
        });

        this.mfps.setOnFailed(e -> {
            mostraAlert("Errore", "Impossibile aggiornare l'immagine: ", Alert.AlertType.ERROR);
            // Ripristino della vecchia immagine in caso di errore
            byte[] vecchiaImmagine = utente.getFotoProfilo();
            Image oldImage = vecchiaImmagine != null ? new Image(new ByteArrayInputStream(vecchiaImmagine)) : getPlaceholderImage();
            fotoProfilo.setImage(oldImage);
            immagineInfoUtente.setImage(oldImage);
            resetService(mfps);
            loadingOverlay.setVisible(false);
        });

        loadingOverlay.setVisible(true);
        mfps.start();
    }

    /**
     * Valida se un file passato è un'immagine corretta e non troppo grande.
     * <p>
     * Il metodo controlla:
     * <ul>
     *   <li>Che la dimensione del file non superi 5MB.</li>
     *   <li>Che il file, se caricato in una scala ridotta, non generi errori (verifica tramite l'oggetto {@link Image}).</li>
     * </ul>
     * Se uno di questi controlli fallisce, viene mostrato un alert d'errore e il metodo ritorna {@code false}.
     * </p>
     *
     * @param file il file da validare
     * @return {@code true} se il file è un'immagine valida e conforme alle dimensioni richieste, {@code false} altrimenti
     */
    private boolean validaImmagine(File file) {
        try {
            long maxSize = 5 * 1024 * 1024; // 5MB in byte
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

    /**
     * Gestisce l'evento del pulsante "Nuovo Documento" nella sezione di gestione documenti.
     * <p>
     * Il metodo chiude tutte le schermate attive invocando {@code chiudiTutto()} e rende visibile
     * la schermata dedicata agli amministratori per la gestione dei documenti.
     * </p *
     * @param event l'evento generato dal clic sul pulsante
     */
    @FXML
    private void gestDocNuovoButtonOnAction(ActionEvent event) {
        chiudiTutto();
        schermataDocumentiAdmin.setVisible(true);
    }

    /**
     * Inizializza la tabella dei documenti.
     * <p>
     * Il metodo:
     * <ul>
     *   <li>Crea un {@code ObservableList} vuoto per contenere i documenti.</li>
     *   <li>Configura le colonne della tabella mappando ciascuna proprietà del modello {@code DocumentoDiTesto}
     *       (difficoltà, emailAmministratore, nomeFile, lingua) tramite {@code PropertyValueFactory}.</li>
     *   <li>Per la colonna dedicata al download del documento, imposta una {@code CellFactory} che crea un pulsante 
     *       (con emoji "🗎") per scaricare il file. Quando il pulsante viene premuto:
     *     <ul>
     *       <li>Il servizio {@code pts} viene configurato con il nome del file.</li>
     *       <li>Se il download ha successo, viene aperto un {@code FileChooser} per salvare il documento e viene scritto su disco.</li>
     *       <li>Se il download fallisce, viene mostrato un alert d'errore.</li>
     *     </ul>
     *   </li>
     * </ul>
     * </p>
     */
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
                    pts.setOnSucceeded(e -> {
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

                    pts.setOnFailed(e -> {
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



    /**
     * Crea una lista filtrata dei documenti a partire dalla lista completa di documenti.
     * <p>
     * Viene creata una {@link FilteredList} inizializzata con un predicato che accetta tutti gli
     * elementi (ossia, <code>p -&gt; true</code>), la quale viene poi impostata come items della
     * tabella dei documenti (<code>gestDocTabella</code>).
     * </p>
     *
     * @return la {@link FilteredList} di {@link DocumentoDiTesto} contenente tutti gli elementi di {@code listaDocumenti}
     */
    private FilteredList<DocumentoDiTesto> creaListaFiltrata() {
        FilteredList<DocumentoDiTesto> filteredList = new FilteredList<>(listaDocumenti, p -> true);
        gestDocTabella.setItems(filteredList);
        return filteredList;
    }

    /**
     * Crea un filtro dinamico per la lista dei documenti.
     * <p>
     * Il filtro, restituito come un {@link Runnable}, imposta un predicato sulla
     * {@link FilteredList} passata che esamina ciascun documento e applica i seguenti criteri:
     * <ul>
     *   <li>
     *     <strong>Difficoltà:</strong> Se uno dei checkbox relativi alla difficoltà
     *     (<code>gestDocCheckFacile</code>, <code>gestDocCheckMedia</code>, <code>gestDocCheckDifficile</code>) è selezionato,
     *     il documento deve avere il valore corrispondente ("facile", "medio" o "difficile"). Se nessuno
     *     di questi checkbox è selezionato, il criterio di difficoltà non filtra alcun documento.
     *   </li>
     *   <li>
     *     <strong>Lingua:</strong> Il documento passa il filtro se la lingua (ottenuta tramite {@code doc.lingua()})
     *     corrisponde a quella selezionata nei controlli (<code>gestDocIT</code>, <code>gestDocCheckEN</code>,
     *     <code>gestDocCheckES</code>, <code>gestDocCheckFR</code>, <code>gestDocCheckDE</code>). Se nessun checkbox di lingua
     *     è selezionato, si considera che il documento soddisfi questo criterio.
     *   </li>
     *   <li>
     *     <strong>Ricerca testuale:</strong> Se il testo inserito nella barra di ricerca (<code>gestDocBarraDiRicerca</code>)
     *     non è vuoto, il documento deve contenere tale query (ignorando la differenza tra maiuscole e minuscole) nel nome del file
     *     o nell'email dell'amministratore.
     *   </li>
     * </ul>
     * Il documento verrà incluso nella lista filtrata solo se soddisfa tutti e tre i criteri.<br>
     * </p>
     *
     * @param filteredList la lista filtrata su cui impostare il predicato
     * @return un {@link Runnable} che, quando eseguito, applica il predicato di filtraggio alla lista
     */
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

    /**
     * Aggiunge listener ai controlli di filtro per aggiornare la lista dei documenti in tempo reale.
     * <p>
     * Vengono aggiunti listener alle seguenti proprietà:
     * <ul>
     *   <li>Checkbox relative alla lingua: <code>gestDocIT</code>, <code>gestDocCheckEN</code>, 
     *       <code>gestDocCheckES</code>, <code>gestDocCheckFR</code>, <code>gestDocCheckDE</code></li>
     *   <li>La barra di ricerca testuale: <code>gestDocBarraDiRicerca</code></li>
     *   <li>Checkbox relative alla difficoltà: <code>gestDocCheckFacile</code>, <code>gestDocCheckMedia</code>, 
     *       <code>gestDocCheckDifficile</code></li>
     * </ul>
     * Ogni volta che uno di questi controlli cambia valore, viene eseguito il {@link Runnable} passato come parametro,
     * aggiornando così il predicato della lista filtrata.
     * </p>
     *
     * @param filtro il filtro (come {@link Runnable}) da eseguire quando uno dei controlli cambia
     */
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


    /**
     * Inizializza la gestione dei documenti.
     * <p>
     * Questo metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Inizializza la tabella dei documenti tramite {@code inizializzaTabellaDocumenti()}.</li>
     *   <li>Crea una {@link FilteredList} filtrata dalla lista completa dei documenti usando {@code creaListaFiltrata()}.</li>
     *   <li>Configura il filtro dinamico chiamando {@code creaFiltro(filteredList)} e aggiunge i listener per aggiornare 
     *       automaticamente il filtro tramite {@code aggiungiListenerFiltri(filtro)}.</li>
     *   <li>Imposta il modello di selezione della tabella su modalità MULTIPLE.</li>
     * </ul>
     * </p>
     */
    private void initializeGestioneDocumenti() {
        inizializzaTabellaDocumenti();
        FilteredList<DocumentoDiTesto> filteredList = creaListaFiltrata();
        Runnable filtro = creaFiltro(filteredList);
        aggiungiListenerFiltri(filtro);
        gestDocTabella.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    /**
     * Torna alla schermata home dalla gestione documenti.
     * <p>
     * Chiude tutte le schermate attive e pulisce l'interfaccia utente, svuotando anche la lista dei documenti.
     * Infine, rende visibile la schermata home.
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad es., clic sul pulsante "Torna alla Home")
     */
    @FXML
    private void tornaAllaHomeFromGestDoc(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        listaDocumenti.clear();
        schermataHome.setVisible(true);
    }

    /**
     * Torna alla vista di gestione documenti.
     * <p>
     * Questo metodo chiude tutte le schermate attualmente attive, pulisce l'interfaccia e svuota la lista
     * dei documenti per evitare duplicati. Avvia poi il servizio {@code ptds} per aggiornare la lista dei documenti
     * e rende visibile la vista di gestione documenti.
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad es., clic sul pulsante "Back to Gestione Documenti")
     */
    @FXML
    private void backToGestDoc(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();

        listaDocumenti.clear(); // Buona pratica: evita duplicati
        this.ptds.setOnSucceeded(e -> {
            listaDocumenti.addAll(this.ptds.getValue());
            this.resetService(ptds);
        });

        this.ptds.start();
        gestioneDocumentiView.setVisible(true);
    }

    /**
     * Elimina i documenti selezionati.
     * <p>
     * Questo metodo recupera i documenti selezionati nella tabella dei documenti e, se la lista è vuota,
     * mostra un alert informativo. Altrimenti, mostra un overlay di caricamento e avvia il servizio
     * {@code EliminaTestoService} per eliminare i documenti selezionati.
     * <ul>
     *   <li>In caso di successo, il servizio rimuove i documenti eliminati dalla lista e nasconde l'overlay,
     *       mostrando un alert di successo.</li>
     *   <li>In caso di fallimento, l'overlay viene nascosto e viene mostrato un alert d'errore.</li>
     *   <li>In caso di cancellazione, l'overlay viene semplicemente nascosto e il servizio viene resettato.</li>
     * </ul>
     * </p>
     *
     * @param event l'evento generato dall'interazione dell'utente (ad es., clic sul pulsante "Elimina Documento")
     */
    @FXML
    private void eliminaDoc(ActionEvent event) {
        List<DocumentoDiTesto> selezionati = new ArrayList<>(gestDocTabella.getSelectionModel().getSelectedItems());

        if (selezionati.isEmpty()) {
            mostraAlert("Attenzione", "Nessun documento selezionato.", Alert.AlertType.INFORMATION);
            return;
        }

        // Mostra l'overlay prima di iniziare l'operazione di eliminazione
        loadingOverlay.setVisible(true);
        ets = new EliminaTestoService(selezionati);

        ets.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                loadingOverlay.setVisible(false);
                listaDocumenti.removeAll(selezionati);
                mostraAlert("Successo", "Documenti eliminati con successo.", Alert.AlertType.INFORMATION);
            });
            this.resetService(ets);
        });

        ets.setOnFailed(e -> {
            loadingOverlay.setVisible(false);
            mostraAlert("Errore", "Errore durante l'eliminazione dei documenti", Alert.AlertType.ERROR);
            this.resetService(ets);
        });

        ets.setOnCancelled(e -> {
            loadingOverlay.setVisible(false);
            this.resetService(ets);
        });

        ets.start();
    }


    /**
     * Gestisce l’avvio della partita.
     * <p>
     * Questo metodo viene invocato quando l’utente clicca sul pulsante per iniziare la partita e si occupa di:
     * <ul>
     *   <li>Verificare che sia stata selezionata una difficoltà (FACILE, MEDIO o DIFFICILE). Se nessuna difficoltà
     *       è selezionata, viene mostrato un alert di avviso e l’operazione viene interrotta.</li>
     *   <li>Verificare che sia stata selezionata almeno una lingua (tra Italiano, Inglese, Francese, Tedesco e Spagnolo).
     *       Se la lista risultante è vuota, viene mostrato un alert e l’operazione viene interrotta.</li>
     *   <li>Aggiornare lo stato globale dell’applicazione (tramite il Singleton {@link AppState}) impostando i valori
     *       di difficoltà e lingue.</li>
     *   <li>Cambiare la visualizzazione passando alla scena della sessione di gioco (con {@code App.setRoot("SessionView")}).</li>
     * </ul>
     * </p>
     *
     * @param event l’evento generato dal clic sull’interfaccia (ad esempio, il pulsante "Inizia Partita")
     */
    @FXML
    private void iniziaPartitaOnAction(ActionEvent event) {
        AppState stato = AppState.getInstance();

        // Controllo selezione difficoltà
        LivelloPartita difficoltà = null;
        if (radioFacile.isSelected()) difficoltà = LivelloPartita.FACILE;
        if (radioMedio.isSelected()) difficoltà = LivelloPartita.MEDIO;
        if (radioDifficile.isSelected()) difficoltà = LivelloPartita.DIFFICILE;

        if (difficoltà == null) {
            mostraAlert("Selezione obbligatoria", "Devi selezionare un livello di difficoltà!", Alert.AlertType.WARNING);
            return;
        }
        stato.setDifficoltà(difficoltà);

        // Controllo selezione lingue
        ArrayList<Lingua> lingue = new ArrayList<>();
        if (checkIT.isSelected()) lingue.add(Lingua.ITALIANO);
        if (checkEN.isSelected()) lingue.add(Lingua.INGLESE);
        if (checkFR.isSelected()) lingue.add(Lingua.FRANCESE);
        if (checkDE.isSelected()) lingue.add(Lingua.TEDESCO);
        if (checkES.isSelected()) lingue.add(Lingua.SPAGNOLO);

        if (lingue.isEmpty()) {
            mostraAlert("Selezione obbligatoria", "Devi selezionare almeno una lingua!", Alert.AlertType.WARNING);
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

    /**
     * Inizializza la schermata di selezione delle difficoltà.
     * <p>
     * Il metodo crea un {@link BooleanBinding} denominato {@code nessunaLinguaSelezionata} che risulta {@code true}
     * se nessuno dei checkbox relativi alle lingue (Italiano, Inglese, Francese, Tedesco, Spagnolo) è selezionato.
     * Successivamente, il pulsante per iniziare la partita ({@code iniziaPartita}) viene disabilitato se:
     * <ul>
     *   <li>Nessun radio button relativo alla difficoltà è selezionato</li>
     *   <li>oppure nessuna lingua è selezionata</li>
     * </ul>
     * In questo modo, l’utente non potrà avviare la partita finché non completa tutte le selezioni obbligatorie.
     * </p>
     */
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

    /**
     * Resetta il servizio specificato se il suo stato terminale (SUCCEEDED, FAILED o CANCELLED) è stato raggiunto.
     * <p>
     * Questo metodo verifica lo stato del {@link Service} passato come parametro e, se risulta terminato, invoca
     * il metodo {@code reset()} per riportarlo allo stato READY, consentendone un eventuale riutilizzo.
     * </p>
     *
     * @param service il servizio da resettare
     */
    private void resetService(Service<?> service) {
        if (service.getState() == Worker.State.SUCCEEDED ||
            service.getState() == Worker.State.FAILED ||
            service.getState() == Worker.State.CANCELLED) {

            service.reset();  // Resetta lo stato a READY
        }
    }

}  
