package up.visulog.analyzer.liste;

public class CString {
  private CString suivant, precedent;
  private String s;
  // CONSTRUCTEUR -----------------------------------------------------------------
  public CString(String s){
    this.s = s;
    if(this.s==null){this.s="";}
  }
  // GET SET -----------------------------------------------------------------------
  public CString getSuivant(){return suivant;}
  public void setSuivant(CString cs){suivant = cs;}
  public CString getPrecedent(){return precedent;}
  public void setPrecedent(CString cs){precedent = cs;}
  public String getContenu(){ return s;}
  public void setContenu(String x){s=x;}
  // Fonctions propre -----------------------------------------------------------
  public String toString(){
    return concatene();
  }
  public int length(){
    if(suivant==null){ return 1;}
    return 1+suivant.length();
  }
  public String concatene(){
    if (suivant == null){ return s;}
    return s + "\n" + suivant.concatene();
  }
  public String concateneCompacte(){
    if (suivant == null){ return s;}
    return s + ", " + suivant.concateneCompacte();
  }
  public void afficheTout(){
    afficheToi();
    if(suivant != null){ suivant.afficheTout();}
  }
  public void afficheToi(){
    System.out.println(s);
  }
  public GString filtreDoublon(){
    GString gsr = new GString();
    CString csTemp = this;
    while(csTemp != null){
      String s = csTemp.getContenu();
      if (!gsr.contient(s)){
        gsr.ajouter(s);
      }
      csTemp = csTemp.getSuivant();
    }
    return gsr;
  }
  // renvoie true si est seulement si s est une des String du GString
  public boolean contient(String s){
    CString csTemp = this;
    while(csTemp != null){
      if(csTemp.getContenu().equals(s)){ return true;}
      csTemp = csTemp.getSuivant();
    }return false;
  }

  public String getElementX(int x){
    if(x==0){ return getContenu();}
    if(suivant!=null){ return suivant.getElementX(x-1);}
    if(x<0){ return null;}
    return null;
  }

  public int compterLigneDifferenteDe(GString gs2){
    int x=0;
    if(!gs2.contient(getContenu())){x=1;}//on compte 1 ligne differente.
    if(suivant==null){//si c'est la derniere ligne qu'on traite.
      return x;
    }//sinon on demande a la Cellule suivante de ce verifier aussi.
    return x+suivant.compterLigneDifferenteDe(gs2);
  }
  public void supprimerLesLignesCommunesAvec(GString gs2, GString gs1){
    if(gs2.contient(getContenu())){
      gs2.supprimer(getContenu());
      gs1.supprimer(getContenu());//pour evite les soucis de "c'est la 1a CString on passe par le GString."
    }//on supprime la ligne en question dans les 2 fichiers.
    if(suivant==null){//si c'est la derniere ligne qu'on traite.
      return;
    }//sinon on demande a la Cellule suivante de ce verifier aussi.
    suivant.supprimerLesLignesCommunesAvec(gs2,gs1);
  }
  public boolean supprimer(String s){//supprime la 1a occurence de s.
    if(suivant == null){return false;}
    if(suivant.getContenu().equals(s)){
      suivant = suivant.getSuivant();//on saute une Cellule.
      return true;
    }
    return suivant.supprimer(s);
  }
}
