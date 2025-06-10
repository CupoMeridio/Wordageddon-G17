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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import javafx.concurrent.Service;
import javafx.concurrent.Worker;
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
    /**
     * Initializes the controller class.
     */
    @Override
   public void initialize(URL url, ResourceBundle rb) {
    this.TestoDaLeggere.setEditable(false);
    this.TestoDaLeggere.setMouseTransparent(true);
    this.TestoDaLeggere.setFocusTraversable(false);
    this.FaseRisposte.setVisible(false);
    this.schermataGameOver.setVisible(false);
    this.FaseLettura.setVisible(true);
    this.NumeroDiDomande=0;
    this.durata=0;
    this.NumeroDiTesto=0;
    MappaDocumenti = new HashMap<>();
    this.stato = AppState.getInstance();
       
     System.out.println("this.stato.isSessioneSalvata()"+ this.stato.isSessioneSalvata());
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
        System.out.println("Service in esecuzione...");
    });
    
    this.caricaSessione.setOnSucceeded(e -> {
        loadingOverlay.setVisible(false);  
        System.out.println("Service completato con successo!");
        List<Domanda> domandeCaricate = caricaSessione.getValue();
        if(domandeCaricate != null && !domandeCaricate.isEmpty()) {
            this.domande.addAll(domandeCaricate);
            System.out.println("Caricate " + domandeCaricate.size() + " domande"); 
            MappaDocumenti = this.sessione.getDocumenti();
            this.inizializzaTimer();
            cambioTesto();
        } else {
            System.out.println("Nessuna domanda caricata o lista vuota");
            return;
        }
        resetService(caricaSessione);
    });
    
    this.caricaSessione.setOnFailed(e -> {
        loadingOverlay.setVisible(false);
        progessBar.progressProperty().set(0);    
        System.out.println("Service fallito!");
        Throwable exception = caricaSessione.getException();
        if(exception != null) {
            exception.printStackTrace();
        } else {
            System.out.println("Nessuna eccezione specificata");
        }
        resetService(caricaSessione);
    });
}

    private void cambioTesto(){
        this.NomiDocumenti=this.sessione.getDocumenti().keySet().toArray(new String[0]);
        System.out.println("MappaDocumenti: "+MappaDocumenti);
        this.TestoDaLeggere.textProperty().setValue(new String(MappaDocumenti.get(NomiDocumenti[this.NumeroDiTesto])));
        //this.NumeroDiTesto++;
        System.out.println("NumeroDiTesto :"+ NumeroDiTesto +" this.NomiDocumenti.length: "+this.NomiDocumenti.length);
        this.contatoreLettura.setText(this.NumeroDiTesto+1+"/"+ this.NomiDocumenti.length);
    }
    
    private void cambioDomanda(){
         
        System.out.println("this.domande.size() ;"+ this.domande.size());
        if(this.domande.isEmpty()){
            this.FaseLettura.setVisible(false);
            this.FaseRisposte.setVisible(false);
            this.schermataGameOver.setVisible(true);
            this.sessione.aggiornaPuntiFatti(this.durata);
            System.out.println("this.sessione.getPunteggioFatto() "+ this.sessione.getPunteggioFatto());
            this.highScoreLabel.setText("Punteggio:"+ this.sessione.getPunteggioFatto());
            System.out.println("\n Verifica "+ this.sessione.getRisposte());
            cps.setDifficoltà(this.sessione.getLivello());
            cps.setEmail(stato.getUtente().getEmail());
            cps.setPunteggio(this.sessione.getPunteggioFatto());
            cps.setOnSucceeded(e->{
                resetService(cps);
            });
            cps.setOnFailed(e->{
                System.out.println("Service fallito CaricaPunteggioService  cps");
                 resetService(cps);
            });
            cps.start();
            
            eliminaSalvataggi();
        }else{
            this.question.setText(this.domande.element().testo);
            this.counter.setText(1+this.NumeroDiDomande-this.domande.size()+"/"+this.NumeroDiDomande);
            this.risposta1button.setText(this.domande.element().opzioni.get(0));
            this.risposta2button.setText(this.domande.element().opzioni.get(1));
            this.risposta3button.setText(this.domande.element().opzioni.get(2));
            this.risposta4button.setText(this.domande.element().opzioni.get(3));
        }
    }
    
    
    private void DaLetturaARisposte(){
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
    private void TestoPrecedente(ActionEvent event){  
        System.out.println("ProssimoTesto: "+ this.NumeroDiTesto);
        this.NumeroDiTesto--;
        if(this.NumeroDiTesto <0)
            this.NumeroDiTesto=this.NomiDocumenti.length-1;
        this.cambioTesto();
    }
 
    @FXML
    private void ProssimoTesto(ActionEvent event) {
        System.out.println("ProssimoTesto: "+ this.NumeroDiTesto);
         this.NumeroDiTesto++;
        if(this.NumeroDiTesto > this.NomiDocumenti.length-1)
            this.NumeroDiTesto=0;
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
    private void tornaAllaHome(ActionEvent event) throws IOException{
        stato.setSessionViewHomeButton(true);
        App.setRoot("AppView");
    }
    
    @FXML
    private void continuaGioco (ActionEvent event) throws IOException{
        stato.setSessionViewContinuaButton(true);
        App.setRoot("AppView");
    } 
    
    private void startTimer() {               
        setTimerLabel();
        tm.playFromStart();
    }
    
    private void inizializzaTimer() {
        tm = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            this.sessione.setDurata( this.sessione.getDurata()-1);
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
        if (service.getState() == Worker.State.SUCCEEDED || 
            service.getState() == Worker.State.FAILED || 
            service.getState() == Worker.State.CANCELLED) {

            service.reset();  // Resetta lo stato a READY
        }
    }

    private boolean eliminaSalvataggi() {
        boolean f1 = new File("SalvataggioDi"+this.sessione.getUtente().getEmail()+".ser").delete();
        System.out.println("Eliminazione file 1: "+ f1);
        boolean f2 =new File(("SalvataggioFaseGenerazioneDi"+this.sessione.getUtente().getEmail()+".ser")).delete();
        System.out.println("Eliminazione file 1: "+ f2);
        return f1 && f2;
    }

    private void caricaSessioneDaFileSalvato() {
         try {
            sessione.caricaSessioneDiGioco("SalvataggioDi"+stato.getUtente().getEmail()+".ser");
        } catch (IOException ex) {
            try {
                sessione.caricaSessioneDiGioco("SalvataggioFaseGenerazioneDi"+stato.getUtente().getEmail()+".ser");
            } catch (IOException ex1) {
                System.out.println("Notifica");
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
        
        System.out.println("getDomande:"+ this.sessione.getDomande());
        System.out.println("domande:"+ domande);
        System.out.println("this.sessione.getRisposte().size() "+this.sessione.getRisposte().size());
        System.out.println("this.sessione.getDurata(): "+ this.sessione.getDurata());
        if(this.sessione.getDurata()>0 && this.sessione.getRisposte().size()<1 ){
            cambioTesto();
            this.inizializzaTimer();
        }else{
            this.durata= this.sessione.getDurata();
            this.DaLetturaARisposte();
            this.cambioDomanda();
        }
    }
}

