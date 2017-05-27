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

//Estatisticas referente a cada cache

public class Estatisticas {
    
        private int hit;
        private int miss, missComp, missConf, missCap, writeMiss;
	private int leitura, escrita;
        private int nivel;
        private float hitRatio, missRatio;
        
      public Estatisticas() {
        this.hit = 0;
        this.miss = 0;
        this.missComp = 0;
        this.missConf = 0;
        this.missCap = 0;
        this.writeMiss =0;
        this.leitura = 0;
        this.escrita = 0;
        this.nivel = 0;
     }  

    public int getHit() {
        return hit;
    }

    public void setHit() {
        this.hit = hit + 1;
    }

    public int getMiss() {
        return miss;
    }

    public void setMiss() {
        this.miss = miss + 1;
    }

    public int getMissComp() {
        return missComp;
    }

    public void setMissComp() {
        this.missComp = missComp + 1;
    }

    public int getMissConf() {
        return missConf;
    }

    public void setMissConf() {
        this.missConf = missConf + 1;
    }

    public int getMissCap() {
        return missCap;
    }

    public void setMissCap(int missCap) {
        this.missCap = missCap;
    }
    
    public int getWriteMiss() {
        return writeMiss;
    }

    public void setWriteMiss() {
        this.writeMiss = writeMiss + 1;
    }

    public int getLeitura() {
        return leitura;
    }

    public void setLeitura() {
        this.leitura = leitura + 1;
    }

    public int getEscrita() {
        return escrita;
    }

    public void setEscrita() {
        this.escrita = escrita + 1;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
	
    public float CalculaHitRatio(int qtAcessos){
        hitRatio = (float)((hit * 100) /(float)qtAcessos);
        return hitRatio;
    }
    
    public float CalculaMissRatio(int qtAcessos){
        missRatio = (float)((miss * 100)/(float)qtAcessos);
        return missRatio;
    }
    
    public void dadosRelatorio(){
        
        System.out.print("\n*Leitura: " + leitura);
        System.out.print("\n*Escrita: " + escrita);
        System.out.print("\n*Total de Hit: " + hit);
        System.out.print("\n*Total de Miss: " + miss);
        System.out.print("\n         -Quantidade de Miss Compulsório: " + missComp);
        System.out.print("\n         -Quantidade de Miss de Capacidade: " + missCap);
        System.out.print("\n         -Quantidade de Miss de Conflito: " + missConf);
        System.out.print("\n         -Quantidade de Write Miss: " + writeMiss);
        System.out.print("\n*Hit Ratio: " + hitRatio);
        System.out.print("\n*Miss Ratio: " + missRatio);  
    }
    
}
