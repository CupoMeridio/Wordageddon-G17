package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseDocumentoDiTesto;
import it.unisa.diem.wordageddong17.database.DatabaseStopWords;
import it.unisa.diem.wordageddong17.database.MancanzaDiDocumenti;
import it.unisa.diem.wordageddong17.interfaccia.DAODocumentoDiTesto;
import it.unisa.diem.wordageddong17.interfaccia.DAOListaStopWords;
import it.unisa.diem.wordageddong17.model.AnalisiDocumenti;
import it.unisa.diem.wordageddong17.model.GeneratoreDomande;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import it.unisa.diem.wordageddong17.model.SessioneDiGioco;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
public class CaricaSessioneDiGiocoService extends Service<List<GeneratoreDomande.Domanda>>  {
    
    private SessioneDiGioco sessione;
    private LivelloPartita livello;
    private DAOListaStopWords dbSW;
    private final DAODocumentoDiTesto dbDT;
    private ArrayList<Lingua> lingua;
    private AnalisiDocumenti analisi;

    public CaricaSessioneDiGiocoService(SessioneDiGioco sessione, LivelloPartita livello,ArrayList<Lingua> lingua) {
        this();
        this.sessione = sessione;
        this.livello = livello;
        this.lingua=lingua; 
    }

    public CaricaSessioneDiGiocoService() {
        this.dbDT= DatabaseDocumentoDiTesto.getInstance();
        this.dbSW= DatabaseStopWords.getInstance();
        this.analisi= new AnalisiDocumenti();
    }
    

    public void setSessione(SessioneDiGioco sessione) {
        this.sessione = sessione;
    }

    public void setLivello(LivelloPartita livello) {
        this.livello = livello;
    }

    public void setLingua(ArrayList<Lingua> lingua) {
        this.lingua = lingua;
    }

    public void setAnalisi(AnalisiDocumenti analisi) {
        this.analisi = analisi;
    }

    
    
    public AnalisiDocumenti getAnalisi() {
        return analisi;
    }

    
    public SessioneDiGioco getSessione() {
        return sessione;
    }

    public LivelloPartita getLivello() {
        return livello;
    }

    public DAOListaStopWords getDbSW() {
        return dbSW;
    }

    public DAODocumentoDiTesto getDbDT() {
        return dbDT;
    }
    
    private int calcolaDurata(LivelloPartita l){
    int durata = this.livello.getCalcolaDurataPerLivello();
          
    int numeroParole= this.sessione.getDocumenti().values().stream().mapToInt(b->(new String(b).split("[\\s,\\n.;!?]+").length)).sum();
    
    return (int) (durata+(numeroParole*l.getMoltiplicatorePerLivello()));
    }
    
    private Map<String, byte[]> prendiDocumentiCasuali( ArrayList<String> nomiDocumenti, LivelloPartita l){
        Map<String, byte[]> documentiEstratti = new HashMap<>();
        Collections.shuffle(nomiDocumenti);
        int numeroDoc;
        if(l.getNumeroDocumenti()>nomiDocumenti.size()){
           numeroDoc= nomiDocumenti.size();
        }else{
            numeroDoc=l.getNumeroDocumenti();
        }
        for(int i=0; i<numeroDoc; i++ ){ 
            documentiEstratti.put(nomiDocumenti.get(i),
                                this.dbDT.prendiTesto(nomiDocumenti.get(i)));
            this.generaAnalisi(nomiDocumenti.get(i), documentiEstratti.get(nomiDocumenti.get(i)));
        }
        return documentiEstratti;
    }
    
    
    public void generaAnalisi(String NomeDocumento,byte[] doc){
        try {
            this.getAnalisi().analisiUnDocumento(doc, NomeDocumento);
        } catch (IOException ex) {
            System.getLogger(SessioneDiGioco.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
    }
    
    
    public List<GeneratoreDomande.Domanda> generaDomande( Map<String,byte[]> Documenti, AnalisiDocumenti analisi, int numeroDomande) throws MancanzaDiDocumenti {
        GeneratoreDomande gen= new GeneratoreDomande(analisi);
        String[] nomiDocumenti =  Documenti.keySet().toArray(new String[0]);
        if(nomiDocumenti.length<=0){
            throw new MancanzaDiDocumenti("Non ci sono abbastanza documenti");
        }
        int domandePerDocumento = numeroDomande / nomiDocumenti.length;
        int documentiExtra = numeroDomande % nomiDocumenti.length;
          List<GeneratoreDomande.Domanda> Domande = new ArrayList<>();
        for(int i=0; i< nomiDocumenti.length;i++){
            Domande.addAll(gen.getRaccoltaDiDomande(domandePerDocumento+(i < documentiExtra ? 1 : 0),nomiDocumenti[i]));
            System.out.println("generaDomande:   "+ Domande);
        }   
        return Domande;
    }
   
    @Override
    protected Task<List<GeneratoreDomande.Domanda>> createTask() {
       
        return new Task<List<GeneratoreDomande.Domanda>>(){
            @Override
            protected List<GeneratoreDomande.Domanda> call() throws Exception {
                
                updateProgress(0, 100);
                CaricaSessioneDiGiocoService.this.sessione.setNumeroDocumenti(CaricaSessioneDiGiocoService.this.livello.getNumeroDocumenti());
                CaricaSessioneDiGiocoService.this.sessione.setNumeroDomande(CaricaSessioneDiGiocoService.this.livello.getNumeroDomande());
                updateProgress(30, 100);
                CaricaSessioneDiGiocoService.this.getAnalisi().setStopWords(dbSW.PrendiStopwords("ListaStopwords"));
                updateProgress(40, 100);
                Map<String, byte[]> Documenti = CaricaSessioneDiGiocoService.this.prendiDocumentiCasuali(dbDT.prendiNomiDocumentiFiltrati(livello, lingua), livello);
                CaricaSessioneDiGiocoService.this.sessione.setDocumenti(Documenti);
                updateProgress(65, 100);
                List<GeneratoreDomande.Domanda> Domande = CaricaSessioneDiGiocoService.this.generaDomande(sessione.getDocumenti(),CaricaSessioneDiGiocoService.this.getAnalisi(), sessione.getNumeroDomande());
                CaricaSessioneDiGiocoService.this.sessione.setDomande(Domande);
                updateProgress(70, 100);
                CaricaSessioneDiGiocoService.this.sessione.setDurata(CaricaSessioneDiGiocoService.this.calcolaDurata(livello));
                updateProgress(80, 100);
                CaricaSessioneDiGiocoService.this.sessione.salvaSessioneDiGioco();
                updateProgress(100, 100);
                System.out.println("Nel service :"+ CaricaSessioneDiGiocoService.this.sessione.getDomande());
                return  CaricaSessioneDiGiocoService.this.sessione.getDomande();
            }  
        };
    } 
}
