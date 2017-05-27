/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;
import java.util.ArrayList;
/**
 *
 * @author Karine Pestana Ramos
 *         Geovana Silveira
 */

//Classe que deve ser usada na classe Simulador para inicializar as Caches

public class CriaCache{
    
    public CriaCache(){    
    }
    
    public int[] MapeiaCache(Estatisticas L1i, Estatisticas L1d, Estatisticas L2, ModeloCache cL1i, ModeloCache cL1d, ModeloCache cL2, BlocoCache[] CacheL1i, BlocoCache[] CacheL1d, BlocoCache[] CacheL2) {
        
        L1i.setNivel(1);
	L1d.setNivel(1);
	L2.setNivel(2);
        
        int[] vet={0,0,0};
    
	//cria L1 de dados
	if (cL1d.getTipo()==1 || cL1d.getTipo()==3){
            //cria cache dado vetor -> Mapeamento direto ou Totalmente Associativa
            vet[0]=cL1d.getNsets();            
            //CacheL1d =  new BlocoCache[cL1d.getNsets()];
            //System.out.println(CacheL1d.size());
	}
	else{
		//cria cache dado vetor para simplificar ->Conjunto Associativo
                vet[0]=cL1d.getNsets()*cL1d.getAssoc(); 
		//CacheL1d =  new BlocoCache[cL1d.getNsets()*cL1d.getAssoc()];
                //System.out.println(CacheL1d.size());
	}
	//cria L1 de instrucoes
	if (cL1i.getTipo()==1 || cL1i.getTipo()==3){
		//cria cache dado vetor -> Mapeamento direto ou Totalmente Associativa
                vet[1]=cL1i.getNsets(); 
		//CacheL1i = new BlocoCache[cL1i.getNsets()];
                //System.out.println(CacheL1d.size());
	}
	else{
		//cria cache dado vetor para simplificar ->Conjunto Associativo
                vet[1]=cL1i.getNsets()*cL1i.getAssoc(); 
		//CacheL1i =  new BlocoCache[cL1i.getNsets()*cL1i.getAssoc()];
               // System.out.println(CacheL1d.size());
	}
	//cria L2 
	if (cL2.getTipo()==1 || cL2.getTipo()==3){
		//cria cache dado vetor -> Mapeamento direto ou Totalmente Associativa  
                vet[2]=cL2.getNsets(); 
		//CacheL2 =  new BlocoCache[cL2.getNsets()];
                //System.out.println(CacheL1d.size());
	}
	else{
		//cria cache dado vetor para simplificar ->Conjunto Associativo
                vet[2]=cL2.getNsets()*cL2.getAssoc(); 
		//CacheL2 =  new BlocoCache[cL2.getNsets()*cL2.getAssoc()];
               // System.out.println(CacheL1d.size());
	}
        return vet;
        
    }   
 
    public int defineTipos(ModeloCache cache){
        int tipo = 0;
	if((cache.getAssoc()==1) && (cache.getNsets()>1)){
		tipo=1; //mapeamento direto
                cache.setTam(cache.getNsets());
	}
	else if((cache.getAssoc()>1) && (cache.getNsets()>1)){
		tipo=2; //conjunto associativa
                cache.setTam(cache.getAssoc()*cache.getNsets());
	}
	else if((cache.getAssoc()>1) && (cache.getNsets() == 1)){ //sÃ³ Ã© totalmente associativa se o numero de associatividade Ã© igual ao numero de blocos
		tipo=3; //totalmente associativa
                cache.setTam(cache.getNsets());
	}
	return tipo;
    }
    
    public void nomeCache(ModeloCache cache){
        if(cache.getTipo()==1){
            System.out.println("\nCache Direta");
        }
        else if(cache.getTipo()==2){
            System.out.println("\nCache Conjunto Associativo");
        }
        else if(cache.getTipo()==3){
            System.out.println("\nCache Totalmente Associativa");
        }
    }
    
}