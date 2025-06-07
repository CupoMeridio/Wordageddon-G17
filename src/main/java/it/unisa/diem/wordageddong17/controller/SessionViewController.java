/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.App;
import it.unisa.diem.wordageddong17.service.CaricaSessioneDiGiocoService;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
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
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

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
    private int durata;
    private AppState stato;
    
    
   
    private String[] NomiDocumenti;
    private final Queue<Domanda> domande = new ConcurrentLinkedDeque<>();
    
    private int NumeroDiDomande;
    private int NumeroDiTesto;
    /**
     * Initializes the controller class.
     */
    @Override
   public void initialize(URL url, ResourceBundle rb) {
    
    this.stato = AppState.getInstance();
    this.TestoDaLeggere.setEditable(false);
    this.TestoDaLeggere.setMouseTransparent(true);
    this.TestoDaLeggere.setFocusTraversable(false);
    this.FaseRisposte.setVisible(false);
    this.schermataGameOver.setVisible(false);
    this.FaseLettura.setVisible(true);
    this.NumeroDiDomande=0;
    sessione = new SessioneDiGioco(4, durata, stato.getUtente(),1);
    this.caricaSessione = new CaricaSessioneDiGiocoService(this.sessione, LivelloPartita.FACILE);
    this.serviceInitialize();
    this.caricaSessione.start();
    this.inizializzaTimer();
    startTimer();
    
}    

private void serviceInitialize() {
    this.caricaSessione.setOnRunning(e -> {
        System.out.println("Service in esecuzione...");
    });
    
    this.caricaSessione.setOnSucceeded(e -> {
        System.out.println("Service completato con successo!");
        List<Domanda> domandeCaricate = caricaSessione.getValue();
        
        if(domandeCaricate != null && !domandeCaricate.isEmpty()) {
            this.domande.addAll(domandeCaricate);
            System.out.println("Caricate " + domandeCaricate.size() + " domande");      
            cambioTesto();
        } else {
            System.out.println("Nessuna domanda caricata o lista vuota");
            // Gestisci il caso in cui non ci sono domande
        }
    });
    
    this.caricaSessione.setOnFailed(e -> {
        System.out.println("Service fallito!");
        Throwable exception = caricaSessione.getException();
        if(exception != null) {
            exception.printStackTrace();
        } else {
            System.out.println("Nessuna eccezione specificata");
        }
    });
}

    private void cambioTesto(){
        Map<String, byte[]> s = this.sessione.getDocumenti();
        this.NomiDocumenti=this.sessione.getDocumenti().keySet().toArray(new String[0]);
        this.TestoDaLeggere.textProperty().setValue(new String(s.get(NomiDocumenti[this.NumeroDiTesto])));
        this.NumeroDiTesto++;
        System.out.println("NumeroDiTesto :"+ NumeroDiTesto +" this.NomiDocumenti.length: "+this.NomiDocumenti.length);
        this.contatoreLettura.setText(this.NumeroDiTesto+"/"+ this.NomiDocumenti.length);
    }
    
    private void cambioDomanda(){
        
        
        
        System.out.println("this.domande.size() ;"+ this.domande.size());
        if(this.domande.isEmpty()){
            this.FaseLettura.setVisible(false);
            this.FaseRisposte.setVisible(false);
            this.schermataGameOver.setVisible(true);
            this.sessione.aggiornaPuntiFatti();
            this.highScoreLabel.setText("Punteggio:"+ this.sessione.getPunteggioFatto());
            System.out.println("\n Verifica "+ this.sessione.getRisposte());
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
        this.NumeroDiDomande=this.domande.size();
    }

    @FXML
    private void risposta1(ActionEvent event) {
        this.sessione.aggiornaRisposte(this.NumeroDiDomande-this.domande.size(), 0);
        this.domande.remove();
        this.risposta1button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void risposta2(ActionEvent event) {
        this.sessione.aggiornaRisposte(this.NumeroDiDomande-this.domande.size(), 1);
        this.domande.remove();
        this.risposta2button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void risposta3(ActionEvent event) {
        this.sessione.aggiornaRisposte(this.NumeroDiDomande-this.domande.size(), 2);
        this.domande.remove();
        this.risposta3button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void risposta4(ActionEvent event) {
        this.sessione.aggiornaRisposte(this.NumeroDiDomande-this.domande.size(), 3);
        this.domande.remove();
        this.risposta4button.setSelected(false);
        this.cambioDomanda();
    }

    @FXML
    private void TestoPrecedente(ActionEvent event){  
        this.NumeroDiTesto--;
        if(this.NumeroDiTesto <0)
            this.NumeroDiTesto=this.NomiDocumenti.length;
        this.cambioTesto();
    }

    @FXML
    private void VaiAlQuiz(ActionEvent event) {
        this.DaLetturaARisposte();
        this.cambioDomanda();
    }

    @FXML
    private void ProssimoTesto(ActionEvent event) {
         this.NumeroDiTesto++;
        if(this.NumeroDiTesto > this.NomiDocumenti.length)
            this.NumeroDiTesto=this.NomiDocumenti.length;
        this.cambioTesto();
    }
     
    @FXML
    private void tornaAllaHome(ActionEvent event) throws IOException{
        stato.setSessionViewHomeButton(true);
        App.setRoot("AppView");
    }
    
    private void continuaGioco (ActionEvent event) throws IOException{
        stato.setSessionViewContinuaButton(true);
        App.setRoot("AppView");
    } 
    
    private void startTimer() {
        durata = 30;               // durata ------------------------> Da settare in base al livello
        setTimerLabel();
        tm.playFromStart();
    }
    
    private void inizializzaTimer() {
        tm = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            durata--;
            setTimerLabel();
            if (durata <= 0) {
                handleTimeout();
            }
        }));
        tm.setCycleCount(Timeline.INDEFINITE);
        startTimer();
    }
     
    private void setTimerLabel() {
        timeLettura.setText(String.valueOf(this.durata));
    }

    private void handleTimeout() {
        tm.stop();
        this.DaLetturaARisposte();
        this.cambioDomanda();
    }
}

