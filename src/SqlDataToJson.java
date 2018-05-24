import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SqlDataToJson {

    public void writeToFile(String text){
        try (PrintWriter out = new PrintWriter("filename.txt")) {
            out.println(text);
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void generateJsonFile (String databaseName) {

    }

    public static void main(String[] args) {
        // create daatbase connection
        MySQLJDBC database = new MySQLJDBC();
        // create query instance, testing schema clones_camellia(smallest)
        SQLQuery sq = new SQLQuery("camellia");
        // whole collection
        // contain 3 child, refers to type 1,2,3 clone
        JSONArray evolutionList = new JSONArray();
        JSONObject evolutionListObject = new JSONObject();
        evolutionListObject.accumulate("name","camellia");
        // i = 1/2/3 means type 1/2/3 clones
        for(int i=1;i<4;i++){
            // get minimum revision of current system in type i clone
            int minRevision = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMinRevision(i)).get(0));
            // collection of all files in type i clone
            JSONArray filesList = new JSONArray();
            JSONObject filesObject = new JSONObject();
            filesObject.accumulate("name",Integer.toString(i));
            // get all file name in type i clone
            List fileList = database.doExecutionWithReturn(sq.selectAllfiles(i));
            for(int j=0;j<fileList.size();j++){
                // get specific file name
                String fileName = (String)fileList.get(j);
               // collection for all clone chain in a file
                JSONArray cloneChainsList = new JSONArray();
                JSONObject cloneChainsObject = new JSONObject();
                cloneChainsObject.accumulate("name",fileName);
                // get all clone chain id in file j
                List cloneChainsInFileX = database.doExecutionWithReturn(sq.selectChainIdFromGivenFile(fileName,i));
                for(int k=0;k<cloneChainsInFileX.size();k++){
                    // get specific clone chain id
                    Integer cloneChainId = Integer.parseInt((String)cloneChainsInFileX.get(k));
                    // -1 refers to a invalid value , pass this instance
                    // not sure about meaning of using -1 in data, currently assume is illegal
                    if(cloneChainId == -1)continue;
                    // get start revision of chain k
                    Integer startRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMinRevisionFromFileByChain(i,cloneChainId,fileName)).get(0));
                    // get end revision of chain k
                    // assuming each chain have only one end
                    Integer endRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMaxRevisionFromFileByChain(i,cloneChainId,fileName)).get(0));
                    JSONArray revisionList = new JSONArray();
                    JSONObject revisionObject = new JSONObject();
                    revisionObject.accumulate("name",cloneChainId.toString());
                    // loop through all revision form first revision of system to end revision of chain
                    for(int m=minRevision;m<endRevisionOfChain+1;m++){
                        // use -999 represent currently this chain do not exist
                        if(m<startRevisionOfChain){
                            revisionList.add("-999");
                        }
                        // of chain exist, store its pcid
                        else{
                            String cloneid = (String)database.doExecutionWithReturn(sq.selectCloneIdFromFromFileByChain(i,cloneChainId,fileName,m)).get(0);
                            revisionList.add(cloneid);
                        }
                    }
                    revisionObject.accumulate("children",revisionList);
                    cloneChainsList.add(revisionObject);
                }
                cloneChainsObject.accumulate("children",cloneChainsList);
                filesList.add(cloneChainsObject);
            }
            filesObject.accumulate("children",filesList);
            evolutionList.add(filesObject);

        }
        evolutionListObject.accumulate("children",evolutionList);

    }
}
