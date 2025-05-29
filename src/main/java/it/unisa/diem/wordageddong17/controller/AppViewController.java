/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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

/**
 * FXML Controller class
 *
 * @author giaro
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    private void toggleDashboard(MouseEvent event) {
    }
    
    @FXML
    private void logout(ActionEvent event) {
        // Nasconde tutte le schermate e mostra solo il login
        schermataDiLogin.setVisible(true);
        schermataHome.setVisible(false);
        schermataClassifiche.setVisible(false);
        schermataSelezioneDifficoltà.setVisible(false);
        dashboardMenu.setVisible(false);
        
        // Reset dei campi di login
        emailTextField.clear();
        passwordTextField.clear();
    } // TODO: Implementare il logout 

    @FXML
    private void chiudiClassificheOnAction(ActionEvent event) {
        schermataClassifiche.setVisible(false);
        contenutoHome.setVisible(true);
    }

    @FXML
    private void selezionaFacileButtonOnAction(ActionEvent event) {
        // Imposta la difficoltà su facile e inizia il gioco
        schermataSelezioneDifficoltà.setVisible(false);
        // TODO: Implementare l'avvio del gioco con difficoltà facile
    }

    @FXML
    private void selezionaMedioButtonOnAction(ActionEvent event) {
        // Imposta la difficoltà su medio e inizia il gioco
        schermataSelezioneDifficoltà.setVisible(false);
        // TODO: Implementare l'avvio del gioco con difficoltà media
    }

    @FXML
    private void selezionaDifficileButtonOnAction(ActionEvent event) {
        // Imposta la difficoltà su difficile e inizia il gioco
        schermataSelezioneDifficoltà.setVisible(false);
        // TODO: Implementare l'avvio del gioco con difficoltà difficile
    }
    
}
