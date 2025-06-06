/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.model;

import it.unisa.diem.wordageddong17.model.GeneratoreDomande.Domanda;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 *
 * @author Mattia Sanzari
 */
abstract public class SessioneDiGioco{
    private Utente utente;
    private List<Domanda> Domande;
    private Map<Domanda, Integer> risposte;
    private AnalisiDocumenti analisi;
    private Map<String, byte[]> Documenti;// dove la chiave è il nome del Documento e byte[] è il documento in bytes
    private int numeroDomande;// numero di domande per documento 
    private float punteggioFatto;
    private int durata;
    

    public SessioneDiGioco(int numeroDomande, int durata, Utente utente) {
        this.numeroDomande = numeroDomande;
        this.Domande = new ArrayList<>();
        this.Documenti= new HashMap<>();
        this.analisi= new AnalisiDocumenti();
        this.punteggioFatto=0;
        this.durata=durata;
        this.risposte= new HashMap<>();
        this.utente= utente;
    }

    public Map<Domanda, Integer> getRisposte() {
        return risposte;
    }

    public float getPunteggioFatto() {
        return punteggioFatto;
    }

    public List<Domanda> getDomande() {
        return Domande;
    }

    public void setDocumenti(Map<String, byte[]> Documenti) {
        this.Documenti = Documenti;
    }
    
    public void addDocumenti(String chiave,byte[] Documento){
        this.Documenti.put(chiave, Documento);
    }

    public AnalisiDocumenti getAnalisi() {
        return this.analisi;
    }

    public Map<String, byte[]> getDocumenti() {
        return Documenti;
    }

    public int getNumeroDomande() {
        return numeroDomande;
    }
    
    public void salvaSessioneDiGioco(String NomeFile){
        
        try(ObjectOutputStream oos= new ObjectOutputStream( new FileOutputStream(NomeFile))){
            System.out.println("Inizio salvataggio");
            oos.writeObject(this);
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void aggiornaRisposte(int numeroDomanda, int indiceRisposta){
        this.risposte.put(this.Domande.get(numeroDomanda), indiceRisposta);
    }
    
    public void aggiornaPuntiFatti(){
       Domanda[] dom= this.Domande.toArray(new Domanda[0]);
       for(Domanda d :  dom){
           
           if(d.rispostaCorretta== this.risposte.get(d)){
               this.punteggioFatto= this.punteggioFatto +10;
           }else{
               this.punteggioFatto= this.punteggioFatto -5;
           }
       }
    }
    
    public void caricaSessioneDiGioco(String NomeFile){
        try(ObjectInputStream ois= new ObjectInputStream( new FileInputStream(NomeFile))){
            System.out.println("Inizio caricamento");
            SessioneDiGioco s= (SessioneDiGioco) ois.readObject();
            this.Documenti= s.getDocumenti();
            this.numeroDomande= s.numeroDomande;
            this.analisi= s.getAnalisi();
            System.out.println("Fine salvataggio");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generaDomande() {
        GeneratoreDomande gen= new GeneratoreDomande(this.analisi);
        String[] nomiDocumenti =  this.Documenti.keySet().toArray(new String[0]);
        int domandePerDocumento = this.numeroDomande / nomiDocumenti.length;
        int documentiExtra = this.numeroDomande % nomiDocumenti.length;
        
        for(int i=0; i< nomiDocumenti.length;i++){
            List<Domanda> prova = gen.getRaccoltaDiDomande(domandePerDocumento+(i < documentiExtra ? 1 : 0),nomiDocumenti[i]);
            System.out.println("generaDomande:  130 "+ prova);
           this.Domande.addAll(prova) ;
        }  
        
        System.out.println("generaDomande:  136 "+ Domande);
    }

    public abstract void generaDocumenti(LivelloPartita l);
    
}
