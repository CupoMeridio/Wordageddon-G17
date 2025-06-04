/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.App;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
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
    
    private SessioneDiGioco sessione;

    private int contatoreDomanda = 1;
    private Timeline tm;
    private int durata;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        this.TestoDaLeggere.setEditable(false);
        this.TestoDaLeggere.setMouseTransparent(true);
        this.TestoDaLeggere.setFocusTraversable(false);
        this.FaseRisposte.setVisible(false);
        this.FaseLettura.setVisible(true);
        sessione= new SessioneDiGioco();
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
    private void TestoPrecedente(ActionEvent event) {
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
    }

    
}
