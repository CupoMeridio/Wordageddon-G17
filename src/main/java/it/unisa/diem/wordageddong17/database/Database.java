package it.unisa.diem.wordageddong17.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * La classe {@code Database} gestisce il pool di connessioni verso PostgreSQL.
 *
 * <p>La configurazione viene letta, in ordine di priorità, da:</p>
 * <ol>
 *   <li>variabili d'ambiente {@code DB_URL}, {@code DB_USER}, {@code DB_PASSWORD};</li>
 *   <li>file {@code .env} nella directory di esecuzione (solo per sviluppo locale);</li>
 *   <li>valori di default pensati per il {@code docker-compose.yml} del progetto.</li>
 * </ol>
 *
 * <p>La classe adotta il pattern Singleton con Lazy Holder. Il pool viene chiuso
 * automaticamente alla terminazione della JVM.</p>
 */
public class Database {

    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    /** Configurazione di default per il database locale avviato con docker compose. */
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/wordageddon";
    private static final String DEFAULT_USER = "wordageddon";
    private static final String DEFAULT_PASSWORD = "wordageddon";

    private final HikariDataSource dataSource;

    /**
     * Costruttore privato che inizializza il pool di connessioni.
     *
     * @throws IllegalStateException se il pool non può essere inizializzato
     */
    private Database() {
        Dotenv dotenv = loadDotenv();

        String url = firstNonBlank(
                System.getenv("DB_URL"),
                dotenv != null ? dotenv.get("DB_URL") : null,
                DEFAULT_URL
        );
        String username = firstNonBlank(
                System.getenv("DB_USER"),
                dotenv != null ? dotenv.get("DB_USER") : null,
                DEFAULT_USER
        );
        String password = firstNonBlank(
                System.getenv("DB_PASSWORD"),
                dotenv != null ? dotenv.get("DB_PASSWORD") : null,
                DEFAULT_PASSWORD
        );

        if (DEFAULT_URL.equals(url)) {
            LOGGER.warning("DB_URL non impostata: uso il database locale di default " + DEFAULT_URL);
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setPoolName("wordageddon-pool");
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(10_000);
        config.setValidationTimeout(5_000);
        config.setInitializationFailTimeout(10_000);

        try {
            this.dataSource = new HikariDataSource(config);
            LOGGER.info(() -> "Pool di connessioni inizializzato verso " + url);
        } catch (RuntimeException ex) {
            LOGGER.log(Level.SEVERE, "Errore nell'inizializzazione del pool di connessioni.", ex);
            throw new IllegalStateException(
                    "Impossibile inizializzare il database. Verifica DB_URL, DB_USER e DB_PASSWORD.", ex);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::close, "wordageddon-db-shutdown"));
    }

    /**
     * Carica il file {@code .env} se presente, senza rendere obbligatoria la sua esistenza.
     *
     * @return l'istanza di {@link Dotenv}, oppure {@code null} se il file non è leggibile
     */
    private static Dotenv loadDotenv() {
        try {
            return Dotenv.configure().ignoreIfMissing().load();
        } catch (RuntimeException ex) {
            LOGGER.log(Level.WARNING, "Impossibile leggere il file .env: verranno usate le variabili d'ambiente.", ex);
            return null;
        }
    }

    /**
     * Restituisce il primo valore non nullo e non vuoto.
     *
     * @param values valori da controllare
     * @return il primo valore valido, oppure {@code null}
     */
    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }

    /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     */
    private static class Holder {
        private static final Database INSTANCE = new Database();
    }

    /**
     * Restituisce l'istanza Singleton della classe {@code Database}.
     *
     * @return istanza Singleton della classe {@code Database}
     */
    public static Database getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Restituisce una connessione dal pool. La connessione deve essere chiusa dal chiamante
     * (preferibilmente tramite try-with-resources) per essere restituita al pool.
     *
     * @return una connessione attiva
     * @throws SQLException se non è possibile ottenere una connessione
     */
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Chiude il pool di connessioni.
     */
    public void close() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOGGER.info("Pool di connessioni chiuso.");
        }
    }
}
