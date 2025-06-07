package it.unisa.diem.wordageddong17.interfaccia;

public interface DAOListaStopWords {
    public boolean CaricareStopwords(String email, byte[] documentoStopwords, String nomeFile);
    public byte[] PrendiStopwords(String nomeFile);
    
}
