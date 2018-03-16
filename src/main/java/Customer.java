import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Customer extends Tables {
	
	private Long pk_id;
	private String name;
	private Double credit;
	
	
	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		update();
	}
	
	public Double getCredit() {
		return credit;
		
	}
	
	public void setCredit(Double credit) {
		this.credit = credit;
		update();
	}

	@Override
	public String toString() {
		return "customer [pk_id=" + pk_id + ", name=" + name + ", credit=" + credit + "]";
	}

	
//	public Customer(Long pk_id,String name, Double credit) {
//		this.pk_id = pk_id;
//		this.name = name;
//		this.credit = credit;
//	}
//	
	
	private Customer() {
		this.pk_id = -1L;
		this.name = "";
		this.credit = 0.0;
	}
	
	
	private Customer(String name, Double credit) {
		this.name = name;
		this.credit = credit;
	}
	
	private Customer(Long pk_id,String name, Double credit) {
		this.pk_id= pk_id;
		this.name= name;
		this.credit= credit;
	}
	
	private Customer(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	public static Customer findCustomerById(Long pk_id) {
		Customer customer= new Customer(pk_id);
		return  customer;
	}
	
	public static Customer create(String name, Double credit) {
		Customer customer= new Customer(name, credit);
		customer.insert();
		return  customer;
	}
	
	
	public void refresh() {
		readFromDB();
	}
	
	
	public static void truncate() {
		String sqlCmd = "TRUNCATE customer;";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.execute();
				System.out.println("Truncate Table customer OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur lors de Truncate Table customer");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	private void insert() {
		String sqlCmd = "INSERT INTO customer (name, credit) VALUES(?, ?);";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setString(1, this.name);
				preparedStatement.setDouble(2, this.credit);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				long key = -1L;
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					key = rs.getLong("pk_id");
				}
				this.pk_id = key;
				System.out.println("Enregistrement en base OK : "  + key + " : " + this);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur lors de l'enregistrement en base de " + this);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	private void update() {

		String sqlCmd = "update customer set name = ?, credit = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, this.name);
				preparedStatement.setDouble(2, this.credit);
				preparedStatement.setLong(3, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				System.out.println("Mise a jour en base OK de " + this);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private void readFromDB() {

		String sqlCmd = "select name, credit from customer where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.name = rs.getString("name");
					this.credit = rs.getDouble("credit");
					System.out.println("Lecture OK de " + this);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static Customer findCustomerByName(String name) {
		Customer result= new Customer();
		String sqlCmd = "select pk_id, name, credit from customer where upper(name) = upper(?);";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, name);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					result= new Customer(rs.getLong("pk_id"), rs.getString("name"), rs.getDouble("credit"));
					
					System.out.println("Lecture OK de " + result);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("Erreur lors de la recherche de " + name);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  result;
	}
	
	
	public void displayAccountBalance() {
		System.out.format("Le solde du compte de %1$s est %2$10.2f\n", this.name, this.credit);
	}
}
