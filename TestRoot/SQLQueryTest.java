import org.junit.Assert;
import org.junit.Test;

public class SQLQueryTest {

    @Test
    public void selectAllfiles() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectAllfiles(1);
        Assert.assertEquals("select distinct filepath from clones_coral.type1clones;",data);
    }

    @Test
    public void selectMinRevision() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectMinRevision(1);
        Assert.assertEquals("select min(revision) from clones_coral.type1clones;",data);
    }

    @Test
    public void selectMaxRevision() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectMaxRevision(1);
        Assert.assertEquals("select max(revision) from clones_coral.type1clones;",data);
    }

    @Test
    public void getFileXInfo() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.getFileXInfo("asp.c",1);
        Assert.assertEquals("select distinct globalcloneid from clones_coral.type1clones where filepath = \"asp.c\" ;",data);
    }

    @Test
    public void selectRevisionCloneidFromFileByChain() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectRevisionCloneidFromFileByChain(1,1,"asp.c");
        Assert.assertEquals("select revision, cloneid from clones_coral.type1clones where globalcloneid = 1 and filepath = \"asp.c\" ;",data);
    }

    @Test
    public void selectMinRevisionFromFileByChain() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectMinRevisionFromFileByChain(1,1,"asp.c");
        Assert.assertEquals("select min(revision) from clones_coral.type1clones where globalcloneid = 1 and filepath = \"asp.c\" ;",data);
    }

    @Test
    public void selectMaxRevisionFromFileByChain() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectMaxRevisionFromFileByChain(1,1,"asp.c");
        Assert.assertEquals("select max(revision) from clones_coral.type1clones where globalcloneid = 1 and filepath = \"asp.c\" ;",data);
    }

    @Test
    public void selectCloneIdFromFromFileByChain() {
        SQLQuery sq = new SQLQuery("coral");
        String data = sq.selectCloneIdFromFromFileByChain(1,1,"asp.c",1);
        Assert.assertEquals("select cloneid from clones_coral.type1clones where globalcloneid = 1 and filepath = \"asp.c\" and revision = 1 ;",data);
    }
}
