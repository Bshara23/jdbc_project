import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FlightsMain {

	private static final String tableName = "flights";
	private static MySqlConn conn;

	public static void main(String[] args) {
		conn = new MySqlConn("root", "Aa123456", "test");

		// a. update the arrival time of KU101 twice
		
		//exampleA();
		 
		// b. update all flights from paris that arrive before 15:00 to arrive = 13:00
		//exampleB0_ChangeArrivalTime();
		exampleB_ChangeArrivalTimeParis();

		// c. manual update

		//exampleC_DownloadAllThenUpdateAll();
		//exampleC_DownloadAllThenUpdateAll_Light();
		// d. print the number if flights that has a delay more than x
		//exampleD_countNumberOfFlightsDelayed();

	}

	private static void exampleA() {
		 updateArrivalTime("KU101", "13:45"); printFlightRecord("KU101");
		 updateArrivalTime("KU101", "13:50"); printFlightRecord("KU101");
	}
	private static void exampleB0_ChangeArrivalTime() {
		printByFromParisAndArrivalTimeLessThan("15:00"); 
		//manualUpdate("3", null, null);
		 printByFromParisAndArrivalTimeLessThan("15:00"); 
		 //manualUpdate("4");
	}

	private static void exampleB_ChangeArrivalTimeParis() {

		System.out.println(getFligthsParisDB());

		updateFromParisAndArrivalTimeLessThan();
		System.out.println(getFligthsParisDB());
	}

	private static void exampleD_countNumberOfFlightsDelayed() {
		long x = 20;
		calcNumberFlightsDelayed(x);
	}

	private static void exampleC_DownloadAllThenUpdateAll_Light() {
		ArrayList<Flight> originalFlights = getFligthsDB();
		ArrayList<Flight> flights = (ArrayList<Flight>) originalFlights.clone();


		// update second row
		String prevSchedule = originalFlights.get(1).getScheduled();
		flights.get(1).setScheduled("13:36");

		
		System.err.println(getFligthsDB());
		
		setFlightsDB_LightVersion(flights, originalFlights);

		System.out.println("DDDDDDDDDDDDDDDDDDDDDD\nSSSSSSSSSSSSSSs\nDDDDDDDDDDDDDDD");

		originalFlights = getFligthsDB();
		flights = (ArrayList<Flight>) originalFlights.clone();

		// re-download flights data flights = getFligthsDB();
		System.out.println(originalFlights);
		
		// revert changes back // update second row
		flights.get(1).setScheduled(prevSchedule);
		setFlightsDB_LightVersion(flights, originalFlights);
	}
	private static void exampleC_DownloadAllThenUpdateAll() {
		ArrayList<Flight> flights = getFligthsDB();
		System.out.println(flights);

		// update second row
		String prevSchedule = flights.get(1).getScheduled();
		flights.get(1).setScheduled("13:36");
		setFlightsDB(flights);

		System.out.println("DDDDDDDDDDDDDDDDDDDDDD\nSSSSSSSSSSSSSSs\nDDDDDDDDDDDDDDD");

		// re-download flights data flights = getFligthsDB();
		System.out.println(flights);

		// revert changes back // update second row
		flights.get(1).setScheduled(prevSchedule);
		setFlightsDB(flights);

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

	private static void updateFromParisAndArrivalTimeLessThan() {
		String query = "SELECT * FROM " + tableName + " WHERE fromm = 'Paris'";
		IStatement statment = stat -> {
			try {
				while (stat.next()) {
					// Print out the values

					String delay = stat.getString(4);
					if (delay.length() == 0)
						continue;
					delay = delay.substring(delay.length() - 5);

					DateFormat sdf = new SimpleDateFormat("hh:mm");
					try {
						Date arivalTime = sdf.parse("15:00");
						Date delayTime = sdf.parse(delay);
						long diff = delayTime.getTime() - arivalTime.getTime();
						// if the flights didnt arrive on time
						if (diff < 0) {
							
							Flight f = new Flight(stat.getString(1), stat.getString(2), stat.getString(3), "15:00", stat.getString(5));
							setFlightDelayArrivalTime(f);							
						}



					} catch (ParseException e) {

					}

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};
		conn.executeQuery(query, statment);
		
		
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
					String delay = stat.getString(4);
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
						// System.out.println(sdf.format(arrTime) + " " + sdf.format(delayTime) + " " +
						// diff);

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

	private static ArrayList<Flight> getFligthsDB() {
		ArrayList<Flight> flights = new ArrayList<Flight>();
		String query = "SELECT * FROM " + tableName;
		IStatement statment = stat -> {
			try {
				while (stat.next()) {

					flights.add(new Flight(stat.getString(1), stat.getString(2), stat.getString(3), stat.getString(4),
							stat.getString(5)));

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};
		conn.executeQuery(query, statment);

		return flights;
	}

	private static ArrayList<Flight> getFligthsParisDB() {
		ArrayList<Flight> flights = new ArrayList<Flight>();
		String query = "SELECT * FROM " + tableName + " WHERE fromm = 'Paris'";
		IStatement statment = stat -> {
			try {
				while (stat.next()) {

					flights.add(new Flight(stat.getString(1), stat.getString(2), stat.getString(3), stat.getString(4),
							stat.getString(5)));

				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		};
		conn.executeQuery(query, statment);

		return flights;
	}

	private static void setFlightArrivalTime(Flight flight) {
		String updateQuery = "UPDATE flights SET scheduled = ? WHERE flight = ?";

		IPreparedStatement ps1_insert = f -> {
			try {
				// Change the flight schedule
				f.setString(1, flight.getScheduled());
				f.setString(2, flight.getFlight());

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		conn.executeUpdate(updateQuery, ps1_insert);
	}

	private static void setFlightDelayArrivalTime(Flight flight) {
		String updateQuery = "UPDATE flights SET delay = ? WHERE flight = ?";

		IPreparedStatement ps1_insert = f -> {
			try {
				// Change the flight schedule
				f.setString(1, flight.getScheduled());
				f.setString(2, flight.getFlight());

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		};

		conn.executeUpdate(updateQuery, ps1_insert);
	}
	
	
	private static void setFlightsDB(ArrayList<Flight> flights) {

		for (Flight f : flights) {
			setFlightArrivalTime(f);
		}
	}

	private static void setFlightsDB_LightVersion(ArrayList<Flight> flights, ArrayList<Flight> originalFlights) {
		// throw exception if sizes don't match
		if (flights.size() != originalFlights.size())
			try {
				throw new Exception("Flight arrays size don't match!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// update where there is a change only
		for (int i = 0; i < flights.size(); i++) {
			if (!flights.get(i).equals(originalFlights.get(i))) {
				setFlightArrivalTime(flights.get(i));
			}
		}

	}

}
