import java.util.ArrayList;
import java.util.List;

public class SqlDataToJson {
    public static void main(String[] args) {
        MySQLJDBC database = new MySQLJDBC();
        SQLQuery sq = new SQLQuery("ctags");
        List evolutionChain = new ArrayList();
        for(int i=1;i<4;i++){
            int minRevision = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMinRevision(i)).get(0));
            int maxRevision = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMaxRevision(i)).get(0));
            JsonData fileCollection = new JsonData();
            fileCollection.setName("");
            fileCollection.setChildren(new ArrayList());
            List fileList = database.doExecutionWithReturn(sq.selectAllfiles(i));
            for(int j=0;j<fileList.size();j++){
                String fileName = (String)fileList.get(j);
                JsonData newFileObject = new JsonData();
                newFileObject.setName(fileName);
                newFileObject.setChildren(new ArrayList());
                List cloneChainsInFileX = database.doExecutionWithReturn(sq.selectChainIdFromGivenFile(fileName,i));
                for(int k=0;k<cloneChainsInFileX.size();k++){
                    Integer cloneChainId = Integer.parseInt((String)cloneChainsInFileX.get(k));
                    Integer startRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMinRevisionFromFileByChain(i,cloneChainId,fileName)).get(0));
                    Integer endRevisionOfChain = Integer.parseInt((String)database.doExecutionWithReturn(sq.selectMaxRevisionFromFileByChain(i,cloneChainId,fileName)).get(0));
                    JsonData newRevision = new JsonData();
                    newRevision.setChildren(new ArrayList());
                    if(startRevisionOfChain>minRevision){
                        newRevision.setName("-999");
                    }
                    else{
                        String cloneId = (String)database.doExecutionWithReturn(sq.selectCloneIdFromFromFileByChain(i,cloneChainId,fileName,startRevisionOfChain)).get(0);
                        newRevision.setName(cloneId);
                    }
                    for(int m=minRevision+1;m<endRevisionOfChain+1;m++){
                        JsonData aNewRevision = new JsonData();
                        aNewRevision.setChildren(new ArrayList());
                        if(m<startRevisionOfChain){
                            aNewRevision.setName("-999");
                        }
                        else{
                            aNewRevision.setName((String)database.doExecutionWithReturn(sq.selectCloneIdFromFromFileByChain(i,cloneChainId,fileName,m)).get(0));
                        }
                        newRevision.getChildren().add(aNewRevision);
                        newRevision = (JsonData) newRevision.getChildren().get(newRevision.getChildren().size());
                    }
                    newFileObject.getChildren().add(newRevision);
                }
                fileCollection.getChildren().add(newFileObject);
            }
            evolutionChain.add(fileCollection);
        }
    }
}
