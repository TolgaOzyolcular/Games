package JavaFXGame;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbConnection {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String SERVER_IP = "localhost";                       // Server laptop IP address
	static final String URL = "jdbc:mysql://"+SERVER_IP+"/UserScore";
	static final String USER = "root";
	static final String PASS = "root";                           // MySql Password

	public DbConnection() {
	}

	public Connection getConnection() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(URL, USER, PASS);
		return conn;

	}

	public boolean register(User user) {
		boolean result = false;
		try {
			
			String sql = "INSERT INTO `USER`(`USERNAME`,`PASSWORD`) VALUES(?,?)";
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			
			if (ps.executeUpdate() > 0) {
				result = true;
			} else {
				result = false;
			}

		} catch (Exception ex) {
			System.out.println("Registration error: " + ex);
		}
		return result;
	}
	
	public boolean checkUserName(User user) {
		boolean result = false;
        int count = 0;
        try {

            String sql = "SELECT count(*) FROM `USER` WHERE `USERNAME` = ? ";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, user.getUsername());
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("User check prior registeration: " + ex);
        }
        return result;
	}

	public boolean login(User user) {
		boolean result = false;
        int count = 0;
        try {

            String sql = "SELECT count(*) FROM `USER` WHERE `USERNAME` = ? AND `PASSWORD` = ? ";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt(1);
            }
            if (count > 0) {
                result = true;
            } else {
                result = false;
            }

        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("User login: " + ex);
        }
        return result;
	}

	public boolean refillCredit(String username, double credit) {
		
	        boolean result = false;
	        try {
	            
	            String sql = "UPDATE `USER` SET  `CREDIT` = ? WHERE `USERNAME` = ?";
	            PreparedStatement ps = getConnection().prepareStatement(sql);
	            
	            ps.setDouble(1,credit);
	            ps.setString(2, username);

	            result = ps.executeUpdate() > 0;

	        } catch (Exception ex) {
	           System.out.println("Credit Refill error: "+ex);
	        }
	        return result;
	    
	}

	public double getOverAllScore(String username) {
		double result = 0;
		try {
            String sql = "SELECT `OVERALL_SCORE` FROM `USER` WHERE `USERNAME` = ? ";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               result  = rs.getDouble("OVERALL_SCORE");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Overall score fetch: " + ex);
        }
		return result;	
	}
	public double getCredit(String username) {
		double result = 0;
		try {
            String sql = "SELECT `CREDIT` FROM `USER` WHERE `USERNAME` = ? ";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, username);
            
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
               result  = rs.getDouble("CREDIT");
            }
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("CREDIT fetch: " + ex);
        }
		return result;	
	}
	
	public void updateRealCredit(User user) {
        try {
           
            String sql = "UPDATE `USER` SET `CREDIT` = ? WHERE `USERNAME` = ?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setDouble(1, user.getCredit());
            ps.setString(2, user.getUsername());
            ps.executeUpdate();

        } catch (Exception ex) {
        	 	System.out.println("Update Real Credit: "+ex);
        }
        
    }
	
	public void updateScore(User user) {
        try {
           
            String sql = "UPDATE `USER` SET `OVERALL_SCORE` = ? WHERE `USERNAME` = ?";
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setDouble(1, user.getOverall_score());
            ps.setString(2, user.getUsername());
            ps.executeUpdate();

        } catch (Exception ex) {
        	 	System.out.println("Update Score : "+ex);
        }
        
    }

}
