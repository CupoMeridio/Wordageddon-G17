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

public class PrendiClassificheService extends Service<Map<LivelloPartita,List<Classifica>>>{
    private final DAOClassifica dbC;
    
    public PrendiClassificheService(){
        this.dbC = DatabaseClassifica.getInstance();
    }
    
    
    @Override
    protected Task<Map<LivelloPartita, List<Classifica>>> createTask() {
        return new Task<Map<LivelloPartita, List<Classifica>>>(){
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
                Map<LivelloPartita,List<Classifica>> tabelle = new HashMap<LivelloPartita, List<Classifica>>();
                tabelle.put(LivelloPartita.FACILE, facile);
                tabelle.put(LivelloPartita.MEDIO, medio);
                tabelle.put(LivelloPartita.DIFFICILE, difficile);
                return tabelle;
            }
            
        };
    }
    
}
