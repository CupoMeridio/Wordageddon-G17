/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.App;
import it.unisa.diem.wordageddong17.model.CaricaSessioneDiGioco;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.SessioneDiGiocoOnline;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
    private AnchorPane anchorPane;
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
    private Label timer;
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
    
    private CaricaSessioneDiGioco caricaSessione;
    private SessioneDiGioco sessione;
    private int contatoreDomanda = 1;
    private Timeline tm;
    private int durata;
    private String[] NomiDocumenti;
    private final Queue<Domanda> domande = new ConcurrentLinkedDeque<>();
    
    private int NumeroDiDomanda;
    /**
     * Initializes the controller class.
     */
    @Override
   public void initialize(URL url, ResourceBundle rb) {
    this.TestoDaLeggere.setEditable(false);
    this.TestoDaLeggere.setMouseTransparent(true);
    this.TestoDaLeggere.setFocusTraversable(false);
    this.FaseRisposte.setVisible(false);
    this.FaseLettura.setVisible(true);
    this.NumeroDiDomanda=0;
    this.counter.setText("1/"+4);
    
    this.durata = 30; // o qualsiasi valore appropriato
    
    sessione = new SessioneDiGiocoOnline(4, 1, durata);
    this.caricaSessione = new CaricaSessioneDiGioco(this.sessione, LivelloPartita.FACILE);
    this.serviceInitialize();
    this.caricaSessione.start();
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
           
       //System.out.println("grandezzaPop: "+ this.domande.toString());
        Map<String, byte[]> s = this.sessione.getDocumenti();
        
        this.NomiDocumenti=this.sessione.getDocumenti().keySet().toArray(new String[0]);
        //System.out.println("139: " + s);
        this.TestoDaLeggere.textProperty().setValue(new String(s.get(NomiDocumenti[0])));
    }
    
    private void DaRisposte(){
        this.FaseLettura.setVisible(false);
        this.FaseRisposte.setVisible(true);
    }

    @FXML
    private void risposta1(ActionEvent event) {
    }

    @FXML
    private void risposta2(ActionEvent event) {
    }

    @FXML
    private void risposta3(ActionEvent event) {
    }

    @FXML
    private void risposta4(ActionEvent event) {
    }

    @FXML
    private void TestoPrecedente(ActionEvent event){
    }

    @FXML
    private void VaiAlQuiz(ActionEvent event) {
    }

    @FXML
    private void ProssimoTesto(ActionEvent event) {
    }
    
    public void displayQuestion(){
        
        this.question.textProperty().setValue("Qui andrà la domanda");
    
    }
   /* 
    private void startTimer() {
        durata = 30;
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
        timer.setText(String.valueOf(this.durata));
    }
    
     private void handleTimeout() {
        tm.stop();
        int numeroDomande=0;
        // classedomande

        if (this.contatoreDomanda < numeroDomande) {
           
            this.contatoreDomanda++;
            displayQuestion();
            counter.textProperty().setValue(this.contatoreDomanda + "/" + numeroDomande);
            this.durata = 30;
            this.startTimer();
        } else {
            
            try {
                App.setRoot("AppViewController");// Qui serve la schermata delle classifiche
            } catch (IOException ex) {
                Logger.getLogger(SessionViewController.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
    }*/

    
}
