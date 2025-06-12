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
 * Questa classe gestisce tutte le interazioni dell'interfaccia utente
 * principale, inclusi login, registrazione, navigazione tra schermate e avvio
 * del gioco. Implementa il pattern MVC per separare la logica di controllo
 * dalla vista.
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
     * Inizializza il controller configurando l'interfaccia utente all'avvio dell'applicazione.
     *
     * <p>Questo metodo esegue diverse operazioni per preparare l'interfaccia, tra cui:</p>
     * <ul>
     *   <li>Ritaglio dell'immagine presente in {@code imageView} e {@code immagineInfoUtente}, creando un ritaglio quadrato di dimensione 250.</li>
     *   <li>Ritaglio circolare dell'immagine {@code fotoProfilo} con raggio 40.</li>
     *   <li>Inizializzazione delle componenti dell'interfaccia tramite i metodi:
     *       {@code initializeClassifiche()}, {@code initializeInfoUtente()},
     *       {@code initializeGestioneDocumenti()} e {@code initializeSchermataDifficoltà()}.</li>
     *   <li>Chiusura di eventuali finestre aperte con {@code chiudiTutto()}.</li>
     * </ul>
     *
     * <p>Successivamente, la configurazione dell'interfaccia dipende dallo stato della sessione, contenuto in {@code appstate}:</p>
     * <ul>
     *   <li>Se {@code sessionViewHomeButton} è attivo:
     *     <ul>
     *       <li>Richiama {@code tornaAllaHome()} per reimpostare la schermata principale.</li>
     *       <li>Configura i pulsanti di amministrazione con {@code configuraPulsantiAdmin()}.</li>
     *       <li>Aggiorna il testo della label di benvenuto con il nome utente corrente.</li>
     *       <li>Imposta l'immagine del profilo basandosi sui dati in byte di {@code appstate.getUtente().getFotoProfilo()}, convertiti con {@code getImageFromByte()}.</li>
     *       <li>Esegue {@code pulisciTutto()} per resettare sezioni aggiuntive.</li>
     *     </ul>
     *   </li>
     *   <li>Se {@code sessionViewContinuaButton} è attivo, avvia la procedura con {@code startOnAction(new ActionEvent())}.</li>
     *   <li>Se nessuno dei flag sopra è impostato, mostra la schermata di login con {@code schermataDiLogin.setVisible(true)}.</li>
     * </ul>
     *
     * @param url Percorso relativo per il caricamento delle risorse (non utilizzato direttamente).
     * @param rb ResourceBundle per l'internazionalizzazione (non utilizzato direttamente).
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
     * Gestisce il processo di login dell'utente, validando i dati e autenticandolo nel database.
     * In caso di successo, l'utente viene reindirizzato alla home.
     *
     * <p>Il processo di validazione prevede:</p>
     * <ul>
     *   <li>Verifica che i campi email e password non siano vuoti.</li>
     *   <li>Controllo della validità del formato email.</li>
     *   <li>Autenticazione delle credenziali tramite il database.</li>
     * </ul>
     *
     * @param event Evento generato dal click sul pulsante di login.
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
     * Reindirizza l'utente alla schermata di registrazione.
     *
     * <p>Quando l'utente clicca sul pulsante di registrazione, 
     * viene visualizzata la schermata dedicata.</p>
     *
     * @param event Evento generato dal click sul pulsante di registrazione.
     */
    @FXML
    private void passaARegistratiOnAction(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataDiRegistrazione.setVisible(true);

    }

    /**
     * Avvia il processo di selezione della difficoltà per iniziare il gioco.
     *
     * <p>Quando l'utente clicca sul pulsante Start, viene mostrata la 
     * schermata di selezione della difficoltà.</p>
     *
     * @param event Evento generato dal click sul pulsante Start.
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
     *
     * <p>Il metodo controlla l'esistenza di due file specifici:</p>
     * <ul>
     *   <li>{@code SalvataggioDi[email].ser}: File di salvataggio principale dell'utente.</li>
     *   <li>{@code SalvataggioFaseGenerazioneDi[email].ser}: File relativo alla fase di generazione del salvataggio.</li>
     * </ul>
     * 
     * <p>L'email dell'utente viene ottenuta tramite {@code this.appstate.getUtente().getEmail()}.
     * Se almeno uno di questi file esiste, il metodo restituisce {@code true}.</p>
     *
     * @return {@code true} se almeno uno dei file di salvataggio esiste, {@code false} altrimenti.
     */

    private boolean verificaEsistenzaSalvataggio() {
        return (new File("SalvataggioDi" + this.appstate.getUtente().getEmail() + ".ser").exists()) ||
               (new File("SalvataggioFaseGenerazioneDi" + this.appstate.getUtente().getEmail() + ".ser").exists());
    }

    
    /**
     * Elimina i file di salvataggio associati all'utente corrente.
     *
     * Il metodo tenta di cancellare due file di salvataggio dell'utente:
     * <ul>
     *   <li>{@code SalvataggioDi[email].ser}: File di salvataggio principale.</li>
     *   <li>{@code SalvataggioFaseGenerazioneDi[email].ser}: File relativo alla fase di generazione del salvataggio.</li>
     * </ul>
     * 
     * L'email dell'utente viene ottenuta tramite {@code appstate.getUtente().getEmail()}.
     * I risultati dell'operazione vengono stampati sulla console per confermare l'eliminazione dei file.
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
     * Visualizza la schermata delle classifiche con i dati aggiornati.
     *
     * Quando l'utente clicca sul pulsante Classifiche, il metodo carica 
     * e mostra la schermata corrispondente, con le informazioni aggiornate
     * sulla posizione degli utenti in classifica.
     *
     * @param event Evento generato dal click sul pulsante Classifiche.
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
     * Alterna la visibilità del menu dashboard dell'utente.
     *
     * Quando l'utente muove il mouse sopra il menu, il metodo cambia il 
     * suo stato di visibilità, mostrando o nascondendo l'interfaccia.
     *
     * @param event Evento del mouse che attiva la modifica della visibilità del menu.
     */
    @FXML
    private void toggleDashboard(MouseEvent event) {
        dashboardMenu.setVisible(!dashboardMenu.isVisible());
    }

    /**
     * Gestisce il logout dell'utente, terminando la sessione e reimpostando l'interfaccia grafica.
     *
     * Quando l'utente clicca sul pulsante di logout, il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Rimuove l'utente corrente impostando {@code appstate.getUtente()} a {@code null}.</li>
     *   <li>Chiude eventuali finestre aperte con {@code chiudiTutto()}.</li>
     *   <li>Ripulisce lo stato dell'interfaccia tramite {@code pulisciTutto()}.</li>
     *   <li>Reimposta l'immagine del profilo con un placeholder usando {@code getPlaceholderImage()}.</li>
     *   <li>Mostra la schermata di login con {@code schermataDiLogin.setVisible(true)}.</li>
     * </ul>
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio il clic sul pulsante di logout).
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
     * Chiude la schermata delle classifiche e ripristina l'interfaccia utente.
     *
     * Quando l'utente chiude la schermata delle classifiche, il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Interrompe il servizio {@code pcs} se attivo, tramite {@code pcs.cancel()}.</li>
     *   <li>Chiude eventuali pannelli o finestre aperte con {@code chiudiTutto()}.</li>
     *   <li>Rende visibile la schermata principale impostando {@code schermataHome.setVisible(true)}.</li>
     * </ul>
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio il clic sul pulsante di chiusura delle classifiche).
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
     * Controlla se un indirizzo email ha un formato valido, basandosi su una regex.
     *
     * Il metodo verifica che l'email rispetti i criteri standard per un formato corretto:
     * <ul>
     *   <li>Il nome utente deve contenere solo caratteri alfanumerici e {@code +}, {@code _}, {@code .}, {@code -} prima della {@code @}.</li>
     *   <li>Il dominio deve includere caratteri alfanumerici, {@code .} e {@code -}.</li>
     *   <li>L'estensione finale deve avere almeno 2 caratteri.</li>
     * </ul>
     *
     * Se uno di questi criteri non viene rispettato, il metodo restituirà {@code false}.
     *
     * @param email Stringa contenente l'email da validare.
     * @return {@code true} se l'email ha un formato valido, {@code false} altrimenti.
     */
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }

    /**
     * Mostra una finestra di alert all'utente.
     *
     * Crea e visualizza una finestra di dialogo in JavaFX con il messaggio specificato. 
     * Il tipo di alert determina l'icona e il colore del dialog.
     * Durante la visualizzazione, il dialog è modale e blocca l'interazione con la finestra principale 
     * fino alla chiusura.
     *
     * @param titolo Titolo della finestra di alert.
     * @param messaggio Testo del messaggio da mostrare.
     * @param tipo Tipo di alert (INFORMATION, WARNING, ERROR, CONFIRMATION).
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
     *
     * Se l'alert è di tipo {@code Alert.AlertType.CONFIRMATION}, vengono impostati due pulsanti: "OK" e "ANNULLA".
     * Dopo l'interazione dell'utente:
     * <ul>
     *   <li>Se seleziona "OK", viene eseguita l'azione definita da {@code azione}, passando il valore {@code valore}.</li>
     *   <li>Se seleziona "ANNULLA", viene invocato il metodo {@code eliminaSalvataggi()}.</li>
     * </ul>
     * Per gli altri tipi di alert, il dialog viene semplicemente mostrato fino alla sua chiusura.
     *
     * @param titolo Titolo della finestra di alert.
     * @param messaggio Testo del messaggio da visualizzare.
     * @param tipo Tipo di alert (ad esempio {@code Alert.AlertType.CONFIRMATION}, {@code Alert.AlertType.INFORMATION}, ecc.).
     * @param azione {@code Consumer<T>} da eseguire se l'utente conferma l'azione (premendo "OK").
     * @param valore Valore da passare al {@code Consumer} in caso di conferma.
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
     * Gestisce la registrazione dell'utente al clic sul pulsante "Register".
     *
     * Questo metodo verifica i dati inseriti e avvia il processo di registrazione eseguendo i seguenti passi:
     * <ol>
     *   <li>Verifica che tutti i campi obbligatori (username, email, password, repeatPassword) siano compilati. 
     *       Se uno è vuoto, mostra un alert e interrompe l'operazione.</li>
     *   <li>Controlla la validità dell'email con {@code isValidEmail}. Se non valida, mostra un alert e interrompe la registrazione.</li>
     *   <li>Controlla se l'email è già in uso impostandola nel servizio {@code pus}. Se il servizio la trova, mostra un alert e la registrazione viene interrotta.</li>
     *   <li>Verifica che password e repeatPassword coincidano. Se non corrispondono, mostra un alert e interrompe l'operazione.</li>
     *   <li>Recupera l'immagine presente nell' {@code imageView}:
     *     <ul>
     *       <li>Se è diversa da quella predefinita ("person.png"), tenta di leggerne i byte.</li>
     *       <li>Se il recupero fallisce, mostra un alert e annulla la registrazione.</li>
     *     </ul>
     *   </li>
     *   <li>Configura il servizio di registrazione ({@code ius}) con email, username, password e immagine, se presente. 
     *       Se il servizio fallisce, mostra un alert e interrompe il processo.</li>
     *   <li>Avvia il servizio di registrazione per creare l'account.</li>
     *   <li>Se la registrazione è riuscita:
     *     <ul>
     *       <li>Crea un nuovo oggetto {@code Utente}.</li>
     *       <li>Imposta l'utente nello stato globale tramite {@code AppState}.</li>
     *       <li>Aggiorna l'immagine del profilo e configura eventuali pulsanti amministrativi.</li>
     *       <li>Chiude eventuali finestre aperte e mostra la schermata principale.</li>
     *       <li>Imposta il messaggio di benvenuto con lo username dell'utente.</li>
     *     </ul>
     *   </li>
     *   <li>Mostra un alert di conferma per comunicare il successo della registrazione.</li>
     * </ol>
     *
     * @param event Evento generato dal clic sul pulsante di registrazione.
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
     * Converte un array di byte in un oggetto {@link Image}. Se l'array è {@code null}, 
     * viene restituita un'immagine di placeholder come fallback.
     *
     * L'array di byte viene convertito in un'istanza di {@link Image} utilizzando {@link ByteArrayInputStream}.
     * Se il valore di {@code img} è nullo, il metodo restituisce un'immagine predefinita per evitare errori di visualizzazione.
     *
     * @param img Array di byte rappresentante l'immagine.
     * @return {@link Image} creata dai byte, oppure un'immagine di placeholder se {@code img} è {@code null}.
     */
    private Image getImageFromByte(byte[] img) {
        if (img == null) {
            return getPlaceholderImage(); // fallback se l'immagine è nulla
        }
        return new Image(new ByteArrayInputStream(img));
    }

    /**
     * Nasconde tutte le schermate e pannelli dell'interfaccia utente impostandone la visibilità a {@code false}.
     *
     * Questo metodo garantisce che nessuna delle seguenti schermate sia visibile:
     * <ul>
     *   <li>{@code schermataDiRegistrazione}</li>
     *   <li>{@code schermataDiLogin}</li>
     *   <li>{@code schermataHome}</li>
     *   <li>{@code schermataClassifiche}</li>
     *   <li>{@code schermataSelezioneDifficoltà}</li>
     *   <li>{@code dashboardMenu}</li>
     *   <li>{@code schermataInfoUtente}</li>
     *   <li>{@code schermataStopwords}</li>
     * <li>{@code schermataDocumentiAdmin}</li>
     *   <li>{@code gestioneDocumentiView}</li>
     * </ul>
     * Una volta eseguito, tutte le schermate saranno completamente nascoste dall'interfaccia.
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
        schermataDocumentiAdmin.setVisible(false);
        gestioneDocumentiView.setVisible(false);
    }

    /**
     * Resetta tutti i campi di input e ripristina lo stato iniziale dell'interfaccia utente.
     *
     * Questo metodo pulisce i campi dell'interfaccia eseguendo le seguenti operazioni:
     * <ul>
     *   <li>Azzeramento del testo in tutti i {@link TextField} (email, password, username, ecc.).</li>
     *   <li>Pulizia dei campi data nei {@link DatePicker}, svuotando gli editor e reimpostando i valori a {@code null}.</li>
     *   <li>Impostazione delle immagini di visualizzazione (es. {@code immagineInfoUtente}, {@code imageView}) su un placeholder di default.</li>
     *   <li>Reset del campo di ricerca relativo alla gestione dei documenti.</li>
     * </ul>
     * Questo metodo garantisce il ripristino dell'interfaccia dopo operazioni di logout, registrazione o reset manuale.
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
     * Chiude tutte le schermate aperte e mostra la schermata di login.
     *
     * Il metodo nasconde le schermate attualmente visibili, ripristina i campi dell'interfaccia utente e 
     * rende visibile la schermata di login, garantendo una transizione pulita.
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio clic sul pulsante di login).
     */
    @FXML
    private void passaALogin(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataDiLogin.setVisible(true);
    }

    /**
     * Ritaglia e centra l'immagine visualizzata in un {@link ImageView} in formato quadrato.
     *
     * Il metodo imposta le dimensioni dell'ImageView in base alla dimensione specificata, mantenendo 
     * il rapporto delle proporzioni. Per ottenere un ritaglio quadrato centrato:
     * <ul>
     *   <li>Viene creato un clip rettangolare che definisce l'area visibile.</li>
     *   <li>Un listener sulla proprietà dell'immagine calcola e imposta un viewport centrato, 
     *       mostrando solo la parte centrale della foto.</li>
     * </ul>
     *
     * @param imageView La vista che contiene l'immagine da ritagliare.
     * @param dimensione La dimensione (larghezza e altezza) desiderata per il ritaglio quadrato.
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
     * Ritaglia l'immagine visualizzata in un {@link ImageView} in forma circolare, centrando il contenuto.
     *
     * Il metodo imposta le dimensioni dell'ImageView in base al parametro {@code dimensione}, 
     * mantenendo il rapporto delle proporzioni. Per ottenere un ritaglio perfetto:
     * <ul>
     *   <li>Viene applicato un clip circolare, centrato sull'immagine.</li>
     *   <li>Un listener sulla proprietà dell'immagine regola dinamicamente il viewport 
     *       per ritagliare l'area circolare in base al lato minore.</li>
     * </ul>
     * Questo assicura che l'immagine mantenga un aspetto bilanciato e armonioso.
     *
     * @param imageView Il {@code ImageView} contenente l'immagine da ritagliare.
     * @param dimensione La dimensione (larghezza e altezza) da applicare per il ritaglio circolare.
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
     * Inizializza le tabelle delle classifiche per i diversi livelli di difficoltà.
     *
     * Il metodo crea un {@code ObservableList} per ciascuna classifica:
     * <ul>
     *   <li>Facile</li>
     *   <li>Media</li>
     *   <li>Difficile</li>
     * </ul>
     * Successivamente, configura ogni tabella invocando {@code setupTable(...)} e associa le colonne 
     * della posizione, del nome e del punteggio alla rispettiva {@code ObservableList}.
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
     * Configura una tabella per visualizzare la classifica degli utenti, associando le colonne ai dati.
     *
     * Il metodo imposta le celle della tabella utilizzando le proprietà dell'oggetto {@link Classifica}.
     * La colonna della posizione viene personalizzata: le prime tre posizioni sono rappresentate da emoji 
     * (🥇, 🥈, 🥉), mentre le altre mostrano il numero corrispondente.  
     * Le colonne "nome" e "punteggio" sono configurate per visualizzare le proprietà "username" e "punti" 
     * del modello {@code Classifica}.
     *
     * Le informazioni sulla creazione della colonna indice (row index column) sono state ispirate da:
     * <a href="https://stackoverflow.com/questions/33353014/creating-a-row-index-column-in-javafx">StackOverflow</a>.
     *
     * @param table           {@link TableView} in cui visualizzare la classifica.
     * @param posizioneCol    {@link TableColumn} per la posizione degli utenti, con emoji per le prime tre posizioni.
     * @param nomeCol         {@link TableColumn} per il nome utente, associata alla proprietà "username" di {@code Classifica}.
     * @param punteggioCol    {@link TableColumn} per il punteggio, associata alla proprietà "punti" di {@code Classifica}.
     * @param data            {@link ObservableList} contenente i dati della classifica.
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
     * Configura la visibilità dei pulsanti amministratore.
     *
     * Mostra i pulsanti {@code newDocument} e {@code stopwordList} solo se l'utente corrente 
     * è di tipo amministratore.  
     * Per garantire il corretto funzionamento, l'oggetto utente deve essere stato inizializzato 
     * prima della chiamata al metodo.
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
     * Mostra la schermata di gestione dei documenti e avvia il processo di recupero dati.
     *
     * Il metodo chiude tutte le schermate attualmente visibili tramite {@code chiudiTutto()}, 
     * poi imposta la visibilità di {@code gestioneDocumentiView} su {@code true}.  
     * Successivamente, avvia il servizio {@code ptds} per recuperare la lista dei documenti:
     * <ul>
     *   <li>Pulisce la lista {@code listaDocumenti} per evitare duplicati.</li>
     *   <li>Aggiunge i documenti recuperati alla lista.</li>
     *   <li>Se la lista risulta vuota, mostra un alert informativo.</li>
     *   <li>Al termine, resetta il servizio con {@code resetService(ptds)}.</li>
     * </ul>
     * Se si verifica un'eccezione, il metodo visualizza un alert di errore con il messaggio corrispondente.
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
     * Mostra la schermata di gestione delle stopwords e aggiorna il contenuto dell'area di testo.
     *
     * Il metodo chiude tutte le schermate attualmente visibili tramite {@code chiudiTutto()}, 
     * popola l'area di testo con le stopwords usando {@code mettiStopwordInTextArea()} e 
     * imposta la visibilità di {@code schermataStopwords} su {@code true}.
     */
    @FXML
    private void mostraGestioneStopwords() {
        chiudiTutto();
        this.mettiStopwordInTextArea();
        schermataStopwords.setVisible(true);
    }

    /**
     * Chiude tutte le schermate aperte e torna alla schermata principale.
     *
     * Il metodo invoca {@code chiudiTutto()} per nascondere le schermate attualmente visibili,
     * quindi imposta {@code schermataHome} su {@code true} per renderla visibile.
     */
    @FXML
    private void tornaAllaHome() {
        chiudiTutto();
        schermataHome.setVisible(true);
    }

    
    /**
     * Salva le stopwords presenti nell'area di testo.
     *
     * Il metodo recupera il testo da {@code stopwordTextArea} e lo converte in un array di byte 
     * con {@code getBytes()}. Successivamente, crea un oggetto {@link CaricaStopWordsService} 
     * con i seguenti parametri:
     * <ul>
     *   <li>"ListaStopwords" come nome del file o identificativo della lista.</li>
     *   <li>L'email dell'utente corrente ottenuta da {@code appstate.getUtente().getEmail()}.</li>
     *   <li>L'array di byte contenente il testo delle stopwords.</li>
     * </ul>
     * Configura i gestori per il completamento del servizio:
     * <ul>
     *   <li>Se il salvataggio è riuscito, mostra un alert di conferma.</li>
     *   <li>Se il servizio fallisce, mostra un alert di errore.</li>
     * </ul>
     * Infine, avvia il servizio con {@code cs.start()}.
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio, il clic sul pulsante di salvataggio).
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
     *
     * Il metodo verifica quale radio button tra "Facile", "Medio" e "Difficile" è selezionato 
     * e restituisce il valore corrispondente tramite {@code getDbValue()} dell'enum {@link LivelloPartita}.  
     * Se nessuna opzione è selezionata, il metodo restituisce {@code null}.
     *
     * @return Il valore della difficoltà selezionata come {@code String}, oppure {@code null} se nessuna scelta è stata effettuata.
     */
    private String getDifficoltaSelezionataAdmin() {
        if (adminRadioFacile.isSelected()) return LivelloPartita.FACILE.getDbValue();
        if (adminRadioMedio.isSelected()) return LivelloPartita.MEDIO.getDbValue();
        if (adminRadioDifficile.isSelected()) return LivelloPartita.DIFFICILE.getDbValue();
        return null;
    }

    /**
     * Restituisce la lingua selezionata nell'interfaccia admin.
     *
     * Il metodo verifica quale radio button tra "Italiano", "Tedesco", "Spagnolo", "Inglese" e "Francese" è selezionato 
     * e restituisce l'enum {@link Lingua} corrispondente. Se nessuna lingua è selezionata, restituisce {@code null}.
     *
     * @return La lingua selezionata come {@code Lingua}, oppure {@code null} se nessuna opzione è stata scelta.
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
     * Restituisce la lingua selezionata nell'interfaccia admin.
     *
     * Il metodo verifica quale radio button tra "Italiano", "Tedesco", "Spagnolo", "Inglese" e "Francese" è selezionato 
     * e restituisce l'enum {@link Lingua} corrispondente. Se nessuna lingua è selezionata, restituisce {@code null}.
     *
     * @return La lingua selezionata come {@code Lingua}, oppure {@code null} se nessuna opzione è stata scelta.
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
     *
     * Il metodo verifica che siano stati selezionati un file, un livello di difficoltà e una lingua.
     * Se uno di questi controlli fallisce, mostra un alert di warning e interrompe l'operazione.
     * 
     * Procede con il caricamento eseguendo le seguenti operazioni:
     * <ul>
     *   <li>Imposta la difficoltà nel servizio ({@code cts}) tramite {@link #getDifficoltaSelezionataAdmin()}.</li>
     *   <li>Legge il contenuto del file in un array di byte e lo assegna al servizio. Se la lettura fallisce, l'errore viene loggato.</li>
     *   <li>Configura nel servizio l'email dell'utente, il nome del file e la lingua selezionata.</li>
     *   <li>Gestisce il completamento del servizio:
     *     <ul>
     *       <li>Se il servizio ha successo, mostra un alert di conferma, pulisce il campo di testo e resetta il file e il servizio.</li>
     *       <li>Se il servizio fallisce, mostra un alert di errore, pulisce il campo di testo e resetta il file e il servizio.</li>
     *     </ul>
     *   </li>
     *   <li>Avvia il servizio per eseguire l'operazione in background.</li>
     * </ul>
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio, clic sul pulsante di caricamento).
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
     * Carica le stopwords nell'area di testo dedicata utilizzando {@code PrendiStopWordsService}.
     *
     * Il metodo recupera la lista delle stopwords associate all'identificativo "ListaStopwords" ed esegue le seguenti operazioni:
     * <ul>
     *   <li>Se il servizio termina con successo, converte il contenuto in byte in una stringa e la imposta in {@code stopwordTextArea}. 
     *       Inoltre, stampa il testo caricato nella console.</li>
     *   <li>Se il servizio fallisce, mostra un alert informativo ({@code Alert.AlertType.INFORMATION}) con il messaggio "Non risultano stopwords."</li>
     * </ul>
     * Infine, avvia il servizio.
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
     * Mostra le informazioni del profilo dell'utente autenticato e aggiorna l'interfaccia.
     *
     * Il metodo recupera l'utente corrente da {@code appstate} e, se non autenticato, mostra un alert 
     * di errore e interrompe l'operazione.  
     * Successivamente, imposta l'email dell'utente nel servizio informativo {@code pius}, chiude le schermate attive
     * tramite {@code chiudiTutto()} e rende visibile {@code schermataInfoUtente}.  
     * Visualizza immediatamente le informazioni di base (username ed email) e avvia il servizio {@code pius} per recuperare i dati dettagliati:
     * <ul>
     *   <li>Se il servizio ha successo, aggiorna:
     *     <ul>
     *       <li>La cronologia delle partite.</li>
     *       <li>I contatori delle partite giocate per ogni difficoltà.</li>
     *       <li>I migliori punteggi.</li>
     *     </ul>
     *     Infine, resetta il servizio con {@code resetService(pius)}.</li>
     *   <li>Se il servizio fallisce, logga l'errore e mostra un alert tramite {@code Platform.runLater()}.</li>
     * </ul>
     * Parallelamente, esegue un task separato per caricare l'immagine del profilo:
     * <ul>
     *   <li>Converte i byte dell'immagine ({@code utente.getFotoProfilo()}) in un oggetto {@link Image} con {@code getImageFromByte()}.</li>
     *   <li>Se il task ha successo, aggiorna {@code immagineInfoUtente}; in caso di errore, lo logga.</li>
     * </ul>
     * Il task viene avviato su un nuovo thread per garantire fluidità nell'aggiornamento dell'interfaccia.
     *
     * @param event Evento generato dall'interazione dell'utente per visualizzare le informazioni del profilo.
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
     * Inizializza la tabella della cronologia delle partite, configurando le colonne e i filtri.
     *
     * Il metodo associa le colonne della tabella alle proprietà dell'oggetto cronologia (data, difficoltà e punteggio) 
     * tramite {@code PropertyValueFactory}.  
     * Inoltre, richiama {@code initializeCronologiaFiltro()} per applicare eventuali filtri alla visualizzazione dei dati.
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
     *
     * Il metodo nasconde tutte le schermate visibili con {@code chiudiTutto()}, 
     * resetta i campi dell'interfaccia con {@code pulisciTutto()} e rende visibile la schermata home.
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio, clic sul pulsante di chiusura).
     */
    @FXML
    private void chiudiInfoUtente(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        schermataHome.setVisible(true);
    }

    /**
     * Gestisce la modifica dell'immagine del profilo tramite un {@link FileChooser}.
     *
     * Quando l'utente clicca sul componente per cambiare l'immagine ({@code MouseEvent}), 
     * il metodo apre un file chooser con {@link #scegliImmagine(ImageView)} per selezionare una nuova immagine.  
     * Se l'immagine viene correttamente selezionata (ovvero, il byte array restituito non è {@code null}), 
     * il metodo aggiorna la foto profilo invocando {@link #aggiornaFotoProfilo(byte[])}.
     *
     * @param event Evento generato dal clic sul componente per cambiare immagine.
     */
    @FXML
    private void cambiaIMG(MouseEvent event) {
        byte[] nuovaImmagine = scegliImmagine(immagineInfoUtente);
        if (nuovaImmagine != null)
            aggiornaFotoProfilo(nuovaImmagine);
    }

    /**
     * Permette all'utente di selezionare un'immagine tramite un {@link FileChooser} e ne restituisce i byte.
     *
     * Il file chooser applica filtri per mostrare solo immagini con estensione ".png", ".jpg", ".jpeg" e ".gif".
     * Se l'utente non seleziona alcun file, mostra un alert di avviso e restituisce {@code null}.  
     * Se il file non supera la validazione ({@code validaImmagine(file)}), il metodo restituisce {@code null}, 
     * evitando ulteriori elaborazioni.  
     * Se il file viene letto correttamente, aggiorna l'{@code ImageView} passato come parametro e mostra un alert di conferma.
     *
     * In caso di errore ({@code IOException}, {@code SecurityException} o altre eccezioni), il metodo mostra un alert 
     * di errore e restituisce {@code null}.
     *
     * @param imgv {@link ImageView} da aggiornare con l'immagine selezionata.
     * @return Array di byte rappresentante l'immagine, oppure {@code null} se l'operazione fallisce.
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
     * Inizializza i dati dell'utente per la visualizzazione della cronologia delle partite.
     *
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Crea un {@code ObservableList} vuoto per la cronologia delle partite.</li>
     *   <li>Imposta filtri di validazione per i campi di punteggio (minimo e massimo) con {@code TextFormatter}, 
     *       accettando solo numeri float, inclusi segni negativi e decimali.</li>
     *   <li>Richiama {@code initializeCronologiaPartite()} per configurare le colonne della tabella della cronologia.</li>
     * </ul>
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
     * Inizializza il filtro per la cronologia delle partite, aggiornandolo dinamicamente con i parametri selezionati.
     *
     * Il metodo costruisce una {@code FilteredList} basata sulla lista delle partite e definisce il predicato 
     * in base ai seguenti filtri:
     * <ul>
     *   <li>Difficoltà, in base ai radio button ({@code checkFacile}, {@code checkMedio}, {@code checkDifficile}).</li>
     *   <li>Data, confrontando le date della partita con i valori dei {@code DatePicker} ({@code dataInizio}, {@code dataFine}).</li>
     *   <li>Punteggio, confrontando il valore della partita con quelli nei campi {@code punteggioMinimo} e {@code punteggioMassimo}.</li>
     * </ul>
     * Il metodo aggiunge listener a ciascuna proprietà per aggiornare dinamicamente il filtro quando i parametri cambiano.
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
     * Converte una stringa in un valore di tipo float, restituendo un valore di fallback in caso di errore.
     *
     * Se la conversione della stringa fallisce, il metodo restituisce il valore specificato come fallback.
     *
     * @param text Testo da convertire.
     * @param fallback Valore di fallback da restituire in caso di conversione fallita.
     * @return Valore float ottenuto dalla conversione oppure il fallback in caso di errore.
     */
    private float parseSafeFloat(String text, float fallback) {
        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    /**
     * Aggiorna la foto del profilo dell'utente tramite il servizio {@code mfps}.
     *
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Recupera l'utente corrente e imposta la sua email nel servizio.</li>
     *   <li>Passa al servizio l'immagine da aggiornare (come array di byte).</li>
     *   <li>Configura i gestori per il completamento:
     *     <ul>
     *       <li>Se l'aggiornamento ha successo, crea un oggetto {@link Image}, aggiorna la foto profilo e resetta il servizio.</li>
     *       <li>Se l'aggiornamento fallisce, mostra un alert di errore e ripristina la vecchia immagine.</li>
     *     </ul>
     *   </li>
     *   <li>Mostra il loading overlay e avvia il servizio {@code mfps}.</li>
     * </ul>
     *
     * @param immagineBytes Array di byte della nuova immagine da impostare come foto profilo.
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
     * Verifica se un file è un'immagine valida e conforme alle dimensioni richieste.
     *
     * Il metodo esegue i seguenti controlli:
     * <ul>
     *   <li>La dimensione del file non deve superare 5MB.</li>
     *   <li>Il file, se caricato in scala ridotta, non deve generare errori (verifica tramite {@link Image}).</li>
     * </ul>
     * Se uno dei controlli fallisce, mostra un alert di errore e restituisce {@code false}.
     *
     * @param file File da validare.
     * @return {@code true} se il file è un'immagine valida, {@code false} altrimenti.
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
     *
     * Il metodo chiude tutte le schermate attive tramite {@code chiudiTutto()} 
     * e rende visibile la schermata dedicata agli amministratori per la gestione dei documenti.
     *
     * @param event Evento generato dal clic sul pulsante.
     */
    @FXML
    private void gestDocNuovoButtonOnAction(ActionEvent event) {
        chiudiTutto();
        schermataDocumentiAdmin.setVisible(true);
    }
    
    /**
     * Inizializza la tabella dei documenti, configurando le colonne e il pulsante di download.
     *
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Crea un {@code ObservableList} vuoto per contenere i documenti.</li>
     *   <li>Mappa le proprietà del modello {@code DocumentoDiTesto} (difficoltà, email amministratore, nome file, lingua) 
     *       alle colonne della tabella tramite {@code PropertyValueFactory}.</li>
     *   <li>Configura la colonna dedicata al download con una {@code CellFactory} che genera un pulsante "🗎".  
     *       Quando il pulsante viene premuto:
     *     <ul>
     *       <li>Il servizio {@code pts} viene configurato con il nome del file.</li>
     *       <li>Se il download ha successo, apre un {@code FileChooser} per salvare il documento e lo scrive su disco.</li>
     *       <li>Se il download fallisce, mostra un alert di errore.</li>
     *     </ul>
     *   </li>
     * </ul>
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
     * Crea una lista filtrata dei documenti a partire dalla lista completa.
     *
     * Il metodo genera una {@link FilteredList} inizializzata con un predicato che accetta tutti gli 
     * elementi ({@code p -> true}) e la imposta come items della tabella {@code gestDocTabella}.
     *
     * @return {@link FilteredList} di {@link DocumentoDiTesto} contenente tutti gli elementi di {@code listaDocumenti}.
     */
    private FilteredList<DocumentoDiTesto> creaListaFiltrata() {
        FilteredList<DocumentoDiTesto> filteredList = new FilteredList<>(listaDocumenti, p -> true);
        gestDocTabella.setItems(filteredList);
        return filteredList;
    }

    /**
     * Crea un filtro dinamico per la lista dei documenti, basato su difficoltà, lingua e ricerca testuale.
     *
     * Il metodo restituisce un {@link Runnable} che imposta un predicato sulla {@link FilteredList} 
     * e applica i seguenti criteri:
     * <ul>
     *   <li><strong>Difficoltà:</strong> Se uno dei checkbox (<code>gestDocCheckFacile</code>, 
     *       <code>gestDocCheckMedia</code>, <code>gestDocCheckDifficile</code>) è selezionato, 
     *       il documento deve avere il valore corrispondente. Se nessun checkbox è selezionato, 
     *       non viene applicato alcun filtro.</li>
     *   <li><strong>Lingua:</strong> Il documento passa il filtro se la lingua (ottenuta tramite 
     *       {@code doc.lingua()}) corrisponde a quella selezionata nei controlli 
     *       (<code>gestDocIT</code>, <code>gestDocCheckEN</code>, <code>gestDocCheckES</code>, 
     *       <code>gestDocCheckFR</code>, <code>gestDocCheckDE</code>). Se nessun checkbox è selezionato, 
     *       il documento soddisfa comunque il criterio.</li>
     *   <li><strong>Ricerca testuale:</strong> Se il campo di ricerca ({@code gestDocBarraDiRicerca}) 
     *       contiene testo, il documento deve avere una corrispondenza nel nome del file o nell'email 
     *       dell'amministratore (ignorando maiuscole/minuscole).</li>
     * </ul>
     * Il documento viene incluso nella lista filtrata solo se soddisfa tutti i criteri.
     *
     * @param filteredList Lista filtrata su cui impostare il predicato.
     * @return {@link Runnable} che, quando eseguito, applica il predicato di filtraggio alla lista.
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
     * Aggiunge listener ai controlli di filtro per aggiornare dinamicamente la lista dei documenti.
     *
     * Il metodo registra listener per i seguenti elementi:
     * <ul>
     *   <li>Checkbox di selezione lingua: {@code gestDocIT}, {@code gestDocCheckEN}, {@code gestDocCheckES}, {@code gestDocCheckFR}, {@code gestDocCheckDE}.</li>
     *   <li>Barra di ricerca testuale: {@code gestDocBarraDiRicerca}.</li>
     *   <li>Checkbox di selezione difficoltà: {@code gestDocCheckFacile}, {@code gestDocCheckMedia}, {@code gestDocCheckDifficile}.</li>
     * </ul>
     * Quando uno di questi controlli cambia valore, il {@link Runnable} passato come parametro viene eseguito,
     * aggiornando il predicato della lista filtrata.
     *
     * @param filtro Filtro ({@link Runnable}) da eseguire quando uno dei controlli cambia.
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
     * Inizializza la gestione dei documenti, configurando la tabella e il filtro dinamico.
     *
     * Il metodo esegue le seguenti operazioni:
     * <ul>
     *   <li>Inizializza la tabella dei documenti con {@code inizializzaTabellaDocumenti()}.</li>
     *   <li>Genera una lista filtrata dalla lista completa usando {@link FilteredList} con {@code creaListaFiltrata()}.</li>
     *   <li>Configura il filtro dinamico tramite {@code creaFiltro(filteredList)} e aggiunge listener con {@code aggiungiListenerFiltri(filtro)} 
     *       per l'aggiornamento automatico.</li>
     *   <li>Imposta il modello di selezione della tabella su modalità MULTIPLE.</li>
     * </ul>
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
     *
     * Il metodo chiude tutte le schermate attive, pulisce l'interfaccia utente svuotando la lista dei documenti 
     * e rende visibile la schermata home.
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio, clic sul pulsante "Torna alla Home").
     */
    @FXML
    private void tornaAllaHomeFromGestDoc(ActionEvent event) {
        chiudiTutto();
        pulisciTutto();
        listaDocumenti.clear();
        schermataHome.setVisible(true);
    }

    /**
     * Torna alla vista di gestione documenti, chiudendo le schermate attive e aggiornando l'interfaccia.
     *
     * Il metodo chiude tutte le schermate attive, pulisce l'interfaccia e svuota la lista dei documenti 
     * per evitare duplicati. Avvia poi il servizio {@code ptds} per aggiornare la lista e rende visibile la vista di gestione documenti.
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio, clic sul pulsante "Back to Gestione Documenti").
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
     * Elimina i documenti selezionati dalla tabella e aggiorna l'interfaccia.
     *
     * Il metodo recupera i documenti selezionati e, se la lista è vuota, mostra un alert informativo.  
     * Altrimenti, visualizza un overlay di caricamento e avvia il servizio {@code EliminaTestoService} 
     * per eliminare i documenti.
     * <ul>
     *   <li>Se l'operazione ha successo, rimuove i documenti dalla lista, nasconde l'overlay e mostra un alert di conferma.</li>
     *   <li>Se l'operazione fallisce, nasconde l'overlay e mostra un alert di errore.</li>
     *   <li>Se viene annullata, l'overlay viene nascosto e il servizio viene resettato.</li>
     * </ul>
     *
     * @param event Evento generato dall'interazione dell'utente (ad esempio, clic sul pulsante "Elimina Documento").
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
     * Gestisce l'avvio della partita, verificando i parametri e cambiando la visualizzazione.
     *
     * Il metodo esegue i seguenti controlli:
     * <ul>
     *   <li>Verifica che sia stata selezionata una difficoltà (FACILE, MEDIO, DIFFICILE); in caso contrario, mostra un alert di avviso.</li>
     *   <li>Controlla che almeno una lingua tra Italiano, Inglese, Francese, Tedesco e Spagnolo sia stata scelta; se nessuna è selezionata, mostra un alert e interrompe l'operazione.</li>
     *   <li>Aggiorna lo stato globale dell'app tramite il Singleton {@link AppState}, impostando difficoltà e lingue.</li>
     *   <li>Passa alla schermata della sessione di gioco con {@code App.setRoot("SessionView")}.</li>
     * </ul>
     *
     * @param event Evento generato dal clic sull'interfaccia (es. pulsante "Inizia Partita").
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
     * Inizializza la schermata di selezione delle difficoltà, gestendo la logica di abilitazione del pulsante di avvio.
     *
     * Il metodo crea un {@link BooleanBinding} {@code nessunaLinguaSelezionata}, che risulta {@code true} 
     * se nessuno dei checkbox relativi alle lingue (Italiano, Inglese, Francese, Tedesco, Spagnolo) è selezionato.  
     * Il pulsante {@code iniziaPartita} viene disabilitato se:
     * <ul>
     *   <li>Non è selezionata alcuna difficoltà.</li>
     *   <li>Oppure nessuna lingua è stata scelta.</li>
     * </ul>
     * In questo modo, l'utente deve completare tutte le selezioni obbligatorie prima di poter avviare la partita.
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
     * Resetta il servizio specificato se ha raggiunto uno stato terminale (SUCCEEDED, FAILED o CANCELLED).
     *
     * Il metodo verifica lo stato del {@link Service} e, se terminato, invoca {@code reset()} 
     * per riportarlo allo stato READY, consentendone un nuovo utilizzo.
     *
     * @param service Servizio da resettare.
     */
    private void resetService(Service<?> service) {
        if (service.getState() == Worker.State.SUCCEEDED ||
            service.getState() == Worker.State.FAILED ||
            service.getState() == Worker.State.CANCELLED) {

            service.reset();  // Resetta lo stato a READY
        }
    }
    
    /**
     * Costruttore di default.
     * <p>
     * Inizializza una nuova istanza di {@code AppViewController}.
     * </p>
     */
    public AppViewController() {
        // eventuali inizializzazioni se necessarie
    }
}  
