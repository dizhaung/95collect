import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;


public class PhoenixTest {

	@Test
	public void testPhoenixConnection(){
		try {
			Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
			Connection conn = DriverManager.getConnection("jdbc:phoenix:111.205.6.229:2181:/hbase");
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("select * from stock_symbol");
			while(rs.next()){
				System.out.println(rs.getString(1));
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
