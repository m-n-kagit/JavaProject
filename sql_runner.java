import java.sql.*;
//basic start for connection with database 

public class sql_runner 
{

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/mohitdb2";
        String user = "root" ; 
        String password = "yuv07";

        try { 
            //load and register the driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(url ,user,password);
            System.out.println("Connected to MySql successfully");

            //Creating a Statement 
            Statement stmt = con.createStatement();
            // stmt.executeUpdate("INSERT INTO emply_table VALUES(7,'Gunjan','TECH',34000,35)");
            ResultSet rs=stmt.executeQuery("Select empid, empname,empdept,emp_sal from emply_table" );
            
            //Just the primitive method to parse through the data 
            while(rs.next()){
                int id = rs.getInt("empid");
                String name = rs.getString("empname");
                String  dept_name = rs.getString("empdept");
                System.out.println(id + "\t" + name + "\t" + dept_name);

            }

            // the modern method (not only forward , we can parse to forward + backward)
            Statement smt2 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            
            rs.close();
            stmt.close();

            con.close();
        }
        catch (Exception e ){
            e.printStackTrace();
        }
    
    
    }
}
