package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

/**
 * Controller per la schermata di configurazione partita (selezione difficoltà e lingue).
 */
public class GameSetupController implements Initializable {

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public GameSetupController() {}

    @FXML
    private RadioButton radioFacile;

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

    private final AppState stato = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    @FXML
    private void iniziaPartitaOnAction(ActionEvent event) {
        LivelloPartita livello = null;
        if (radioFacile.isSelected()) {
            livello = LivelloPartita.FACILE;
        }
        if (radioMedio.isSelected()) {
            livello = LivelloPartita.MEDIO;
        }
        if (radioDifficile.isSelected()) {
            livello = LivelloPartita.DIFFICILE;
        }

        if (livello == null) {
            AlertUtils.mostraAlert("Selezione obbligatoria", "Devi selezionare un livello di difficoltà!",
                    Alert.AlertType.WARNING);
            return;
        }
        stato.setDifficolta(livello);

        ArrayList<Lingua> lingue = new ArrayList<>();
        if (checkIT.isSelected()) {
            lingue.add(Lingua.ITALIANO);
        }
        if (checkEN.isSelected()) {
            lingue.add(Lingua.INGLESE);
        }
        if (checkFR.isSelected()) {
            lingue.add(Lingua.FRANCESE);
        }
        if (checkDE.isSelected()) {
            lingue.add(Lingua.TEDESCO);
        }
        if (checkES.isSelected()) {
            lingue.add(Lingua.SPAGNOLO);
        }

        if (lingue.isEmpty()) {
            AlertUtils.mostraAlert("Selezione obbligatoria", "Devi selezionare almeno una lingua!",
                    Alert.AlertType.WARNING);
            return;
        }
        stato.setLingue(lingue);

        nav.navigateToSession();
    }

    @FXML
    private void tornaAllaHome(ActionEvent event) {
        nav.navigateTo(View.HOME);
    }
}
