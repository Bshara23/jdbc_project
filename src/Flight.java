
public class Flight {

	private String scheduled, flight, from, delay, terminal;

	public Flight(String scheduled, String flight, String from, String delay, String terminal) {
		super();
		this.scheduled = scheduled;
		this.flight = flight;
		this.from = from;
		this.delay = delay;
		this.terminal = terminal;
	}

	public String getScheduled() {
		return scheduled;
	}

	public void setScheduled(String scheduled) {
		this.scheduled = scheduled;
	}

	public String getFlight() {
		return flight;
	}

	public void setFlight(String flight) {
		this.flight = flight;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDelay() {
		return delay;
	}

	public void setDelay(String delay) {
		this.delay = delay;
	}

	public String getTerminal() {
		return terminal;
	}

	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	
}
