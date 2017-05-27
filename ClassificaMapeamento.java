/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Geovana Silveira
 *	   Karine Pestana          
 */

//Classe usada junto com a classe Mapeamento

public class ClassificaMapeamento{
    
    private static BlocoCache aux, cache;
    
    
    public static void mapeamentoDireto(BlocoCache[] cacheL, BlocoCache[] cacheL2, int endereco, ModeloCache infos, ModeloCache infosL2, Estatisticas L, Estatisticas L2, int le){
	//L1 ou L2 se for leitura, e os tratamentos caso seja cache L1 são feitos dentro desse laço       
        
	if(le==0){
		Mapeamento.calculaBits(endereco, infos);                
                aux = cacheL[Mapeamento.getIndice()];
		if(aux.getBitVal() == 0){//Se o bit validade é 0  pq é o primeiro acesso a ela
			L.setMiss();                                   // Miss total de dados sobe
			L.setMissComp();                              // Esse é compulsorio
			aux.setBitVal(1);       // validade = 1
			L.setEscrita(); //escreverá em L1
			L.setLeitura(); //Vai escrever mas também vai ler
			aux.setTag(Mapeamento.getTag());
			if(L.getNivel()==1){ 	// Se for L1(dado ou instrucao) e leitura
				//Trata a L2-> se um dado está na memória i ela precisa estar em i+1
                                Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);				
			}
		}
		else {//bit validade 1, já usou essa memoria, vai ocorrer hit ou substituicao
			if(aux.getTag() == Mapeamento.getTag()){
                                L.setHit();
                                L.setLeitura();				
			}
			else {     //nao encontrou, precisa confererir o dirty bit
                                L.setMiss();
                                L.setMissConf();             // miss conflito
				if(aux.getDirtyBit() == 0){
                                        L.setEscrita();
                                        L.setLeitura();
					aux.setTag(Mapeamento.getTag());         // seta novo tag
					if(L.getNivel()==1){ 	// Se for L1(dado ou instrucao) e leitura
						//Trata a L2
						Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
					}
				}
				else {// dirtyBit == 1
					if (L.getNivel()==1){
						int oldEndereco;
						//soma em binário para conseguir o endereço antigo para atualizar L2, write-back
						oldEndereco= ((aux.getTag() << Mapeamento.getNum_bits_indice()) << Mapeamento.getNum_bits_Offset()) | (Mapeamento.getNum_bits_indice() << Mapeamento.getNum_bits_Offset()) | Mapeamento.getNum_bits_Offset();
						//Atualiza os dados de L1
						aux.setTag(Mapeamento.getTag());
						L.setEscrita();
                                                L.setLeitura();
						//Trata L2, porém com o endereco antigo
						endereco=oldEndereco; //O endereco que vai ser atualizado em L2 é o endereco antigo
						Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
					}
					//Se L2 que tiver o dirty, nao precisa passar para a memoria principal, entao só atualiza nela
					else{
						aux.setTag(Mapeamento.getTag());
						L.setEscrita();
                                                L.setLeitura();
					}
				}
			}
		}
	}
	// L1(dado ou instrucao) escrita
	else if(le==1 && L.getNivel()==1){ // metodo write-back
		//precisa fazer algo por causa do write-back
		Mapeamento.calculaBits(endereco, infos);
                aux = cacheL[Mapeamento.getIndice()];
		if(aux.getBitVal() == 0){//Se o bit validade é 0  pq é o primeiro acesso a ela
			L.setWriteMiss(); //miss write 
			aux.setBitVal(1);   // validade = 1
			L.setEscrita(); //escreverá em L1
			aux.setTag(Mapeamento.getTag());
			aux.setDirtyBit(1); //ocorreu uma escrita e nao ocorreu atualização em L2
		}
		else {//bit validade 1, já usou essa memoria, vai ter que atualizar L2 e depois L1
			if(aux.getTag() == Mapeamento.getTag()){
				//hitL1d++; //encontrou, hit
				//é escrita, o que ia escrever já está escrita, nao faz nada então.
			}
			else {     //nao encontrou, precisa confererir o dirty
				if(aux.getDirtyBit() == 0){
					L.setEscrita();
					aux.setTag(Mapeamento.getTag());     // seta novo tag
					aux.setDirtyBit(1);//não está atualizado em L2, quando se for fazer a leitura/escrita de algum valor diferente, irá atualizar em L2 (WRITE-BACK)
				}
				else {// dirtyBit ==1, dai precisa atualizar L2
					int oldEndereco;
					//soma em binário para conseguir o endereço antigo para atualizar L2, write-back
					oldEndereco= ((aux.getTag() << Mapeamento.getNum_bits_indice()) << Mapeamento.getNum_bits_Offset()) | (Mapeamento.getNum_bits_indice() << Mapeamento.getNum_bits_Offset()) | Mapeamento.getNum_bits_Offset();
					aux.setTag(Mapeamento.getTag());
					L.setEscrita();
					//Trata L2, porém com o endereco antigo
					endereco=oldEndereco; //O endereco que vai ser atualizado em L2 é o endereco antigo
					Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
				}
			}
		}
	}
	//escrita em L2
	else if(le==1 && L.getNivel()==2){
		Mapeamento.calculaBits(endereco, infos);
                aux = cacheL[Mapeamento.getIndice()];
                if (aux.getBitVal() == 0)
                    L.setWriteMiss(); //miss write  
		aux.setTag(Mapeamento.getTag()); //Atualiza valor
		aux.setBitVal(1);
                L.setEscrita();
	}
}
    
  public static void totalAssoc(BlocoCache[] cacheL, BlocoCache[] cacheL2, int endereco, ModeloCache infos, ModeloCache infosL2, Estatisticas L, Estatisticas L2, int le){
	int i, flag = 0, flag2 = 0, aux2;
        Random gerador = new Random();
	
	if(le == 0){ //leitura pra qualquer cache, tanto L1d ou L1i como L2
		Mapeamento.calculaBits(endereco, infos);
		for(i=0; i<infos.getAssoc(); i++){
                       cache = cacheL[i];
			if(cache.getTag() == Mapeamento.getTag() && cache.getBitVal() == 1){ //HIT
				L.setHit();    
                                L.setLeitura();				
				flag = 1; //essa flag simboliza se houve um hit (1) ou um miss (continua em 0 e deverá ser feita escrita)
				break;
			}
		}
		if(flag == 0){ //MISS
			for(i=0; i<infos.getAssoc(); i++){ //procura um espaço "livre" na cache para inserir o valor
                            cache = cacheL[i];
                            if(cache.getBitVal() == 0){ //achou um espaço "livre" -> com bit de validade 0
					L.setMiss();
                                        L.setMissComp();
                                        cache.setBitVal(1);
                                        L.setEscrita();
                                        L.setLeitura();
					cache.setTag(Mapeamento.getTag());
					
					if(L.getNivel() == 1){ //se a cache for nivel 1 (dados ou instruções), tem que tratar o dado no segundo nível também
						Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	 //trata o dado na L2
					}
					flag2 = 1;
					break;
				}
			}
			if(flag2 == 0){ //não achou um espaço livre, tem que fazer substituição randômica -> conferindo o dirtybit do lugar a ser substituído
				L.setMiss();
				L.setMissConf();
				aux2 = gerador.nextInt()%infos.getAssoc();                                
                                cache = cacheL[aux2];
				if(cache.getDirtyBit() == 0){ //dirty bit em 0, não há necessidade de passar o valor pro nível inferior antes de substituí-lo
					L.setEscrita();
                                        L.setLeitura();
					cache.setTag(Mapeamento.getTag());
					if(L.getNivel() == 1){
						Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
					}
				}
				else{ //dirty bit em 1, precisa passar o valor velho pra L2 antes de substituí-lo
					if(L.getNivel() == 1){ //dirty em 1 no primeiro nível, passa o endereço velho pro segundo nível
						int oldEndereco;
                                                oldEndereco = ((cache.getTag() << Mapeamento.getNum_bits_indice()) << Mapeamento.getNum_bits_Offset()) | (Mapeamento.getNum_bits_indice() << Mapeamento.getNum_bits_Offset()) | Mapeamento.getNum_bits_Offset();
						L.setEscrita();
                                                L.setLeitura();
                                                cache.setTag(Mapeamento.getTag());
						endereco = oldEndereco;
						Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	 //vai tratar na L2 com o endereço velho que estava no bloco antes de ser substituído
					}
					if(L.getNivel() == 2){ //se for no segundo nível, só atualiza (não há a memória principal no simulador, então não há para onde levar o valor antigo)
						L.setEscrita();
                                                L.setLeitura();
                                                cache.setTag(Mapeamento.getTag());
					}
				}
			}
		}
	}
	else{ //casos de escrita, difere para L1 e L2
		if(le == 1 && L.getNivel() == 1){
                        Mapeamento.calculaBits(endereco, infos);
			for(i=0; i<infos.getAssoc(); i++){ //confere primeiro se o que quer ser escrito já não existe na cache
                            cache = cacheL[i];
                            if(cache.getTag() == Mapeamento.getTag() && cache.getBitVal() == 1){
					flag = 1; //essa flag simboliza se houve um hit (1) ou um miss (continua em 0 e deverá ser feita escrita)
					break;
				}
			}
			if(flag == 0){ //não está na cache, deve ser escrito
				for(i=0; i<infos.getAssoc(); i++){ //procura um espaço "livre" na cache para inserir o valor
                                    cache = cacheL[i];
                                    if(cache.getBitVal() == 0){ //achou um espaço "livre" -> com bit de validade 0
                                                L.setWriteMiss(); //miss write 
                                                L.setEscrita();
                                                cache.setBitVal(1);						
						cache.setTag(Mapeamento.getTag());
                                                cache.setDirtyBit(1);
						flag2 = 1;
						break;
					}
				}
				if(flag2 == 0){ //não achou espaço livre para escrever, vai ter que substituir algum bloco já existente
					aux2 = gerador.nextInt()%infos.getAssoc(); 
                                        cache = cacheL[aux2];
					if(cache.getDirtyBit() == 0){ //dirty bit em 0, não faz nada na L2
						L.setEscrita();
						cache.setTag(Mapeamento.getTag());
                                                cache.setDirtyBit(1);
					}
					else{ //dirty bit em 1, atualiza na L2
						int oldEndereco;
						oldEndereco = ((cache.getTag() << Mapeamento.getNum_bits_indice()) << Mapeamento.getNum_bits_Offset()) | (Mapeamento.getNum_bits_indice() << Mapeamento.getNum_bits_Offset()) | Mapeamento.getNum_bits_Offset();
						L.setEscrita();
						cache.setTag(Mapeamento.getTag());
						endereco = oldEndereco; //O endereco que vvai ser atualizado em L2 é o endereco antigo
						Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
					}
				}
			}
		}
		if(le == 1 && L.getNivel() == 2){ //escrita na L2, não há preocupação com o dirty bit nesse caso. Primeiro, procura espaço livre para escrever, se não houver, insere em bloco aleatório
			Mapeamento.calculaBits(endereco, infos);
			for(i=0; i<infos.getAssoc(); i++){ //procura um espaço "livre" na cache para inserir o valor
                            cache = cacheL[i];
                            if(cache.getBitVal() == 0){  //achou um espaço "livre" -> com bit de validade 0
                                        L.setWriteMiss(); //miss write 
					L.setEscrita();
					cache.setTag(Mapeamento.getTag());
                                        cache.setDirtyBit(1);
					flag2 = 1;
					break;
				}
			}
			if(flag2 == 0){
				aux2 = gerador.nextInt()%infos.getAssoc(); 
                                cache = cacheL[aux2];
				L.setEscrita();
				cache.setTag(Mapeamento.getTag());
			}
		}
	}				
}
  
 public static void ConjAssoc(BlocoCache[] cacheL, BlocoCache[] cacheL2, int endereco, ModeloCache infos, ModeloCache infosL2, Estatisticas L, Estatisticas L2, int le){
     int i ,flag = 0 ,flag2 = 0, aux2;
     Random gerador = new Random();
   
     if(le == 0){
         Mapeamento.calculaBits(endereco, infos);
         for(i = 0; i < infos.getAssoc(); i++){
             cache = cacheL[i];
             if(cache.getTag() == Mapeamento.getTag() && cache.getBitVal()== 1){ //hit
                L.setHit();
                L.setLeitura();
                flag = 1; // flag em 1 se houve um hit, continua em 0 se for miss e devera ser feita escrita
                break; 
             }
         }
         if(flag == 0){ //miss
             for(i = 0 ; i < infos.getAssoc(); i++){ //procura um espaco livre para inserir o valor
                 cache = cacheL[i];
                 if(cache.getBitVal()==0){ //Bit de validade em 0, simboliza que existe um espaco livre na cache
                     L.setMiss();
                     L.setMissComp();
                     cache.setBitVal(1);
                     L.setEscrita();
                     L.setLeitura();
                     cache.setTag(Mapeamento.getTag());
                     if(L.getNivel()==1){ //se a cache for nivel 1 (dados ou instruções), tem que tratar o dado no segundo nível também
                       Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	//trata o dado na L2
                     }
                     flag2 = 1;
                     break;
                 }
             }
             if(flag2==0){ //não achou um espaço livre, tem que fazer substituição randômica -> conferindo o dirtybit do lugar a ser substituído
                 L.setMiss();
                 L.setMissConf();
                 aux2 = gerador.nextInt() % infos.getAssoc();
                 cache = cacheL[aux2];
                 if(cache.getDirtyBit()==0){
                     L.setEscrita();
                     L.setLeitura();
                     cache.setTag(Mapeamento.getTag());
                     if(L.getNivel() == 1){
                         Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
                     }
                 }
               else{ //dirty bit em 1, precisa passar o valor velho pra L2 antes de substituí-lo
                   if(L.getNivel() == 1){
                       int oldEndereco;
                       oldEndereco = ((cache.getTag() << Mapeamento.getNum_bits_indice()) << Mapeamento.getNum_bits_Offset() | (Mapeamento.getNum_bits_indice() << Mapeamento.getNum_bits_Offset()) | Mapeamento.getNum_bits_Offset());
                       cache = cacheL[aux2];
                       cache.setTag(Mapeamento.getTag());
                       L.setEscrita();
                       L.setLeitura();
                       endereco = oldEndereco;
                       Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	
                     }
                   if(L.getNivel()==2){
                       cache = cacheL[aux2];
                       cache.setTag(Mapeamento.getTag());
                       L.setEscrita();
                       L.setLeitura();
                   }
                 }               
             }
         }
     }
     else{  //casos de escrita, difere para L1 e L2
         if(le == 1 && L.getNivel()==1){
             Mapeamento.calculaBits(endereco, infos);
             for(i = 0; i < infos.getAssoc(); i++){  //confere primeiro se o que quer ser escrito já não existe na cache
                 cache = cacheL[i];
                 if(cache.getTag() == Mapeamento.getTag() && cache.getBitVal()==1){
                     flag = 1; //essa flag simboliza se houve um hit (1) ou um miss (continua em 0 e deverá ser feita escrita)
                     break;
                 }
             }
             if(flag == 0){ //não está na cache, deve ser escrito
                  for(i = 0; i < infos.getAssoc(); i++){  //procura um espaço "livre" na cache para inserir o valor
                    cache = cacheL[i];
                      if(cache.getBitVal()==0){
                          L.setWriteMiss(); //miss write 
                          L.setEscrita();
                          cache.setBitVal(1);
                          cache.setTag(Mapeamento.getTag());
                          cache.setDirtyBit(1);
                          flag2 = 1;
                          break;
                      }
                  }
              if(flag2 == 0){ //não achou espaço livre para escrever, vai ter que substituir algum bloco já existente
                    aux2 = gerador.nextInt()%infos.getAssoc(); 
                    cache = cacheL[aux2];
                   if(cache.getDirtyBit() == 0){
                       L.setEscrita();
                       cache.setTag(Mapeamento.getTag());
                       cache.setDirtyBit(1); 
                   }
                   else{ //dirty bit em 1, atualiza na L2
                       	int oldEndereco;
                          oldEndereco = ((cache.getTag() << Mapeamento.getNum_bits_indice()) << Mapeamento.getNum_bits_Offset()) | (Mapeamento.getNum_bits_indice() << Mapeamento.getNum_bits_Offset()) | Mapeamento.getNum_bits_Offset();
                          cache.setTag(Mapeamento.getTag());
                          L.setEscrita();
                          endereco = oldEndereco;  //O endereco que vai ser atualizado em L2 é o endereco antigo
                          Mapeamento.CacheUnificada(cacheL2, endereco, L2, le, infosL2);	   
                   }
              }    
         }
     }
         
     if(le == 1 && L.getNivel()==2){ //escrita na L2, não há preocupação com o dirty bit nesse caso. Primeiro, procura espaço livre para escrever, se não houver, insere em bloco aleatório
         Mapeamento.calculaBits(endereco, infos);
           for(i=0; i<infos.getAssoc(); i++){ //procura um espaço "livre" na cache para inserir o valor
              cache = cacheL[i];
            if(cache.getBitVal() == 0){ //achou um espaço "livre" -> com bit de validade 0
              L.setWriteMiss(); //miss write 
              cache.setBitVal(1);
              L.setEscrita();
              cache.setTag(Mapeamento.getTag());
              flag2 = 1;
              break;
          }
        }
        if(flag2 == 0){
               aux2 = gerador.nextInt() % infos.getAssoc();
               cache = cacheL[aux2];
               cache.setTag(Mapeamento.getTag());
               L.setEscrita();
           }
      }  
    }
 }
}
