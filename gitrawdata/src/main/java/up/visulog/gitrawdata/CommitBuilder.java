package up.visulog.gitrawdata;

public class CommitBuilder {
	/*
	 
	 *
	 * 
	 */
	//*****************Les attribus***************************
    private final String id;
    private String author;
    private String date;
    private String description;
    private String mergedFrom;
    
    
    //*********************Les Constructeurs**********************
    
    /*Ce constructeur attribue la valeur de id passée en commentaire à l'id de l'objet courant
     * @param un id de type String
     */
    public CommitBuilder(String id) {
        this.id = id;
    }
    //*********************Les méthodes****************************
    /*Un Setter d'auteur
     * @param Un auteur de type "String"
     * @return l'objet de type CommitBuilder qui a comme auteur @param
     */
    public CommitBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }
    //****************************************************************
    /*Un Setter de date 
     * @param Une date  de type "String"
     * @return l'objet de type CommitBuilder qui a comme date  @param
     */
    public CommitBuilder setDate(String date) {
        this.date = date;
        return this;
    }
  //****************************************************************
    /*Un Setter de déscription
     * @param Une description  de type "String"
     * @return l'objet de type CommitBuilder qui a comme description  @param
     */
    public CommitBuilder setDescription(String description) {
        this.description = description;
        return this;
    }
  //****************************************************************
    /*Un Setter de (provenance de la fusion)
     * @param Un mergedFrom (provenance de la fusion) de type "String"
     * @return l'objet de type CommitBuilder qui a comme provenance @param
     */
    public CommitBuilder setMergedFrom(String mergedFrom) {
        this.mergedFrom = mergedFrom;
        return this;
    }
  //****************************************************************
    /*Permet de créer un Commit
     * @return Commit
     */
    public Commit createCommit() {
        return new Commit(id, author, date, description, mergedFrom);
    }
}