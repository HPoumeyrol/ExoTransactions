import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection implements Serializable
{   
	
	private static final long serialVersionUID = 1426783525973463317L;
	/** Instance unique pré-initialisée */
    private static DbConnection INSTANCE = new DbConnection();
    private Connection dbConn= null;
	private Boolean IsDbConnected= false;
	
	

	/** Constructeur privé */
    private DbConnection(){
    	  
    	try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	// Open DataBase
		try {
			this.dbConn = DriverManager.getConnection(Params.getDbUrl(), Params.getDbUser(), Params.getDbPwd());
			this.dbConn.setSchema(Params.getDbSchema());
			System.out.println("Catalog= " + dbConn.getCatalog());
			System.out.println("Schema= " + dbConn.getSchema());
			this.IsDbConnected= true;
		} catch (SQLException e) {
			System.out.println("DataBase Connexion error : ");
			e.printStackTrace();
			System.out.println("Program terminated on error.");
			System.exit(99);
		}
    }
 
     
    /** Point d'accès pour l'instance unique du singleton */
    public static DbConnection getInstance()
    {   return INSTANCE;
    }
 
    /** Sécurité anti-désérialisation */
    private Object readResolve() {
        return INSTANCE;
    }

    public static Connection getDbConn() {
		return INSTANCE.dbConn;
    }
    

	public static Boolean isDbConnected() {
		return INSTANCE.IsDbConnected;
	}

	public static void setAutoCommit(Boolean autoCommit) throws SQLException {
		if(INSTANCE.IsDbConnected) INSTANCE.dbConn.setAutoCommit(autoCommit);
	}

	public static void commit() throws SQLException {
		if(INSTANCE.IsDbConnected) INSTANCE.dbConn.commit();
		
		INSTANCE.dbConn.rollback();
	}
	
	public static void rollback() throws SQLException {
		if(INSTANCE.IsDbConnected) INSTANCE.dbConn.rollback();
	}
	
}
