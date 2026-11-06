package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.Utente;
import it.unisa.diem.wordageddong17.service.LoginService;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import it.unisa.diem.wordageddong17.util.ValidationUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * Controller per la schermata di autenticazione (Login).
 */
public class LoginController {

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public LoginController() {}

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button accediButton;

    @FXML
    private Button passaARegistratiButton;

    private final AppState appState = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    @FXML
    private void accediOnAction(ActionEvent event) {
        if (accediButton.isDisabled()) {
            return;
        }

        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            AlertUtils.mostraAlert("Errore", "I campi non possono essere vuoti", Alert.AlertType.WARNING);
            return;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            AlertUtils.mostraAlert("Errore", "Inserisci un indirizzo email valido.", Alert.AlertType.WARNING);
            return;
        }

        accediButton.setDisable(true);
        passaARegistratiButton.setDisable(true);
        nav.setLoading(true);

        LoginService loginService = new LoginService();
        loginService.setEmail(email);
        loginService.setPassword(password);

        loginService.setOnSucceeded(workerStateEvent -> {
            accediButton.setDisable(false);
            passaARegistratiButton.setDisable(false);
            nav.setLoading(false);

            Utente utente = loginService.getValue();
            if (utente != null) {
                appState.setUtente(utente);
                nav.navigateTo(View.HOME);
            } else {
                AlertUtils.mostraAlert("Errore", "Email o password errati", Alert.AlertType.WARNING);
            }
        });

        loginService.setOnFailed(workerStateEvent -> {
            accediButton.setDisable(false);
            passaARegistratiButton.setDisable(false);
            nav.setLoading(false);

            Throwable ex = loginService.getException();
            String msg = (ex != null && ex.getMessage() != null)
                    ? ex.getMessage()
                    : "Si è verificato un errore durante la connessione al database. "
                            + "Verifica che PostgreSQL sia attivo.";
            AlertUtils.mostraAlert("Errore Connessione", msg, Alert.AlertType.ERROR);
        });

        loginService.start();
    }

    @FXML
    private void passaARegistratiOnAction(ActionEvent event) {
        nav.navigateTo(View.REGISTRATION);
    }
}
