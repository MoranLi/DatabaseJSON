import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

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
        int maxChangeCount = 0;
        // loop through three type of clone
        for(int i=1;i<4;i++){
            HashMap<Integer,String> cloneChainList = database.doExecutionWithReturnJSON(sq.selectChainId(i));
            LinkedList<Integer> visited = new LinkedList<>();
            HashMap<Integer,HashMap<Integer,String>> sumOfChainChangeCount = new HashMap<>();
            // data for globalcloneid == 0 is strange
            for(int n = 1;n < cloneChainList.size();n++){
                Integer a_chainId = Integer.parseInt(cloneChainList.get(n));
                HashMap<Integer,String> currentChainChangeCount = database.doExecutionWithReturnJSON(sq.selectRevisionChangeCountOfChain(i,a_chainId));
                sumOfChainChangeCount.put(a_chainId,currentChainChangeCount);
            }
            for(int j=1;j<cloneChainList.size();j++){
                Integer chainId = Integer.parseInt((String)cloneChainList.get(j));
                // ignore error case
                if(chainId <= 0) continue;
                JSONObject similarCloneX = new JSONObject();
                HashMap<Integer,String> currentChainChangeCount = database.doExecutionWithReturnJSON(sq.selectRevisionChangeCountOfChain(i,chainId));
                for (int m=0;m<cloneChainList.size();m++){
                    Integer anotherChainId = Integer.parseInt((String)cloneChainList.get(m));
                    if( anotherChainId == chainId) continue;
                    if(visited.contains(anotherChainId)) continue;
                    HashMap<Integer,String> anotherChainChangeCount = sumOfChainChangeCount.get(anotherChainId);
                    Set<Integer> currentkeySet = currentChainChangeCount.keySet();
                    Set<Integer> anotherkeySet = anotherChainChangeCount.keySet();
                    Set<Integer> sumkeySet = new HashSet<>();
                    sumkeySet.addAll(currentkeySet);
                    sumkeySet.addAll(anotherkeySet);
                    int same = 0;
                    for(Integer n : sumkeySet){
                        // both chain at revision n do have change count and it is not 0
                        if(!(currentChainChangeCount.get(n) == null || anotherChainChangeCount.get(n) == null) &&
                                !(Integer.parseInt((String)currentChainChangeCount.get(n)) == 0 || Integer.parseInt((String)anotherChainChangeCount.get(n)) == 0
                                )){
                            same++;
                        }
                    }
                    similarCloneX.accumulate(Integer.toString(anotherChainId),same/currentkeySet.size());
                }
                HashMap revisionChangecountData = database.doExecutionWithReturnJSON(sq.selectRevisionChangecountStartEndCloneClassByChain(i,chainId));
                int startRevsion = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMinRevisionByChain(i,chainId)).get(0));
                int endRevsion = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectMaxRevisionByChain(i,chainId)).get(0));
                int sumChangeCount = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.sumChangeCountOfCloneChain(i,chainId)).get(0));
                maxChangeCount = Math.max(maxChangeCount,sumChangeCount);
                JSONObject cloneChainsObject = new JSONObject();
                cloneChainsObject.accumulate("Name",chainId);
                cloneChainsObject.accumulate("StartRevision",startRevsion);
                cloneChainsObject.accumulate("EndRevision",endRevsion);
                cloneChainsObject.accumulate("SumChangeCount",sumChangeCount);
                cloneChainsObject.accumulate("similar",similarCloneX.toString());
                cloneChainsObject.accumulate("Type",i);
                for(int k = startRevsion;k<=endRevsion;k++){
                    JSONObject cloneInstanceObject = new JSONObject();
                    String cloneInfo = (String)revisionChangecountData.get(k);
                    String [] Infos = cloneInfo.split(":");
                    cloneInstanceObject.accumulate("cloneid",Infos[0]);
                    cloneInstanceObject.accumulate("changecount",Infos[1]);
                    cloneInstanceObject.accumulate("startline",Infos[2]);
                    cloneInstanceObject.accumulate("endline",Infos[3]);
                    cloneInstanceObject.accumulate("cloneclass",Infos[4]);
                    cloneChainsObject.accumulate(Integer.toString(k),cloneInstanceObject.toString());
                }
                evolutionList.add(cloneChainsObject);
                visited.push(chainId);
            }
        }
        int numOfFile = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectNumOfFile()).get(0));
        int numOfClone = Integer.parseInt((String)database.doExecutionWithReturnJSON(sq.selectNumOfClone()).get(0));
        systemInfo.accumulate("clone_evolution",evolutionList);
        systemInfo.accumulate("min_revision",minRevision);
        systemInfo.accumulate("max_revision",maxRevision);
        systemInfo.accumulate("system_name",databaseName);
        systemInfo.accumulate("num_file",numOfFile);
        systemInfo.accumulate("num_clone",numOfClone);
        systemInfo.accumulate("max_changecount",maxChangeCount);
        writeToFile(databaseName,systemInfo.toString());
    }

    public static void main(String[] args) {
        SqlDataToPCJson sdpcj = new SqlDataToPCJson();
        sdpcj.generateJSONFile("ctags");
    }

}
