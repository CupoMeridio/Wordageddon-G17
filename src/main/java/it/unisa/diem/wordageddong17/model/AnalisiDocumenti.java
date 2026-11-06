package it.unisa.diem.wordageddong17.model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
 * - Filtraggio delle stopwords tramite lookup O(1) con Set
 * - Gestione corretta dei caratteri Unicode (inclusi accenti ed elisioni)
 * 
 * @author Mattia Sanzari
 */
public class AnalisiDocumenti implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(AnalisiDocumenti.class.getName());
    private static final long serialVersionUID = 1L;

    /**
     * Matrice principale che mantiene l'associazione tra nomi dei documenti
     * e le mappe delle parole con le loro frequenze.
     * Struttura: NomeDocumento -> (Parola -> Frequenza)
     */
    private Map<String, Map<String, Integer>> matrice;

    /** Array di byte contenente le stop words da escludere nell'analisi */
    private byte[] stopWords;

    /** Set pre-calcolato in memoria per verificare le stop words in tempo O(1) */
    private transient Set<String> stopWordsSet;

    /**
     * Costruttore della classe AnalisiDocumenti.
     * Inizializza la matrice vuota pronta per ricevere le analisi dei documenti.
     */
    public AnalisiDocumenti() {
        this.matrice = new HashMap<>();
        this.stopWords = null;
        this.stopWordsSet = Collections.emptySet();
    }

    /**
     * Ripristina lo stato dell'oggetto dopo la deserializzazione.
     * <p>
     * Il campo {@link #stopWordsSet} è {@code transient} e non viene quindi serializzato:
     * viene ricostruito a partire dalle stop words ripristinate.
     * </p>
     *
     * @param in il flusso di input da cui leggere lo stato serializzato
     * @throws IOException se la lettura fallisce
     * @throws ClassNotFoundException se una classe deserializzata non viene trovata
     */
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        aggiornaStopWordsSet();
    }

    /**
     * Imposta l'array di byte contenente le stop words da utilizzare nell'analisi
     * e pre-calcola il set di parole in maiuscolo per ricerche O(1).
     *
     * @param stopWords array di byte rappresentante le stop words
     */
    public void setStopWords(byte[] stopWords) {
        this.stopWords = stopWords;
        aggiornaStopWordsSet();
    }

    /**
     * Ricostruisce il set di stopwords a partire dal byte array.
     */
    private void aggiornaStopWordsSet() {
        if (this.stopWords != null && this.stopWords.length > 0) {
            String text = new String(this.stopWords, StandardCharsets.UTF_8);
            this.stopWordsSet = Arrays.stream(text.toUpperCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+"))
                    .filter(p -> !p.isBlank())
                    .collect(Collectors.toSet());
        } else {
            this.stopWordsSet = Collections.emptySet();
        }
    }

    /**
     * Aggiunge un documento analizzato alla matrice.
     * 
     * @param nomeDocumento il nome/identificativo del documento
     * @param m la mappa contenente le parole e le loro frequenze per il documento
     */
    private void aggiungiDocumento(String nomeDocumento, Map<String, Integer> m) {
        matrice.put(nomeDocumento, m);
    }

    /**
     * Restituisce la mappa delle parole e frequenze per un documento specifico.
     * 
     * @param nomeDocumento il nome del documento di cui si vogliono ottenere i dati
     * @return Map contenente le parole come chiavi e le loro frequenze come valori, oppure null se non presente
     */
    public Map<String, Integer> restituisciDocumento(String nomeDocumento) {
        return matrice.get(nomeDocumento);
    }

    /**
     * Restituisce l'intera matrice dei documenti analizzati.
     * 
     * @return Map completa contenente tutti i documenti analizzati
     */
    public Map<String, Map<String, Integer>> getMatrice() {
        return matrice;
    }

    /**
     * Controlla se una parola è presente nella lista di stop words in tempo O(1).
     *
     * @param parola la parola da controllare
     * @return {@code true} se la parola è una stop word, {@code false} altrimenti
     */
    public boolean appartenenzaStopWords(String parola) {
        if (parola == null || parola.isBlank()) {
            return false;
        }
        if (this.stopWordsSet == null) {
            aggiornaStopWordsSet();
        }
        return this.stopWordsSet.contains(parola.toUpperCase(Locale.ROOT));
    }

    /**
     * Esegue l'analisi di un singolo documento direttamente in memoria,
     * estraendo le parole secondo le regole Unicode (preservando lettere accentate
     * e caratteri internazionalizzati), filtrando le stop words e calcolando le frequenze.
     *
     * @param doc il contenuto del documento in formato byte array
     * @param nomeDocumento il nome identificativo del documento
     * @throws IOException se i parametri sono non validi
     */
    public void analisiUnDocumento(byte[] doc, String nomeDocumento) throws IOException {
        if (doc == null || nomeDocumento == null) {
            throw new IllegalArgumentException("Contenuto documento o nome documento nullo.");
        }

        if (this.stopWordsSet == null) {
            aggiornaStopWordsSet();
        }

        String testo = new String(doc, StandardCharsets.UTF_8);

        // Regex [^\p{L}\p{N}]+ divide per qualsiasi carattere che non sia lettera o numero Unicode,
        // preservando correttamente lettere accentate (à, è, é, ì, ò, ù) e caratteri europei (ä, ö, ü, ß, ñ, ç).
        Map<String, Integer> documento = Arrays.stream(testo.toUpperCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+"))
                .filter(parola -> !parola.isBlank())
                .filter(parola -> !appartenenzaStopWords(parola))
                .collect(Collectors.toMap(parola -> parola, parola -> 1, Integer::sum));

        this.aggiungiDocumento(nomeDocumento, documento);
    }

    @Override
    public String toString() {
        return "AnalisiDocumenti{" + "Matrice=" + matrice + '}';
    }
}
