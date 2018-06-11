import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SqlDataToPCJson {
    /**
     *  write generated json string to file
     * @param name name of generated json file
     * @param text string of json
     */
    public void writeToFile(String name, String text){
        try (PrintWriter out = new PrintWriter("D://"+name+".json")) {
            out.println(text);
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     *  main function perform system logic to generate a json file base on user input
     * @param databaseName name of database choose
     */
    public void generateJSONFile(String databaseName) {
        // create database connection
        MySQLJDBC database = new MySQLJDBC();
        // create query instance, testing schema clones_camellia(smallest)
        SQLQuery sq = new SQLQuery(databaseName);
        // whole collection
        // contain 3 child, refers to type 1,2,3 clone
        JSONArray evolutionList = new JSONArray();
        // data description about whole system
        JSONObject systemInfo = new JSONObject();
        // get minimum revision of current system in type i clone
        int minRevision = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMinSystemRevision()).get(0));
        int maxRevision = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMaxSystemRevision()).get(0));
        // loop through three type of clone
        for(int i=1;i<4;i++){
            HashMap cloneChainList = database.doExecutionWithReturnJSON(sq.selectChainId(i));
            for(int j=0;j<cloneChainList.size();j++){
                Integer chainId = Integer.parseInt((String)cloneChainList.get(j));
                if(chainId <= 0) continue;
                HashMap revisionChangecountData = database.doExecutionWithReturnJSON(sq.selectRevisionChangecountByChain(i,chainId));
                int startRevsion = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMinRevisionByChain(i,chainId)).get(0));
                int endRevsion = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMaxRevisionByChain(i,chainId)).get(0));
                JSONObject cloneChainsObject = new JSONObject();
                cloneChainsObject.accumulate("Name",chainId);
                cloneChainsObject.accumulate("StartRevision",startRevsion);
                cloneChainsObject.accumulate("EndRevision",endRevsion);
                for(int k = minRevision;k<=maxRevision;k++){
                    try{
                        cloneChainsObject.accumulate(Integer.toString(k),Integer.parseInt((String)revisionChangecountData.get(k)));
                    } catch (Exception e){
                        cloneChainsObject.accumulate(Integer.toString(k),-1);
                    }
                }
                evolutionList.add(cloneChainsObject);
            }
        }
        systemInfo.accumulate("clone_evolution",evolutionList);
        systemInfo.accumulate("min_revsion",minRevision);
        systemInfo.accumulate("max_revision",maxRevision);
        writeToFile(databaseName,systemInfo.toString());
    }

    public static void main(String[] args) {
        SqlDataToPCJson sdpcj = new SqlDataToPCJson();
        sdpcj.generateJSONFile("camellia");
    }

}
