package it.unisa.diem.wordageddong17.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;

/**
 * Servizio per la gestione e la persistenza del tema grafico dell'applicazione
 * (Tema Scuro e Tema Chiaro con accenti del fuoco).
 */
public final class ThemeService {

    private static final Logger LOGGER = Logger.getLogger(ThemeService.class.getName());

    /**
     * Enumerazione dei temi disponibili.
     */
    public enum Theme {
        /** Tema scuro con accenti fuoco (predefinito). */
        DARK,
        /** Tema chiaro con accenti fuoco. */
        LIGHT
    }

    /** Classe CSS applicata alla radice per attivare il tema chiaro. */
    public static final String LIGHT_THEME_CLASS = "light-theme";

    private static final String PREFS_DIR = System.getProperty("user.home") + File.separator + ".wordageddon";
    private static final String PREFS_FILE = PREFS_DIR + File.separator + "theme.properties";
    private static final String PREFS_KEY = "activeTheme";

    private static final ThemeService INSTANCE = new ThemeService();

    private Theme currentTheme = Theme.DARK;
    private Scene scene;
    private final List<Consumer<Theme>> themeChangeListeners = new ArrayList<>();

    private ThemeService() {
        loadPreference();
    }

    /**
     * Restituisce l'istanza singleton di {@link ThemeService}.
     *
     * @return istanza condivisa
     */
    public static ThemeService getInstance() {
        return INSTANCE;
    }

    /**
     * Inizializza il servizio associandolo alla scena principale di JavaFX.
     * Ascolta i cambi di radice della scena per mantenere il tema sincronizzato.
     *
     * @param scene Scena principale dell'applicazione.
     */
    public void init(Scene scene) {
        this.scene = scene;
        if (scene != null) {
            scene.rootProperty().addListener((obs, oldRoot, newRoot) -> {
                if (newRoot != null) {
                    applyCurrentTheme(newRoot);
                }
            });
            applyCurrentTheme(scene.getRoot());
        }
    }

    /**
     * Restituisce il tema correntemente attivo.
     *
     * @return {@link Theme} attivo
     */
    public Theme getCurrentTheme() {
        return currentTheme;
    }

    /**
     * Indica se il tema attivo è quello scuro.
     *
     * @return {@code true} se il tema attivo è DARK, {@code false} altrimenti
     */
    public boolean isDark() {
        return currentTheme == Theme.DARK;
    }

    /**
     * Alterna tra tema scuro e tema chiaro.
     */
    public void toggleTheme() {
        setTheme(currentTheme == Theme.DARK ? Theme.LIGHT : Theme.DARK);
    }

    /**
     * Imposta il tema specificato, applicandolo alla scena e salvando la preferenza.
     *
     * @param theme Nuovo tema da applicare.
     */
    public void setTheme(Theme theme) {
        if (theme == null || this.currentTheme == theme) {
            return;
        }
        this.currentTheme = theme;
        if (scene != null) {
            applyCurrentTheme(scene.getRoot());
        }
        savePreference();
        notifyThemeChange();
    }

    /**
     * Applica il tema a un DialogPane JavaFX per armonizzare i dialoghi modali.
     *
     * @param dialog Dialogo a cui applicare lo stile del tema.
     */
    public void applyThemeToDialog(Dialog<?> dialog) {
        if (dialog != null && dialog.getDialogPane() != null) {
            applyCurrentTheme(dialog.getDialogPane());
        }
    }

    /**
     * Registra un listener che viene notificato quando cambia il tema.
     *
     * @param listener Callback che riceve il nuovo tema.
     */
    public void addThemeChangeListener(Consumer<Theme> listener) {
        if (listener != null && !themeChangeListeners.contains(listener)) {
            themeChangeListeners.add(listener);
        }
    }

    /**
     * Rimuove un listener precedentemente registrato.
     *
     * @param listener Callback da rimuovere.
     */
    public void removeThemeChangeListener(Consumer<Theme> listener) {
        themeChangeListeners.remove(listener);
    }

    /**
     * Applica la classe di stile corrispondente al nodo radice indicato.
     *
     * @param root Nodo radice della vista o del dialogo.
     */
    private void applyCurrentTheme(Parent root) {
        if (root == null) {
            return;
        }
        Runnable applyTask = () -> {
            if (currentTheme == Theme.LIGHT) {
                if (!root.getStyleClass().contains(LIGHT_THEME_CLASS)) {
                    root.getStyleClass().add(LIGHT_THEME_CLASS);
                }
            } else {
                root.getStyleClass().remove(LIGHT_THEME_CLASS);
            }
        };

        if (Platform.isFxApplicationThread()) {
            applyTask.run();
        } else {
            Platform.runLater(applyTask);
        }
    }

    private void notifyThemeChange() {
        for (Consumer<Theme> listener : new ArrayList<>(themeChangeListeners)) {
            try {
                listener.accept(currentTheme);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Errore durante la notifica del cambio tema", ex);
            }
        }
    }

    private void loadPreference() {
        File file = new File(PREFS_FILE);
        if (!file.exists()) {
            return;
        }
        try (FileInputStream fis = new FileInputStream(file)) {
            Properties props = new Properties();
            props.load(fis);
            String saved = props.getProperty(PREFS_KEY);
            if (saved != null) {
                try {
                    currentTheme = Theme.valueOf(saved.toUpperCase(Locale.ROOT));
                } catch (IllegalArgumentException ex) {
                    currentTheme = Theme.DARK;
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.FINE, "Impossibile caricare le preferenze del tema", ex);
        }
    }

    private void savePreference() {
        try {
            File dir = new File(PREFS_DIR);
            if (!dir.exists() && !dir.mkdirs()) {
                return;
            }
            Properties props = new Properties();
            props.setProperty(PREFS_KEY, currentTheme.name());
            try (FileOutputStream fos = new FileOutputStream(PREFS_FILE)) {
                props.store(fos, "Wordageddon - Theme Preference");
            }
        } catch (IOException ex) {
            LOGGER.log(Level.WARNING, "Impossibile salvare le preferenze del tema", ex);
        }
    }
}
