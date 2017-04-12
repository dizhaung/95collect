

/***
 * 
 * 閺佺増宓佺拋鍧楁６閹恒儱褰涚�鐐靛箛缁拷
 * 
 * 
 * 
 * 
 * 
 */

package com.database;




import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;





public class DatabaseAccessImpl implements DatabaseAccessInterface {



	public void DatabaseAccessImpl()
	{
		
	}
	
	
	
	/**
	 * 
	 * 
	 * 
	 *
	 */
	public void executeSQL(String sqlStatement) throws SQLException { 
        Connection connection = null; 
        PreparedStatement statement = null; 
        try { 
            connection = DBConnectionManager.getConnection(); 
            connection.setAutoCommit(true); 
            statement = connection.prepareStatement(sqlStatement); 
            statement.execute(); 
        } catch (SQLException ex) { 
            ex.printStackTrace(); 
            //log.error(ex.getMessage()); 
            throw ex; 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
            //og.error(ex.getMessage()); 
        } finally { 
            try { 
                try { 
                    statement.close(); 
                } catch (SQLException ex) { 
                    ex.printStackTrace(); 
                    //log.error("close statement exception in execute sql method"); 
                } 
                connection.close(); 
            } catch (SQLException ex) { 
                ex.printStackTrace(); 
                //log.error(ex.getMessage()); 
                throw ex; 
            } 
        } 
        //log.debug("exit execute sql statement method"); 
        return; 
    } 



	public void executeSQL(String[] sqlStatement) throws SQLException {
		// TODO Auto-generated method stub

	}

	public static void main(String [] are)
	{
		
		System.out.println("===========1==================");
		DatabaseAccessImpl altsql=new DatabaseAccessImpl();
		try {
			
			for(int i=11;i<114;i++)
			{
				String community="sxyd-idc";
				String sql="insert into topo_host_node (id,ip_address,sys_name,community,managed)"+
				"values("+i+",'183.203.0."+i+
			"','183.203.0."+i+
			"','"+community+
			"','0')";
			 altsql.executeSQL(sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("=========2====================");
		
	}
	
	

}
