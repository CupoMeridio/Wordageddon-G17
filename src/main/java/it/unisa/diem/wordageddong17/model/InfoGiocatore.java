package it.unisa.diem.wordageddong17.model;

import java.util.List;

public record InfoGiocatore(
    List<Classifica> cronologiaPartiteList,
    int facileCount,
    int medioCount,
    int difficileCount,
    float facilePunteggio,
    float medioPunteggio,
    float difficilePunteggio       
) {
    
    public List<Classifica> getCronologiaPartiteList(){
        return cronologiaPartiteList();
    }
    
    public int getFacileCount(){
        return facileCount();
    }
    
    public int getMedioCount(){
        return medioCount();
    }
    
    public int getDifficileCount(){
        return difficileCount();
    }
    
    public float getFacilePunteggio(){
        return facilePunteggio();
    }
    
    public float getMedioPunteggio(){
        return medioPunteggio();
    }
    
    public float getDifficilePunteggio(){
        return difficilePunteggio();
    }
    

}
