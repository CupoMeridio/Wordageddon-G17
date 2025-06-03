/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.unisa.diem.wordageddong17.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author Mattia Sanzari
 */
public class AnalisiDocumenti {
    private Map<String,Map<String,Integer>> Matrice;
    
    public AnalisiDocumenti() {
         Matrice = new HashMap<String,Map<String,Integer>>();
    }
    
    private void aggiungiDocumento(String NomeDocumento,Map<String,Integer> m){
        Matrice.put(NomeDocumento, m);
    }
    
    public Map<String, Integer> restituisciDocumento(String NomeDocumento){
       return Matrice.get(NomeDocumento);
    }
    
    public Map<String, Integer> restituisciDocumentiContenenteParola( String Parola ){
        Map<String, Integer> risultato= new HashMap<>();
        Matrice.entrySet().stream().filter(e->e.getValue().containsKey(Parola.toUpperCase())).forEach(e-> risultato.put(e.getKey(), e.getValue().get(Parola.toUpperCase())));
        return risultato;
    }

    public Map<String, Map<String, Integer>> getMatrice() {
        return Matrice;
    }
    
    
    public boolean appartenenzaStopWords(String parola){
        return false;
    }
    
    public void analisiUnDocumento(String Path) throws IOException{
        Map<String,Integer>  documento;
        Stream<String> stringStream = Files.lines(Paths.get(Path));
    
        documento= stringStream.flatMap(riga -> Arrays.stream(riga.toUpperCase().split("\\W+")))
                .filter(parola-> !parola.isEmpty())
                .filter(parola ->!appartenenzaStopWords(parola))
                .collect(Collectors.toMap(parola -> parola, parola -> 1, Integer::sum));
        System.out.println(documento.toString());
        this.aggiungiDocumento(Path, documento);
    }
    
    
    public void salvaMatrice(){
        try( ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream("salvaMatrice.ser"))){
            o.writeObject(this.Matrice);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void sovrascriviMatriceDaSalvataggio(){
        
        try( ObjectInputStream o = new ObjectInputStream(new FileInputStream("salvaMatrice.ser"))){
           this.Matrice= (HashMap<String, Map<String, Integer>>) o.readObject();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void mergeTraMatriceeSalvataggio(){
    
         try( ObjectInputStream o = new ObjectInputStream(new FileInputStream("salvaMatrice.ser"))){
           HashMap<String, Map<String, Integer>> MatriceDiAppoggio= (HashMap<String, Map<String, Integer>>) o.readObject();
           
           MatriceDiAppoggio.entrySet().stream().forEach(e-> this.aggiungiDocumento(e.getKey(), e.getValue()));
           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AnalisiDocumenti.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }

    @Override
    public String toString() {
        return "Analisi_documenti{" + "Matrice=" + Matrice + '}';
    }
    
}
