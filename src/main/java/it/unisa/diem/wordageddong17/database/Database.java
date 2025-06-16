package it.unisa.diem.wordageddong17.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.github.cdimascio.dotenv.Dotenv;


/**
 * La classe {@code Database} gestisce la connessione al database PostgreSQL 
 * utilizzando il pattern Singleton con il Lazy Holder idiom.
 * 
 * Le credenziali di accesso sono ottenute da variabili d'ambiente per garantire la sicurezza delle informazioni sensibili.  
 * La connessione viene inizializzata al primo accesso e chiusa automaticamente alla terminazione del programma.
 */
public class Database {

    private Connection connection;
    private static final Logger LOGGER = Logger.getLogger(Database.class.getName());
    private final Dotenv dotenv = Dotenv.load();
    private final String dbname = dotenv.get("DBNAME");
    private final String username = dotenv.get("USER");
    private final String password = dotenv.get("PASSWORD");

    /**
     * Costruttore privato che inizializza la connessione al database.
     *
     * @throws IllegalStateException Se una delle variabili d'ambiente {@code DBNAME}, {@code USERNAME} o {@code PASSWORD} non è impostata.
     * @throws RuntimeException Se si verifica un errore durante la connessione al database.
     */
    private Database() {
        if (dbname == null || username == null || password == null) {
            throw new IllegalStateException("Variabili d'ambiente DBNAME, USERNAME o PASSWORD non impostate.");
        }

        try {
            String url = "jdbc:postgresql://wordageddon-cupomeridio.i.aivencloud.com:17446/" + dbname + "?ssl=require&user=" + username + "&password=" + password;
            connection = DriverManager.getConnection(url);
            LOGGER.info("Connessione al database riuscita.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore nella connessione al database.", e);
            throw new RuntimeException("Errore nella connessione al database.", e);
        }

        // Chiude la connessione quando il programma termina
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    LOGGER.info("Connessione al database chiusa.");
                }
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Errore durante la chiusura della connessione al database.", ex);
            }
        }));
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
     * @return Istanza Singleton della classe {@code Database}.
     */
    public static Database getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Restituisce la connessione attiva al database.
     *
     * @return Connessione {@link Connection} attiva al database.
     */
    public Connection getConnection() {
        return connection;
    }
}
