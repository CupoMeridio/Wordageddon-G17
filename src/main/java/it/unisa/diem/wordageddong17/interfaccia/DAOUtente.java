package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.Utente;

public interface DAOUtente {
    
    public boolean modificaUsername(String email, String username);
    public boolean modificaFotoProfilo(String email, byte[] foto);
    public void inserisciUtente(String username, String email, String password, byte[] foto);
    public String prendiUsername(String email);
    public boolean verificaPassword(String email, String password);
    public Utente prendiUtente(String email);
}
