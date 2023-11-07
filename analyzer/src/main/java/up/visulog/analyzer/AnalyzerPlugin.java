package up.visulog.analyzer;

import java.lang.module.Configuration;
//est une interface qui demande au fichier qui la complète de possèder une fonction run et une fonction getResult.
public interface AnalyzerPlugin {
    interface Result {
        String getResultAsString();
        String getResultAsHtmlDiv();//Une sous interface dans le fichier doit permettre d'afficher les info comme String ou code html.
    }

    /**
     * run this analyzer plugin
     */
    void run();

    /**
     *
     * @return the result of this analysis. Runs the analysis first if not already done.
     */
    Result getResult();

    /**
     *
     * @return text description of this Analyzer
     */
    String getHelp();

    /**
     *
     * @return the name of the plugin
     */
    String getName();
}
