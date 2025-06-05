package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.database.DatabaseSessioneDiGioco;
import it.unisa.diem.wordageddong17.database.DosSessioneDiGioco;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class SessioneDiGiocoOnline extends SessioneDiGioco {
    
    private DosSessioneDiGioco db;
    private int numeroDocumenti;
    
    public SessioneDiGiocoOnline(int numeroDomande,int numeroDocumenti, int durata) {
        super(numeroDomande, durata);
        db= DatabaseSessioneDiGioco.getInstance();
        this.numeroDocumenti=numeroDocumenti;
    }


    @Override
    public void generaDocumenti(LivelloPartita l) {
        Map<String, byte[]> documentiDifficolta = db.prendiDocumenti(l);
        List<String> chiavi= new ArrayList<>(documentiDifficolta.keySet());
        
        Random rnd = new Random();
        System.out.println("generaDocumenti: " +documentiDifficolta);
        
        if(documentiDifficolta != null && !documentiDifficolta.isEmpty()){
            for(int i=0; i<this.numeroDocumenti; i++){
                int numerocasuale=rnd.nextInt(this.numeroDocumenti);
                this.addDocumenti(chiavi.get(numerocasuale), documentiDifficolta.get(chiavi.get(numerocasuale))); 
                this.generaAnalisi( chiavi.get(numerocasuale),documentiDifficolta.get(chiavi.get(numerocasuale)));
            }
        }else{
            System.out.println("Non ci sono documenti con quella difficoltà");
        }   
    }
    
    private void generaAnalisi(String NomeDocumento,byte[] doc){
        try {
            this.getAnalisi().analisiUnDocumento(doc, NomeDocumento);
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGiocoOnline.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //per ora non è utile-> nel caso si elimina
    private void SalvaDocumentiInLocale(String nomedoc, byte[] documento){
    
         try (FileOutputStream fos = new FileOutputStream(nomedoc)) {
            fos.write(documento);
            System.out.println("Scrittura completata!");
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }
}
