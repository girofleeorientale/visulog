package up.visulog.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Configuration {

    private Path gitPath; //gitpath est de type Path et qui sert à stocker le chemin vers notre git
    private final Map<String, PluginConfig> plugins;// identifier et stocker les plugins utilisés dans une collection(Map ici)

    public Configuration(Path gitPath, Map<String, PluginConfig> plugins) {
        this.gitPath = gitPath;
        this.plugins = Map.copyOf(plugins); // copie les plugins passés en paramètres
    }

    public Configuration() {
        this.plugins = new HashMap<>();
    }

    public Path getGitPath() {//getter pour retourner le gitpath
        return gitPath;
    }

    public void setGitPath (Path p) {
        this.gitPath =p;
    }

    public void addPluginConfig (String name, PluginConfig config) {
        plugins.put(name,config);
        //this.gitPath = p;
    }

    //
    //public void addPluginConfig (String name, PluginConfig config) {
    //    plugins.put(name, config);
    //}

    public Map<String, PluginConfig> getPluginConfigs() {//getter pour retourner notre collection qui stocke les plugins
        return plugins;
    }
}
