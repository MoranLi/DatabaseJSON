import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List doExecutionWithReturn(String input){
        try{
            p = conn.prepareStatement(input);
            temp = p.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return generateBiListByResultSet();
    }

    public List generateBiListByResultSet(){
        try{
            List biList = new ArrayList();
            ResultSetMetaData rsmd = temp.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            // when there is only one column of data
            // means is a version number / file list / clone chain list
            if(columnsNumber == 1){
                while(temp.next()){
                    biList.add(temp.getString(1));
                }
                return biList;
            }
            else {
                while(temp.next()){
                    List childList = new ArrayList();
                    for (int i = 1; i <= columnsNumber; i++) {
                        String columnValue = temp.getString(i);
                        childList.add(columnValue);
                    }
                    biList.add(childList);
                }
                return biList;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    /*

    public String doExecutionWithReturn(String input){
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
                result+="\n hehehehe \n";
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }
    */

    public static void main(String[] args) {
        MySQLJDBC some = new MySQLJDBC();
        System.out.println(some.doExecutionWithReturn("select * from clones_ctags.changes where revision = 15"));
    }
}

