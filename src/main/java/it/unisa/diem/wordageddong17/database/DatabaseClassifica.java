package it.unisa.diem.wordageddong17.database;

import it.unisa.diem.wordageddong17.model.Classifica;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseClassifica {
    private Database db;  
    
    
    private DatabaseClassifica() {
        db= Database.getInstance();
    }
    
    
    private List<Classifica> prendiClassifica(String difficolta){
        List<Classifica> L= new ArrayList<>();
        String query= "SELECT utente.username, MAX(punti) AS miglior_punteggio, difficolta\n"
                + "FROM punteggio\n"
                + "JOIN utente ON utente.email = punteggio.email_utente"
                + "WHERE \n difficolta = ?"
                + "GROUP BY email_utente, difficolta"
                + "ORDER BY miglior_punteggio DESC;";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(query)) {
            pstmt.setString(1, difficolta);
            ResultSet result = pstmt.executeQuery();
            while(result.next()){
                L.add(new Classifica(result.getString("username"),result.getTimestamp("data"),result.getFloat("punti"),LivelloPartita.Valueof(result.getString("difficolta")));
            }
        }
        catch (SQLException ex) {
            Logger.getLogger(DatabaseRegistrazioneLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        return L;
    }
    
}
