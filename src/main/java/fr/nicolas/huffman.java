package fr.nicolas;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;



public class huffman 
{   


    /** 
     * Charge la frequence des lettres contenus depuis le fichier ODS.
     * 
     * @param path chemin d'accès au fichier ODS. 
     * @return une liste chainée à deux dimensions qui contient les values du fichier ODS.
     * @return Si il y a eu une quelconque erreur la function return null
     */
    public static LinkedList<LinkedList<String>> load(String path) {
        try {
            InputStream is = huffman.class.getResourceAsStream(path);

            SpreadSheet spread = new SpreadSheet(is);

            List<Sheet> sheets = spread.getSheets();

            Sheet sheet = sheets.get(0);

            Range range = sheet.getDataRange();

            LinkedList<LinkedList<String>> list = new LinkedList<LinkedList<String>>();

            //on convertie la liste de type Object en Liste chainée
            for (Object[] freq : range.getValues()) {
                if (freq[0] == null) { freq[0] = " ";}
                if (freq[0] instanceof Double) {freq[0] = (int) Math.round((double) freq[0]);}
                if (freq[1] instanceof Double) {freq[1] = (int) Math.round((double) freq[1]);}  
                list = list.prefixer(new LinkedList<String>(freq[1].toString(), new LinkedList<String>(freq[0].toString(), new LinkedList<String>())));
            }
                         
            return list;
            
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Function récursive.
     * Permet de convertir la list de frequence 
     * en une list d'Arbre pour permettre l'utilisation du
     * code de Huffman.
     * 
     * @param frequency list chainée à deux dimensions de frequences des carracteres.
     * @return une liste d'arbre contenant chaque valeur.
     */
    public static LinkedList<Arbre> Convert(LinkedList<LinkedList<String>> frequency ) {
        if (frequency.size() == 0) return new LinkedList<Arbre>();
        int a = (int) Math.round((double) Integer.parseInt(frequency.tete().tete()));
        String b = frequency.tete().reste().tete();
        return Convert(frequency.reste()).insererOrd(new Arbre(a, b, Arbre.FIN, Arbre.FIN));
    }




    /**
     * Récupère la liste renvoyée de la fonction Huffman et retourne
     * l'arbre contenu dedans
     * 
     * @param frequency_l list des frequences des differents caractères mais dans un liste d'arbre
     * @return un Arbre
     * @see huffman
     */
    public static Arbre getHuffman(LinkedList<Arbre> frequency_l) {
        return huffman(frequency_l).tete();
    }

    /** 
     * Function récursive qui suis l'algoritme de huffman.
     * @param frequency_l list des frequences des differents caractères mais dans un liste d'arbre
     * @return Une list contenant qu'un seul arbre.
     */
    private static LinkedList<Arbre> huffman(LinkedList<Arbre> frequency_l) {
        if (frequency_l.size() == 1) return frequency_l;
        Arbre frist = frequency_l.tete();
        Arbre second = frequency_l.reste().tete();
        int somme = frist.infoi() + second.infoi();
        return huffman(frequency_l.reste().reste().insererOrd(new Arbre(somme, "somme", frist, second)));
    }


    /**
     *  function récursive qui regarde dans un arbre le chemin pour aller
     *  jusqu'a la valeurs val et return une string de 1 et de 0 en fonction
     *  du chemin parcouru.
     * 
     * @param tree l'arbre crée via huffman.
     * @param val le caractère à trouver. 
     * @return un string de 1 et de 0 en fonction du chemin parcouru.
     * @return -1 si la valeurs n'est pas trouvée.
     */
    public static String getBinaryCode(Arbre tree, String val) {
        String D;
        String G;
        if (tree.filsDroite().vide()) {
            D =  "-1";
        } else {
            if (tree.filsDroite().infos().equals(val)) {
                D = "1";
            } else {
                String next = getBinaryCode(tree.filsDroite(), val);
                if (next == "-1") {
                    D = "-1";
                } else {
                    D = "1" + next;
                }
            }
        }
        if (tree.filsGauche().vide()) {
            G =  "-1";
        } else {
            if (tree.filsGauche().infos().equals(val)) {
                G = "0";
            } else {
                String next = getBinaryCode(tree.filsGauche(), val);
                if (next == "-1") {
                    G = "-1";
                } else {
                    G = "0" + next;
                }
            }
        }
        return D == "-1" ? G : D;
    }
    /**
     * Charge le contenue d'un fichier text
     * 
     * @param path le lien vers le fichier text
     * @return le contenue du fichier dans une string
     */
    public static String loadText(String path) {
        InputStream text_path = huffman.class.getResourceAsStream(path);
        Scanner scan = new Scanner(text_path);
        String str = "";
        while(scan.hasNextLine()) {
            str = str + (scan.nextLine().replace("’","'"));
        }
        scan.close();
        return str;
    }

    /**
     * Function qui prend un text et convertie chaque carractere
     * en ça version coder et fais gères les codes d'erreur. 
     * 
     * @param text le text à encoder
     * @param huffman l'arbre crée via la function huffman.
     * @return string de 0 et de 1
     */
    public static String encode(String text, Arbre huffman) {
        String[] text_ = text.split("");
        String text_combined = "";
        for (String t : text_) {
            switch(t) {
                case "œ":
                text_combined +=  getBinaryCode(huffman, "o") + getBinaryCode(huffman, "e");
                break;
                case "æ": 
                text_combined +=  getBinaryCode(huffman, "a") + getBinaryCode(huffman, "e");
                break;
                default:
                text_combined +=  getBinaryCode(huffman, t);
            }
        }
        return text_combined;
    }


    public static String decode(String binaire, Arbre tree) {
        String carrac = callback_decode(binaire,tree);
        int one = getBinaryCode(tree, carrac).length();
        int two = binaire.length();
        if (two - one <= 0) {
            return carrac;
        } else {
            return carrac + decode(binaire.substring(one ,two),tree);
        }
    }


    /**
     * 
     * 
     * @param binaire le text en binaire a décodé
     * @param codes liste des codes de chaque carractere
     * @return
     */
    private static String callback_decode(String binaire, Arbre tree) {
        String valeur;
        try {
            valeur = binaire.substring(0,1);
        } catch ( StringIndexOutOfBoundsException e) {
            valeur = binaire;
        }
        
        Arbre temp  = valeur.equals("0") ? tree.filsGauche() : tree.filsDroite();
        if (tree.infos().equals("somme")) {
            return callback_decode(binaire.substring(1), temp);
        } else {
            return tree.infos();
        }
    }
}
