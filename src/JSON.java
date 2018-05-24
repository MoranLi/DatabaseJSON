import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

public class JSON {


    public static void main(String[] args) {
        JSONObject jo1 = new JSONObject();
        jo1.accumulate("name",0);
        jo1.accumulate("chain_key",0);
        jo1.accumulate("children",new JSONArray());
        JSONArray ja = new JSONArray();
        ja.add(jo1);
        ja.add("0");
        JSONObject js = new JSONObject();
        js.accumulate("name","hello world.java");
        js.accumulate("children",ja);
        System.out.println(js.toString());
    }
}
