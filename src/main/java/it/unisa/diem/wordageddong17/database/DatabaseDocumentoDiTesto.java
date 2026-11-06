package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestione dei documenti di testo nel database (operazioni di inserimento,
 * cancellazione e lettura dei testi e metadati associati).
 */
public class DatabaseDocumentoDiTesto implements DAODocumentoDiTesto {

    private static final Logger LOGGER = Logger.getLogger(DatabaseDocumentoDiTesto.class.getName());
    private final Database db;

    private DatabaseDocumentoDiTesto() {
        this.db = Database.getInstance();
    }

    /**
     * Cancella un documento dal database specificandone il nome.
     *
     * @param nomeDocumento nome del documento da eliminare
     * @return true se l'eliminazione ha avuto successo
     */
    @Override
    public boolean cancellaTesto(String nomeDocumento) {
        String query = "DELETE FROM public.testo WHERE nome_file = ?;";
        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nomeDocumento);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new DataAccessException("Documento '" + nomeDocumento + "' non trovato per la cancellazione.");
            }
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore cancellazione testo", ex);
            throw new DataAccessException("Errore durante la cancellazione del documento '" + nomeDocumento + "'.", ex);
        }
    }

    /**
     * Carica un nuovo documento di testo nel database.
     *
     * @param documento metadati del documento
     * @param file contenuto in byte del documento
     * @return true se l'inserimento ha avuto successo
     */
    @Override
    public boolean caricaTesto(DocumentoDiTesto documento, byte[] file) {
        String query = """
                      INSERT INTO public.testo (nome_file, id_amministratore, difficolta, documento, lingua)
                      VALUES (?, ?, ?, ?, ?::lingua_type)
                      """;

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, documento.nomeFile());
            pstmt.setString(2, documento.emailAmministratore());
            pstmt.setString(3, documento.difficolta().getDbValue());
            pstmt.setBytes(4, file);
            pstmt.setString(5, documento.lingua().getDbValue());

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new DataAccessException("Nessun record inserito per il documento: " + documento.nomeFile());
            }
            return true;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore caricamento testo", ex);
            if ("23505".equals(ex.getSQLState())) {
                throw new DataAccessException("Esiste già un documento con il nome '" + documento.nomeFile() + "'.");
            }
            throw new DataAccessException(
                    "Errore durante il caricamento del documento nel database: " + ex.getMessage(), ex);
        }
    }

    /**
     * Recupera il contenuto in byte di un documento dato il nome.
     *
     * @param nomeDocumento nome del documento da recuperare
     * @return byte[] del documento, oppure null se non trovato
     */
    @Override
    public byte[] prendiTesto(String nomeDocumento) {
        String query = "SELECT documento FROM testo WHERE nome_file = ?;";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nomeDocumento);
            try (ResultSet r = pstmt.executeQuery()) {
                if (r.next()) {
                    return r.getBytes("documento");
                }
            }
            return null;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore lettura testo", ex);
            throw new DataAccessException("Impossibile recuperare il contenuto del documento: " + nomeDocumento, ex);
        }
    }

    /**
     * Recupera tutti i documenti presenti nel database (solo metadati, senza bytea).
     *
     * @return ArrayList di DocumentoDiTesto
     */
    @Override
    public ArrayList<DocumentoDiTesto> prendiTuttiIDocumenti() {
        ArrayList<DocumentoDiTesto> lista = new ArrayList<>();
        String query = "SELECT nome_file, id_amministratore, difficolta, lingua FROM testo ORDER BY nome_file ASC";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet result = pstmt.executeQuery()) {
            while (result.next()) {
                lista.add(new DocumentoDiTesto(
                        result.getString("nome_file"),
                        LivelloPartita.fromDbValue(result.getString("difficolta").trim()),
                        result.getString("id_amministratore"),
                        Lingua.fromDbValue(result.getString("lingua").trim())
                ));
            }
            return lista;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero tutti i documenti", ex);
            throw new DataAccessException("Impossibile recuperare l'elenco dei documenti dal database.", ex);
        }
    }

    /**
     * Restituisce una mappa che associa il nome del file alla relativa lingua,
     * filtrando per livello di difficoltà e per elenco di lingue supportate.
     *
     * @param livello il livello di difficoltà richiesto
     * @param lingue lista delle lingue selezionate
     * @return mappa (nome_file -> Lingua)
     */
    @Override
    public Map<String, Lingua> prendiNomiDocumentiFiltrati(LivelloPartita livello, ArrayList<Lingua> lingue) {
        Map<String, Lingua> mappaDocumenti = new HashMap<>();

        if (livello == null || lingue == null || lingue.isEmpty()) {
            return mappaDocumenti;
        }

        StringBuilder placeholdersBuilder = new StringBuilder();
        for (int i = 0; i < lingue.size(); i++) {
            placeholdersBuilder.append("?::lingua_type");
            if (i < lingue.size() - 1) {
                placeholdersBuilder.append(", ");
            }
        }
        String placeholdersPerLingue = placeholdersBuilder.toString();
        String query = "SELECT nome_file, lingua FROM testo WHERE difficolta = ? AND lingua IN ("
                + placeholdersPerLingue + ")";

        try (Connection conn = db.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, livello.getDbValue());

            for (int i = 0; i < lingue.size(); i++) {
                pstmt.setString(i + 2, lingue.get(i).getDbValue());
            }

            try (ResultSet result = pstmt.executeQuery()) {
                while (result.next()) {
                    mappaDocumenti.put(
                            result.getString("nome_file"),
                            Lingua.fromDbValue(result.getString("lingua").trim())
                    );
                }
            }
            return mappaDocumenti;
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Errore recupero documenti filtrati", ex);
            throw new DataAccessException("Impossibile recuperare i documenti filtrati per la sessione.", ex);
        }
    }

    private static class Holder {
        private static final DatabaseDocumentoDiTesto INSTANCE = new DatabaseDocumentoDiTesto();
    }

    /**
     * Restituisce l'istanza singleton di {@link DatabaseDocumentoDiTesto}.
     *
     * @return istanza condivisa
     */
    public static DatabaseDocumentoDiTesto getInstance() {
        return Holder.INSTANCE;
    }
}
