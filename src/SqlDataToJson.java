import java.util.ArrayList;
import java.util.List;

public class SqlDataToJson {
    public static void main(String[] args) {
        // create daatbase connection
        MySQLJDBC database = new MySQLJDBC();
        // create query instance, testing schema clones_camellia(smallest)
        SQLQuery sq = new SQLQuery("camellia");
        // whole collection
        // contain 3 child, refers to type 1,2,3 clone
        List evolutionChain = new ArrayList();
        // i = 1/2/3 means type 1/2/3 clones
        for(int i=1;i<4;i++){
            // comment printing methods for debugging
            // System.out.println("Type"+i);
            // get minimum revision of current system in type i clone
            int minRevision = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMinRevision(i)).get(0));
            // collection of all files in type i clone
            JsonData fileCollection = new JsonData();
            fileCollection.setName("");
            fileCollection.setChildren(new ArrayList());
            // get all file name in type i clone
            List fileList = database.doExecutionWithReturn(sq.selectAllfiles(i));
            for(int j=0;j<fileList.size();j++){
                // get specific file name
                String fileName = (String)fileList.get(j);
                // comment printing methods for debugging
                // System.out.println("\t file: "+fileName);
                // collection for all clone chain in a file
                JsonData newFileObject = new JsonData();
                newFileObject.setName(fileName);
                newFileObject.setChildren(new ArrayList());
                // get all clone chain id in file j
                List cloneChainsInFileX = database.doExecutionWithReturn(sq.selectChainIdFromGivenFile(fileName,i));
                for(int k=0;k<cloneChainsInFileX.size();k++){
                    // get specific clone chain id
                    Integer cloneChainId = Integer.parseInt((String)cloneChainsInFileX.get(k));
                    // -1 refers to a invalid value , pass this instance
                    // not sure about meaning of using -1 in data, currently assume is illegal
                    if(cloneChainId == -1)continue;
                    // comment printing methods for debugging
                    // System.out.println("\t\t chain:"+cloneChainId);
                    // get start revision of chain k
                    Integer startRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMinRevisionFromFileByChain(i,cloneChainId,fileName)).get(0));
                    // get end revision of chain k
                    // assuming each chain have only one end
                    Integer endRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMaxRevisionFromFileByChain(i,cloneChainId,fileName)).get(0));
                    // first revision of chain k
                    JsonData newRevision = new JsonData();
                    newRevision.setChildren(new ArrayList());
                    // if chain start at later revision of system (do not exist in first revision of system)
                    if(startRevisionOfChain>minRevision){
                        newRevision.setName("-999");
                    }
                    // clone chain start at first revision of system
                    else{
                        // assuming each parent only have one child
                        String cloneId = (String)database.doExecutionWithReturn(sq.selectCloneIdFromFromFileByChain(i,cloneChainId,fileName,startRevisionOfChain)).get(0);
                        newRevision.setName(cloneId);
                    }
                    // loop through all revision form first revision of system to end revision of chain
                    for(int m=minRevision+1;m<endRevisionOfChain+1;m++){
                        // comment printing methods for debugging
                        // System.out.println("\t\t\t revision:"+m);
                        JsonData aNewRevision = new JsonData();
                        aNewRevision.setChildren(new ArrayList());
                        // use -999 represent currently this chain do not exist
                        if(m<startRevisionOfChain){
                            aNewRevision.setName("-999");
                        }
                        // of chain exist, store its pcid
                        else{
                            aNewRevision.setName((String)database.doExecutionWithReturn(sq.selectCloneIdFromFromFileByChain(i,cloneChainId,fileName,m)).get(0));
                        }
                        // push to its parent
                        newRevision.getChildren().add(aNewRevision);
                    }
                    // comment printing methods for debugging
                    // System.out.println(newRevision);
                    newFileObject.getChildren().add(newRevision);
                }
                // comment printing methods for debugging
                //System.out.println(newFileObject);
                fileCollection.getChildren().add(newFileObject);
            }
            // comment printing methods for debugging
            //System.out.println(fileCollection);
            evolutionChain.add(fileCollection);
        }
        // comment printing methods for debugging
        //System.out.println(evolutionChain);
    }
}
