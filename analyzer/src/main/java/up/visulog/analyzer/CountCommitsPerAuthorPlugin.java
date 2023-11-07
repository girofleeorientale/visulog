package up.visulog.analyzer;
//L'oppération principale de ce fichier est "processLog".
import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * un plugin pour compter le pourcentage des commits par auteurs
 */

public class CountCommitsPerAuthorPlugin implements AnalyzerPlugin {
    private final Configuration configuration;//L'objet personnalisté Configuration y est déclarez via le contructeur. La configuration est définitive.
    private Result result;//Result contient une table de hashage qu'elle peut renvoyer tel quel ou alors au format String ou Html.

    public CountCommitsPerAuthorPlugin() {
        this.configuration = null;
    }

    public CountCommitsPerAuthorPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;//L'objet personnalisté Configuration y est déclarez via le contructeur. La configuration est définitive.
    }
    //Dans cette fonction on parcours la liste des "commit" et ajoute au resultat une information suplémentaire.
    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        for (var commit : gitLog) {
            var nb = result.commitsPerAuthor.getOrDefault(commit.author, 0);
            result.commitsPerAuthor.put(commit.author, nb + 1);
        }
        return result;
    }

    @Override
    public void run() {//La méthode run lance une opération qui est enregistrer dans result.
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }
    //La méthode getResult revoie un restultat (qu'elle calcule si nécéssaire).
    @Override
    public Result getResult() {
        if (result == null) run();
        return result;
    }

    @Override
    public String getHelp() {
        return "This plugin counts commits number per author from repository";
    }

    @Override
    public String getName() {
        return "countCommits";
    }

    static class Result implements AnalyzerPlugin.Result {
        private final Map<String, Integer> commitsPerAuthor = new HashMap<>();

        Map<String, Integer> getCommitsPerAuthor() {
            return commitsPerAuthor;
        }

        @Override
        public String getResultAsString() {
            return commitsPerAuthor.toString();
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
                    "\t\t\ttext: \"My First Chart in CanvasJS\"              \n" +
                    "\t\t},\n" +
                    "\t\tdata: [              \n" +
                    "\t\t{\n" +
                    "\t\t\t// Change type to \"doughnut\", \"line\", \"splineArea\", etc.\n" +
                    "\t\t\ttype: \"column\",\n" +
                    "\t\t\tdataPoints: [\n" ;
                    String b="";

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

            StringBuilder html = new StringBuilder("<div>Commits per author: <ul>");
            for (var item : commitsPerAuthor.entrySet()) {
//                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
//               String label = item.getKey().length() > 9? item.getKey().substring(0,9) : item.getKey();
                b+="{label:'"+item.getKey()+"', y:"+item.getValue()+"},\n";

            }
            html.append("</ul></div>");
            return a+b+c;


        }

    }
}
