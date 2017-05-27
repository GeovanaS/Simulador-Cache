/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;
import static java.lang.Math.log;
import java.util.ArrayList;

/**
 *
 * @author Geovana Silveira 
 *         Karine Pestana
 */

//Classe com todos os métodos estáticos e é usada com a classe ClassificaMapeamento

public class Mapeamento {
    
    protected static int num_bits_tag; 
    protected static int num_bits_indice;
    protected static int num_bits_Offset;
    protected static int tag;
    protected static int indice;

    public static int getNum_bits_tag() {
        return num_bits_tag;
    }

    public static void setNum_bits_tag(int num_bits_tag) {
        Mapeamento.num_bits_tag = num_bits_tag;
    }

    public static int getNum_bits_indice() {
        return num_bits_indice;
    }

    public static void setNum_bits_indice(int num_bits_indice) {
        Mapeamento.num_bits_indice = num_bits_indice;
    }

    public static int getNum_bits_Offset() {
        return num_bits_Offset;
    }

    public static void setNum_bits_Offset(int num_bits_Offset) {
        Mapeamento.num_bits_Offset = num_bits_Offset;
    }
    
    public static int getTag() {
        return tag;
    }

    public static void setTag(int tag) {
        Mapeamento.tag = tag;
    }

    public static int getIndice() {
        return indice;
    }

    public static void setIndice(int indice) {
        Mapeamento.indice = indice;
    }    
    
    
     public static void calculaBits(int endereco, ModeloCache cache){
      num_bits_Offset = (int) (log(cache.getNbits())/log(2));
      num_bits_indice = (int) (log(cache.getNsets()/log(2)));
      num_bits_tag = (int) 32 - num_bits_Offset - num_bits_indice;   
      //indice = ((endereco >> (byte) num_bits_Offset) & (2^(num_bits_indice - 1)));
      indice = endereco % cache.getNsets();	
      //tag = (endereco >> (byte) num_bits_Offset >>  num_bits_indice);
      tag = (endereco >> (byte) num_bits_Offset + num_bits_indice); 
    }
    
    public static void CacheUnificada(BlocoCache[] cacheL2, int endereco, Estatisticas L2, int le, ModeloCache cache){
       if(cache.getTipo() == 1){
           ClassificaMapeamento.mapeamentoDireto(cacheL2, cacheL2, endereco, cache, cache, L2, L2, le);           
       } 
       else if(cache.getTipo() == 2){
           ClassificaMapeamento.ConjAssoc(cacheL2, cacheL2, endereco, cache, cache, L2, L2, le);
       }
       else if(cache.getTipo() == 3){
           ClassificaMapeamento.totalAssoc(cacheL2, cacheL2, endereco, cache, cache, L2, L2, le);
       }       
    }
    
    public static void CacheSeparada(int end, int XX, int le, ModeloCache cacheD, ModeloCache cacheI, ModeloCache cache, Estatisticas L1d, Estatisticas L1i, Estatisticas L2, BlocoCache[] cacheL1d, BlocoCache[] cacheL1i, BlocoCache[] cacheL2){
        if(end < XX){
            if(cacheD.getTipo()== 1){
                ClassificaMapeamento.mapeamentoDireto(cacheL1d, cacheL2, end, cacheD, cache, L1d, L2, le); 
            }
            else if(cacheD.getTipo()==2){
                ClassificaMapeamento.ConjAssoc(cacheL1i, cacheL2, end, cacheI, cache, L1i, L2, le);
            }
            else if(cacheD.getTipo()==3){
                ClassificaMapeamento.totalAssoc(cacheL1d, cacheL2, end, cacheD, cache, L1d,  L2, le);
            }
        }
        else if(end >= XX){
              if(cacheI.getTipo() == 1){
                 ClassificaMapeamento.mapeamentoDireto(cacheL1i, cacheL2, end, cacheI, cache, L1i, L2, le); 
            }
            else if(cacheI.getTipo()==2){
                 ClassificaMapeamento.ConjAssoc(cacheL1i, cacheL2, end, cacheI, cache, L1i, L2, le);
            }
            else if(cacheI.getTipo()==3){
                ClassificaMapeamento.totalAssoc(cacheL1i, cacheL2, end, cacheI, cache, L1i, L2, le);
            }
        }
    }

 
    
}
