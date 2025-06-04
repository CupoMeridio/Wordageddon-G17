/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Mattia Sanzari
 */
abstract class SessioneDiGioco {
    private String [] Domande;
    private AnalisiDocumenti analisi;
    private Map<String, byte[]> Documenti;
    private int numeroDomande;

    public SessioneDiGioco(int numeroDomande) {
        this.numeroDomande = numeroDomande;
        this.Documenti= new HashMap<>();
    }

    public String[] getDomande() {
        return Domande;
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
        } catch (IOException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SessioneDiGioco.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
