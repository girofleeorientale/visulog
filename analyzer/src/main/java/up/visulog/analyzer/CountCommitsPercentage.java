package up.visulog.analyzer;
//L'oppération principale de ce fichier est "processLog".

import up.visulog.config.Configuration;
import up.visulog.gitrawdata.Commit;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class CountCommitsPercentage implements AnalyzerPlugin {
    private final static SimpleDateFormat dateCommitFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.ENGLISH);
    private final static SimpleDateFormat dateCommitFormatParse = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z",Locale.ENGLISH);
    private final Configuration configuration;
    private Result result;//Result contient une table de hashage qu'elle peut renvoyer tel quel ou alors au format String ou Html.


    public CountCommitsPercentage() {
        this.configuration = null;
    }

    public CountCommitsPercentage(Configuration generalConfiguration) {
        this.configuration = generalConfiguration;//L'objet personnalisté Configuration y est déclarez via le contructeur. La configuration est définitive.
    }

    static Result processLog(List<Commit> gitLog) {
        var result = new Result();
        Map<String, String> resultsPerAuthor = new HashMap<>();
        Map<String, Integer> middleMap = new HashMap<>();

        for (var commit : gitLog) {
            if (middleMap.containsKey(commit.author)) {
                middleMap.put(commit.author, middleMap.get(commit.author)+1);
            }
            else {
                middleMap.put(commit.author, 1);
            }
        }
         middleMap.forEach((author,countCommit) -> {
             resultsPerAuthor.put(author, String.format("%.2f",(double)countCommit*100/gitLog.size()).replace(",","."));
         });
        double res = resultsPerAuthor.values().stream()
                .map(Double::parseDouble)
                .mapToDouble(Double::doubleValue)
                .sum();
        System.out.println("RES "+res);
        if (res != 100) {
            System.out.println("result : " + (100-res));
        }

        result.commitsMap = resultsPerAuthor;
        return result;
    }

        @Override
        public void run () {//La méthode run lance une opération qui est enregistrer dans result.
            result = processLog(Commit.parseLogFromCommand(configuration.getGitPath()));
        }
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
            return "countPercentage";
        }

        static class Result implements AnalyzerPlugin.Result {
            Map<String, String> commitsMap = new HashMap<>();


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


                StringBuilder html = new StringBuilder("<div>{Percentage of commits per author: <ul>");
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
