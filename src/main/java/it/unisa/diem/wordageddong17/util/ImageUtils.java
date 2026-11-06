package it.unisa.diem.wordageddong17.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Window;

/**
 * Utility per la manipolazione, ritaglio e caricamento sicuro di immagini in JavaFX.
 */
public final class ImageUtils {

    private static final long MAX_IMAGE_SIZE_BYTES = 5 * 1024 * 1024; // 5 MB

    private ImageUtils() {}

    /**
     * Restituisce l'immagine segnaposto standard dell'applicazione.
     *
     * @return {@link Image} del placeholder utente.
     */
    public static Image getPlaceholderImage() {
        return new Image(ImageUtils.class.getResource("/imgs/person.png").toExternalForm());
    }

    /**
     * Converte un array di byte in un oggetto {@link Image}. Se l'array è nullo o vuoto,
     * restituisce il placeholder predefinito.
     *
     * @param img Byte dell'immagine.
     * @return Istanza di {@link Image}.
     */
    public static Image getImageFromByte(byte[] img) {
        if (img == null || img.length == 0) {
            return getPlaceholderImage();
        }
        return new Image(new ByteArrayInputStream(img));
    }

    /**
     * Applica un ritaglio quadrato centrato all'immagine visualizzata in un {@link ImageView}.
     *
     * @param imageView Componente grafico da ritagliare.
     * @param dimensione Dimensione lato del quadrato in pixel.
     */
    public static void ritagliaQuadrato(ImageView imageView, double dimensione) {
        if (imageView == null) {
            return;
        }
        imageView.setFitWidth(dimensione);
        imageView.setFitHeight(dimensione);
        imageView.setPreserveRatio(true);

        Rectangle clip = new Rectangle(dimensione, dimensione);
        imageView.setClip(clip);

        imageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                double imgWidth = newImg.getWidth();
                double imgHeight = newImg.getHeight();
                double viewportSize = Math.min(imgWidth, imgHeight);
                double xOffset = (imgWidth - viewportSize) / 2;
                double yOffset = (imgHeight - viewportSize) / 2;
                imageView.setViewport(new Rectangle2D(xOffset, yOffset, viewportSize, viewportSize));
            }
        });
    }

    /**
     * Applica un ritaglio circolare centrato all'immagine visualizzata in un {@link ImageView}.
     *
     * @param imageView Componente grafico da ritagliare.
     * @param dimensione Diametro del cerchio in pixel.
     */
    public static void ritagliaCerchio(ImageView imageView, double dimensione) {
        if (imageView == null) {
            return;
        }
        imageView.setFitWidth(dimensione);
        imageView.setFitHeight(dimensione);
        imageView.setPreserveRatio(true);

        Circle clip = new Circle(dimensione / 2, dimensione / 2, dimensione / 2);
        imageView.setClip(clip);

        imageView.imageProperty().addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                double imgWidth = newImg.getWidth();
                double imgHeight = newImg.getHeight();
                double viewportSize = Math.min(imgWidth, imgHeight);
                double xOffset = (imgWidth - viewportSize) / 2;
                double yOffset = (imgHeight - viewportSize) / 2;
                imageView.setViewport(new Rectangle2D(xOffset, yOffset, viewportSize, viewportSize));
            }
        });
    }

    /**
     * Verifica che un file immagine sia valido e non superi la dimensione massima consentita.
     *
     * @param file File da convalidare.
     * @return {@code true} se valido, {@code false} altrimenti.
     */
    public static boolean validaImmagine(File file) {
        if (file == null) {
            return false;
        }
        if (file.length() > MAX_IMAGE_SIZE_BYTES) {
            AlertUtils.mostraAlert("Errore", "L'immagine è troppo grande (max 5MB)", Alert.AlertType.ERROR);
            return false;
        }
        try {
            Image test = new Image(file.toURI().toString(), 10, 10, true, true);
            if (test.isError()) {
                AlertUtils.mostraAlert("Errore", "File immagine non valido", Alert.AlertType.ERROR);
                return false;
            }
            return true;
        } catch (Exception e) {
            AlertUtils.mostraAlert("Errore", "Impossibile leggere il file: " + e.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    /**
     * Apre un selettore di file per immagini e carica il file selezionato nell'ImageView specificato.
     *
     * @param imgv ImageView di destinazione.
     * @param owner Finestra genitore.
     * @return Array di byte del file immagine letto, oppure {@code null} se annullato o non valido.
     */
    public static byte[] scegliImmagine(ImageView imgv, Window owner) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un'immagine");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        try {
            File file = fileChooser.showOpenDialog(owner);
            if (file == null) {
                return null;
            }
            if (!validaImmagine(file)) {
                return null;
            }

            byte[] imageBytes = Files.readAllBytes(file.toPath());
            Image image = new Image(file.toURI().toString());

            Platform.runLater(() -> {
                if (imgv != null) {
                    imgv.setImage(image);
                }
                AlertUtils.mostraAlert("Successo", "Immagine caricata correttamente", Alert.AlertType.INFORMATION);
            });

            return imageBytes;
        } catch (IOException e) {
            AlertUtils.mostraAlert("Errore", "Errore di lettura: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        } catch (SecurityException e) {
            AlertUtils.mostraAlert("Errore", "Permessi insufficienti per leggere il file", Alert.AlertType.ERROR);
            return null;
        } catch (Exception e) {
            AlertUtils.mostraAlert("Errore", "Errore inaspettato: " + e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }
}
