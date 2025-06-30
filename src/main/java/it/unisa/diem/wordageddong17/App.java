package it.unisa.diem.wordageddong17;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.image.Image;

/**
 * Punto di ingresso principale dell'applicazione JavaFX Wordageddon.
 * <p>
 * Questa classe inizializza e avvia l'applicazione impostando il primary stage con una scena
 * caricata tramite un file FXML (di default "AppView.fxml"). Il stage viene massimizzato, viene
 * assegnato un titolo e un'icona personalizzata. Inoltre, la classe fornisce un metodo per modificare
 * dinamicamente la radice della scena durante il runtime.
 * </p>
 *
 * @see javafx.application.Application
 * @see javafx.stage.Stage
 * @see javafx.scene.Scene
 * @see FXMLLoader
 */
public class App extends Application {

    /**
     * La scena principale dell'applicazione.
     */
    private static Scene scene;

    /**
     * Avvia l'applicazione JavaFX.
     * <p>
     * Questo metodo viene invocato automaticamente al lancio dell'applicazione. Carica il file FXML
     * iniziale (per impostazione predefinita "AppView.fxml"), configura la scena e il primary stage,
     * massimizza la finestra, imposta il titolo e aggiunge l'icona personalizzata.
     * </p>
     *
     * @param stage il primary stage dell'applicazione
     * @throws IOException se il file FXML non può essere caricato
     */
    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("AppView"));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Wordageddon");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/imgs/Wordageddon_Logo.png")));
        stage.show();
    }

    /**
     * Modifica la radice della scena corrente caricando un nuovo file FXML.
     * <p>
     * Questo metodo permette di cambiare dinamicamente il contenuto della scena durante l'esecuzione
     * dell'applicazione. È sufficiente specificare il nome del file FXML (senza l'estensione ".fxml")
     * da caricare come nuova radice.
     * </p>
     *
     * @param fxml il nome del file FXML da caricare, senza estensione
     * @throws IOException se il file FXML non può essere caricato
     */
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carica il file FXML specificato.
     * <p>
     * Questo metodo helper costruisce il percorso del file FXML basandosi sul nome fornito e lo carica.
     * Il file è ubicato nella cartella "/fxml/" e si assume che abbia l'estensione ".fxml".
     * </p>
     *
     * @param fxml il nome del file FXML da caricare, senza estensione
     * @return il nodo radice {@link Parent} ottenuto dal caricamento del file FXML
     * @throws IOException se il file FXML non viene trovato o non può essere caricato
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    /**
     * Metodo principale che avvia l'applicazione JavaFX.
     *
     * @param args argomenti della linea di comando (non utilizzati)
     */
    public static void main(String[] args) {
        launch();
    }
    
    /**
     * Costruttore di default per la classe App.
     */
    public App() {
        super();
    }
}
