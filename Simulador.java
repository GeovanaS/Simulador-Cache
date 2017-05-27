/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author  Geovana Silveira
 *	    Karine Pestana        
 */

//Classe principal que vai "unir" todas as outras
public class Simulador { 
    
   public static void main(String[] args) throws IOException {

        CriaCache IniciaCache = new CriaCache();
        ManipulacaoArquivos ManiArquivos = new ManipulacaoArquivos(); 
        String arquivo = null; 
        String part1, part2;
        ArrayList <Integer> enderecos = null;
        int flag = 0, missL1 = 0;
           
        int XX = 100, aux = 0,arq = 0;
        String nomeArquivo = null;
        Scanner entrada = new Scanner(System.in);
        
        //infos sobre as caches 
        //na inilicializaÃ§Ã£o zera todos os dados
        ModeloCache ModCacheL1D = new ModeloCache();
        ModeloCache ModCacheL1I = new ModeloCache();
        ModeloCache ModCacheL2 = new ModeloCache();
        
        //Estatisticas de cada cache
        Estatisticas EL1d = new Estatisticas();
        Estatisticas EL1i = new Estatisticas();
        Estatisticas EL2 = new Estatisticas();
        
        //Caches propriamente ditas
        BlocoCache[] CacheL1d = null;
        BlocoCache[] CacheL1i = null;
        BlocoCache[] CacheL2 = null;     
        
        //Texto de inicializaÃ§Ã£o
        System.out.println("----------- Simulador de Cache ---------- ");
        System.out.println("Para ler os atributos das caches do teclado digite 0");
        System.out.println("Se deseja que seja usada a configuracao default digite 1");
        System.out.println("ConfiguraÃ§Ã£o default: ");
        System.out.println("     *Mapeamento direto");
        System.out.println("     *Tamanho de bloco de 4bytes");
        System.out.println("     *1024 conjuntos");
        System.out.println("Digite a sua opção: ");
        aux = entrada.nextInt();
        
        //cache_simulator <nsets_L1i> <bsize_L1i> <assoc_L1i> <nsets_L1d> <bsize_L1d> <assoc_L1d> 
        //<nsets_L2>:<bsize_L2>:<assoc_L2> arquivo_de_entrada
      
       do {           
           if (aux == 0) {
                   //Leitura dos atributos nsets, nbits e assoc do teclado para as "trÃªs" caches 
                System.out.println("Entre com o nsets, nbits e assoc para a cache L1 de dados: ");
                ModCacheL1D.setNsets(entrada.nextInt());      
                ModCacheL1D.setNbits(entrada.nextInt());  
                ModCacheL1D.setAssoc(entrada.nextInt());
                System.out.println("\nEntre com o nsets, nbits e assoc para a cache L1 de instruÃ§Ãµes: ");
                ModCacheL1I.setNsets(entrada.nextInt());
                ModCacheL1I.setNbits(entrada.nextInt());  
                ModCacheL1I.setAssoc(entrada.nextInt());
                System.out.println("\nEntre com o nsets, nbits e assoc para a cache L2: ");        
                ModCacheL2.setNsets(entrada.nextInt());      
                ModCacheL2.setNbits(entrada.nextInt());  
                ModCacheL2.setAssoc(entrada.nextInt());           
               
                System.out.println("Digite o nome do arquivo:");
                arquivo = entrada.next();
                
                //Seta os tipos de caches em cada uma
                ModCacheL1D.setTipo(IniciaCache.defineTipos(ModCacheL1D));
                ModCacheL1I.setTipo(IniciaCache.defineTipos(ModCacheL1I));
                ModCacheL2.setTipo(IniciaCache.defineTipos(ModCacheL2));    
            
           }
           else
               if(aux == 1){
                   /* Configuracao default da Cache utilizando mapeamento Direto 
                    com tamanho de bloco de 4 bytes e com 1024 conjuntos(nas duas caches) */
                    ModCacheL1D.setNsets(1024);      
                    ModCacheL1D.setNbits(4);  
                    ModCacheL1D.setAssoc(1);                       
                    
                    ModCacheL1I.setNsets(1024);
                    ModCacheL1I.setNbits(4);  
                    ModCacheL1I.setAssoc(1);                       
                    
                    ModCacheL2.setNsets(1024);      
                    ModCacheL2.setNbits(4);  
                    ModCacheL2.setAssoc(1);                    
                   
                    //Seta os tipos de caches em cada uma
                    ModCacheL1D.setTipo(1);
                    ModCacheL1I.setTipo(1);
                    ModCacheL2.setTipo(1);                   
                    
                    System.out.println("Digite o nome do arquivo:");
                    arquivo = entrada.next();

             }       
               else{
                   System.out.println("\nArgumento inválido!\nEntre com a escolha novamente:");
                   aux = entrada.nextInt();
               }
       } while (!(aux == 0 || aux == 1));    
       
      //Cria as trÃªs caches
      int[] n = IniciaCache.MapeiaCache(EL1i, EL1d, EL2, ModCacheL1I, ModCacheL1D, ModCacheL2, CacheL1i, CacheL1d, CacheL2);
      
      CacheL1d = new BlocoCache[n[0]];
      CacheL1i = new BlocoCache[n[1]];
      CacheL2 = new BlocoCache[n[2]];
      
       for (int i = 0; i < n[0]; i++)           
           CacheL1d[i] = new BlocoCache(0, 0, 0);
       
       for (int i = 0; i < n[1]; i++)
           CacheL1i[i] = new BlocoCache(0, 0, 0);
           
       for (int i = 0; i < n[2]; i++)
           CacheL2[i] = new BlocoCache(0, 0, 0); 
      
       //laço que trata do arquivo, caso a extensão não conhecida continua lendo
       do {           
           //separa a extensão do arquivo e nome do arquivo
                      
           part1 = arquivo.substring(0, arquivo.length()-4);
           part2 = arquivo.substring(arquivo.length()-3);
            
            //verifica a extensão do arquivo para trata-lo apropriadamente
            if(part2.equals("txt"))       
                enderecos = ManiArquivos.LeArquivoTexto(arquivo);       
            else
                if(part2.equals("dat"))
                    enderecos = ManiArquivos.LeArquivoBinario(arquivo);           
                else{
                    System.out.println("Formato do arquivo não reconhecido.\nEntre novamente:");
                    flag = 1;
                    //lê arquivo novamente
                    entrada.next(arquivo);
                }            
       } while (flag == 1);       
       
       //percorre todos os endereços simulando de fato a cache
       for (int i = 0; i < enderecos.size(); i+=2) {
           int end = enderecos.get(i);
           int le = enderecos.get(i+1);  
       
           Mapeamento.CacheSeparada(end, XX, le, ModCacheL1D, ModCacheL1I, ModCacheL2, EL1d, EL1i, EL2, CacheL1d, CacheL1i, CacheL2);           
       }
       
       //Saída final do programa é um relatório       
        System.out.println("\n\t\t###############################\n\t\t## Relatório de Estatísticas ##\n\t\t###############################\n\n");
	System.out.println("*Número de acessos: " +ManiArquivos.getNumAcessos());
	System.out.println("\n################### Cache L1 de dados ##################");
	// Informa qual cache se refere
	IniciaCache.nomeCache(ModCacheL1D);
        EL1d.CalculaHitRatio(ManiArquivos.getNumAcessos());
        EL1d.CalculaMissRatio(ManiArquivos.getNumAcessos());
	EL1d.dadosRelatorio();
	System.out.println("\n\n################ Cache L1 de instruções ################");
	// Informa qual cache se refere
	IniciaCache.nomeCache(ModCacheL1I);
        EL1i.CalculaHitRatio(ManiArquivos.getNumAcessos());
        EL1i.CalculaMissRatio(ManiArquivos.getNumAcessos());
	EL1i.dadosRelatorio();             
	System.out.println("\n\n####################### Cache L2 #######################");
	// Informa qual cache se refere
	IniciaCache.nomeCache(ModCacheL2);
        missL1 = EL1d.getMiss() + EL1i.getMiss();
        EL2.CalculaHitRatio(ManiArquivos.getNumAcessos());
        EL2.CalculaMissRatio(ManiArquivos.getNumAcessos());
	EL2.dadosRelatorio();
        System.out.print("\n\n*Local Miss Rate: " + (float)(EL2.getMiss()/missL1));
        System.out.print("\n*Global Miss Rate: " + (float)(ManiArquivos.getNumAcessos()/(EL1d.getMiss() + EL1i.getMiss() + EL2.getMiss())));
	System.out.println("\n\n########################################################\n");
       
   }  
}
