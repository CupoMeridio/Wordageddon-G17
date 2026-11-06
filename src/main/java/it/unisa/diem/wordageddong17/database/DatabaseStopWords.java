package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.ListaStopWords;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementazione del DAO per la gestione della lista di stop words.
 */
public class DatabaseStopWords implements DAOListaStopWords {

    private static final Logger LOGGER = Logger.getLogger(DatabaseStopWords.class.getName());
    private final Database db;

    private DatabaseStopWords() {
        this.db = Database.getInstance();
    }

    /**
     * Carica le stopwords nel database, aggiornando il record se già esistente.
     *
     * @param listaStopWords metadati della lista
     * @param documentoStopwords contenuto in byte del file di stopwords
     * @return true se l'operazione ha avuto successo
     */
    @Override
    public boolean caricareStopwords(ListaStopWords listaStopWords, byte[] documentoStopwords) {
        String query = """
                       INSERT INTO stopwords (nome_file, documento, id_amministratore)
                       VALUES (?, ?, ?)
                       ON CONFLICT(nome_file) DO UPDATE SET documento = ?, id_amministratore = ?,
                       data_ultima_modifica = CURRENT_TIMESTAMP
                       """;

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, listaStopWords.getNomeFile());
            pstmt.setBytes(2, documentoStopwords);
            pstmt.setString(3, listaStopWords.getAmministratore());
            pstmt.setBytes(4, documentoStopwords);
            pstmt.setString(5, listaStopWords.getAmministratore());

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new DataAccessException("Nessun record aggiornato per la lista di stopwords.");
            }
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore caricamento stopwords", ex);
            throw new DataAccessException("Impossibile salvare le stopwords nel database: " + ex.getMessage(), ex);
        }
    }

    /**
     * Recupera il contenuto delle stopwords dato il nome file.
     *
     * @param nomeFile il nome identificativo del file di stopwords
     * @return byte[] del documento o null se non trovato
     */
    @Override
    public byte[] prendiStopwords(String nomeFile) {
        String query = "SELECT documento FROM stopwords WHERE nome_file = ?;";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nomeFile);
            try (ResultSet r = pstmt.executeQuery()) {
                if (r.next()) {
                    return r.getBytes("documento");
                }
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore lettura stopwords", ex);
            throw new DataAccessException("Impossibile leggere le stopwords dal database: " + nomeFile, ex);
        }
    }

    /**
     * Recupera i metadati della lista di stopwords.
     *
     * @param nomeFile il nome del file
     * @return istanza di ListaStopWords o null se non trovato
     */
    @Override
    public ListaStopWords prendiInfoStopwords(String nomeFile) {
        String query = "SELECT nome_file, id_amministratore, data_ultima_modifica FROM stopwords WHERE nome_file = ?;";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nomeFile);
            try (ResultSet r = pstmt.executeQuery()) {
                if (r.next()) {
                    return new ListaStopWords(
                            r.getString("nome_file"),
                            r.getString("id_amministratore"),
                            r.getTimestamp("data_ultima_modifica")
                    );
                }
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore lettura metadati stopwords", ex);
            throw new DataAccessException("Impossibile recuperare i metadati delle stopwords: " + nomeFile, ex);
        }
    }

    private static class Holder {
        private static final DatabaseStopWords INSTANCE = new DatabaseStopWords();
    }

    /**
     * Restituisce l'istanza singleton di {@link DatabaseStopWords}.
     *
     * @return istanza condivisa
     */
    public static DatabaseStopWords getInstance() {
        return Holder.INSTANCE;
    }
}
