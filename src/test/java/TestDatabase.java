
import it.unisa.diem.wordageddong17.database.Database;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Mattia Sanzari
 */
public class TestDatabase {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connection cn= Database.getInstance().getConnection();
        
        try {
            System.out.println("Sto aspettando un secondo");
            sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(TestDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
