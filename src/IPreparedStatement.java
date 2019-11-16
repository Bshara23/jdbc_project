import java.sql.PreparedStatement;
import java.sql.ResultSet;

public interface IPreparedStatement {
	/**
	 * 
	 * Only add the changes, the function will automatically execute update.
	 * 
	 * */
	public void executeChanges(PreparedStatement ps);

}
