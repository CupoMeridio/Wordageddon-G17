
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import it.unisa.diem.wordageddong17.model.TipoUtente;
import it.unisa.diem.wordageddong17.model.Utente;
import java.util.Arrays;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Mattia Sanzari
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String parola = "abbastanza";
        String stringheStopWords = "a\n" +
                                    "abbastanza\n" +
                                    "abbia\n" +
                                    "abbiamo\n" +
                                    "abbiano\n";

        String[] parole = stringheStopWords.split("[\\s,\\n.;]+");
        System.out.println("parole: "+ parole[0]);
        boolean containsExactWord = Arrays.asList(parole).contains(parola);

        System.out.println("Contiene la parola esatta? " + containsExactWord); // Restituisce true
        
        
       /* SessioneDiGioco sessione= new SessioneDiGioco(1, 30, new Utente("mattia","sanzari",TipoUtente.giocatore), 1);
        sessione.salvaSessioneDiGioco("SalvataggioDimattiasanzari2003@gmail.com.ser");
        */
        SessioneDiGioco sessione2= new SessioneDiGioco();
        sessione2.caricaSessioneDiGioco("SalvataggioDimattiasanzari2003@gmail.com.ser");
        
        System.out.println(sessione2);
        
    }
    
}
