package up.visulog.analyzer.liste;

public class GString {
  private CString debut, fin;
  // CONSTRUCTEUR -----------------------------------------------------------------
  public GString(){}

  // GET SET -----------------------------------------------------------------------
  public CString getdebut(){ return debut;}
  public CString getFin(){ return fin;}
  // Fonctions propre -----------------------------------------------------------
  public String toString(){
    if(debut==null){return "";}
    return debut.toString();
  }
  public int length(){
    if(debut==null){ return 0;}
    return debut.length();
  }
  public void ajouter(String s){ // On ajoute a la fin par defaut.
    CString c = new CString(s);
    if (fin == null){ // si la liste est completement vide.
      fin = c;
      debut = c;
    } else {
      fin.setSuivant(c);
      c.setPrecedent(fin);
      fin = c;
    }
  }
  public void ajouter(GString gs){
    if(gs==null){ return;}
    if(this.getdebut()==null){debut = gs.getdebut();return;}
    //on lie l'anciene fin au debut de gs.
    this.fin.setSuivant(gs.getdebut());
    this.fin.getSuivant().setPrecedent(fin);
    // on change la fin actuelle.
    this.fin = gs.getFin();
  }
  public void add(String s){ ajouter(s);}
  public void add(GString s){ ajouter(s);}
  public String concatene(){
    if (debut == null){ // 0
      return "";
    } else if (debut.getSuivant() == null) { // 1
      return debut.getContenu();
    } else {
      return debut.concatene(); // plus d'1
    }
  }
  public String concateneCompacte(){
    if (debut == null){ // 0
      return "";
    } else if (debut.getSuivant() == null) { // 1
      return debut.getContenu();
    } else {
      return debut.concateneCompacte(); // plus d'1
    }
  }
  public void afficheToi(){
    if(debut==null){
      System.out.println("Le GString est vide");
    }else{
      debut.afficheTout();
    }
  }
  public void filtreDoublon(){//permet de filtre les doublon dans un GString
    if (debut==null){ return;}
    GString gs = debut.filtreDoublon();
    debut = gs.getdebut();
    fin = gs.getFin();
  }
  // renvoie true si est seulement si s est une des String du GString
  public boolean contient(String s){
    if (debut==null){ return false;}
    return debut.contient(s);
  }
  public void classer(){
    if (debut==null){ return;}
    //debut.classer(); a poursuivre
  }
  public int compterLigneDifferenteDe(GString gs2){
    if (debut==null){ return 0;}
    return debut.compterLigneDifferenteDe(gs2);
  }
  public void supprimerLesLignesCommunesAvec(GString gs2){
    if (debut==null){ return;}
    debut.supprimerLesLignesCommunesAvec(gs2,this);//on met aussi ce GString pour pouvoir retirer une CString.
  }
  public boolean supprimer(String s){
    if (debut==null){ return false;}
    if(debut.getContenu().equals(s)){//on teste la 1a CString car apres on verifira seulement la suivante. (puis la suivante et ainsi de suite).
      debut = debut.getSuivant(); return true;
    }
    boolean b = debut.supprimer(s);
    actualiserFin();
    if(!b){System.out.println("la suppression de la ligne qui suis n'as pas pu etre effectuee"+s);}
    return b;
  }
  public void actualiserFin(){ //remet fin a ce place.
    fin = debut;
    while(fin.getSuivant()!=null){//tant que ce n'est pas le dernier elements de la chaine.
      fin = fin.getSuivant();
    }
  }
}
