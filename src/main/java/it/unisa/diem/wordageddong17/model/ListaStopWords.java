package it.unisa.diem.wordageddong17.model;

import java.sql.Timestamp;

public record ListaStopWords(
    String nomeFile,
    String amministratore,
    Timestamp dataUltimaModifica
) 
{
    public String getNomeFile(){
        return nomeFile();
    }
    
    public String getAmministratore(){
        return amministratore();
    }
    
    public Timestamp getDataUltimaModifica(){
        return dataUltimaModifica();
    }
    
}
