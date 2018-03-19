import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Cash extends Tables {
	
	private Long pk_id;
	private Double amount;
	
	
	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	
	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
		update();
	}

	
	
	@Override
	public String toString() {
		return "Cash [pk_id=" + pk_id + ", amount=" + amount + "]";
	}

	private Cash() {
		this.pk_id = -1L;
		this.amount = 0.0;
	}
	
	
	private Cash(Double amount) {
		this.amount = amount;
	}
	
	private Cash(Long pk_id, Double amount) {
		this.pk_id= pk_id;
		this.amount = amount;
	}
	
	private Cash(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	
	public static Cash create(Double amount) {
		Cash cash= new Cash(amount);
		cash.insert();
		return  cash;
	}
	
	
	public static Cash getCashFromDb() {
		Cash resultCash= new Cash();
		String sqlCmd = "select pk_id, amount from cash;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					resultCash.setPk_id(rs.getLong("pk_id"));
					resultCash.setAmount(rs.getDouble("amount"));
					//System.out.println("Lecture OK de " + this);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(" erreur lors de recherche de Cash en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultCash;
	}
	
	
	
	public void refresh() {
		readFromDB();
	}

	public static void truncate() {
		String sqlCmd = "TRUNCATE cash;";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.execute();
				//System.out.println("Truncate Table cash OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de Truncate Table cash");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	
	private void insert() {
		String sqlCmd = "INSERT INTO cash (amount) VALUES(?);";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setDouble(1, this.amount);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				long key = -1L;
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					key = rs.getLong("pk_id");
				}
				this.pk_id = key;
				//System.out.println("Enregistrement en base OK : "  + key + " : " + this);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de l'enregistrement en base de " + this);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private void update() {

		String sqlCmd = "update cash set amount = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setDouble(1, this.amount);
				preparedStatement.setLong(2, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				//System.out.println("Mise a jour en base OK de " + this);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private void readFromDB() {

		String sqlCmd = "select amount from cash where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.amount = rs.getDouble("amount");
					//System.out.println("Lecture OK de " + this);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void displayAccountBalance() {
		System.out.format("Le solde de cash est %2$10.2f\n", this.amount);
	}
	
	
}
