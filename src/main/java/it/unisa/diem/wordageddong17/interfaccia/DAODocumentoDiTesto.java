package it.unisa.diem.wordageddong17.interfaccia;

import it.unisa.diem.wordageddong17.model.DocumentoDiTesto;
import it.unisa.diem.wordageddong17.model.Lingua;
import it.unisa.diem.wordageddong17.model.LivelloPartita;
import java.util.ArrayList;
import java.util.Map;

public interface DAODocumentoDiTesto {
    public boolean cancellaTesto(String nomeDocumento);
    public boolean caricaTesto(String email, String nomeFile, String difficolta, byte[] file, Lingua lingua);
    public byte[] prendiTesto(String nomeDocumento);
    public ArrayList<DocumentoDiTesto> prendiTuttiIDocumenti();
    public ArrayList<String> prendiNomiDocumentiFiltrati(LivelloPartita livello,ArrayList<Lingua> lingue);
}
