package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseClassifica implements DosClassifica {
    private Database db;  
    
    
    private DatabaseClassifica(){
        db= Database.getInstance();
    }
    
    @Override
    public List<Classifica> prendiClassifica(LivelloPartita difficolta) {
        List<Classifica> L = new ArrayList<>();
        String query = "SELECT utente.username, MAX(data) AS data, MAX(punti) AS miglior_punteggio, difficolta\n"
                + "FROM punteggio\n"
                + "JOIN utente ON utente.email = punteggio.email_utente\n"
                + "WHERE difficolta = ?\n"
                + "GROUP BY utente.username, difficolta\n"
                + "ORDER BY miglior_punteggio DESC;";
    
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            // Use the database-friendly value
            pstmt.setString(1, difficolta.getDbValue());
        
            ResultSet result = pstmt.executeQuery();
            while(result.next()) {
                L.add(new Classifica(
                    result.getString("username"),
                    result.getTimestamp("data"),
                    result.getFloat("miglior_punteggio"),
                    LivelloPartita.fromDbValue(result.getString("difficolta"))));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseClassifica.class.getName()).log(Level.SEVERE, "Error loading leaderboard", ex);
        }
        return L;
    }
    
    private static class Holder {
        private static final DatabaseClassifica INSTANCE = new DatabaseClassifica();
    }
    
    public static DatabaseClassifica getInstance() {
        return DatabaseClassifica.Holder.INSTANCE;
    }
}
