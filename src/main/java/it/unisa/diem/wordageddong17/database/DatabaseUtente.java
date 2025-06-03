package it.unisa.diem.wordageddong17.database;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Gestione operazioni utente come modifica del nome utente o della foto profilo.
 */
public class DatabaseUtente implements DosUtente {

    private final Database db;

    private DatabaseUtente() {
        db = Database.getInstance();
    }

    @Override
    public boolean modificaUsername(String email, String username) {
        String query = "UPDATE utente SET username=? WHERE email=?";
        boolean risultato = false;

        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            int updated = pstmt.executeUpdate();
            risultato = (updated == 1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseUtente.class.getName()).log(Level.SEVERE, null, ex);
        }

        return risultato;
    }

    @Override
    public boolean modificaFotoProfilo(String email, byte[] fotoProfilo) {
        Connection conn = db.getConnection();

        try {
            conn.setAutoCommit(false); // Inizia la transazione

            String sql = "UPDATE utente SET foto_profilo = ? WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                if (fotoProfilo != null) {
                    stmt.setBinaryStream(1, new ByteArrayInputStream(fotoProfilo), fotoProfilo.length);
                } else {
                    stmt.setNull(1, Types.BLOB);
                }
                stmt.setString(2, email);

                int righe = stmt.executeUpdate();
                if (righe != 1) {
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseUtente.class.getName()).log(Level.SEVERE, "Errore durante rollback", ex);
            }
            Logger.getLogger(DatabaseUtente.class.getName()).log(Level.SEVERE, "Errore aggiornamento foto", e);
            return false;

        } finally {
            try {
                conn.setAutoCommit(true); // Non chiudere la connessione condivisa
            } catch (SQLException e) {
                Logger.getLogger(DatabaseUtente.class.getName()).log(Level.WARNING, "Errore ripristino autoCommit", e);
            }
        }
    }

    private byte[] getFotoProfilo(String email, Connection conn) throws SQLException {
        String sql = "SELECT foto_profilo FROM utente WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBytes("foto_profilo");
            }
        }
        return null;
    }

    // Singleton pattern con classe interna
    private static class Holder {
        private static final DatabaseUtente INSTANCE = new DatabaseUtente();
    }

    public static DatabaseUtente getInstance() {
        return Holder.INSTANCE;
    }
}
