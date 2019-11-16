import java.sql.SQLException;

public class mysqlConnection2 {

	public static void main(String[] args) {
		MySqlConn conn = new MySqlConn("root", "Aa123456", "test");

		String query = "SELECT * FROM courses;";

		IStatement sPrintTable = stat -> {
			try {
				while (stat.next()) {
					// Print out the values
					System.out.println(stat.getString(1) + "  " + stat.getString(2) + "  " + stat.getString(3));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		String insertCourseQuery = "insert into courses values(?,?,?);";

		IPreparedStatement ps1_insert = f -> {
			try {
				f.setString(1, "99999");
				f.setString(3, "name1");
				f.setString(2, "Software Development");

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		

		
		
		
		

		conn.executeQuery(query, sPrintTable);
		conn.executeUpdate(insertCourseQuery , ps1_insert);
		System.out.println("After insertion of a new course, ID 9999");
		conn.executeQuery(query, sPrintTable);
		
		

	}
	
	

}
