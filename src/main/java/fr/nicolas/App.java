package fr.nicolas;

import com.github.miachm.sods.Range;
import com.github.miachm.sods.Sheet;
import com.github.miachm.sods.SpreadSheet;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;



public class App 
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
            InputStream is = App.class.getResourceAsStream(path);

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
        return Convert(frequency.reste()).prefixer(new Arbre(a, b, Arbre.FIN, Arbre.FIN));
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
        Arbre filsG = null;
        Arbre filsD = null;
        Arbre min = null;
        LinkedList<Arbre> buffArbres = null;
        for(int i = 0; i < 2; i++) {
            buffArbres = new LinkedList<Arbre>();
            min = new Arbre(214748364, null, Arbre.FIN, Arbre.FIN);
            LinkedList<Arbre> a = frequency_l;
            Arbre b;
            for(int e = 0 ; e < frequency_l.size(); e++) {
                b = a.tete();
                if (b.infoi() < min.infoi()) {
                    if (min.infoi() != 214748364) {
                        buffArbres = buffArbres.prefixer(min);
                    }
                    min = b;
                } else { 
                    buffArbres = buffArbres.prefixer(b);
                }
                a = a.reste();
            }
            if (i == 0) filsG = min;
            if (i == 1) filsD = min;
            frequency_l = buffArbres;
        }
        int somme = filsG.infoi() + filsD.infoi();
        buffArbres = buffArbres.prefixer(new Arbre(somme, "somme", filsG, filsD));
        return huffman(buffArbres);
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
     * @see getAllBinaryCodes
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
     * function récursive pour récupré le codage de chanque caractère
     * à trouver dans la list chainée de frequence
     * 
     * @param tree
     * @param frequency
     * @return une liste à deux dimensions avec la code binaire de chaque carractere.
     */
    public static LinkedList<LinkedList<String>> getAllBinaryCodes(Arbre tree, LinkedList<LinkedList<String>> frequency) {
        if(frequency.size() == 0 ) return new LinkedList<LinkedList<String>>();
        String value = frequency.tete().reste().tete();
        String code = getBinaryCode(tree, value);
        return getAllBinaryCodes(tree,frequency.reste()).prefixer(new LinkedList<String>(value, new LinkedList<String>(code, new LinkedList<String>())));
    }
    
    /**
     * Charge le contenue d'un fichier text
     * 
     * @param path le lien vers le fichier text
     * @return le contenue du fichier dans une string
     */
    public static String loadText(String path) {
        InputStream text_path = App.class.getResourceAsStream(path);
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


    /**
     * Function récursive qui récupre
     * le code binaire associé à un carractere
     * contenue dans str
     * 
     * @param str le carrectere rechercher
     * @param codes liste des codes.
     * @return str de 0 et de 1;
     */
    private static String getCodes(String str, LinkedList<LinkedList<String>> codes ) {
        if (codes.vide()) return null;
        if (codes.tete().reste().tete().equals(str)) {
            return codes.tete().tete();
        } else {
            return getCodes(str, codes.reste());
        }
    }


    /**
     * @param codes
     * @return la taille du plus long code binaire 
     */
    private static int mostbig(LinkedList<LinkedList<String>> codes) {
        if (codes.vide()) return 0;
        return Math.max(codes.tete().reste().tete().length() , mostbig(codes.reste()));
    }


    /**
     * On prend la taille du plus long code binaire et on le reduit 
     * jusqu'a que la valeurs sois dans le tableux des codes et
     * recomence à l'index fin du code trouvé 
     * 
     * exemple d'excusion simplifié:
     *  Mon code à décoder : 11101
     *  Taille du code max : 4;
     *  On prend les 0 à 3 première valeurs du code à décoder
     *  1110 : pas dans le tableau => on reduit de 1
     *  111 : pas dens le tableau => on reduit de 1
     *  11 : dans le tableau ! => on conserve la valeurs qui lui est associé
     *  On décale le debut de la boucle
     *  On prend les 2 à 4 première valeurs du code à décoder
     *  101 : dans le tableau ! =>  on conserve la valeurs qui lui est associé
     *  Il n'y a plus rien à prendre dans le code à décodér
     *  donc on return les valeurs associé.
     * 
     * 
     * @param binaire le text en binaire a décodé
     * @param codes liste des codes de chaque carractere
     * @return
     */
    public static String decode(String binaire, LinkedList<LinkedList<String>> codes) {
        int max = mostbig(codes);
        int start = 0;
        String code_buff;
        int end = max > binaire.length() ? binaire.length() : max;
        String result = "";
        while(end > start) {
            code_buff = getCodes(slice(start, end, binaire), codes); 
            if ( code_buff != null) {
                result += code_buff;
                start = end;
                end = (start + max) > binaire.length() ? binaire.length() : start + max;
            } else {
                end--;
            }
        }
        return result;
    }


    /**
     * Reproduction de la function regex slice.
     * return les valeurs d'un string entre un interval
     * 
     * @param start index de debut de la découpe
     * @param end index de fin de la découpe
     * @param str string à découpé
     * @return la string découpé
     */
    public static String slice(int start, int end, String str) {
        if (start < 0 && end > str.length() && start > end) return null; //on verif que start et end ne sont pas en déhort de valeurs du string
        String finish = "";
        String[] splited = str.split("");
        for (int i = start ; i < end; i++) finish += splited[i];
        return finish;
    }


    public static void main( String[] args ) 
    {   
        //load text et frequence
        LinkedList<LinkedList<String>> frequency = load("/tab.ods");
        String texteSE = loadText("/texteSE.txt");
        String texteVH = loadText("/texteVH.txt");

        //on crée l'arbre
        LinkedList<Arbre> list_abr = Convert(frequency);
        Arbre tree = getHuffman(list_abr);        
        LinkedList<LinkedList<String>> codes = getAllBinaryCodes(tree, frequency);

        //test utilisater
        Scanner input = new Scanner(System.in);
        System.out.print("TexteSE (1), TexteVH (2) :");
        String userName = input.nextLine();

        switch(userName) {
            case "1":
            System.out.println(encode(texteSE, tree));
            System.out.println("\n\n");
            System.out.println(decode(encode(texteSE, tree), codes));
            break;
            case "2":
            System.out.println(encode(texteVH, tree));
            System.out.println("\n\n");
            System.out.println(decode(encode(texteVH, tree), codes));
            break;
            default:
            System.out.println("pas trouvé");
        }
    }
}
