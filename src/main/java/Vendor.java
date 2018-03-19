import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Vendor extends Tables {
	
	private Long pk_id;
	private String name;
	private Boolean is_busy;
	
	
	public Long getPk_id() {
		return pk_id;
	}
	
	public void setPk_id(Long pk_id) {
		this.pk_id = pk_id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean setName(String name) {
		this.name = name;
		return update();
	}
	
	public Boolean getIs_busy() {
		return is_busy;
		
	}
	
	public boolean setIs_Busy(Boolean is_busy) {
		this.is_busy = is_busy;
		return update();
	}

	@Override
	public String toString() {
		return "vendor [pk_id=" + pk_id + ", name=" + name + ", is_busy=" + is_busy + "]";
	}

	
	
	private Vendor() {
		this.pk_id = -1L;
		this.name = "";
		this.is_busy = false;
	}
	
	
	private Vendor(String name, Boolean is_busy) {
		this.name = name;
		this.is_busy = is_busy;
	}
	
	private Vendor(Long pk_id,String name, Boolean is_busy) {
		this.pk_id= pk_id;
		this.name= name;
		this.is_busy= is_busy;
	}
	
	private Vendor(Long pk_id) {
		this.pk_id = pk_id;
		readFromDB();
	}
	
	
	public static Vendor findVendorById(Long pk_id) {
		Vendor vendor= new Vendor(pk_id);
		return  vendor;
	}
	
	public static Vendor create(String name, Boolean is_busy) {
		Vendor vendor= new Vendor(name, is_busy);
		vendor.insert();
		return  vendor;
	}
	
	
	public void refresh() {
		readFromDB();
	}
	
	
	public static void truncate() {
		String sqlCmd = "TRUNCATE vendor CASCADE;";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.execute();
				//System.out.println("Truncate Table vendor OK");
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de Truncate Table vendor");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	private boolean insert() {
		boolean res= false;
		String sqlCmd = "INSERT INTO vendor (name, is_busy) VALUES(?, ?);";
		
		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd, Statement.RETURN_GENERATED_KEYS)) {

			try {
				preparedStatement.setString(1, this.name);
				preparedStatement.setBoolean(2, this.is_busy);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				long key = -1L;
				ResultSet rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					key = rs.getLong("pk_id");
				}
				this.pk_id = key;
				//System.out.println("Enregistrement en base OK : "  + key + " : " + this);
				res= true;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("erreur lors de l'enregistrement en base de " + this);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	private boolean update() {
		boolean res= false;
		String sqlCmd = "update vendor set name = ?, is_busy = ? where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, this.name);
				preparedStatement.setBoolean(2, this.is_busy);
				preparedStatement.setLong(3, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				preparedStatement.execute();
				//System.out.println("Mise a jour en base OK de " + this);
				res= true;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println(this + " erreur lors de la mise a jour en base !");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	
	
	private void readFromDB() {

		String sqlCmd = "select name, is_busy from vendor where pk_id = ?;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setLong(1, this.pk_id);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					this.name = rs.getString("name");
					this.is_busy = rs.getBoolean("is_busy");
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
	
	
	public static Vendor findVendorByName(String name) {
		Vendor result= new Vendor();
		String sqlCmd = "select pk_id, name, is_busy from vendor where upper(name) = upper(?);";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				preparedStatement.setString(1, name);
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					result= new Vendor(rs.getLong("pk_id"), rs.getString("name"), rs.getBoolean("is_busy"));
					//System.out.println("Lecture OK de " + result);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche de " + name);
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  result;
	}
	
	
	public static Vendor findFreeVendor() {
		Vendor result= null;
		String sqlCmd = "select pk_id, name, is_busy from vendor where is_busy = false;";

		try (PreparedStatement preparedStatement = DbConnection.getDbConn().prepareStatement(sqlCmd)) {

			try {
				//System.out.println("sqlCmd= " + preparedStatement);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					result= new Vendor(rs.getLong("pk_id"), rs.getString("name"), rs.getBoolean("is_busy"));
					//System.out.println("Lecture OK de " + result);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.err.println("Erreur lors de la recherche d\' vendeur ");
				e.printStackTrace();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return  result;
	}
	
	
}
