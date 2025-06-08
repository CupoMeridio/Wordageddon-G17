package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia Sanzari
 */
public class SessioneDiGioco implements Serializable{
    private Utente utente;
    private List<Domanda> Domande;
    private Map<Domanda, Integer> risposte;
    private Map<String, byte[]> Documenti;// dove la chiave è il nome del Documento e byte[] è il documento in bytes
    private int numeroDomande;// numero di domande per documento 
    private float punteggioFatto;
    private int durata;
    private int numeroDocumenti;
    private byte[] stopWords;
    private  int durataIniziale;

    public SessioneDiGioco() {
        this.Domande = new ArrayList<>();
        this.Documenti= new HashMap<>();
        this.risposte= new HashMap<>();
        this.durata=0;
        this.durataIniziale=0;
    }

    public SessioneDiGioco(int numeroDomande, int durata, Utente utente, int numeroDocumenti) {
        this.numeroDomande = numeroDomande;
        this.Domande = new ArrayList<>();
        this.Documenti= new HashMap<>();
        this.risposte= new HashMap<>();
        this.stopWords = null;
        this.punteggioFatto=0;
        this.durata=durata; 
        this.durataIniziale=durata;
        this.utente= utente;
        this.numeroDocumenti = numeroDocumenti;

        // Salvataggi se la sessione viene interrotta bruscamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            
            Thread salva = new Thread(()->{
                System.out.println("Inizio salvataggio");
                this.salvaSessioneDiGioco("SalvataggioDi"+this.utente.getEmail()+".ser");
                System.out.println("Fine salvataggio");
            });
            salva.start();
            try{
                salva.join(10000);
                if(salva.isAlive()){
                    System.out.println("Non hai avuto tempo");
                    salva.interrupt();
                }
            }catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }));
    }

    public SessioneDiGioco(Utente utente) {
        this.Domande = new ArrayList<>();
        this.Documenti= new HashMap<>();
        this.risposte= new HashMap<>();
        this.stopWords = null;
        this.utente = utente;
        this.durata=0;
        this.durataIniziale=0;
    }
    
    public void setNumeroDomande(int numeroDomande) {
        this.numeroDomande = numeroDomande;
    }

    public void setNumeroDocumenti(int numeroDocumenti) {
        this.numeroDocumenti = numeroDocumenti;
    }
   
    public synchronized void setDurata(int durata) {
        if(this.durata==0 && this.durataIniziale==0)
            this.durataIniziale=durata;
        System.out.println("Dopo :" +this.durata);
        this.durata = durata;
        System.out.println("Dopo :" +this.durata);
    }

    public byte[] getStopWords() {
        return stopWords;
    }

    public Utente getUtente() {
        return utente;
    }

    public int getDurata() {
        return durata;
    }

    public int getNumeroDocumenti() {
        return numeroDocumenti;
    }

    public Map<Domanda, Integer> getRisposte() {
        return risposte;
    }

    public float getPunteggioFatto() {
        return punteggioFatto;
    }

    public List<Domanda> getDomande() {
        return Domande;
    }

    public void setDocumenti(Map<String, byte[]> Documenti) {
        this.Documenti = Documenti;
    }

    public void setDomande(List<Domanda> Domande) {
        this.Domande = Domande;
    }
    
    public void addDocumenti(String chiave,byte[] Documento){
        this.Documenti.put(chiave, Documento);
    }


    public Map<String, byte[]> getDocumenti() {
        return Documenti;
    }

    public int getNumeroDomande() {
        return numeroDomande;
    }
    
    public void salvaSessioneDiGioco(String NomeFile){
        
        try(ObjectOutputStream oos= new ObjectOutputStream( new FileOutputStream(NomeFile))){
            System.out.println("Inizio salvataggio");
            oos.writeObject(this);
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void salvaSessioneDiGioco(){
        
        try(ObjectOutputStream oos= new ObjectOutputStream( new FileOutputStream("SalvataggioNormaleDi"+this.utente.getEmail()+".ser"))){
            System.out.println("Inizio salvataggio");
            oos.writeObject(this);
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    public void aggiornaRisposte(int numeroDomanda, int indiceRisposta){
        this.risposte.put(this.Domande.get(numeroDomanda), indiceRisposta);
    }
    
    public void aggiornaPuntiFatti(int tempoRimasto){
       Domanda[] dom= this.Domande.toArray(new Domanda[0]);
       int numRisposteCorrette=0;
       for(Domanda d :  dom){
           if(d.rispostaCorretta== this.risposte.get(d)){
               numRisposteCorrette++;
           }else{
              // this.punteggioFatto= this.punteggioFatto -5;
           }
       }
       if(numRisposteCorrette!=0){
            float puntiMassimi=142;
            float precisione= this.punteggioFatto/dom.length;
            float puntBase= puntiMassimi*precisione;
            float bonusTempo= (float) ((tempoRimasto/this.durataIniziale) * (puntiMassimi*0.3));
            this.punteggioFatto= puntBase+bonusTempo;
       }else{
           this.punteggioFatto=0;
       }
    }
    
    public void caricaSessioneDiGioco(String NomeFile){
        try(ObjectInputStream ois= new ObjectInputStream( new FileInputStream(NomeFile))){
            System.out.println("Inizio caricamento");
            SessioneDiGioco s= (SessioneDiGioco) ois.readObject();
            System.out.println("s:"+s.toString());
                this.utente = s.getUtente();
                this.Domande = s.getDomande();
                this.risposte = s.getRisposte();
                this.Documenti = s.getDocumenti();
                this.numeroDomande = s.numeroDomande;
                this.punteggioFatto = s.getPunteggioFatto();
                this.durata = s.getDurata();
                this.numeroDocumenti = s.getNumeroDocumenti();
                this.stopWords = s.getStopWords();
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
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

    @Override
    public String toString() {
        return "SessioneDiGioco{" + "utente=" + utente + ", Domande=" + Domande + ", risposte=" + risposte + ", Documenti=" + Documenti + ", numeroDomande=" + numeroDomande + ", punteggioFatto=" + punteggioFatto + ", durata=" + durata + ", numeroDocumenti=" + numeroDocumenti + ", stopWords=" + stopWords + '}';
    }
    
}
