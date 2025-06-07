package it.unisa.diem.wordageddong17.service;

import it.unisa.diem.wordageddong17.database.DatabaseClassifica;
import it.unisa.diem.wordageddong17.interfaccia.DAOClassifica;
import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.InfoGiocatore;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.List;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class PrendiInfoUtenteService extends Service<InfoGiocatore>{
    List<Classifica> cronologiaPartiteList;
    private int facileCount;
    private int medioCount;
    private int difficileCount;
    private float facilePunteggio;
    private float medioPunteggio;
    private float difficilePunteggio;
    private final DAOClassifica dbC;
    private String email;
    
    public PrendiInfoUtenteService(String email){
        this.dbC = DatabaseClassifica.getInstance();
        this.email = email;
    }
    
    public PrendiInfoUtenteService(){
        this.dbC = DatabaseClassifica.getInstance();
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    @Override
    protected Task<InfoGiocatore> createTask() {
        return new Task<InfoGiocatore>(){
            @Override
            protected InfoGiocatore call() throws Exception {
                cronologiaPartiteList = dbC.recuperaCronologiaPartite(email);
                facileCount = dbC.recuperaNumeroPartite(email, LivelloPartita.FACILE.getDbValue());
                medioCount = dbC.recuperaNumeroPartite(email, LivelloPartita.MEDIO.getDbValue());
                difficileCount = dbC.recuperaNumeroPartite(email, LivelloPartita.DIFFICILE.getDbValue());
                facilePunteggio = dbC.recuperaMigliorPunteggio(email, LivelloPartita.FACILE.getDbValue());
                medioPunteggio = dbC.recuperaMigliorPunteggio(email, LivelloPartita.MEDIO.getDbValue());
                difficilePunteggio = dbC.recuperaMigliorPunteggio(email, LivelloPartita.DIFFICILE.getDbValue());
                
                return new InfoGiocatore(cronologiaPartiteList,facileCount,medioCount,difficileCount,facilePunteggio,medioPunteggio,difficilePunteggio); 
            }
        };
    }
}
