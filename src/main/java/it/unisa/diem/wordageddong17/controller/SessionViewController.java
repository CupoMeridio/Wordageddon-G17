/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.App;
import it.unisa.diem.wordageddong17.service.CaricaSessioneDiGiocoService;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import it.unisa.diem.wordageddong17.model.AppState;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import it.unisa.diem.wordageddong17.service.CaricaPunteggioService;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Mattia Sanzari
 */
public class SessionViewController implements Initializable {

    @FXML
    private HBox FaseRisposte;
    @FXML
    private VBox vbox;
    @FXML
    private Label question;
    @FXML
    private RadioButton risposta1button;
    @FXML
    private ToggleGroup RispostaAllaDomanda;
    @FXML
    private RadioButton risposta2button;
    @FXML
    private RadioButton risposta3button;
    @FXML
    private RadioButton risposta4button;
    @FXML
    private Label counter;
    @FXML
    private VBox FaseLettura;
    @FXML
    private TextArea TestoDaLeggere;
    @FXML
    private Button TestoPrecedentebutton;
    @FXML
    private Label timeLettura;
    @FXML
    private Button VaiAlQuizbutton;
    @FXML
    private Button ProssimoTestobutton;
    @FXML
    private Button tornaHomeButton;
    @FXML
    private VBox schermataGameOver;
    @FXML
    private Label highScoreLabel;
    @FXML
    private Button continuaGiocoButton;
    @FXML
    private Label contatoreLettura;

    private CaricaSessioneDiGiocoService caricaSessione;
    private SessioneDiGioco sessione;
    private int contatoreDomanda = 1;
    private Timeline tm;
    private int durata;// variabile che salva il tempo in più del giocatore 
    private AppState stato;
    private final CaricaPunteggioService cps = new CaricaPunteggioService();

    Map<String, byte[]> MappaDocumenti;
    private String[] NomiDocumenti;
    private final Queue<Domanda> domande = new ConcurrentLinkedDeque<>();
    private int NumeroDiDomande;
    private int NumeroDiTesto;
    @FXML
    private AnchorPane loadingOverlay;
    @FXML
    private ProgressBar progessBar;
    @FXML
    private TextArea resocontoDomande;

    /**
     * Initializes the controller class.
     */
    @Override
   public void initialize(URL url, ResourceBundle rb) {
    this.resocontoDomande.setEditable(false);
    this.resocontoDomande.setFocusTraversable(false);
    this.resocontoDomande.setOnKeyPressed(e -> e.consume());
    this.resocontoDomande.setOnKeyTyped(e -> e.consume());
    this.resocontoDomande.setOnKeyReleased(e -> e.consume());
    
    this.TestoDaLeggere.setEditable(false);
    this.TestoDaLeggere.setFocusTraversable(false);
    
    // Impedisce la modifica ma mantiene lo scroll
    this.TestoDaLeggere.setStyle("-fx-text-input-disabled: true;");
    
    this.FaseRisposte.setVisible(false);
    this.schermataGameOver.setVisible(false);
    this.FaseLettura.setVisible(true);
    this.NumeroDiDomande=0;
    this.durata=0;
    this.NumeroDiTesto=0;
    MappaDocumenti = new HashMap<>();
    this.stato = AppState.getInstance();
       
    if(this.stato.isSessioneSalvata()){
        this.inizializeConSalvataggio();
    }else{
        sessione = new SessioneDiGioco( stato.getUtente());
        sessione.setLivello(this.stato.getDifficoltà());
        this.caricaSessione = new CaricaSessioneDiGiocoService(this.sessione, this.sessione.getLivello(),this.stato.getLingue());
        this.progessBar.progressProperty().bind(this.caricaSessione.progressProperty());
        this.serviceInitialize();
        loadingOverlay.setVisible(true);
        this.caricaSessione.start();     
    }   
}    

    private void serviceInitialize() {
        this.caricaSessione.setOnRunning(e -> {
            // Service in esecuzione
        });

        this.caricaSessione.setOnSucceeded(e -> {
            progessBar.progressProperty().unbind();
            loadingOverlay.setVisible(false);
            // Service completato con successo
            List<Domanda> domandeCaricate = caricaSessione.getValue();
            if (domandeCaricate != null && !domandeCaricate.isEmpty()) {
                this.domande.addAll(domandeCaricate);
                MappaDocumenti = this.sessione.getDocumenti();
                this.inizializzaTimer();
                cambioTesto();
            } else {
                this.mostraAlert("ERRORE", "Impossibile caricare le domande riprova più tardi", Alert.AlertType.ERROR);
                return;
            }
            resetService(caricaSessione);
        });

        this.caricaSessione.setOnFailed(e -> {
            loadingOverlay.setVisible(false);
            progessBar.progressProperty().unbind();
            // Service fallito
            Throwable exception = caricaSessione.getException();
            if (exception != null) {
                exception.printStackTrace();
            }
            this.mostraAlert("ERRORE", "Impossibile caricare la sessione riprova più tardi", Alert.AlertType.ERROR);
            resetService(caricaSessione);
        });
    }

    private void cambioTesto() {
        this.NomiDocumenti = this.sessione.getDocumenti().keySet().toArray(new String[0]);
        this.TestoDaLeggere.textProperty().setValue(
            new String(MappaDocumenti.get(NomiDocumenti[this.NumeroDiTesto]), StandardCharsets.UTF_8)
        );
        this.contatoreLettura.setText(this.NumeroDiTesto + 1 + "/" + this.NomiDocumenti.length);
    }

    private void cambioDomanda() {

        if (this.domande.isEmpty()) {
            this.FaseLettura.setVisible(false);
            this.FaseRisposte.setVisible(false);
            this.schermataGameOver.setVisible(true);
            this.sessione.aggiornaPuntiFatti(this.durata);
            this.highScoreLabel.setText(String.valueOf(this.sessione.getPunteggioFatto()));
            cps.setDifficoltà(this.sessione.getLivello());
            cps.setEmail(stato.getUtente().getEmail());
            cps.setPunteggio(this.sessione.getPunteggioFatto());
            cps.setOnSucceeded(e -> {
                resetService(cps);
            });
            cps.setOnFailed(e -> {
                mostraAlert("Errore", "Impossibile salvare il punteggio.", Alert.AlertType.WARNING);
                resetService(cps);
            });
            cps.start();
            stampaResocontoDomande();
            eliminaSalvataggi();
        }else{
            this.question.setText(this.domande.element().testo);
            this.counter.setText(1 + this.NumeroDiDomande - this.domande.size() + "/" + this.NumeroDiDomande);
            this.risposta1button.setText(this.domande.element().opzioni.get(0));
            this.risposta2button.setText(this.domande.element().opzioni.get(1));
            this.risposta3button.setText(this.domande.element().opzioni.get(2));
            this.risposta4button.setText(this.domande.element().opzioni.get(3));
        }
    }

    private void DaLetturaARisposte() {
        this.FaseLettura.setVisible(false);
        this.FaseRisposte.setVisible(true);
        this.NumeroDiDomande=this.sessione.getNumeroDomande();
    }

    @FXML
    private void risposta1(ActionEvent event) {
        this.aggiornaPilaDopoLaRisposta(0);
        this.risposta1button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void risposta2(ActionEvent event) {
        this.aggiornaPilaDopoLaRisposta(1);
        this.risposta2button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void risposta3(ActionEvent event) {
        this.aggiornaPilaDopoLaRisposta(2);
        this.risposta3button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void risposta4(ActionEvent event) {
        this.aggiornaPilaDopoLaRisposta(3);
        this.risposta4button.setSelected(false);
        this.cambioDomanda();
    }
    
    private void aggiornaPilaDopoLaRisposta(int i){
        this.sessione.aggiornaRisposte(this.NumeroDiDomande-this.domande.size(), i);
        this.domande.remove();
    }

    @FXML
    private void TestoPrecedente(ActionEvent event) {
        this.NumeroDiTesto--;
        if (this.NumeroDiTesto < 0) {
            this.NumeroDiTesto = this.NomiDocumenti.length - 1;
        }
        this.cambioTesto();
    }

    @FXML
    private void ProssimoTesto(ActionEvent event) {
        this.NumeroDiTesto++;
        if (this.NumeroDiTesto > this.NomiDocumenti.length - 1) {
            this.NumeroDiTesto = 0;
        }
        this.cambioTesto();
    }

    @FXML
    private void VaiAlQuiz(ActionEvent event) {
        this.DaLetturaARisposte();
        this.cambioDomanda();
        this.durata=this.sessione.getDurata();
        if(tm!=null)
            tm.stop();
    }

    @FXML
    private void tornaAllaHome(ActionEvent event) throws IOException {
        stato.setSessionViewHomeButton(true);
        App.setRoot("AppView");
    }

    @FXML
    private void continuaGioco(ActionEvent event) throws IOException {
        stato.setSessionViewContinuaButton(true);
        App.setRoot("AppView");
    }

    private void startTimer() {
        setTimerLabel();
        tm.playFromStart();
    }

    private void inizializzaTimer() {
        tm = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            this.sessione.setDurata(this.sessione.getDurata() - 1);
            setTimerLabel();
            if (this.sessione.getDurata() <= 0) {
                handleTimeout();
            }
        }));
        tm.setCycleCount(Timeline.INDEFINITE);
        startTimer();
    }

    private void setTimerLabel() {
        timeLettura.setText(String.valueOf(this.sessione.getDurata()));
    }

    private void handleTimeout() {
        tm.stop();
        this.DaLetturaARisposte();
        this.cambioDomanda();
    }

    private void resetService(Service<?> service) {
        if (service.getState() == Worker.State.SUCCEEDED
                || service.getState() == Worker.State.FAILED
                || service.getState() == Worker.State.CANCELLED) {

            service.reset();  // Resetta lo stato a READY
        }
    }

    private boolean eliminaSalvataggi() {
        boolean f1 = new File("SalvataggioDi"+this.sessione.getUtente().getEmail()+".ser").delete();
        boolean f2 =new File(("SalvataggioFaseGenerazioneDi"+this.sessione.getUtente().getEmail()+".ser")).delete();
        return f1 && f2;
    }

    private void caricaSessioneDaFileSalvato() {
         try {
            sessione.caricaSessioneDiGioco("SalvataggioDi"+stato.getUtente().getEmail()+".ser");
        } catch (IOException ex) {
            try {
                sessione.caricaSessioneDiGioco("SalvataggioFaseGenerazioneDi"+stato.getUtente().getEmail()+".ser");
            } catch (IOException ex1) {
                System.getLogger(SessionViewController.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex1);
                this.eliminaSalvataggi();
            }
        }
    }
    
    private void inizializeConSalvataggio(){
        this.sessione= new SessioneDiGioco();
            caricaSessioneDaFileSalvato();
        this.NumeroDiDomande = sessione.getNumeroDomande();
        this.MappaDocumenti = this.sessione.getDocumenti();
        // popola domande
        this.domande.addAll(
                this.sessione.getDomande().stream().skip((int)this.sessione.getRisposte().size()).collect(Collectors.toList()));
        
        // Inizializzazione completata
        if(this.sessione.getDurata()>0 && this.sessione.getRisposte().size()<1 ){
            cambioTesto();
            this.inizializzaTimer();
        }else{
            this.durata= this.sessione.getDurata();
            this.DaLetturaARisposte();
            this.cambioDomanda();
        }
    }

    private void mostraAlert(String titolo, String messaggio, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
    
    /**
     * Costruttore di default.
     * 
     * Inizializza una nuova istanza del controller.
     * 
     */
    public SessionViewController() {
        // Costruttore vuoto o con inizializzazioni se necessarie
    }
    private void stampaResocontoDomande() {
    StringBuffer risultato = new StringBuffer();
    
    this.sessione.getDomande().forEach(domanda -> {
        String rispostaCorretta = domanda.opzioni.get(domanda.rispostaCorretta);
        String rispostaData = domanda.opzioni.get(this.sessione.getRisposte().get(domanda));
        
        risultato.append("Domanda:\n")
                .append(domanda.testo).append("\n\n")
                .append("Risposta corretta:\n")
                .append(rispostaCorretta).append("\n\n")
                .append("Risposta data:\n")
                .append(rispostaData).append("\n")
                .append("----------------------------\n\n");
    });
    
    this.resocontoDomande.setText(risultato.toString());
}
}


