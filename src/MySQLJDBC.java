import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.sql.*;

/**
 * Created by yul04 on 2017/9/19.
 */
public class MySQLJDBC {
    Connection conn;

    PreparedStatement p;

    ResultSet temp;

    public MySQLJDBC(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager
                    .getConnection("jdbc:mysql://i.sudo.gq:3306",
                            "forclones", "123456");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        System.out.println("Connection done");
    }

    private String doExecutionWithReturn(String input){
        boolean returned = false;
        String in = input.substring(0,6);
        if("SELECT".equals(in)||"select".equals(in)) {
            returned = true;
        }
        try{
            p = conn.prepareStatement(input);
            temp = p.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return printResultSet();
    }

    private String printResultSet(){
        String result = "";
        try {
            ResultSetMetaData rsmd = temp.getMetaData();
            result+=("querying SELECT * FROM XXX \n");
            int columnsNumber = rsmd.getColumnCount();
            while (temp.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    //if (i > 1) System.out.print(",  ");
                    String columnValue = temp.getString(i);
                    result+=(columnValue + " " + rsmd.getColumnName(i)+"\n");
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        //MySQLJDBC some = new MySQLJDBC();
        //System.out.println(some.doExecutionWithReturn("select * from clones_ctags.changes where revision = 15"));
        /*
        JSONObject jo1 = new JSONObject();
        jo1.accumulate("name",0);
        jo1.accumulate("chain_key",0);
        jo1.accumulate("children",new JSONArray());
        JSONArray ja = new JSONArray();
        ja.add(jo1);
        JSONObject js = new JSONObject();
        js.accumulate("name","hello world.java");
        js.accumulate("children",ja);
        System.out.println(js.toString());
        */
        String some = "hwoo";
        System.out.println(some.substring(0,some.length()-1));

    }
}

