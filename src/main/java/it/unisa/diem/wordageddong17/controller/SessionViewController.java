/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.service.CaricaSessioneDiGiocoService;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import it.unisa.diem.wordageddong17.service.CaricaPunteggioService;
import it.unisa.diem.wordageddong17.util.AlertUtils;
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
    private Label question;
    @FXML
    private RadioButton risposta1button;
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
    private Label timeLettura;
    @FXML
    private VBox schermataGameOver;
    @FXML
    private Label highScoreLabel;
    @FXML
    private Label contatoreLettura;

    private CaricaSessioneDiGiocoService caricaSessione;
    private SessioneDiGioco sessione;
    private Timeline tm;
    private int durata;// variabile che salva il tempo in più del giocatore 
    private AppState stato;
    private final CaricaPunteggioService cps = new CaricaPunteggioService();

    private Map<String, byte[]> mappaDocumenti;
    private String[] nomiDocumenti;
    private final Queue<Domanda> domande = new ConcurrentLinkedDeque<>();
    private int numeroDiDomande;
    private int numeroDiTesto;
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
        this.numeroDiDomande = 0;
        this.durata = 0;
        this.numeroDiTesto = 0;
        mappaDocumenti = new HashMap<>();
        this.stato = AppState.getInstance();

        if (this.stato.isSessioneSalvata()) {
            this.stato.setSessioneSalvata(false);
            this.inizializeConSalvataggio();
        } else {
            sessione = new SessioneDiGioco(stato.getUtente());
            sessione.setLivello(this.stato.getDifficolta());
            this.caricaSessione = new CaricaSessioneDiGiocoService(this.sessione, this.sessione.getLivello(),
                    this.stato.getLingue());
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
                mappaDocumenti = this.sessione.getDocumenti();
                this.inizializzaTimer();
                cambioTesto();
            } else {
                AlertUtils.mostraAlert("ERRORE", "Impossibile caricare le domande riprova più tardi",
                        Alert.AlertType.ERROR);
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
                System.getLogger(SessionViewController.class.getName())
                        .log(System.Logger.Level.ERROR, "Errore nel caricamento della sessione", exception);
            }
            AlertUtils.mostraAlert("ERRORE", "Impossibile caricare la sessione riprova più tardi",
                    Alert.AlertType.ERROR);
            resetService(caricaSessione);
        });
    }

    private void cambioTesto() {
        this.nomiDocumenti = this.sessione.getDocumenti().keySet().toArray(new String[0]);
        this.TestoDaLeggere.textProperty().setValue(
            new String(mappaDocumenti.get(nomiDocumenti[this.numeroDiTesto]), StandardCharsets.UTF_8)
        );
        this.contatoreLettura.setText(this.numeroDiTesto + 1 + "/" + this.nomiDocumenti.length);
    }

    private void cambioDomanda() {
        if (this.domande.isEmpty()) {
            if (tm != null) {
                tm.stop();
            }
            this.FaseLettura.setVisible(false);
            this.FaseRisposte.setVisible(false);
            this.schermataGameOver.setVisible(true);
            this.sessione.aggiornaPuntiFatti(this.durata);
            this.highScoreLabel.setText(String.valueOf(this.sessione.getPunteggioFatto()));
            cps.setDifficolta(this.sessione.getLivello());
            cps.setEmail(stato.getUtente().getEmail());
            cps.setPunteggio(this.sessione.getPunteggioFatto());
            cps.setOnSucceeded(e -> {
                resetService(cps);
            });
            cps.setOnFailed(e -> {
                AlertUtils.mostraAlert("Errore", "Impossibile salvare il punteggio.", Alert.AlertType.WARNING);
                resetService(cps);
            });
            cps.start();
            stampaResocontoDomande();
            eliminaSalvataggi();
        } else {
            Domanda corrente = this.domande.element();
            this.question.setText(corrente.testo);
            this.counter.setText((1 + this.numeroDiDomande - this.domande.size()) + "/" + this.numeroDiDomande);
            this.risposta1button.setText(corrente.opzioni.get(0));
            this.risposta2button.setText(corrente.opzioni.get(1));
            this.risposta3button.setText(corrente.opzioni.get(2));
            this.risposta4button.setText(corrente.opzioni.get(3));
        }
    }

    private void DaLetturaARisposte() {
        this.FaseLettura.setVisible(false);
        this.FaseRisposte.setVisible(true);
        this.numeroDiDomande = this.sessione.getNumeroDomande();
    }

    private void gestisciRisposta(int indice) {
        if (this.domande.isEmpty()) {
            return;
        }
        disabilitaPulsantiRisposta(true);
        try {
            this.aggiornaPilaDopoLaRisposta(indice);
            if (!this.domande.isEmpty()) {
                this.sessione.salvaSessioneDiGioco();
            }
            this.cambioDomanda();
        } finally {
            deselezionaPulsantiRisposta();
            disabilitaPulsantiRisposta(false);
        }
    }

    private void disabilitaPulsantiRisposta(boolean disabilita) {
        if (risposta1button != null) {
            risposta1button.setDisable(disabilita);
        }
        if (risposta2button != null) {
            risposta2button.setDisable(disabilita);
        }
        if (risposta3button != null) {
            risposta3button.setDisable(disabilita);
        }
        if (risposta4button != null) {
            risposta4button.setDisable(disabilita);
        }
    }

    private void deselezionaPulsantiRisposta() {
        if (risposta1button != null) {
            risposta1button.setSelected(false);
        }
        if (risposta2button != null) {
            risposta2button.setSelected(false);
        }
        if (risposta3button != null) {
            risposta3button.setSelected(false);
        }
        if (risposta4button != null) {
            risposta4button.setSelected(false);
        }
    }

    @FXML
    private void risposta1(ActionEvent event) {
        gestisciRisposta(0);
    }

    @FXML
    private void risposta2(ActionEvent event) {
        gestisciRisposta(1);
    }

    @FXML
    private void risposta3(ActionEvent event) {
        gestisciRisposta(2);
    }

    @FXML
    private void risposta4(ActionEvent event) {
        gestisciRisposta(3);
    }
    
    private void aggiornaPilaDopoLaRisposta(int i){
        this.sessione.aggiornaRisposte(this.numeroDiDomande-this.domande.size(), i);
        this.domande.remove();
    }

    @FXML
    private void TestoPrecedente(ActionEvent event) {
        this.numeroDiTesto--;
        if (this.numeroDiTesto < 0) {
            this.numeroDiTesto = this.nomiDocumenti.length - 1;
        }
        this.cambioTesto();
    }

    @FXML
    private void ProssimoTesto(ActionEvent event) {
        this.numeroDiTesto++;
        if (this.numeroDiTesto > this.nomiDocumenti.length - 1) {
            this.numeroDiTesto = 0;
        }
        this.cambioTesto();
    }

    @FXML
    private void VaiAlQuiz(ActionEvent event) {
        this.DaLetturaARisposte();
        this.cambioDomanda();
        this.durata=this.sessione.getDurata();
        if (tm != null) {
            tm.stop();
        }
    }

    @FXML
    private void tornaAllaHome(ActionEvent event) {
        if (tm != null) {
            tm.stop();
        }
        if (sessione != null && sessione.getUtente() != null && !domande.isEmpty()) {
            sessione.salvaSessioneDiGioco();
        }
        NavigationService.getInstance().navigateToApp(View.HOME);
    }

    @FXML
    private void continuaGioco(ActionEvent event) {
        if (tm != null) {
            tm.stop();
        }
        NavigationService.getInstance().navigateToApp(View.GAME_SETUP);
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
        if (tm != null) {
            tm.stop();
        }
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
        if (this.sessione != null && this.sessione.getUtente() != null) {
            return SessioneDiGioco.eliminaSalvataggio(this.sessione.getUtente().getEmail());
        } else if (stato.getUtente() != null) {
            return SessioneDiGioco.eliminaSalvataggio(stato.getUtente().getEmail());
        }
        return false;
    }

    private void caricaSessioneDaFileSalvato() {
        try {
            sessione.caricaSessioneDiGiocoPerEmail(stato.getUtente().getEmail());
        } catch (IOException ex) {
            System.getLogger(SessionViewController.class.getName())
                    .log(System.Logger.Level.ERROR, (String) null, ex);
            this.eliminaSalvataggi();
        }
    }
    
    private void inizializeConSalvataggio() {
        this.sessione = new SessioneDiGioco();
        caricaSessioneDaFileSalvato();
        this.numeroDiDomande = sessione.getNumeroDomande();
        this.mappaDocumenti = this.sessione.getDocumenti();
        // popola domande
        this.domande.addAll(
                this.sessione.getDomande().stream().skip((int) this.sessione.getRisposte().size())
                        .collect(Collectors.toList()));
        
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
        StringBuilder risultato = new StringBuilder();

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


