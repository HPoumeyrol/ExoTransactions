import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Product extends Tables {
	
	private Long pk_id;
	private Double price;
	private Integer qty;
	
	
	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
		update();
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
		update();
	}

	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	
	

	@Override
	public String toString() {
		return "Product [pk_id=" + pk_id + ", price=" + price + ", qty=" + qty + "]";
	}

	private Product() {
		this.pk_id= -1L;
		this.price= 0.0;
		this.qty= 0;
	}
	
	
	private Product(Double price, Integer qty) {
		this.price= price;
		this.qty= qty;
	}
	
	private Product(Long pk_id, Double price, Integer qty) {
		this.pk_id= pk_id;
		this.price= price;
		this.qty= qty;
	}
	
	private Product(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	public static Product findProductById(Long pk_id) {
		Product product= new Product(pk_id);
		return  product;
	}
	
	public static Product create(Double price, Integer qty) {
		Product product= new Product(price, qty);
		product.insert();
		return  product;
	}
	
	
	public void refresh() {
		readFromDB();
	}
	
	public static void truncate() {
		String sqlCmd = "TRUNCATE product CASCADE;";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.execute();
				System.out.println("Truncate Table product OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println("erreur lors de Truncate Table product");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void insert() {
		String sqlCmd = "INSERT INTO product (price, qty) VALUES(?, ?);";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setDouble(1, this.price);
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

		String sqlCmd = "update product set price = ?, qty = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setDouble(1, this.price);
				preparedStatement.setInt(2, this.qty);
				preparedStatement.setLong(3, this.pk_id);
				System.out.println("sqlCmd= " + preparedStatement);
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

		String sqlCmd = "select price, qty from product where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.price = rs.getDouble("price");
					this.qty= rs.getInt("qty");
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
