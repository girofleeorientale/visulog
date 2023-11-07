package up.visulog.analyzer;

import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
     * {@resum Class d'analyse.}
     */
public class Analyzer {
    private final Configuration config;

    private AnalyzerResult result;

    public Analyzer(Configuration config) {
        this.config = config;
    }

    /**
     * creer un plugin avec un nom et une configuration issu d'1 objet Configuration. Puis lance run sur tout les plugin les un après les autres pour l'instant.
     */
    public AnalyzerResult computeResults() {
        List<AnalyzerPlugin> plugins = new ArrayList<>();
        for (var pluginConfigEntry: config.getPluginConfigs().entrySet()) {
            var pluginName = pluginConfigEntry.getKey();
            var pluginConfig = pluginConfigEntry.getValue();
            var plugin = makePlugin(pluginName, pluginConfig);

            System.out.println("tentative d'ajout du plugin : "+pluginName);//@a
            plugin.ifPresent(plugins::add);
        }
        // run all the plugins
        // TODO: try running them in parallel
        for (var plugin: plugins) plugin.run();


        return new AnalyzerResult(plugins.stream().map(AnalyzerPlugin::getResult).collect(Collectors.toList()));
    }

    // TODO: find a way so that the list of plugins is not hardcoded in this factory
    //Make plugin fonctionne avec le package java Optional.
    private Optional<AnalyzerPlugin> makePlugin(String pluginName, PluginConfig pluginConfig) {
        switch (pluginName) {
            // Optional est une classe-enveloppe qui permet d eviter nullPointerException
            // il faut rajouter des cases ??? (hardcoded == utilisé pour des constantes, pas le cas ici)
            //TODO réinverser les case & s'assurer que le case diff marche.
            case "countCommits" : return Optional.of(new CountCommitsPerAuthorPlugin(config));
            case "diff" : return Optional.of(new Diff(config));
            case "countCommitsPerDay" : return Optional.of(new CountCommitsPerDayPlugin(config));
            case "countCommitsPerMonth" : return Optional.of(new CountCommitsPerMonthPlugin(config));
            case "countPercentage" : return Optional.of(new CountCommitsPercentage(config));
            default : return Optional.empty();
        }
    }

}
