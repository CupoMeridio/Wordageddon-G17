package it.unisa.diem.wordageddong17.model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Classe che si occupa di generare domande a risposta multipla basate
 * sull'analisi di un documento. Le domande sono di vario tipo: frequenza
 * assoluta, confronto, esclusione.
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
     * @param analisi Oggetto AnalisiDocumenti già popolato con i dati del
     * documento
     */
    public GeneratoreDomande(AnalisiDocumenti analisi) {
        this.analisi = analisi;
    }

    /**
     * Classe che rappresenta una domanda a risposta multipla. Contiene il testo
     * della domanda, le opzioni e l'indice della risposta corretta.
     */
    public static class Domanda implements Serializable {

        /**
         * Testo della domanda
         */
        public final String testo;
        /**
         * Lista delle opzioni di risposta
         */
        public final List<String> opzioni;
        /**
         * Indice (0-3) della risposta corretta nella lista delle opzioni
         */
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
        
        if (parole == null || parole.isEmpty()) {
            return null;
        }
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
        String nomeSenzaEstensione = nomeDocumento.endsWith(".txt") ? nomeDocumento.substring(0, nomeDocumento.lastIndexOf(".txt")) : nomeDocumento;
        String testo = String.format("Quante volte compare la parola \"%s\" nel documento \"%s\"?", parola, nomeSenzaEstensione);
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Genera una domanda sulla parola più frequente nel documento. Esempio:
     * "Quale parola compare più spesso nel documento?"
     *
     * @param nomeDocumento Nome del documento su cui basare la domanda
     * @return Oggetto Domanda, oppure null se non ci sono parole disponibili
     */
    private Domanda domandaParolaPiuFrequente(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        
        if (parole == null || parole.isEmpty()) {
            return null;
        }
        String parolaMax = Collections.max(parole.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        paroleDoc.remove(parolaMax);
        Collections.shuffle(paroleDoc);
        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaMax);
        for (int i = 0; i < 3 && i < paroleDoc.size(); i++) {
            opzioni.add(paroleDoc.get(i));
        }
        Collections.shuffle(opzioni);
        int idx = opzioni.indexOf(parolaMax);
        String nomeSenzaEstensione = nomeDocumento.endsWith(".txt") ? nomeDocumento.substring(0, nomeDocumento.lastIndexOf(".txt")) : nomeDocumento;
        String testo = String.format("Quale parola compare più spesso nel documento \"%s\"?", nomeSenzaEstensione);
        return new Domanda(testo, opzioni, idx);
    }

   /**
    * Dizionario multilingua per domandaParolaMaiComparsa
    * (Parole convertite in minuscolo per coerenza)
    */
    private static final Map<Lingua, List<String>> DIZIONARIO_PAROLE = new EnumMap<>(Lingua.class);
    static {
        // Dizionario italiano (convertito in minuscolo)


        // Italiano
        List<String> italiano = Arrays.asList(
            "CASA", "LIBRO", "TAVOLO", "SEDIA", "FINESTRA", "PORTA", "GIARDINO", "ALBERO",
            "FIORE", "SOLE", "LUNA", "STELLA", "MARE", "MONTAGNA", "FIUME", "LAGO",
            "CANE", "GATTO", "UCCELLO", "PESCE", "CAVALLO", "MUCCA", "PECORA", "MAIALE",
            "AUTO", "TRENO", "AEREO", "BICICLETTA", "MOTO", "NAVE", "AUTOBUS", "TRAM",
            "SCUOLA", "UNIVERSITÀ", "OSPEDALE", "CINEMA", "TEATRO", "MUSEO", "BIBLIOTECA",
            "RISTORANTE", "BAR", "NEGOZIO", "MERCATO", "BANCA", "UFFICIO", "FABBRICA",
            "ROSSO", "BLU", "VERDE", "GIALLO", "NERO", "BIANCO", "GRIGIO", "VIOLA",
            "GRANDE", "PICCOLO", "ALTO", "BASSO", "LUNGO", "CORTO", "VELOCE", "LENTO",
            "BELLO", "BRUTTO", "BUONO", "CATTIVO", "FACILE", "DIFFICILE", "NUOVO", "VECCHIO",
            "MANGIARE", "BERE", "DORMIRE", "CAMMINARE", "CORRERE", "SALTARE", "VOLARE", "NUOTARE",
            "PARLARE", "SENTIRE", "VEDERE", "TOCCARE", "ANNUSARE", "PENSARE", "RICORDARE", "DIMENTICARE",
            "AMORE", "ODIO", "FELICITÀ", "TRISTEZZA", "PAURA", "CORAGGIO", "SPERANZA", "DISPERAZIONE",
            "AMICO", "NEMICO", "FAMIGLIA", "MADRE", "PADRE", "FIGLIO", "FIGLIA", "FRATELLO", "SORELLA",
            "TEMPO", "SPAZIO", "VITA", "MORTE", "NASCITA", "CRESCITA", "CAMBIO", "STABILITÀ"
        );
        DIZIONARIO_PAROLE.put(Lingua.ITALIANO, italiano);

        // Inglese
        List<String> inglese = Arrays.asList(
            "HOUSE", "BOOK", "COMPUTER", "CHAIR", "WINDOW", "DOOR", "GARDEN", "TREE",
            "FLOWER", "SUN", "MOON", "STAR", "OCEAN", "MOUNTAIN", "RIVER", "LAKE",
            "DOG", "CAT", "BIRD", "FISH", "HORSE", "COW", "SHEEP", "PIG",
            "CAR", "TRAIN", "AIRPLANE", "BICYCLE", "MOTORCYCLE", "SHIP", "BUS", "SUBWAY",
            "SCHOOL", "UNIVERSITY", "HOSPITAL", "CINEMA", "THEATER", "MUSEUM", "LIBRARY",
            "RESTAURANT", "CAFE", "STORE", "MARKET", "BANK", "OFFICE", "FACTORY",
            "RED", "BLUE", "GREEN", "YELLOW", "BLACK", "WHITE", "GRAY", "PURPLE",
            "BIG", "SMALL", "TALL", "SHORT", "LONG", "BRIEF", "FAST", "SLOW",
            "BEAUTIFUL", "UGLY", "GOOD", "BAD", "EASY", "HARD", "NEW", "OLD",
            "EAT", "DRINK", "SLEEP", "WALK", "RUN", "JUMP", "FLY", "SWIM",
            "TALK", "HEAR", "SEE", "TOUCH", "SMELL", "THINK", "REMEMBER", "FORGET",
            "LOVE", "HATE", "HAPPINESS", "SADNESS", "FEAR", "COURAGE", "HOPE", "DESPAIR",
            "FRIEND", "ENEMY", "FAMILY", "MOTHER", "FATHER", "SON", "DAUGHTER", "BROTHER", "SISTER",
            "TIME", "SPACE", "LIFE", "DEATH", "BIRTH", "GROWTH", "CHANGE", "STABILITY"
        );
        DIZIONARIO_PAROLE.put(Lingua.INGLESE, inglese);

        // Francese
        List<String> francese = Arrays.asList(
            "MAISON", "LIVRE", "TABLE", "CHAISE", "FENÊTRE", "PORTE", "JARDIN", "ARBRE",
            "FLEUR", "SOLEIL", "LUNE", "ÉTOILE", "MER", "MONTAGNE", "RIVIÈRE", "LAC",
            "CHIEN", "CHAT", "OISEAU", "POISSON", "CHEVAL", "VACHE", "MOUTON", "COCHON",
            "VOITURE", "TRAIN", "AVION", "VÉLO", "MOTO", "BATEAU", "BUS", "MÉTRO",
            "ÉCOLE", "UNIVERSITÉ", "HÔPITAL", "CINÉMA", "THÉÂTRE", "MUSÉE", "BIBLIOTHÈQUE",
            "RESTAURANT", "CAFÉ", "MAGASIN", "MARCHÉ", "BANQUE", "BUREAU", "USINE",
            "ROUGE", "BLEU", "VERT", "JAUNE", "NOIR", "BLANC", "GRIS", "VIOLET",
            "GRAND", "PETIT", "HAUT", "BAS", "LONG", "COURT", "RAPIDE", "LENT",
            "BEAU", "LAID", "BON", "MAUVAIS", "FACILE", "DIFFICILE", "NOUVEAU", "VIEUX",
            "MANGER", "BOIRE", "DORMIR", "MARCHER", "COURIR", "SAUTER", "VOLER", "NAGER",
            "PARLER", "ENTENDRE", "VOIR", "TOUCHER", "SENTIR", "PENSER", "SOUVENIR", "OUBLIER",
            "AMOUR", "HAINE", "BONHEUR", "TRISTESSE", "PEUR", "COURAGE", "ESPOIR", "DÉSESPOIR",
            "AMI", "ENNEMI", "FAMILLE", "MÈRE", "PÈRE", "FILS", "FILLE", "FRÈRE", "SŒUR",
            "TEMPS", "ESPACE", "VIE", "MORT", "NAISSANCE", "CROISSANCE", "CHANGEMENT", "STABILITÉ"
        );
        DIZIONARIO_PAROLE.put(Lingua.FRANCESE, francese);

        // Tedesco
        List<String> tedesco = Arrays.asList(
            "HAUS", "BUCH", "TISCH", "STUHL", "FENSTER", "TÜR", "GARTEN", "BAUM",
            "BLUME", "SONNE", "MOND", "STERN", "MEER", "BERG", "FLUSS", "SEE",
            "HUND", "KATZE", "VOGEL", "FISCH", "PFERD", "KUH", "SCHAF", "SCHWEIN",
            "AUTO", "ZUG", "FLUGZEUG", "FAHRRAD", "MOTORRAD", "SCHIFF", "BUS", "BAHN",
            "SCHULE", "UNIVERSITÄT", "KRANKENHAUS", "KINO", "THEATER", "MUSEUM", "BIBLIOTHEK",
            "RESTAURANT", "CAFÉ", "GESCHÄFT", "MARKT", "BANK", "BÜRO", "FABRIK",
            "ROT", "BLAU", "GRÜN", "GELB", "SCHWARZ", "WEIß", "GRAU", "LILA",
            "GROß", "KLEIN", "HOCH", "NIEDRIG", "LANG", "KURZ", "SCHNELL", "LANGSAM",
            "SCHÖN", "HÄSSLICH", "GUT", "SCHLECHT", "EINFACH", "SCHWIERIG", "NEU", "ALT",
            "ESSEN", "TRINKEN", "SCHLAFEN", "GEHEN", "LAUFEN", "SPRINGEN", "FLIEGEN", "SCHWIMMEN",
            "SPRECHEN", "HÖREN", "SEHEN", "BERÜHREN", "RIECHEN", "DENKEN", "ERINNERN", "VERGESSEN",
            "LIEBE", "HASS", "GLÜCK", "TRAURIGKEIT", "ANGST", "MUT", "HOFFNUNG", "VERZWEIFLUNG",
            "FREUND", "FEIND", "FAMILIE", "MUTTER", "VATER", "SOHN", "TOCHTER", "BRUDER", "SCHWESTER",
            "ZEIT", "RAUM", "LEBEN", "TOD", "GEBURT", "WACHSTUM", "ÄNDERUNG", "STABILITÄT"
        );
        DIZIONARIO_PAROLE.put(Lingua.TEDESCO, tedesco);

        // Spagnolo
        List<String> spagnolo = Arrays.asList(
            "CASA", "LIBRO", "MESA", "SILLA", "VENTANA", "PUERTA", "JARDÍN", "ÁRBOL",
            "FLOR", "SOL", "LUNA", "ESTRELLA", "MAR", "MONTAÑA", "RÍO", "LAGO",
            "PERRO", "GATO", "PÁJARO", "PEZ", "CABALLO", "VACA", "OVEJA", "CERDO",
            "COCHE", "TREN", "AVIÓN", "BICICLETA", "MOTO", "BARCO", "AUTOBÚS", "TRANVÍA",
            "ESCUELA", "UNIVERSIDAD", "HOSPITAL", "CINE", "TEATRO", "MUSEO", "BIBLIOTECA",
            "RESTAURANTE", "CAFETERÍA", "TIENDA", "MERCADO", "BANCO", "OFICINA", "FÁBRICA",
            "ROJO", "AZUL", "VERDE", "AMARILLO", "NEGRO", "BLANCO", "GRIS", "MORADO",
            "GRANDE", "PEQUEÑO", "ALTO", "BAJO", "LARGO", "CORTO", "RÁPIDO", "LENTO",
            "HERMOSO", "FEO", "BUENO", "MALO", "FÁCIL", "DIFÍCIL", "NUEVO", "VIEJO",
            "COMER", "BEBER", "DORMIR", "CAMINAR", "CORRER", "SALTAR", "VOLAR", "NADAR",
            "HABLAR", "OÍR", "VER", "TOCAR", "OLER", "PENSAR", "RECORDAR", "OLVIDAR",
            "AMOR", "ODIO", "FELICIDAD", "TRISTEZA", "MIEDO", "CORAGE", "ESPERANZA", "DESESPERACIÓN",
            "AMIGO", "ENEMIGO", "FAMILIA", "MADRE", "PADRE", "HIJO", "HIJA", "HERMANO", "HERMANA",
            "TIEMPO", "ESPACIO", "VIDA", "MUERTE", "NACIMIENTO", "CRECIMIENTO", "CAMBIO", "ESTABILIDAD"
        );
        DIZIONARIO_PAROLE.put(Lingua.SPAGNOLO, spagnolo);
    }

    
    /**
    * Genera una domanda su una parola assente nel documento.
    * 
    * @param nomeDocumento Nome del documento da analizzare
    * @param l Lingua del documento
    * @return Oggetto Domanda o null se non possibile generare
    */
   private Domanda domandaParolaMaiComparsa(String nomeDocumento, Lingua l) {
       Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
       if (parole == null || parole.isEmpty()) {
           return null;
       }

       // Controlla se la lingua è supportata
       if (!DIZIONARIO_PAROLE.containsKey(l)) {
           return null;
       }
       List<String> dizionario = DIZIONARIO_PAROLE.get(l);

       // Trova parole del dizionario non presenti nel documento
       List<String> paroleAssenti = dizionario.stream()
               .filter(parola -> !parole.containsKey(parola))
               .collect(Collectors.toList());

       if (paroleAssenti.isEmpty()) {
           return null;
       }

       // Seleziona una parola assente casuale
       String parolaCorretta = paroleAssenti.get(rnd.nextInt(paroleAssenti.size()));

       // Prepara le opzioni (1 assente + 3 presenti)
       List<String> opzioni = new ArrayList<>();
       opzioni.add(parolaCorretta); // Parola assente

       // Seleziona 3 parole presenti dal documento
       List<String> parolePresenti = new ArrayList<>(parole.keySet());
       if (parolePresenti.size() < 3) {
           return null; // Non abbastanza parole presenti
       }
       Collections.shuffle(parolePresenti);
       opzioni.addAll(parolePresenti.subList(0, 3));

       // Mescola le opzioni e trova l'indice della risposta corretta
       Collections.shuffle(opzioni);
       int indiceCorretto = opzioni.indexOf(parolaCorretta);

       // Costruisci il testo della domanda
       String nomeDoc = nomeDocumento.replace(".txt", "");
       String testoDomanda = String.format(
           "Quale di queste parole NON è presente nel documento \"%s\"?",
           nomeDoc
       );

       return new Domanda(testoDomanda, opzioni, indiceCorretto);
   }

    /**
     * Genera una domanda di confronto tra frequenze di parole. "Quale tra
     * queste parole è la più frequente nel documento?"
     *
     * @param nomeDocumento Nome del documento su cui basare la domanda
     * @return Oggetto Domanda, oppure null se non ci sono abbastanza parole
     * disponibili
     */
    private Domanda domandaConfrontoFrequenze(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        
        if (parole == null || parole.size() < 4) {
            return null;
        }
        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        Collections.shuffle(paroleDoc);
        List<String> scelte = new ArrayList<>(paroleDoc.subList(0, 4));
        String parolaMax = scelte.stream().max(Comparator.comparingInt(parole::get)).get(); // viene creato uno stream di parole, e si trova quella con frequenza massima
        int idx = scelte.indexOf(parolaMax); // indice della parola più frequente

        String nomeSenzaEstensione = nomeDocumento.endsWith(".txt") ? nomeDocumento.substring(0, nomeDocumento.lastIndexOf(".txt")) : nomeDocumento;
        String testo = String.format("Quale tra queste parole è la più frequente nel documento \"%s\"?", nomeSenzaEstensione);
        return new Domanda(testo, scelte, idx);
    }

    /**
     * Genera una domanda sulla parola meno frequente nel documento. Esempio:
     * "Quale parola compare meno spesso nel documento?"
     *
     * @param nomeDocumento Nome del documento su cui basare la domanda
     * @return Oggetto Domanda, oppure null se non ci sono parole disponibili
     */
    private Domanda domandaParolaMenoFrequente(String nomeDocumento) {
        Map<String, Integer> parole = analisi.restituisciDocumento(nomeDocumento);
        if (parole == null || parole.isEmpty()) {
            return null;
        }

        // Trova la parola con frequenza minima
        String parolaMin = Collections.min(parole.entrySet(), Map.Entry.comparingByValue()).getKey();

        List<String> paroleDoc = new ArrayList<>(parole.keySet());
        paroleDoc.remove(parolaMin);
        Collections.shuffle(paroleDoc);

        List<String> opzioni = new ArrayList<>();
        opzioni.add(parolaMin);
        for (int i = 0; i < 3 && i < paroleDoc.size(); i++) {
            opzioni.add(paroleDoc.get(i));
        }
        Collections.shuffle(opzioni);
        int idx = opzioni.indexOf(parolaMin);
        
        String nomeSenzaEstensione = nomeDocumento.endsWith(".txt") ? nomeDocumento.substring(0, nomeDocumento.lastIndexOf(".txt")) : nomeDocumento;
        String testo = String.format("Quale parola compare meno spesso nel documento \"%s\"?", nomeSenzaEstensione);
        return new Domanda(testo, opzioni, idx);
    }

    /**
     * Restituisce una lista di domande generate da un documento specificato.
     *
     * @param num il numero massimo di domande da generare
     * @param nomeDocumento il nome del documento da cui estrarre le domande
     * @param l lingua del documento
     * @return una lista di oggetti {@link Domanda}
     */
    public List<Domanda> getRaccoltaDiDomande(int num, String nomeDocumento, Lingua l) {
        List<Domanda> lista = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            switch (this.rnd.nextInt(5)) {

                case 0 -> lista.add(this.domandaConfrontoFrequenze(nomeDocumento));
                case 1 -> lista.add(this.domandaFrequenzaAssoluta(nomeDocumento));
                case 2 -> lista.add(this.domandaParolaMenoFrequente(nomeDocumento));
                case 3 -> lista.add(this.domandaParolaMaiComparsa(nomeDocumento, l));

                default -> lista.add(this.domandaParolaPiuFrequente(nomeDocumento));
            }

            
        }
        return lista;
    }

}
