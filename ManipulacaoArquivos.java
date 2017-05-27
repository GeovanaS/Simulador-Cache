/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author  Geovana Silveira 
 *          Karine Pestana
 */
public class ManipulacaoArquivos {
    
   private int endereco,le,numAcessos;

    public ManipulacaoArquivos() {
        this.endereco = 0;
        this.le = 0;
        this.numAcessos = 0;
    }
   
     public int getEndereco() {
        return endereco;
    }

    public void setEndereco(int endereco) {
        this.endereco = endereco;
    }

    public int getLe() {
        return le;
    }

    public void setLe(int le) {
        this.le = le;
    }
    
    public int getNumAcessos() {
        return numAcessos;
    }

    public void setNumAcessos(int numAcessos) {
        this.numAcessos = numAcessos;
    }


  public ArrayList <Integer> LeArquivoBinario (String nomeArq){
        int line = 0 ;       
        ArrayList <Integer> enderecos =  new ArrayList<>();
               DataInputStream end = null;
               FileInputStream arquivoBinario = null;
               
                try{  
                    arquivoBinario = new FileInputStream(nomeArq);
                    end = new DataInputStream(arquivoBinario);
                    
               while((line =(int) arquivoBinario.read()) != -1){
                    int a, b, c, d;
                   a = (int)arquivoBinario.read();
                   b = (int)arquivoBinario.read();
                   c = (int)arquivoBinario.read();
                   d = (int)arquivoBinario.read();
                   endereco = a + b + c + d;
                  // System.out.println("endereco: " + (int)endereco);
                   a = (int)arquivoBinario.read();
                   //System.out.println("a: "+a);
                   b = (int)arquivoBinario.read();
                   //System.out.println("b: "+b); 
                   c = (int)arquivoBinario.read();
                   //System.out.println("c: "+c);
                   d = (int)arquivoBinario.read();
                   //System.out.println("d: "+d);
                   le = a + b + c + d; 
                   //System.out.println("le: " + (int)le);
                   enderecos.add(line);
                   enderecos.add(le);
                  //  System.out.println(line);
                    numAcessos++;
                 }
                    
                } catch(IOException ex){
                    System.out.println("Não foi possível ler arquivo \n");
                    Logger.getLogger(ManipulacaoArquivos.class.getName()).log(Level.SEVERE, null, ex);
                }
                //Fecha arquivo
                try{
                    arquivoBinario.close();
                }
                catch(IOException ex){
                      System.out.println("Falha ao fechar\n");
                }
                
                return enderecos;
    }

   
   public ArrayList <Integer> LeArquivoTexto(String nomeArq){
                ArrayList <Integer> leitura =  new ArrayList<>();
               BufferedReader buffRead;
                try{  
                   buffRead = new BufferedReader(new FileReader(nomeArq));
                    String linha = "";
                    
                    while((linha = buffRead.readLine()) != null){
                        //adiciona no array e já converte para inteiro
                        leitura.add(Integer.parseInt(linha));
                        numAcessos++;
                    }
                    //Divide o número de acessos por causa de leitura
                    numAcessos = numAcessos/2;
                    buffRead.close();
                }
                catch(IOException ex){
                    System.out.println("Não foi possível ler arquivo \n");
                    Logger.getLogger(ManipulacaoArquivos.class.getName()).log(Level.SEVERE, null, ex);
                }                
                return leitura;
    }

   
}
