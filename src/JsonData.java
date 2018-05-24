import java.util.List;

public class JsonData {
    private String name;
    private List children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List getChildren() {
        return children;
    }

    public void setChildren(List children) {
        this.children = children;
    }

    public String toString(){
        return name;
    }

}
