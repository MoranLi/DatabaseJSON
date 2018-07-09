public class SQLQuery {

    // name of database
    String datbaseName;

    public SQLQuery(String datbase){
        datbaseName = "clones_"+datbase;
    }

    /**
     *  return a string declare which typeXclone table it query oon
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    private String fromtypeXclones(int type){
        // example: "from ctags.type1Clones;"
        return " from ".concat(datbaseName.concat(".type".concat(Integer.toString(type).concat("clones"))));
    }

    /**
     * return a string SQL query to select all distinct file name from typeXclone table
     * X refers to a number
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    public String selectAllfiles(int type){
        return "select distinct filepath".concat(fromtypeXclones(type).concat(";")) ;
    }

    /**
     * return a string SQL query to select minimum revision from typeXclone table
     * X refers to a number
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    public String selectMinRevision(int type){
        return "select min(revision)".concat(fromtypeXclones(type).concat(";")) ;
    }

    /**
     * return a string SQL query to select maximum revision from typeXclone table
     * X refers to a number
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    public String selectMaxRevision(int type){
        return "select max(revision)".concat(fromtypeXclones(type).concat(";")) ;
    }

    /**
     * return a string SQL query to select all file information base on filepath from typeXclone table
     * @param file filepath of file
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    public String getFileXInfo(String file, int type){
        String selectQuery = "select distinct globalcloneid";
        String fromQuery = fromtypeXclones(type);
        // example: "where filepath = "hello.java" "
        String whereQuery = " where filepath = \"".concat(file.concat("\" ;"));
        // return selectQuery + fromQuery + whereQuery
        return selectQuery.concat(fromQuery.concat(whereQuery));
    }

//    /**
//     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
//     * @param selectTableQuery a select sql query , could be result of getFileXInfo()
//     * @return String SQL query
//     */
//    private String selectChainIdFromFile(String selectTableQuery){
//        // the sql will first execute the selectTableQuery and store result as a table
//        // a is the name of table generated above
//        return "select distinct globalcloneid from (".concat(selectTableQuery.concat(") a ;"));
//    }
//
//
//    /**
//     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a table of a file
//     * file is base on filepath from typeXclone table
//     * @param file filepath of file
//     * @param type number id of table, could be 1,2,3,123
//     * @return String SQL query
//     */
//    public String selectChainIdFromGivenFile(String file, int type){
//        String getFileinfo = getFileXInfo(file,type);
//        return selectChainIdFromFile(getFileinfo.substring(0,getFileinfo.length()-1));
//    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @return String SQL query
     */
    public String selectRevisionCloneidFromFileByChain(int type, int globalCloneId, String file){
        return "select revision, cloneid".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId).concat(" and filepath = \"".concat(file.concat("\" ;"))))));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @return String SQL query
     */
    public String selectMinRevisionFromFileByChain(int type, int globalCloneId, String file){
        return "select min(revision)".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId).concat(" and filepath = \"".concat(file.concat("\" ;"))))));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @return String SQL query
     */
    public String selectMaxRevisionFromFileByChain(int type, int globalCloneId, String file){
        return "select max(revision)".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId).concat(" and filepath = \"".concat(file.concat("\" ;"))))));
    }

    /**
     * return a string SQL query to select cloneid from a given table and a revision
     * @param selectTableQuery a select sql query , could be result of selectRevisionCloneidFromFileByChain()
     * @param revision number of start version of a clone chain
     * @return String SQL query
     */
    private String selectCloneIdFromGlobalCloneId(String selectTableQuery, int revision){
        return "select cloneid from (".concat(selectTableQuery.concat(") a where revision = ".concat(Integer.toString(revision).concat(";"))));
    }

    /**
     * return a string SQL query to select chain id from a table of a globalChainId and a revision
     * globalChainId is base on filepath
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @param revision number of start version of a clone chain
     * @return String SQL query
     */
    public String selectCloneIdFromFromFileByChain(int type, int globalCloneId, String file,int revision){
        return "select cloneid".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId).concat(" and filepath = \"".concat(file.concat("\" and revision = ".concat(Integer.toString(revision).concat(" ;"))))))));
    }


    /**
     *  return a string SQL query to select distinct clone chain id from a typeXClone
     * @param type number id of table, could be 1, 2, 3, 123
     * @return String SQL query
     */
    public String selectChainId(int type){
        return "select distinct globalcloneid".concat(fromtypeXclones(type));
    }

    /**
     *  return a string SQL query to select distinct revision from a typeXClone
     * @param type number id of table, could be 1, 2, 3, 123
     * @return String SQL query
     */
    public String selectRevision(int type){
        return "select distinct revision".concat(fromtypeXclones(type));
    }

    /**
     * return a string SQL query to select min revision of a chain base on globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @return String SQL query
     */
    public String selectMinRevisionByChain(int type, int globalCloneId){
        return "select min(revision)".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId))));
    }

    /**
     * return a string SQL query to select max revision of a chain base on globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @return String SQL query
     */
    public String selectMaxRevisionByChain(int type, int globalCloneId){
        return "select max(revision)".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId))));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @return String SQL query
     */
    public String selectRevisionChangecountStartEndCloneClassByChain(int type, int globalCloneId){
        return "select revision, cloneid, changecount, startline, endline, cloneclass".concat(fromtypeXclones(type).concat(" where globalcloneid = ".concat(Integer.toString(globalCloneId))));
    }

    /**
     * return a string SQL query to select min revision of system
     * @return String SQL query
     */
    public String selectMinSystemRevision(){
        return "select min(revision) from ".concat(datbaseName.concat(".methods"));
    }

    /**
     * return a string SQL query to select max revision of system
     * @return String SQL query
     */
    public String selectMaxSystemRevision(){
        return "select max(revision) from ".concat(datbaseName.concat(".methods"));
    }

    public String selectNumOfFile() {
        return "select count(distinct filepath) from ".concat(datbaseName.concat(".methods"));
    }

    public String selectNumOfCloneChain() {
        return "select count(distinct filepath) from ".concat(datbaseName.concat(".methods"));
    }

    public String selectNumOfClone() {
        String t1 = "select count(distinct cloneid)".concat(fromtypeXclones(1));
        String t2 = "select count(distinct cloneid)".concat(fromtypeXclones(2));
        String t3 = "select count(distinct cloneid)".concat(fromtypeXclones(3));
        return "select (".concat(t1).concat(") + (").concat(t2).concat(") + (").concat(t3).concat(");");
    }

    public String sumChangeCountOfCloneChain(int type, int chainId){
        return "select sum(changecount)".concat(fromtypeXclones(type)).concat( " where globalcloneid = ").concat(Integer.toString(chainId)).concat(";");
    }

    public String selectFilepathOfCloneChain(int type, int chainId){
        return "select distinct filepath ".concat(fromtypeXclones(type)).concat( " where globalcloneid = ").concat(Integer.toString(chainId)).concat(";");
    }

    public String selectRevisionChangeCountOfChain(int type, int chainId){
        return "select revision, changecount".concat(fromtypeXclones(type)).concat( " where globalcloneid = ").concat(Integer.toString(chainId)).concat(";");
    }

    public static void main(String[] args) {
        SQLQuery sq = new SQLQuery("ctags");
        System.out.println(sq.selectAllfiles(1));
        System.out.println(sq.selectMaxRevision(1));
        System.out.println(sq.selectMinRevision(1));
        System.out.println(sq.getFileXInfo("asp.c",1));
        System.out.println(sq.selectMaxRevisionFromFileByChain(1,1,"asp.c"));
        System.out.println(sq.selectMinRevisionFromFileByChain(1,1,"asp.c"));
        System.out.println(sq.selectCloneIdFromFromFileByChain(1,1,"asp.c",1));
        System.out.println(sq.selectChainId(1));
        System.out.println(sq.selectRevision(1));
        System.out.println(sq.selectMaxRevisionByChain(1,1));
        System.out.println(sq.selectRevisionChangecountStartEndCloneClassByChain(1,1));
        System.out.println(sq.selectNumOfFile());
        System.out.println(sq.selectNumOfClone());
        System.out.println(sq.sumChangeCountOfCloneChain(1,1));
        System.out.println(sq.selectRevisionChangeCountOfChain(1,1));
    }


}
