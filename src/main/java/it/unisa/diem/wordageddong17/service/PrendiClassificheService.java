package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Servizio per il recupero delle classifiche per ciascun livello di partita.
 * 
 * Questo servizio recupera le classifiche dal database per i livelli
 * {@link LivelloPartita#FACILE}, {@link LivelloPartita#MEDIO} e {@link LivelloPartita#DIFFICILE}
 * e le aggrega in una mappa {@code Map<LivelloPartita, List<Classifica>>}. L'operazione viene eseguita
 * in modo asincrono tramite un {@link Task}.
 * 
 * @see DatabaseClassifica
 * @see DAOClassifica
 * @see Classifica
 * @see LivelloPartita
 */
public class PrendiClassificheService extends Service<Map<LivelloPartita, List<Classifica>>> {
    private final DAOClassifica dbC;
    
    /**
     * Costruttore di default che inizializza l'accesso al database delle classifiche.
     */
    public PrendiClassificheService() {
        this.dbC = DatabaseClassifica.getInstance();
    }

    /**
     * Crea un task asincrono per il recupero delle classifiche.
     * 
     * Il task esegue le seguenti operazioni:
     * 1. Verifica se il task è stato cancellato; in tal caso, ritorna null.
     * 2. Recupera la classifica per il livello {@link LivelloPartita#FACILE}.
     * 3. Verifica nuovamente se il task è stato cancellato.
     * 4. Recupera la classifica per il livello {@link LivelloPartita#MEDIO}.
     * 5. Controlla se il task è stato cancellato.
     * 6. Recupera la classifica per il livello {@link LivelloPartita#DIFFICILE}.
     * 7. Aggrega le tre liste di classifiche in una mappa e la restituisce.
     * 
     * @return Un {@link Task} che restituisce una mappa contenente le classifiche organizzate per livello.
     */
    @Override
    protected Task<Map<LivelloPartita, List<Classifica>>> createTask() {
        return new Task<Map<LivelloPartita, List<Classifica>>>() {
            @Override
            protected Map<LivelloPartita, List<Classifica>> call() throws Exception {
                if (isCancelled()) {
                    return null;
                }

                List<Classifica> facile = dbC.prendiClassifica(LivelloPartita.FACILE);
                if (isCancelled()) {
                    return null;
                }

                List<Classifica> medio = dbC.prendiClassifica(LivelloPartita.MEDIO);
                if (isCancelled()) {
                    return null;
                }

                List<Classifica> difficile = dbC.prendiClassifica(LivelloPartita.DIFFICILE);
                if (isCancelled()) {
                    return null;
                }
                
                Map<LivelloPartita, List<Classifica>> tabelle = new HashMap<>();
                tabelle.put(LivelloPartita.FACILE, facile);
                tabelle.put(LivelloPartita.MEDIO, medio);
                tabelle.put(LivelloPartita.DIFFICILE, difficile);
                return tabelle;
            }
        };
    }
}
