package up.visulog.analyzer;

import java.util.List;

public class AnalyzerResult {
    private final List<AnalyzerPlugin.Result> subResults;
    //un getter permet de changer cette liste.
    public List<AnalyzerPlugin.Result> getSubResults() {
        return subResults;
    }

    public AnalyzerResult(List<AnalyzerPlugin.Result> subResults) {
        this.subResults = subResults;
    }

    @Override
    public String toString() {//renvoie les info en temps que string
        return subResults.stream().map(AnalyzerPlugin.Result::getResultAsString).reduce("", (acc, cur) -> acc + "\n" + cur);
    }

    public String toHTML() {//renvoie les info pour qu'elle s'affiche dans une page web.
        return "<html><body>"+subResults.stream().map(AnalyzerPlugin.Result::getResultAsHtmlDiv).reduce("", (acc, cur) -> acc + cur) + "</body></html>";
    }
}
