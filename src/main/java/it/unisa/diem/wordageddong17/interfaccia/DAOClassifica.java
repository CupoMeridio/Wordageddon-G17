package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.Classifica;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.List;

public interface DAOClassifica {
    public List<Classifica> prendiClassifica(LivelloPartita difficolta);
    public List<Classifica> recuperaCronologiaPartite(String email);
    public int recuperaNumeroPartite(String email, String difficoltà);
    public float recuperaMigliorPunteggio(String email, String difficoltà);
    public void inserisciPunteggio(String email, float punteggio, LivelloPartita difficoltà);
}
