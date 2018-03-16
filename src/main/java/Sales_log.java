import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Sales_log extends Tables {
	
	private Long pk_id;
	private Long fk_produc_id;
	private Integer qty;
	
	
	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	public Long getFk_produc_id() {
		return fk_produc_id;
	}

	public void setFk_produc_id(Long fk_produc_id) {
		this.fk_produc_id = fk_produc_id;
		update();
	}

	
	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
		update();
	}

	
	@Override
	public String toString() {
		return "Sales_log [pk_id=" + pk_id + ", fk_produc_id=" + fk_produc_id + ", qty=" + qty + "]";
	}

	private Sales_log() {
		this.pk_id= -1L;
		this.fk_produc_id= -1L;
		this.qty= 0;
	}
	
	
	private Sales_log(Long fk_produc_id, Integer qty) {
		this.fk_produc_id= fk_produc_id;
		this.qty= qty;
	}
	
	private Sales_log(Long pk_id, Long fk_produc_id, Integer qty) {
		this.pk_id= pk_id;
		this.fk_produc_id= fk_produc_id;
		this.qty= qty;
	}
	
	private Sales_log(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	public static Sales_log findSales_logById(Long pk_id) {
		Sales_log sales_log= new Sales_log(pk_id);
		return  sales_log;
	}
	
	public static Sales_log create(Long fk_produc_id, Integer qty) {
		Sales_log sales_log= new Sales_log(fk_produc_id, qty);
		sales_log.insert();
		return  sales_log;
	}
	
	
	public void refresh() {
		readFromDB();
	}
	
	public static void truncate() {
		String sqlCmd = "TRUNCATE sales_Log;";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.execute();
				System.out.println("Truncate Table sales_Log OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur lors de Truncate Table sales_Log");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	private void insert() {
		String sqlCmd = "INSERT INTO sales_log (fk_product_id, qty) VALUES(?, ?);";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setLong(1, this.fk_produc_id);
				preparedStatement.setInt(2, this.qty);
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

		String sqlCmd = "update sales_log set fk_produc_id = ?, qty = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.fk_produc_id);
				preparedStatement.setInt(2, this.qty);
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

		String sqlCmd = "select fk_produc_id, qty from sales_log where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.fk_produc_id = rs.getLong("fk_produc_id");
					this.qty= rs.getInt("pqty");
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
	
	
	
	
}
