package it.unisa.diem.wordageddong17;

/**
 * Questa classe funge da punto d'ingresso alternativo per l'applicazione JavaFX.
 * <p>
 * È stata introdotta esclusivamente per ovviare a un bug di NetBeans, in cui il file jar
 * generato non riconosceva correttamente i componenti JavaFX. Senza questa classe,
 * lanciare l'applicazione potrebbe non funzionare come previsto.
 * </p>
 *
 * <p>
 * La classe semplicemente inoltra la chiamata al metodo <code>main</code> della classe {@link App}.
 * </p>
 *
 * @see App
 */
public class RunMain {

    /**
     * Metodo principale che lancia l'applicazione.
     *
     * @param args gli argomenti della linea di comando
     */
    public static void main(String[] args) {
        App.main(args);
    }
}
