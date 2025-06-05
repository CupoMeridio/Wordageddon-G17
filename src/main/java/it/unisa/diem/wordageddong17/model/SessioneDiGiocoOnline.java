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
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class SessioneDiGiocoOnline extends SessioneDiGioco {
    
    private DosSessioneDiGioco db;
    private int numeroDocumenti;
    private LivelloPartita livello;
    
    public SessioneDiGiocoOnline(int numeroDomande,int numeroDocumenti, LivelloPartita livello) {
        super(numeroDomande);
        db= DatabaseSessioneDiGioco.getInstance();
        this.numeroDocumenti=numeroDocumenti;
        this.livello=livello;
    }


    @Override
    public void generaDocumenti(LivelloPartita l) {
        Map<String, byte[]> documentiDifficolta = db.prendiDocumenti(l);
        List<String> chiavi= new ArrayList(documentiDifficolta.keySet());
        
        Random rnd = new Random();
        
        
        if(documentiDifficolta != null || !documentiDifficolta.isEmpty()){
            for(int i=0; i<this.numeroDocumenti; i++){
                int numerocasuale=rnd.nextInt(this.numeroDocumenti);
                this.addDocumenti(chiavi.get(numerocasuale), documentiDifficolta.get(chiavi.get(numerocasuale)));
            }
        }else{
            System.out.println("Non ci sono documenti con quella difficoltà");
        }   
    }
    
    
    private void SalvaDocumentiInLocale(String nomedoc, byte[] documento){
    
         try (FileOutputStream fos = new FileOutputStream(nomedoc)) {
            fos.write(documento);
            System.out.println("Scrittura completata!");
        } catch (IOException e) {
            System.err.println("Errore durante la scrittura del file: " + e.getMessage());
        }
    }
   
    @Override
    protected Task<List<Domanda>> createTask() {
       
        return new Task<List<Domanda>>(){
            @Override
            protected List<Domanda> call() throws Exception {
               
                updateProgress(0, 100);
                SessioneDiGiocoOnline.this.generaDocumenti(SessioneDiGiocoOnline.this.livello);
                updateProgress(60, 100);
                SessioneDiGiocoOnline.this.generaDomande();
                return  SessioneDiGiocoOnline.this.getDomande();
            }  
        };
    }

    
}
