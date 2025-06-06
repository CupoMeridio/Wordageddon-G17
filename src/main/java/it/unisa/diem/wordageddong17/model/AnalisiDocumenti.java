package it.unisa.diem.wordageddong17.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe per l'analisi e la gestione di documenti testuali.
 * Questa classe fornisce funzionalità per analizzare documenti, contare le occorrenze
 * delle parole, gestire stopwords e salvare/caricare le analisi effettuate.
 * 
 * La classe mantiene una matrice che associa ogni documento analizzato
 * a una mappa delle parole contenute e le loro frequenze.
 * 
 * Le funzionalità principali includono:
 * - Analisi di documenti testuali con conteggio delle parole
 * - Filtraggio delle stopwords
 * - Ricerca di documenti contenenti parole specifiche
 * - Persistenza dei dati su file serializzato
 * - Merge tra matrici salvate e matrice corrente
 * 
 * @author Mattia Sanzari
 */
public class AnalisiDocumenti {
    
    /**
     * Matrice principale che mantiene l'associazione tra nomi dei documenti
     * e le mappe delle parole con le loro frequenze.
     * Struttura: NomeDocumento -> (Parola -> Frequenza)
     */
    
    private Map<String,Map<String,Integer>> Matrice;
    
    /**
     * Costruttore della classe AnalisiDocumenti.
     * Inizializza la matrice vuota pronta per ricevere le analisi dei documenti.
     */
    
    public AnalisiDocumenti() {
         Matrice = new HashMap<String,Map<String,Integer>>();
    }
    
    /**
     * Aggiunge un documento analizzato alla matrice.
     * Metodo privato utilizzato per inserire i risultati
     * dell'analisi di un documento nella struttura dati principale.
     * 
     * @param NomeDocumento il nome/identificativo del documento
     * @param m la mappa contenente le parole e le loro frequenze per il documento
     */
    private void aggiungiDocumento(String NomeDocumento,Map<String,Integer> m){
        Matrice.put(NomeDocumento, m);
    }
    
    /**
     * Restituisce la mappa delle parole e frequenze per un documento specifico.
     * 
     * @param NomeDocumento il nome del documento di cui si vogliono ottenere i dati
     * @return Map contenente le parole come chiavi e le loro frequenze come valori
     */
    public Map<String, Integer> restituisciDocumento(String NomeDocumento){
        //System.out.println("restituisciDocumento: "+ this.Matrice.get(NomeDocumento));
       return Matrice.get(NomeDocumento);
    }
    
    /**
     * Trova tutti i documenti che contengono una parola specifica.
     * Effettua una ricerca nella matrice per trovare
     * tutti i documenti che contengono la parola specificata.
     * 
     * @param Parola la parola da cercare nei documenti (case-insensitive)
     * @return Map contenente i nomi dei documenti come chiavi e la frequenza
     *         della parola in ciascun documento come valore
     */
    public Map<String, Integer> restituisciDocumentiContenenteParola( String Parola ){
        Map<String, Integer> risultato= new HashMap<>();
        Matrice.entrySet().stream().filter(e->e.getValue().containsKey(Parola.toUpperCase())).forEach(e-> risultato.put(e.getKey(), e.getValue().get(Parola.toUpperCase())));
        return risultato;
    }

    /**
     * Restituisce l'intera matrice dei documenti analizzati.
     * 
     * @return Map completa contenente tutti i documenti analizzati
     */
    public Map<String, Map<String, Integer>> getMatrice() {
        return Matrice;
    }
    
    
    
    public boolean appartenenzaStopWords(String parola){
        return false;
    }
    
    public void analisiUnDocumento(byte[] doc, String NomeDocumento) throws IOException{
        Map<String,Integer>  documento;
        Path p=null;
        File f=null;
        try {
            f=File.createTempFile("FileAnalisiUnDocumento", ".txt");
            p = Files.write(f.toPath(), doc);
         }catch(IOException e){
            System.out.println("Eccezione: "+e);
         }
        Stream<String> stringStream = Files.lines(p);
    
        documento= stringStream.flatMap(riga -> Arrays.stream(riga.toUpperCase().split("\\W+")))
                .filter(parola-> !parola.isEmpty())
                .filter(parola ->!appartenenzaStopWords(parola))
                .collect(Collectors.toMap(parola -> parola, parola -> 1, Integer::sum));
        System.out.println(documento.toString());
        this.aggiungiDocumento(NomeDocumento, documento);
        f.deleteOnExit();
    }
    
    /**
     * Salva la matrice corrente su file serializzato.
     * Serializza l'intera matrice dei documenti analizzati nel file "salvaMatrice.ser"
     * In caso di errori durante il salvataggio, viene registrato un log di errore.
     */
    
    public void salvaMatrice(){
        try( ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("salvaMatrice.ser"))){
            o.writeObject(this.Matrice);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      /**
     * Carica e sostituisce la matrice corrente con quella salvata su file.
     * Legge il file "salvaMatrice.ser" e sostituisce completamente
     * la matrice corrente con quella deserializzata dal file.
     * 
     * Questa operazione sovrascrive tutti i dati correnti.
     * 
     * In caso di errori durante il caricamento, viene registrato un log di errore.
     */
    @SuppressWarnings("unchecked")
    public void sovrascriviMatriceDaSalvataggio(){
        
        try( ObjectInputStream o = new ObjectInputStream(new FileInputStream("salvaMatrice.ser"))){
           this.Matrice= (HashMap<String, Map<String, Integer>>) o.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    /**
     * Effettua il merge tra la matrice corrente e quella salvata su file.
     * Legge la matrice dal file "salvaMatrice.ser" e aggiunge tutti i suoi documenti
     * alla matrice corrente, mantenendo i dati già presenti.
     * 
     * In caso di conflitti di nomi di documenti, i documenti del file salvato
     * sovrascriveranno quelli della matrice corrente.
     * 
     * In caso di errori durante il caricamento, viene registrato un log di errore.
     */
    public void mergeTraMatriceeSalvataggio(){
    
         try( ObjectInputStream o = new ObjectInputStream(new FileInputStream("salvaMatrice.ser"))){
           @SuppressWarnings("unchecked")
           HashMap<String, Map<String, Integer>> MatriceDiAppoggio= (HashMap<String, Map<String, Integer>>) o.readObject();
           
           MatriceDiAppoggio.entrySet().stream().forEach(e-> this.aggiungiDocumento(e.getKey(), e.getValue()));
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    /**
     * Restituisce una rappresentazione testuale dell'oggetto AnalisiDocumenti.
     * 
     * @return String contenente la rappresentazione della matrice dei documenti
     */
    @Override
    public String toString() {
        return "Analisi_documenti{" + "Matrice=" + Matrice + '}';
    }
    
}
