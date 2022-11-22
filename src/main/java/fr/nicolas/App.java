package fr.nicolas;

import java.util.Scanner;

public class App {
    public static void main( String[] args )  {
        LinkedList<LinkedList<String>> frequency = huffman.load("/tab.ods");
        String texteSE = huffman.loadText("/texteSE.txt");
        String texteVH = huffman.loadText("/texteVH.txt");
    
        LinkedList<Arbre> list_abr = huffman.Convert(frequency);
        Arbre tree = huffman.getHuffman(list_abr);
        Scanner myObj = new Scanner(System.in);

        System.out.println("texteSE(1) , texteVH(2) :");
        while(myObj.nextLine() != null) {
            switch(myObj.nextLine()) {
                case "1":
                    System.out.println(huffman.encode(texteSE, tree));
                    System.out.println(huffman.decode(huffman.encode(texteSE, tree), tree));
                    break;
                case "2":
                    System.out.println(huffman.encode(texteVH, tree));
                    System.out.println(huffman.decode(huffman.encode(texteVH, tree), tree));
                    break;
                default:
                    System.out.println("not found ! Retry !");
            }
        }
        myObj.close();
    }
}
