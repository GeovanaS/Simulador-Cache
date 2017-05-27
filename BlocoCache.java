/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;
/**
 *
 * @author Geovana Silveira
 * 	   Karine Pestana        
 */

//Classe que representa o Bloco da Cache

public class BlocoCache {
        private int bitVal;
	private int tag;
	private int dirtyBit;
        
   public BlocoCache(int bitVal, int tag, int dirtyBit){
       this.bitVal = bitVal;
       this.tag = tag;
       this.dirtyBit = dirtyBit;
   }     

    public int getBitVal() {
        return bitVal;
    }

    public void setBitVal(int bitVal) {
        this.bitVal = bitVal;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getDirtyBit() {
        return dirtyBit;
    }

    public void setDirtyBit(int dirtyBit) {
        this.dirtyBit = dirtyBit;
    }

}
