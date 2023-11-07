package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import javax.management.StringValueExp;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * un plugin pour compter le nombre de commits par jour indépendemment des auteurs
 */

public class CountCommitsPerDayPlugin implements AnalyzerPlugin {
    private final static SimpleDateFormat dateCommitFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
    private final static SimpleDateFormat dateCommitFormatParse = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",Locale.ENGLISH);
    private final Configuration configuration;//L'objet personnalisté Configuration y est déclarez via le contructeur. La configuration est définitive.
    private Result result;//Result contient une table de hashage qu'elle peut renvoyer tel quel ou alors au format String ou Html.

    public CountCommitsPerDayPlugin() {
        this.configuration = null;
    }

    public CountCommitsPerDayPlugin(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<String, Integer> resultsDay = new HashMap<>();

        for (var commit : gitLog) {
            try {
                String date = dateCommitFormat.format(dateCommitFormatParse.parse(commit.date));
                if (resultsDay.containsKey(date)) {
                    resultsDay.put(date, resultsDay.get(date)+1);
                }
                else {
                    resultsDay.put(date, 1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        result.commitsMap = resultsDay;
        return result;
    }

    @Override
    public void run () {//La méthode run lance une opération qui est enregistrer dans result.
        result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
    }
    //La méthode getResult revoie un restultat (qu'elle calcule si nécéssaire).
    @Override
    public Result getResult () {
        if (result == null) run();
        return result;
    }

    @Override
    public String getHelp () {
        return "This plugin counts commits number per day from all authors from repository";
    }

    @Override
    public String getName () {
        return "countCommitsPerDay";
    }

    static class Result implements AnalyzerPlugin.Result {
        Map<String, Integer> commitsMap = new HashMap<>();


        @Override
        public String getResultAsString() {
            return commitsMap.toString();
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

            StringBuilder html = new StringBuilder("<div>Commits per day: <ul>");

            for (var item : commitsMap.entrySet()) {
//                html.append("<li>").append(item.getKey()).append(": ").append(item.getValue()).append("</li>");
//               String label = item.getKey().length() > 9? item.getKey().substring(0,9) : item.getKey();
                b+="{label:'"+item.getKey()+"', y:"+item.getValue()+"},\n";

            }
            html.append("</ul></div>");
            return a+b+c;
        }
    }
}
