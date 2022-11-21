package fr.nicolas;

/**
 * class Arbre
 * @author nicolas riedel
 */
public class Arbre {

    private  Arbre droite;
    private  Arbre gauche;
    private  int value_int;
    private  String value_String;

    public static final Arbre FIN = new Arbre(0,"", null, null);


    /**
     * Abre binaire modifier pour permetre l'utilisation de l'alog d'huffman
     * 
     * @param value_int une valeurs de type int
     * @param value_String une valeurs de tyoe string
     * @param droite un autre abre
     * @param gauche un autre arbre
     */
    public Arbre(int value_int,String value_String, Arbre droite, Arbre gauche) {
        this.value_int = value_int;
        this.value_String = value_String == null ? " " : value_String;
        this.droite = droite;
        this.gauche = gauche;
    }

    public int infoi() {
        if (this.droite == null && this.gauche == null)  throw new IllegalArgumentException("Arbre is empty");
        return this.value_int;
    }

    public String infos() {
        if (this.droite == null && this.gauche == null)  throw new IllegalArgumentException("Arbre is empty");
        return this.value_String;
    }

    public boolean vide() {
        return (this.droite == null && this.gauche == null);
    }

    public Arbre filsGauche() {
        if (this.droite == null && this.gauche == null)  throw new IllegalArgumentException("Arbre is empty");
        return this.gauche; 
    }

    public Arbre filsDroite() {
        if (this.droite == null && this.gauche == null)  throw new IllegalArgumentException("Arbre is empty");
        return this.droite;
    }

    @Override
    public String toString() {
        return this.affiche();
    }
    private String affiche() {
        if (this.vide()) return "x";
        return "[" +this.infoi() + ","+this.infos() + "]" + " => ("+ this.filsGauche().affiche() +"/"+ this.filsDroite().affiche()+")";
    }
}