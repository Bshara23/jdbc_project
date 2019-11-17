
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

	@Override
	public String toString() {
		return "Flight [scheduled=" + scheduled + ", flight=" + flight + ", from=" + from + ", delay=" + delay
				+ ", terminal=" + terminal + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Flight) {
			Flight o = (Flight) obj;
			if (o.getDelay() != getDelay()) {
				return false;
			}
			if (o.getFlight() != getFlight()) {
				return false;
			}
			if (o.getTerminal() != getTerminal()) {
				return false;
			}
			if (o.getFrom() != getFrom()) {
				return false;
			}
			if (o.getScheduled() != getScheduled()) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	
}
