
import it.unisa.diem.wordageddong17.model.AnalisiDocumenti;
import it.unisa.diem.wordageddong17.service.CaricaSessioneDiGiocoService;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author Mattia Sanzari
 */
public class Test_stop_words {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AnalisiDocumenti analisi = new  AnalisiDocumenti();
        
        analisi.setStopWords("Ciao Mattia Ti  voglio bene".getBytes());
        
        System.out.println("1 analisi.appartenenzaStopWords: "+ analisi.appartenenzaStopWords(""));
        System.out.println("2 analisi.appartenenzaStopWords: "+ analisi.appartenenzaStopWords("aittaM"));
        System.out.println("3 analisi.appartenenzaStopWords: "+ analisi.appartenenzaStopWords("Mattia"));
    }
    
}
