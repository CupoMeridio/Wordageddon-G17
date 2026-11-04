package it.unisa.diem.wordageddong17.util;

import java.util.Optional;
import java.util.function.Consumer;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Utility per la visualizzazione centralizzata dei dialoghi e degli alert JavaFX.
 * Garantisce l'esecuzione sicura sul thread grafico JavaFX.
 */
public final class AlertUtils {

    private AlertUtils() {}

    /**
     * Mostra una finestra di dialogo standard sul thread JavaFX.
     *
     * @param titolo Titolo dell'alert.
     * @param messaggio Testo del messaggio.
     * @param tipo Tipologia di alert.
     */
    public static void mostraAlert(String titolo, String messaggio, Alert.AlertType tipo) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> mostraAlert(titolo, messaggio, tipo));
            return;
        }
        Alert alert = new Alert(tipo);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    /**
     * Mostra una finestra di dialogo di conferma con pulsanti "OK" e "ANNULLA".
     *
     * @param titolo Titolo del dialogo.
     * @param messaggio Testo della richiesta di conferma.
     * @param onResult Callback invocata con {@code true} se confermato ("OK"), altrimenti {@code false}.
     */
    public static void mostraAlertConferma(String titolo, String messaggio, Consumer<Boolean> onResult) {
        if (!Platform.isFxApplicationThread()) {
            Platform.runLater(() -> mostraAlertConferma(titolo, messaggio, onResult));
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.getButtonTypes().setAll(new ButtonType("OK"), new ButtonType("ANNULLA"));
        Optional<ButtonType> result = alert.showAndWait();
        boolean confirmed = result.isPresent() && "OK".equalsIgnoreCase(result.get().getText());
        if (onResult != null) {
            onResult.accept(confirmed);
        }
    }
}
