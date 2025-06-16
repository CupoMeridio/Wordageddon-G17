import java.io.File;
import java.util.Arrays;


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
       /*
        SessioneDiGioco sessione2= new SessioneDiGioco();
        sessione2.caricaSessioneDiGioco("SalvataggioDimattiasanzari2003@gmail.com.ser");
        
        System.out.println(sessione2);*/
        System.out.print("Esiste il file 1 ? "+((new File("SalvataggioDi"+"mattiasanzari2003@gmail.com"+".ser").exists()) || 
                (new File("SalvataggioFaseGenerazioneDi"+"mattiasanzari2003@gmail.com"+".ser").exists())));
        
         System.out.print("Esiste il file 2 ? "+((new File("SalvataggioFaseGenerazioneDi"+"mattiasanzari2003@gmail.com"+".ser").exists())));
        
    }
    private boolean verificaEsistenzaSalvataggio(){
        return (new File("SalvataggioDi"+"mattiasanzari2003@gmail.com"+".ser").exists()) || 
                (new File("SalvataggioFaseGenerazioneDi"+"mattiasanzari2003@gmail.com"+".ser").exists());
    }
    
}
