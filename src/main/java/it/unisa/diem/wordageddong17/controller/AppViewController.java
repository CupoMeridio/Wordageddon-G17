package it.unisa.diem.wordageddong17.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class AppViewController implements Initializable {

    @FXML
    private TextField emailTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Button accediButton;
    @FXML
    private Button passaARegistratiButton;
    @FXML
    private VBox schermataDiLogin;
    @FXML
    private AnchorPane schermataHome;
    @FXML
    private Button startButton;
    @FXML
    private Button classificheButton;
    @FXML
    private StackPane root;
    @FXML
    private VBox contenutoHome;
    @FXML
    private Label benvenutoLabel;
    @FXML
    private ImageView fotoProfilo;
    @FXML
    private VBox dashboardMenu;
    @FXML
    private HBox quickInfoUtente;
    @FXML
    private VBox schermataClassifiche;
    @FXML
    private TableView<?> facileTable;
    @FXML
    private TableView<?> mediaTable;
    @FXML
    private TableView<?> difficileTable;
    @FXML
    private Button chiudiClassificheButton;
    @FXML
    private VBox schermataSelezioneDifficoltà;
    @FXML
    private Button selezionaFacileButton;
    @FXML
    private Button selezionaMedioButton;
    @FXML
    private Button selezionaDifficileButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    


    @FXML
    private void toggleDashboard(MouseEvent event) {
    }

    @FXML
    private void logout(ActionEvent event) {
    }

    @FXML
    private void accediOnAction(ActionEvent event) {
    }

    @FXML
    private void passaARegistratiOnAction(ActionEvent event) {
    }

    @FXML
    private void startOnAction(ActionEvent event) {
    }

    @FXML
    private void classificheOnAction(ActionEvent event) {
    }

    @FXML
    private void chiudiClassificheOnAction(ActionEvent event) {
    }

    @FXML
    private void selezionaFacileButtonOnAction(ActionEvent event) {
    }

    @FXML
    private void selezionaMedioButtonOnAction(ActionEvent event) {
    }

    @FXML
    private void selezionaDifficileButtonOnAction(ActionEvent event) {
    }
    
}
