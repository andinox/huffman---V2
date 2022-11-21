package fr.nicolas;

/**
 * Liste chainée
 * @author nicolas riedel
 */
public class LinkedList<T> {

    private final LinkedList<T> reste;
    private final T value;


    /** 
     * Liste chainée
     * Cette class prend deux attributs
     * {@code value} qui contient la valeurs que tu decide
     * et de type {@code T}.
     * {@code reste} contient un autre liste chainée obligatoirement
     * du meme type de {@code T}.
     * @param value variable de type T
     * @param reste liste chainée
     * @see LinkedListe;
     */
    public LinkedList(T value, LinkedList<T> reste ) {
        this.value = value;
        this.reste = reste;
    }

    /**
     * Liste chainée vide
     * Permet de crée un liste vide pour initialisé une liste
     * ou définir la fin d'une liste chainée
     */
    public LinkedList() {
        this.value = null;
        this.reste = null;
    }

    /**
     * 
     * @return le contenue en tete de la liste
     */
    public T tete()
    {
        if (this.value == null)
        {
            throw new IllegalArgumentException("Liste is empty");
        } 
        else 
        {
            return this.value;
        }
    }

    
    /**
     * 
     * @return le liste chainée en reste de type {@code T}
     */
    public LinkedList<T> reste()
    {
        if (this.reste == null)
        {
            throw new IllegalArgumentException("Liste is empty");
        } 
        else 
        {
            return this.reste;
        }
    }


    /**
     * true si vide
     * false sinon
     * 
     * @return boolean
     */
    public boolean vide()
    {
        return this.reste == null;
    }



    
    public LinkedList<T> prefixer(T value2)
    {
        return new LinkedList<T>(value2, this);
    }

    public LinkedList<T> inserer(T value_)
    {
        if(this.vide()) return this.prefixer(value_);
        return this.reste().inserer(value_).prefixer(this.tete());
    }
    
    public LinkedList<T> insererOrd(T value)
    {
        if(this.vide()) return this.prefixer(value);
        return ((Arbre) this.tete()).infoi()  > ((Arbre) value).infoi()  ? this.prefixer(value) : this.reste().insererOrd(value).prefixer((T) this.tete());
    }


    @Override
    public String toString()
    {
        return "[" + this.affiche();
    }
    private String affiche() {
        if (this.vide()) return "]";
        return this.tete().toString() + (this.reste().vide() ? "]" : "," + this.reste().affiche());
    }


    public Integer size() {
        if (this.vide()) return 0;
        return 1 + this.reste().size();
    }
}
