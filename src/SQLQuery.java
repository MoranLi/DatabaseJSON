public class SQLQuery {

    /**
     *  return a string declare which typeXclone table it query oon
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    public String fromtypeXclones(int type){
        // example: "from type1Clones;"
        return " from type".concat(Integer.toString(type).concat("clones"));
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
        String selectQuery = "select revision, globalcloneid";
        String fromQuery = fromtypeXclones(type);
        // example: "where filepath = "hello.java" "
        String whereQuery = "where filepath = \"".concat(file.concat("\" "));
        String orderQuery = "order by globalcloneid;";
        // return selectQuery + fromQuery + whereQuery + orderQuery
        return selectQuery.concat(fromQuery.concat(whereQuery.concat(orderQuery)));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param selectTableQuery a select sql query , could be result of getFileXInfo()
     * @return String SQL query
     */
    public String selectChainIdFromFile(String selectTableQuery){
        // the sql will first execute the selectTableQuery and store result as a table
        // a is the name of table generated above
        return "select distinct globalcloneid from (".concat(selectTableQuery.concat(") a ;"));
    }


    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a table of a file
     * file is base on filepath from typeXclone table
     * @param file filepath of file
     * @param type number id of table, could be 1,2,3,123
     * @return String SQL query
     */
    public String selectChainIdFromGivenFile(String file, int type){
        String getFileinfo = getFileXInfo(file,type);
        return selectChainIdFromFile(getFileinfo.substring(0,getFileinfo.length()-1));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @return String SQL query
     */
    public String selectRevisionCloneidFromFileByChain(int type, int globalCloneId, String file){
        //
        return "Select revision, cloneid".concat(fromtypeXclones(type).concat("where globalcloneid = ".concat(Integer.toString(globalCloneId).concat("and filepath = \"".concat(file.concat("\" ;"))))));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @return String SQL query
     */
    public String selectMinRevisionFromFileByChain(int type, int globalCloneId, String file){
        //
        return "Select min(revision)".concat(fromtypeXclones(type).concat("where globalcloneid = ".concat(Integer.toString(globalCloneId).concat("and filepath = \"".concat(file.concat("\" ;"))))));
    }

    /**
     * return a string SQL query to select distinct globalcloneid(chain_id of a evolution chain) from a given table
     * @param type number id of table, could be 1,2,3,123
     * @param globalCloneId number if of clone chain
     * @param file name of file
     * @return String SQL query
     */
    public String selectMaxRevisionFromFileByChain(int type, int globalCloneId, String file){
        return "Select max(revision)".concat(fromtypeXclones(type).concat("where globalcloneid = ".concat(Integer.toString(globalCloneId).concat("and filepath = \"".concat(file.concat("\" ;"))))));
    }


}
