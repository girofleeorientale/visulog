package up.visulog.analyzer;
import up.visulog.analyzer.liste.GString;
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;
import java.io.*;

public class Diff implements AnalyzerPlugin {

  private final Configuration configuration;
  private Result result;

  // CONSTRUCTEUR ---------------------------------------------------------------
  public Diff(Configuration c){
    configuration=c;
  }
  public Diff(){
    this(null);
  }
  // GET SET --------------------------------------------------------------------
  public Result getResult() {
      if (result == null) run();
      return result;
  }
  @Override
  public String getHelp() {
      return "This plugin count de difference betwen 2 fonctions.";
  }
  @Override
  public String getName() {
      return "diff";
  }
  // Fonctions propre -----------------------------------------------------------
  public void run() {
    String newBranch = "ajoutDOutilsDeCouparateurDeFichier";
    String oldBranch = "develop";
    String fileName = "analyzer/src/main/java/up/visulog/analyzer/Analyzer.java";
    result = new Result(nbrDeLigneDiff(newBranch,oldBranch,fileName),fileName);
    //System.out.println("FIN RUN");
  }
  /**
  *Use the command <code>git show branch:file</code> to read the 2 version of a file.
  */
  public static GString readFile(String s){
    GString gs = new GString();
    try {
      String[] cmd = new String[3];
      cmd[0] = "git";
      cmd[1] = "show";
      cmd[2] = s;
      //System.out.println("launch of the cmd :\"git show "+s+"\"");

      // create runtime to execute external command
      Runtime rt = Runtime.getRuntime();
      Process pr = rt.exec(cmd);

      // retrieve output from git script
      String line = null; int k=0;
      BufferedReader bfr = new BufferedReader(new InputStreamReader(pr.getInputStream()));
      while ((line = bfr.readLine()) != null) {
        gs.add(bfr.readLine());k++;
      }
      //System.out.println(k+" line have been found");
    }catch (Exception e) {
      System.out.println("error in reading file");
    }
    return gs;
  }
  /**
  *Read the 2 versions as "chained list" and use the other nbrDeLigneDiff.
  */
  public static int [] nbrDeLigneDiff(String newBranch, String oldBranch, String fileName){
    //GString gs1 = lireUnFichier.lireUnFichierGs(nouveauFichier);
    //GString gs2 = lireUnFichier.lireUnFichierGs(ancienFichier);
    GString gs1 = readFile(newBranch+":"+fileName);
    GString gs2 = readFile(oldBranch+":"+fileName);

    return nbrDeLigneDiff(gs1,gs2);
  }
  /**
  *calculate the 2 data, number of modify line and number of add line.
  */
  public static int [] nbrDeLigneDiff(GString gs1, GString gs2){
    //on compte la différence de ligne entre les 2 fichiers.
    int ligneAjoute = gs1.length() - gs2.length();
    //on parcours le nouveau fichier en cherchant si la ligne existe déjà dans l'ancien fichier.
    //int ligneModifieOuAjoute = gs1.compterLigneDifferenteDe(gs2);
    //autre méthode qui prend compte le fait que certaine ligne comme } sont suceptible d'extisté dans le fichier de base et de pourtant compter comme une ligne ajouté.
    gs1.supprimerLesLignesCommunesAvec(gs2);
    int ligneModifieOuAjoute = gs1.length();
    int t[] = new int [2];
    t[0] = ligneAjoute;
    t[1] = gs1.length()-ligneAjoute; //c'est les lignes modifiés.
    return t;
  }
  /*public static void affNbrDeLigneDiff(String nouveauFichier, String ancienFichier){
    affNbrDeLigneDiff(nbrDeLigneDiff(nouveauFichier,ancienFichier));
  }*/
  public static void affNbrDeLigneDiff(int t []){
    System.out.println("ligne ajouté : "+t[0]);
    System.out.println("ligne modifié : "+t[1]);
    System.out.println("ligne modifié ou ajouté : "+(t[0]+t[1]));
    //System.out.println();
  }
  /**
  *methode use to test if 2 int [] are equals.
  */
  public static boolean egal(int [] t, int t2 []){
    if(t.length!=t2.length){return false;}
    int len = t.length;
    for (int i=0;i<len ;i++ ) {
      if(t[i]!=t2[i]){return false;}
    }
    return true;
  }
  static class Result implements AnalyzerPlugin.Result {
    private final String fileName;
    /**
    *Array with the 2 data.
    */
    private final int tr [];

    Result(int tr[],String fn){
      this.tr=tr;
      fileName=fn;
    }

    @Override
    public String getResultAsString() {
        String sr = "";
        sr+="ligne ajouté : "+tr[0];sr+="\n";
        sr+="ligne modifié : "+tr[1];sr+="\n";
        sr+="ligne modifié ou ajouté : "+(tr[0]+tr[1]);sr+="\n";
        //sr+="\n";
        return sr;
    }
    @Override
    public String getResultAsHtmlDiv() {
      String a =
              "<!DOCTYPE HTML>\n" +
                      "<html>\n" +
                      "<head>\n" +
                      "<script src=\"https://canvasjs.com/assets/script/canvasjs.min.js\"></script>\n" +
                      "<script type=\"text/javascript\">\n" +
                      "\n" +
                      "window.onload = function () {\n" +
                      "\tvar chart = new CanvasJS.Chart(\"chartContainer\", {\n" +
                      "\t\ttitle:{\n" +
                      "\t\t\ttext: \""+fileName+"\"              \n" +
                      "\t\t},\n" +
                      "\t\tdata: [              \n" +
                      "\t\t{\n" +
                      "\t\t\t// Change type to \"doughnut\", \"line\", \"splineArea\", etc.\n" +
                      "\t\t\ttype: \"column\",\n" +
                      "\t\t\tdataPoints: [\n" ;

                      String c =
                              "\t\t\t]\n" +
                                      "\t\t}\n" +
                                      "\t\t]\n" +
                                      "\t});\n" +
                                      "\tchart.render();\n" +
                                      "}\n" +
                                      "</script>\n" +
                                      "</head>\n" +
                                      "<body>\n" +
                                      "<div id=\"chartContainer\" style=\"height: 300px; width: 100%;\"></div>\n" +
                                      "</body>\n" +
                                      "</html>";
      String sr = "";sr+="\n";
      //sr+="Fichier : "+fileName;sr+="<br>";sr+="\n";
      sr+="{label:'"+"line add"+"', y:"+tr[0]+"},\n";
      //sr+="ligne ajouté : "+tr[0];sr+="<br>";sr+="\n";
      sr+="{label:'"+"line modify"+"', y:"+tr[1]+"},\n";
      //sr+="ligne modifié : "+tr[1];sr+="<br>";sr+="\n";
      //sr+="ligne modifié ou ajouté : "+(tr[0]+tr[1]);sr+="<br>";sr+="\n";
      //sr+="\n";
      return a+sr+c;
    }
  }
}
