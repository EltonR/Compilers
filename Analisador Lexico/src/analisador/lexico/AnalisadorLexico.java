package analisador.lexico;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class AnalisadorLexico {

    static ArrayList<Token> listaTokens = new ArrayList<>();
    static ArrayList<String> listaCarcateresEsp = new ArrayList<>();
    static ArrayList<String> listaPalavrasReserv = new ArrayList<>();
    static ArrayList<String> listaOperadores = new ArrayList<>();
    
    static String codigo = "";
    
    public static void main(String[] args) {
        if(args.length<1){
            System.out.println("Sintax: java -jar analyzer.jar [file to analize]");
            System.exit(0);
        }
        readFile(args[0]);
        geraListaTokens();
        codigo = codigo.replaceAll("\n", " ")+" ";
        String tokenAtual = "";
        System.out.println("Input: "+codigo+"\n\nOutput:\n");
        for(int i=0; i<codigo.length(); i++){
            if(codigo.charAt(i) != ' '){
                Token t = new Token();
                String value = "";
                if(Character.isLetter(codigo.charAt(i))){ //é palavra reservada ou identificador
                    while(isNumberOrLetter(codigo.charAt(i))){
                        value += codigo.charAt(i);
                        i++;
                    }
                    t.setValor(value);
                    if(listaPalavrasReserv.contains(value)){
                        t.setClasse("Palavra Reservada");
                    }else{
                        t.setClasse("Identificador");
                    }
                    i--;
                }else if(listaOperadores.contains(String.valueOf(codigo.charAt(i)))){ // é operador
                    t.setClasse("Operador");
                    if( (codigo.charAt(i) == '>' || codigo.charAt(i) == '<' || codigo.charAt(i) == '=') && (codigo.charAt(i+1) == '=')){
                        value = String.valueOf(codigo.charAt(i))+String.valueOf(codigo.charAt(i+1));
                        if(listaOperadores.contains(String.valueOf(codigo.charAt(i+2)))){
                            System.out.println("Error: can't recognize token '" + value+codigo.charAt(i+2) + "'.");
                            System.exit(0);
                        }
                        i++;
                    }else{
                        value = String.valueOf(codigo.charAt(i));
                    }
                    t.setValor(value);
                }else if(listaCarcateresEsp.contains(String.valueOf(codigo.charAt(i)))){ // é delimitador
                    t.setClasse("Delimitador");
                    value = String.valueOf(codigo.charAt(i));
                    t.setValor(value);
                }else if(Character.isDigit(codigo.charAt(i))){
                    t.setClasse("Constante");
                    while(Character.isDigit(codigo.charAt(i))){
                        value += codigo.charAt(i);
                        i++;
                    }
                    t.setValor(value);
                    i--;
                }else{
                    System.out.println("Error: can't recognize character '" + codigo.charAt(i) + "'");
                    System.exit(0);
                }
                listaTokens.add(t);
                System.out.println(" - "+t.getClasse()+ " "+t.getValor());
            }
        }
        writeFile();
    }
 
    private static boolean isNumberOrLetter(char c){
        if(String.valueOf(c).matches("[a-z]|[0-9]"))
            return true;
        return false;
    }
    
    private static void geraListaTokens(){
        
        listaCarcateresEsp.add("{");
        listaCarcateresEsp.add("}");
        listaCarcateresEsp.add("(");
        listaCarcateresEsp.add(")");
        listaCarcateresEsp.add(",");
        listaCarcateresEsp.add(";");
        
        listaPalavrasReserv.add("main");
        listaPalavrasReserv.add("float");
        listaPalavrasReserv.add("if");
        listaPalavrasReserv.add("while");
        
        listaOperadores.add("<=");
        listaOperadores.add(">=");
        listaOperadores.add("==");
        listaOperadores.add("<");
        listaOperadores.add(">");
        listaOperadores.add("=");
        listaOperadores.add("+");
        listaOperadores.add("-");
       
    }
    
    private static void readFile(String file){
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line ;
            while ((line = br.readLine()) != null) {
                codigo += line;
            }
        } catch( IOException ioe ){
            System.out.println("Error reading file: "+ioe.getMessage());
        }
    }
    
    private static void writeFile(){
        try{
            PrintWriter writer = new PrintWriter("output.txt", "ASCII");
            writer.println();
            for(int i=0; i<listaTokens.size(); i++){
                writer.println(" - "+listaTokens.get(i).getClasse() + " "+ listaTokens.get(i).getValor());
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Error Writing file:");
        }
    }
    
}
