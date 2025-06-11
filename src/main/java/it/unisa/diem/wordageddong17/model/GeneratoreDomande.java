package it.unisa.diem.wordageddong17.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe che si occupa di generare domande a risposta multipla
 * basate sull'analisi di un documento.
 * Le domande sono di vario tipo: frequenza assoluta, confronto, esclusione.
 * 
 * Ogni domanda è rappresentata dalla classe interna Domanda.
 * 
 */
public class GeneratoreDomande {
    private final AnalisiDocumenti analisi;
    private final Random rnd = new Random();

    /**
     * Costruttore del generatore di domande.
     * 
     * @param analisi Oggetto AnalisiDocumenti già popolato con i dati del documento
     */
    public GeneratoreDomande(AnalisiDocumenti analisi) {
        this.analisi = analisi;
    }

    /**
     * Classe che rappresenta una domanda a risposta multipla.
     * Contiene il testo della domanda, le opzioni e l'indice della risposta corretta.
     */
    public static class Domanda implements Serializable{
        /** Testo della domanda */
        public final String testo;
        /** Lista delle opzioni di risposta */
        public final List<String> opzioni;
        /** Indice (0-3) della risposta corretta nella lista delle opzioni */
        public final int rispostaCorretta;

        /**
         * Costruttore della domanda.
         * 
         * @param testo Testo della domanda
         * @param opzioni Lista delle opzioni di risposta
         * @param rispostaCorretta Indice della risposta corretta
         */
        public Domanda(String testo, List<String> opzioni, int rispostaCorretta) {
            this.testo = testo;
            this.opzioni = opzioni;
            this.rispostaCorretta = rispostaCorretta;
        }
    }

    /**
     * Genera una domanda sulla frequenza assoluta di una parola nel documento.
     * Esempio: "Quante volte compare la parola 'X' nel documento?"
     * 
     * @param nomeDocumento Nome del documento su cui basare la domanda
     * @return Oggetto Domanda, oppure null se non ci sono parole disponibili
     */
    private Domanda domandaFrequenzaAssoluta(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
       // System.out.println("domandaFrequenzaAssoluta "+ parole );
        if (parole == null || parole.isEmpty()) return null;
        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        String parola = paroleDoc.get(rnd.nextInt(paroleDoc.size()));
        int freq = parole.get(parola);

        // Opzioni plausibili
        Set<Integer> opzioniSet = new HashSet<>();
        opzioniSet.add(freq);
        while (opzioniSet.size() < 4) {
            int fake = Math.max(0, freq + rnd.nextInt(5) - 2);
            opzioniSet.add(fake);
        }
        List<Integer> opzioniNum = new ArrayList<>(opzioniSet);
        Collections.shuffle(opzioniNum);
        int idx = opzioniNum.indexOf(freq);

        List<String> opzioni = opzioniNum.stream().map(String::valueOf).collect(Collectors.toList());
        String testo = String.format("Quante volte compare la parola \"%s\" nel documento?", parola);
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Genera una domanda sulla parola più frequente nel documento.
     * Esempio: "Quale parola compare più spesso nel documento?"
     * 
     * @param nomeDocumento Nome del documento su cui basare la domanda
     * @return Oggetto Domanda, oppure null se non ci sono parole disponibili
     */
    private Domanda domandaParolaPiuFrequente(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        //System.out.println("domandaParolaPiuFrequente :" + parole);
        if (parole == null || parole.isEmpty()) return null;
        String parolaMax = Collections.max(parole.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        paroleDoc.remove(parolaMax);
        Collections.shuffle(paroleDoc);
        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaMax);
        for (int i = 0; i < 3 && i < paroleDoc.size(); i++) opzioni.add(paroleDoc.get(i));
        Collections.shuffle(opzioni);
        int idx = opzioni.indexOf(parolaMax);

        String testo = "Quale parola compare più spesso nel documento?";
        return new Domanda(testo, opzioni, idx);
    }

    /**
     *
     */
    // public Domanda domandaParolaMaiComparsa
        // da implementare, serve anche un dizionario, una lista di parole

    /**
     * Genera una domanda di confronto tra frequenze di parole.
     * "Quale tra queste parole è la più frequente nel documento?"
     * 
     * @param nomeDocumento Nome del documento su cui basare la domanda
     * @return Oggetto Domanda, oppure null se non ci sono abbastanza parole disponibili
     */
    private Domanda domandaConfrontoFrequenze(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento); 
        //System.out.println("domandaConfrontoFrequenze :" + parole);
        if (parole == null || parole.size() < 4) return null; 
        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        Collections.shuffle(paroleDoc); 
        List<String> scelte = new ArrayList<>(paroleDoc.subList(0, 4)); 
        String parolaMax = scelte.stream().max(Comparator.comparingInt(parole::get)).get(); // viene creato uno stream di parole, e si trova quella con frequenza massima
        int idx = scelte.indexOf(parolaMax); // indice della parola più frequente

        String testo = "Quale tra queste parole è la più frequente nel documento?";
        return new Domanda(testo, scelte, idx); 
    }
    
    
    /**
     * Restituisce una lista di domande generate da un documento specificato.
     *
     * @param num il numero massimo di domande da generare
     * @param nomeDocumento il nome del documento da cui estrarre le domande
     * @return una lista di oggetti {@link Domanda}
     */
    public List<Domanda> getRaccoltaDiDomande(int num, String nomeDocumento){
        List<Domanda> lista= new ArrayList<>();
        for (int i=0; i<num; i++){
            switch (this.rnd.nextInt(3)){

                case 0: lista.add(this.domandaConfrontoFrequenze(nomeDocumento));
                    break;
                case 1: lista.add(this.domandaFrequenzaAssoluta(nomeDocumento));
                    break;
                default : lista.add(this.domandaParolaPiuFrequente(nomeDocumento));
            }
            
            System.out.println("getRaccoltaDiDomande: " + lista.get(i).testo);
            //System.out.println("getRaccoltaDiDomande: "+ lista);
        }
         return lista;
    }
}