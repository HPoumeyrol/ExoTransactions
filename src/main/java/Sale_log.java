import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Sale_log extends Tables {
	
	private Long pk_id;
	private Long fk_product_id;
	private Integer qty;
	
	
	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	public Long getfk_product_id() {
		return fk_product_id;
	}

	public void setfk_product_id(Long fk_product_id) {
		this.fk_product_id = fk_product_id;
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
		Product product= Product.findProductById(fk_product_id);
		return "Sales_log [pk_id=" + pk_id + ", fk_product_id=" + fk_product_id + "(" + product.getLabel() + "), qty=" + qty + "]";
	}

	private Sale_log() {
		this.pk_id= -1L;
		this.fk_product_id= -1L;
		this.qty= 0;
	}
	
	
	private Sale_log(Long fk_product_id, Integer qty) {
		this.fk_product_id= fk_product_id;
		this.qty= qty;
	}
	
	private Sale_log(Long pk_id, Long fk_product_id, Integer qty) {
		this.pk_id= pk_id;
		this.fk_product_id= fk_product_id;
		this.qty= qty;
	}
	
	private Sale_log(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	public static Sale_log findSales_logById(Long pk_id) {
		Sale_log sales_log= new Sale_log(pk_id);
		return  sales_log;
	}
	
	public static Sale_log create(Long fk_product_id, Integer qty) {
		Sale_log sales_log= new Sale_log(fk_product_id, qty);
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
				//ystem.out.println("Truncate Table sales_Log OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de Truncate Table sales_Log");
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
				preparedStatement.setLong(1, this.fk_product_id);
				preparedStatement.setInt(2, this.qty);
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

		String sqlCmd = "update sales_log set fk_product_id = ?, qty = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.fk_product_id);
				preparedStatement.setInt(2, this.qty);
				preparedStatement.setLong(3, this.pk_id);
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

		String sqlCmd = "select fk_product_id, qty from sales_log where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.fk_product_id = rs.getLong("fk_product_id");
					this.qty= rs.getInt("pqty");
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
	
	
	private static  ArrayList<Sale_log> readAllFromDB() {
		ArrayList<Sale_log> resultArrayList= new ArrayList<Sale_log>();
		
		String sqlCmd = "select pk_id, fk_product_id, qty from sales_log;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {
			try {
				ResultSet rs = preparedStatement.executeQuery();
				while (rs.next()) {
					resultArrayList.add(new Sale_log(rs.getLong("pk_id"), rs.getLong("fk_product_id"), rs.getInt("qty")));
				}
			} catch (Exception e ){
				e.printStackTrace();
			}
		} catch (Exception e ){
			e.printStackTrace();
		}
		return resultArrayList;
	}

	
	public static Double getTotalSalesAmount() {
		Double result= 0.0;
		for(Sale_log sale_log : readAllFromDB()) {
			result+= sale_log.qty * Product.findProductById(sale_log.fk_product_id).getPrice();
		}
		return result;
	}
	
	public static void printAllSalesLog() {
		for(Sale_log sale_log : readAllFromDB()) {
			sale_log.display();
		}
	}
	
}
