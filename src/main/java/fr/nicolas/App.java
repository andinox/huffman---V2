package fr.nicolas;

public class App {
    public static void main( String[] args )  {
        LinkedList<LinkedList<String>> frequency = huffman.load("/tab.ods");
        String texteSE = huffman.loadText("/texteSE.txt");
        String texteVH = huffman.loadText("/texteVH.txt");
    
        LinkedList<Arbre> list_abr = huffman.Convert(frequency);
        Arbre tree = huffman.getHuffman(list_abr);

        String text = huffman.encode(texteVH, tree);
        System.out.println(text);

        String d = huffman.decode(text, tree);
        System.out.println(d);
    }
}
