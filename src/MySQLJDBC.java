import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySQLJDBC {

    Connection conn;

    PreparedStatement p;

    ResultSet temp;

    /**
     * constructor try to connected to default database, initialize global var conn
     */
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

    /**
     *  execute a sql query
     * @param input sql query string
     * @return hashmap from generated result set
     */
    public HashMap<Integer,String> doExecutionWithReturn(String input){
        try{
            p = conn.prepareStatement(input);
            temp = p.executeQuery();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return generateBiListByResultSet();
    }

    /**
     *  generate hash map from result set
     * @return hashmap represent sqery result
     */
    public HashMap<Integer,String> generateBiListByResultSet(){
        try{
            HashMap<Integer,String> map = new HashMap<>();
            ResultSetMetaData rsmd = temp.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            // when there is only one column of data
            // means is a version number / file list / clone chain list
            if(columnsNumber == 1){
                // use int key strart from 0 to easily looping
                int i = 0;
                while(temp.next()){
                    map.put(i,temp.getString(1));
                    i++;
                }
                return map;
            }
            else {
                // only one situation
                // first field is revision, second is cloneid
                // in future will use revision to search cloneid
                while(temp.next()){
                    map.put(temp.getInt(1),temp.getString(2));
                }
                return map;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

}
