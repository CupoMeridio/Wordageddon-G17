package it.unisa.diem.wordageddong17.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import io.github.cdimascio.dotenv.Dotenv;


/**
 * La classe {@code Database} gestisce la connessione al database PostgreSQL utilizzando il pattern Singleton
 * con l'idioma Lazy Holder (informazioni reperite dalla seguente pagina: https://www.javaboss.it/singleton-design-pattern/).
 * Le credenziali di accesso sono ottenute da variabili d'ambiente per garantire la sicurezza delle informazioni sensibili.
 * (informazioni reperite dalla seguente pagina: https://stackoverflow.com/questions/11823233/how-to-set-environment-variable-in-netbeans)
 * <p>
 * La connessione viene inizializzata al primo accesso e chiusa automaticamente alla terminazione del programma
 * tramite una shutdown hook.
 * </p>
 */
public class Database {

    /**
     * Connessione attiva al database.
     */
    private Connection connection;
    
    /**
     * Recupero delle credenziali per la connessione al database dal file .env escluso dal git.
     */
    Dotenv dotenv = Dotenv.load();
    
    /**
     * Nome del database, ottenuto dalla variabile d'ambiente {@code DBNAME}.
     */
    private final String dbname = dotenv.get("DBNAME");

    /**
     * Nome utente per l'accesso al database, ottenuto dalla variabile d'ambiente {@code USERNAME}.
     */
    private final String username = dotenv.get("USER");

    /**
     * Password per l'accesso al database, ottenuta dalla variabile d'ambiente {@code PASSWORD}.
     */
    private final String password = dotenv.get("PASSWORD");

    /**
     * Costruttore privato che inizializza la connessione al database.
     * <p>
     * Verifica la presenza delle variabili d'ambiente necessarie e stabilisce la connessione utilizzando link DriverManager}.
     * Inoltre viene impostato un sistema di terminazione della connessione alla chiusura del programma.
     * </p>
     *
     * @throws IllegalStateException se una delle variabili d'ambiente {@code DBNAME}, {@code USERNAME} o {@code PASSWORD} non è impostata.
     * @throws RuntimeException se si verifica un errore durante la connessione al database.
     */
    private Database() {
        if (dbname == null || username == null || password == null) {
            throw new IllegalStateException("Variabili d'ambiente DBNAME, USERNAME o PASSWORD non sono impostate.");
        }

        try {
            String url = "jdbc:mysql://localhost:3306/" + dbname + "?useSSL=false&serverTimezone=UTC";
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connessione al database riuscita.");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore nella connessione al database.", e);
        }

        // Chiude la connessione quando il programma termina
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                    System.out.println("Connessione al database chiusa.");
                }
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }));
    }

    /**
     * Classe statica interna che contiene l'istanza Singleton della classe {@code Database}.
     * <p>
     * L'istanza viene creata solo quando la classe {@code Holder} viene caricata, garantendo la thread safety.
     * </p>
     */
    private static class Holder {
        private static final Database INSTANCE = new Database();
    }

    /**
     * Restituisce l'istanza Singleton della classe {@code Database}.
     * <p>
     * Utilizza il pattern del Lazy Holder per garantire l'inizializzazione pigra e la thread safety
     * senza la necessità di sincronizzazione esplicita.
     * </p>
     *
     * @return l'istanza Singleton della classe {@code Database}.
     */
    public static Database getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Restituisce la connessione attiva al database.
     *
     * @return la connessione {@link Connection} attiva al database.
     */
    public Connection getConnection() {
        return connection;
    }
}
