/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

/**
 *
 * @author Karine Pestana Ramos
 *         Geovana Silveira
 */

//Classe responsÃ¡vel pelos dados de cada cache

public class ModeloCache {
    private int nsets;
    private int nbits;
    private int assoc;
    private int tipo;
    private int tam;
    
    public ModeloCache(){
        this.nsets = 0;
        this.nbits = 0;
        this.assoc = 0;
    }
        
    public int getTam() {
        return tam;
    }

    public void setTam(int tam) {
        this.tam = tam;
    }
    
    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getNsets() {
        return nsets;
    }

    public void setNsets(int nsets) {
        this.nsets = nsets;
    }

    public int getNbits() {
        return nbits;
    }

    public void setNbits(int nbits) {
        this.nbits = nbits;
    }

    public int getAssoc() {
        return assoc;
    }

    public void setAssoc(int assoc) {
        this.assoc = assoc;
    } 
    
}
