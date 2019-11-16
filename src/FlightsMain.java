import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class FlightsMain {

	private static final String tableName = "flights";
	private static MySqlConn conn;

	public static void main(String[] args) {
		conn = new MySqlConn("root", "Aa123456", "test");

		
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

		// a. update the arrival time of KU101 twice
		/*
		updateArrivalTime("KU101", "13:45");
		printFlightRecord("KU101");
		updateArrivalTime("KU101", "13:50");
		printFlightRecord("KU101");
		*/
		
		// b. update all flights from paris that arrive before 15:00 to arrive = 13:00 
		
		/*
		printByFromParisAndArrivalTimeLessThan("15:00");
		updateFromParisAndArrivalTimeLessThan("15:00", "13:30");
		printByFromParisAndArrivalTimeLessThan("15:00");
		updateFromParisAndArrivalTimeLessThan("15:00", "13:45");
		*/
		
		// c. manual update
		/*
		printByFromParisAndArrivalTimeLessThan("15:00");
		manualUpdate("3");
		printByFromParisAndArrivalTimeLessThan("15:00");
		manualUpdate("4");
		*/
		
		// d. print the number if flights that has a delay more than x
		long x = 20;
		calcNumberFlightsDelayed(x);
		
	
	}

	private static void updateArrivalTime(String flightID, String arrivalTime) {
		String updateQuery = "UPDATE " + tableName + " SET scheduled='" + arrivalTime + "' WHERE flight='" + flightID
				+ "'";
		conn.executeUpdate(updateQuery, null);
	}

	private static void printFlightRecord(String flightID) {
		
		String query = "SELECT * FROM " + tableName + " WHERE flight ='" + flightID + "';";
		IStatement statment = stat -> {
			try {
				while (stat.next()) {
					// Print out the values
					System.out.println(stat.getString(1) + "  " + stat.getString(2) + "  " + stat.getString(3) + "  "
							+ stat.getString(4) + "  " + stat.getString(5));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		conn.executeQuery(query, statment);
	}
	
	private static void printByFromParisAndArrivalTimeLessThan(String arrivalTime) {
		String query = "SELECT * FROM " + tableName + " WHERE scheduled <'" + arrivalTime + "' AND fromm='paris';";
		IStatement statment = stat -> {
			try {
				while (stat.next()) {
					// Print out the values
					System.out.println(stat.getString(1) + "  " + stat.getString(2) + "  " + stat.getString(3) + "  "
							+ stat.getString(4) + "  " + stat.getString(5));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		conn.executeQuery(query, statment);
	}
	
	private static void updateFromParisAndArrivalTimeLessThan(String arrivalTime, String newArrivalTime) {
		String updateQuery = "UPDATE flights SET scheduled = ? WHERE scheduled< ? AND fromm='paris'";

		IPreparedStatement ps1_insert = f -> {
			try {
				// Change the terminal
				f.setString(1, newArrivalTime);
				f.setString(2, arrivalTime);


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		conn.executeUpdate(updateQuery, ps1_insert);
	}
	
	
	private static void manualUpdate(String from, String arrivalTime, String newArrivalTime) {
		String updateQuery = "UPDATE flights SET scheduled = ? WHERE scheduled < ? AND fromm=?";

		IPreparedStatement ps1_insert = f -> {
			try {
				// Change the terminal
				f.setString(1, newArrivalTime);
				f.setString(2, arrivalTime);
				f.setString(3, from);


			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};
		
		conn.executeUpdate(updateQuery, ps1_insert);
	}
	
	private static void calcNumberFlightsDelayed(long delayedMoreThan) {
		String query = "SELECT * FROM " + tableName;
		IStatement statment = stat -> {
			try {
				int cnt = 0;
				while (stat.next()) {
					// Print out the values
					
					String arrival = stat.getString(1);
					String delay =  stat.getString(4);
					if (delay.length() == 0)
						continue;
					delay = delay.substring(delay.length() - 5);

					DateFormat sdf = new SimpleDateFormat("hh:mm");
					try {
						Date arrTime = sdf.parse(arrival);
						Date delayTime = sdf.parse(delay);
						long diff = delayTime.getTime() - arrTime.getTime();
						// convert to minutes
						diff /= 1000;
						diff /= 60;

						if (diff > delayedMoreThan)
							cnt++;
						//System.out.println(sdf.format(arrTime) + "  " + sdf.format(delayTime) + " " + diff);

					} catch (ParseException e) {
					
					}
					
				}
				System.out.println(cnt);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		};
		conn.executeQuery(query, statment);
	}
	


	
}
