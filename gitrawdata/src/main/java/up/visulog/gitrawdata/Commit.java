package up.visulog.gitrawdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Commit {


	//*****************Les attribus***************************

    // FIXME: (some of) these fields could have more specialized types than String
    public final String id;
    public final String date;
    public final String author;
    public final String description;
    public final String mergedFrom;
    //*********************Les Constructeurs**********************
    /*
     * Le constructeur d'un commit
     * @param Les informations décrivant un commit son id, ses auteurs,sa date , une description du changement,et d'ou est ce qu'il a étè fusionné
     */
    public Commit(String id, String author, String date, String description, String mergedFrom) {
        this.id = id;
        this.author = author;
        this.date = date;
        this.description = description;
        this.mergedFrom = mergedFrom;
    }
  //*********************Les méthodes****************************

    // TODO: factor this out (similar code will have to be used for all git commands)
    /*
     * Une méthode static
     * @param prends en argument un Path (gitPath)
     * @return une Liste de Commit
     */
    public static List<Commit> parseLogFromCommand(Path gitPath) {
        ProcessBuilder builder =
                new ProcessBuilder("git", "log").directory(gitPath.toFile());
        Process process;
        try {
        	//Essayer de lancer le process
            process = builder.start();
        } catch (IOException e) {
        	//Message d'erreur "L'exception"
            throw new RuntimeException("Error running \"git log\".", e);
        }
        //réucupére le InputSteam du process
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        return parseLog(reader);
    }
  //*****************************************************************
/*
 * @param un objet de type BufferReader
 * @return une liste de Commit
 *
 */
    public static List<Commit> parseLog(BufferedReader reader) {
    	//result est une liste de tableaux
        var result = new ArrayList<Commit>();
        //parseCommit permet de  scanner le commit vite fait
        Optional<Commit> commit = parseCommit(reader);
        //Condition tant que ce commit existe
        while (commit.isPresent()) {
        	//on ajoute le commit à result
            result.add(commit.get());
            //parseCommit permet de  scanner le commit vite fait
            commit = parseCommit(reader);
        }
        return result;
    }

    //*****************************************************************
    /**
     * Parses a log item and outputs a commit object. Exceptions will be thrown in case the input does not have the proper format.
     * Returns an empty optional if there is nothing to parse anymore.
     */
    /*
     * @return une liste ?: Optional est unt classe qui vérifie l'existance d'un objet
     */
    public static Optional<Commit> parseCommit(BufferedReader input) {
        try {

            var line = input.readLine();
            if (line == null) return Optional.empty(); // if no line can be read, we are done reading the buffer
            // idChunks est un tableau des mots de la lignes car split(..) divise une chaine à partir d'un séparateur
            var idChunks = line.split(" ");
            if (!idChunks[0].equals("commit")) parseError();
            //crée un commit avec l'id=idChunks[1]
            var builder = new CommitBuilder(idChunks[1]);

            line = input.readLine();
            //Condition Si la ligne est vide
            while (!line.isEmpty()) {
            	//le premier indice contenant ":"
                var colonPos = line.indexOf(":");
                //retourne la sous-chaine de  "0" jusqu'au colonPos
                var fieldName = line.substring(0, colonPos);
                //Permet d'enlever les blancs (caractéres, espaces) juste apres le fieldName dans une ligne
                var fieldContent = line.substring(colonPos + 1).trim();
                //Suivant le fieldName
                switch (fieldName) {//??? setAuthor prends un auteur en param alors que ici ça prend ce qui vient apres author dans la ligne
                case "Author":
                        builder.setAuthor(fieldContent);
                        break;
                    case "Merge":
                        builder.setMergedFrom(fieldContent);
                        break;
                    case "Date":
                        builder.setDate(fieldContent);
                        break;
                    default: // TODO: warn the user that some field was ignored
                }
                line = input.readLine(); //prepare next iteration
                if (line == null) parseError(); // end of stream is not supposed to happen now (commit data incomplete)
            }

            // now read the commit message per se
            var description = input
                    .lines() // get a stream of lines to work with
                    .takeWhile(currentLine -> !currentLine.isEmpty()) // take all lines until the first empty one (commits are separated by empty lines). Remark: commit messages are indented with spaces, so any blank line in the message contains at least a couple of spaces.
                    .map(String::trim) // remove indentation
                    .reduce("", (accumulator, currentLine) -> accumulator + currentLine); // concatenate everything
            builder.setDescription(description);
            return Optional.of(builder.createCommit());
        } catch (IOException e) {
            parseError();
        }
        return Optional.empty(); // this is supposed to be unreachable, as parseError should never return
    }


    //*****************************************************************

    // Helper function for generating parsing exceptions. This function *always* quits on an exception. It *never* returns.
    private static void parseError() {
        throw new RuntimeException("Wrong commit format.");
    }

    //*****************************************************************
    /*
     * méthode public
     * @parm ne prend pas de paramétres
     * @return une chaine de caractéres contenant la description compléte d'un commit
     */

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                (mergedFrom != null ? ("mergedFrom...='" + mergedFrom + '\'') : "") + //TODO: find out if this is the only optional field
                ", date='" + date + '\'' +
                ", author='" + author + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
