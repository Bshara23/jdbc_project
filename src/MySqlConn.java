import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConn {

	private String username;
	private String password;
	private String databaseName;
	private Connection conn;

	public MySqlConn(String username, String password, String databaseName) {

		this.username = username;
		this.password = password;
		this.databaseName = databaseName;

		setDrive();
		connect();
	}

	private void setDrive() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			// System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}
	}

	private void connect() {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/" + databaseName + "?serverTimezone=IST",
					username, password);

			//System.out.println("SQL connection succeed");

		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void executeQuery(String query, IStatement func) {
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			if (func != null)
				func.executeQuery(rs);
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int executeUpdate(String query, IPreparedStatement func) {
		try {
			PreparedStatement ps = conn.prepareStatement(query);
			if(func != null)
				func.executeChanges(ps);
			return ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
