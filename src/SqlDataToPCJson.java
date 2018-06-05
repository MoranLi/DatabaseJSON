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
        // create daatbase connection
        MySQLJDBC database = new MySQLJDBC();
        // create query instance, testing schema clones_camellia(smallest)
        SQLQuery sq = new SQLQuery(databaseName);
        // whole collection
        // contain 3 child, refers to type 1,2,3 clone
        JSONArray evolutionList = new JSONArray();
        // loop through three type of clone
        for(int i=1;i<4;i++){
            // get minimum revision of current system in type i clone
            int minRevision = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMinRevision(i)).get(0));
            HashMap cloneChainList = database.doExecutionWithReturnJSON(sq.selectChainId(i));
            for(int j=0;j<cloneChainList.size();j++){
                Integer chainId = Integer.parseInt((String)cloneChainList.get(j));
                if(chainId <= 0) continue;
                HashMap revisionChangecountData = database.doExecutionWithReturnJSON(sq.selectRevisionChangecountByChain(i,chainId));
                Integer endRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMaxRevisionByChain(i,chainId)).get(0));
                JSONObject cloneChainsObject = new JSONObject();
                for(int k = minRevision;k<=endRevisionOfChain;k++){
                    try{
                        cloneChainsObject.accumulate(Integer.toString(k),Integer.parseInt((String)revisionChangecountData.get(k)));
                    } catch (Exception e){
                        cloneChainsObject.accumulate(Integer.toString(k),0);
                    }
                    //
                }
                evolutionList.add(cloneChainsObject);
            }
        }
        writeToFile(databaseName,evolutionList.toString());
    }

    public static void main(String[] args) {
        SqlDataToPCJson sdpcj = new SqlDataToPCJson();
        sdpcj.generateJSONFile("camellia");
    }

}
