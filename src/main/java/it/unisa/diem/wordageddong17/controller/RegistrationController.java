package it.unisa.diem.wordageddong17.controller;

import it.unisa.diem.wordageddong17.model.AppState;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import it.unisa.diem.wordageddong17.service.InserisciUtenteService;
import it.unisa.diem.wordageddong17.service.NavigationService;
import it.unisa.diem.wordageddong17.service.NavigationService.View;
import it.unisa.diem.wordageddong17.service.PrendiUtenteService;
import it.unisa.diem.wordageddong17.service.ThemeService;
import it.unisa.diem.wordageddong17.util.AlertUtils;
import it.unisa.diem.wordageddong17.util.ImageUtils;
import it.unisa.diem.wordageddong17.util.ValidationUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Controller per la schermata di registrazione di un nuovo utente.
 */
public class RegistrationController implements Initializable {

    /**
     * Costruttore predefinito utilizzato da FXMLLoader.
     */
    public RegistrationController() {}

    @FXML
    private TextField username;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;

    @FXML
    private PasswordField repeatPassword;

    @FXML
    private Button registerButton;

    @FXML
    private Button registerPageLogin;

    @FXML
    private Button themeToggleButton;

    @FXML
    private ImageView imageView;

    private byte[] immagineBytes;
    private final AppState appState = AppState.getInstance();
    private final NavigationService nav = NavigationService.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageUtils.ritagliaQuadrato(imageView, 208);
        updateThemeButtonText();
    }

    @FXML
    private void toggleTheme(ActionEvent event) {
        ThemeService.getInstance().toggleTheme();
        updateThemeButtonText();
    }

    private void updateThemeButtonText() {
        if (themeToggleButton != null) {
            boolean isDark = ThemeService.getInstance().isDark();
            themeToggleButton.setText(isDark ? "☀️ Tema Chiaro" : "🌙 Tema Scuro");
        }
    }

    @FXML
    private void caricaIMG(MouseEvent event) {
        byte[] bytes = ImageUtils.scegliImmagine(imageView,
                imageView.getScene() != null ? imageView.getScene().getWindow() : null);
        if (bytes != null) {
            this.immagineBytes = bytes;
        }
    }

    @FXML
    private void registerButtonOnAction(ActionEvent event) {
        String userTxt = username.getText();
        String emailTxt = email.getText();
        String passTxt = password.getText();
        String repPassTxt = repeatPassword.getText();

        if (userTxt.isEmpty() || emailTxt.isEmpty() || passTxt.isEmpty() || repPassTxt.isEmpty()) {
            AlertUtils.mostraAlert("Errore", "Tutti i campi sono obbligatori.", Alert.AlertType.WARNING);
            return;
        }

        if (!ValidationUtils.isValidUsername(userTxt)) {
            AlertUtils.mostraAlert("Username non valido",
                    "Lo username non può essere vuoto e deve contenere al massimo "
                            + ValidationUtils.USERNAME_MAX_LENGTH + " caratteri.",
                    Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidEmail(emailTxt)) {
            AlertUtils.mostraAlert("Email non valida", "L'email inserita non è valida.", Alert.AlertType.ERROR);
            return;
        }

        if (!ValidationUtils.isValidPassword(passTxt)) {
            AlertUtils.mostraAlert("Password non valida",
                    "La password deve contenere almeno " + ValidationUtils.PASSWORD_MIN_LENGTH
                            + " caratteri, tra cui almeno una lettera e una cifra.",
                    Alert.AlertType.ERROR);
            return;
        }

        if (!passTxt.equals(repPassTxt)) {
            AlertUtils.mostraAlert("Password non corrispondenti", "Le due password inserite non corrispondono",
                    Alert.AlertType.ERROR);
            return;
        }

        registerButton.setDisable(true);
        registerPageLogin.setDisable(true);
        nav.setLoading(true);

        PrendiUtenteService pus = new PrendiUtenteService();
        pus.setEmail(emailTxt);

        pus.setOnSucceeded(e -> {
            if (pus.getValue() != null) {
                registerButton.setDisable(false);
                registerPageLogin.setDisable(false);
                nav.setLoading(false);
                AlertUtils.mostraAlert("Email già utilizzata", "Questa email è già associata a un altro account.",
                        Alert.AlertType.ERROR);
                return;
            }

            InserisciUtenteService ius = new InserisciUtenteService();
            ius.setEmail(emailTxt);
            ius.setUsername(userTxt);
            ius.setPassword(passTxt);
            ius.setImmagineBytes(immagineBytes);

            ius.setOnFailed(ev -> {
                registerButton.setDisable(false);
                registerPageLogin.setDisable(false);
                nav.setLoading(false);
                Throwable ex = ius.getException();
                String msg = (ex != null && ex.getMessage() != null)
                        ? ex.getMessage()
                        : "Errore durante la registrazione.";
                AlertUtils.mostraAlert("Errore Registrazione", msg, Alert.AlertType.ERROR);
            });

            ius.setOnSucceeded(ev -> {
                registerButton.setDisable(false);
                registerPageLogin.setDisable(false);
                nav.setLoading(false);

                Utente nuovoUtente;
                if (immagineBytes == null) {
                    nuovoUtente = new Utente(userTxt, emailTxt, TipoUtente.GIOCATORE);
                } else {
                    nuovoUtente = new Utente(userTxt, emailTxt, immagineBytes, TipoUtente.GIOCATORE);
                }

                appState.setUtente(nuovoUtente);
                AlertUtils.mostraAlert("Registrazione completata", "Registrazione avvenuta con successo!",
                        Alert.AlertType.INFORMATION);
                nav.navigateTo(View.HOME);
            });

            ius.start();
        });

        pus.setOnFailed(e -> {
            registerButton.setDisable(false);
            registerPageLogin.setDisable(false);
            nav.setLoading(false);
            AlertUtils.mostraAlert("Errore", "Errore durante il controllo dell'email.", Alert.AlertType.ERROR);
        });

        pus.start();
    }

    @FXML
    private void passaALogin(ActionEvent event) {
        nav.navigateTo(View.LOGIN);
    }
}
