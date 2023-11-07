package up.visulog.cli;

import up.visulog.analyzer.*;
import up.visulog.config.Configuration;
import up.visulog.config.PluginConfig;
import up.visulog.config.DiffConfig;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;
import java.io.FileWriter;

public class CLILauncher {

    public static void main(String[] args) {
        var config = makeConfigFromCommandLineArgs(args);
        if (config.isPresent()) {
            var analyzer = new Analyzer(config.get());
            var results = analyzer.computeResults();
            System.out.println(results.toHTML());
        } else displayHelpAndExit();
    }

    static Optional<Configuration> makeConfigFromCommandLineArgs(String[] args) {
        var gitPath = FileSystems.getDefault().getPath(".");
        Configuration configuration = new Configuration();
        if (args.length != 0) {
            for (var arg : args) {
                if (arg.startsWith("--")) {
                    String[] parts = arg.split("=");
                    String pName = parts[0];
                    String pValue = "";
                    if (parts.length == 2) {
                        pValue = parts[1];
                    }

                    switch (pName) {
                        case "--addPlugin":
                            // TODO: parse argument and make an instance of PluginConfig

                            // Let's just trivially do this, before the TODO is fixed:

                            if (pValue.equals("countCommits"))
                                configuration.addPluginConfig("countCommits", new PluginConfig() {
                                });
                            else if (pValue.equals("countCommitsPerDay"))
                                configuration.addPluginConfig("countCommitsPerDay", new PluginConfig() {
                                });
                            else if (pValue.equals("countPercentage"))
                                configuration.addPluginConfig("countPercentage", new PluginConfig() {
                                });
                            else if (pValue.equals("diff"))
                                configuration.addPluginConfig("diff", new PluginConfig() {
                                });
                            else if (pValue.equals("countCommitsPerMonth"))
                                configuration.addPluginConfig("countCommitsPerMonth", new PluginConfig() {
                                });

                            break;
                        case "--loadConfigFile":
                            // TODO (load options from a file)
                            try {
                                Properties properties = new Properties();
                                properties.load(new FileInputStream("config.cfg"));
                                gitPath = Paths.get(properties.getProperty("gitPath"));
                                for (String plugins : properties.getProperty("plugins").split(",")) {
                                    configuration.addPluginConfig(plugins, new PluginConfig() {

                                    });
                                }

                            } catch (Exception exception) {
                                System.out.println("Cannot read config file");
                            }
                            break;
                        case "--justSaveConfigFile":
                            // TODO (save command line options to a file instead of running the analysis)
                            try {
                                FileWriter fileWriter = new FileWriter("saveConfig.txt", true);
                                fileWriter.write(pValue + "\n");//écriture et sauvegarde de l'option de la commande utilisé dans le fichier
                                fileWriter.close();//fermeture du fichier
                            } catch (IOException exception) {
                                System.err.println("IOException: " + exception.getMessage());
                            }
                            break;
                        default:
                            return Optional.empty();
                    }

                } else {
                    gitPath = FileSystems.getDefault().getPath(arg);
                }
            }
            configuration.setGitPath(gitPath);
            return Optional.of(configuration);
        }
        return Optional.empty();
    }

    private static void displayHelpAndExit() {
        List<AnalyzerPlugin> pluginsList = new ArrayList<>();
        pluginsList.add(new CountCommitsPerAuthorPlugin());
        pluginsList.add(new CountCommitsPerDayPlugin());
        pluginsList.add(new CountCommitsPercentage());
        pluginsList.add(new CountCommitsPerMonthPlugin());
        pluginsList.add(new Diff());
        StringBuilder s = new StringBuilder();
        s.append("--addPlugin\n");
        for (AnalyzerPlugin analyzerPlugin : pluginsList) {
            s.append("\t").append(analyzerPlugin.getName()).append("\n")
                    .append("\t").append(analyzerPlugin.getHelp())
                    .append("\n\n");
        }
        s.deleteCharAt(s.length() - 1);
        System.out.println(s);
        System.exit(0);
    }
}
